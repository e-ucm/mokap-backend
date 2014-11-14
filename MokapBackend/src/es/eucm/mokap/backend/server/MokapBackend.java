package es.eucm.mokap.backend.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.gson.JsonSyntaxException;

import es.eucm.mokap.backend.model.response.SearchResponse;
import es.eucm.mokap.backend.model.response.InsertResponse;
import es.eucm.mokap.backend.utils.GoogleAccess;
import es.eucm.mokap.backend.utils.Utils;

public class MokapBackend extends HttpServlet {
	private static final long serialVersionUID = -1883047452996950111L;
	private static String BUCKET_NAME = System.getProperty("backend.BUCKET_NAME");
	private static int MAX_FILE_SIZE = Integer.parseInt(System.getProperty("backend.MAX_FILE_SIZE"));
	private static GoogleAccess ga = new GoogleAccess(BUCKET_NAME);
	
	
	/**
	 * Method: POST
	 * Processes post requests. 
	 * -Requests must be multipart/form-data.
	 * -The field with the file must be named "file".
	 * -The file must be a .zip compressed file with the following contents:
	 * 	-contents.zip -> A zip file with the information we'll store in Cloud Storage
	 * 	-A folder with the desired thumbnails
	 * 	-descriptor.json -> A .json file with the indexing information to store in Datastore
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		InsertResponse pr = new InsertResponse();
		PrintWriter out = resp.getWriter();				
		long assignedKeyId = 0;		
	
		try{
			// Process the upload request and store the file temporarily
			String tempFileName = storePostedTempFile(req);
			// If the file was successfully uploaded, let's process it
			if(tempFileName != null){
				assignedKeyId = processPostedTempFile(tempFileName);				
				// Send the response
				pr.setId(assignedKeyId);
				if(assignedKeyId != 0)
					pr.setMessage("OK.");
				else
					pr.setError("ERROR: Couldn't process the file.");
			}else{
				pr.setError("ERROR: Couldn't upload the file.");	
			}
		}catch(Exception e){
			pr.setError("ERROR: "+e.getMessage());
			e.printStackTrace();
		}
		
		out.print(pr.toJsonString());
		out.flush();
		out.close();
	}
	
	/**
	 * Method: GET
	 * Processes get requests.	
	 * -Requires a header called searchstring. It performs an index search with the keyword in that header. 
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();				
		String searchString = "";
		// Get the search string from the header / parameter
		String searchStringH = req.getHeader("searchstring");
		String searchStringP = req.getParameter("searchstring");
		if(searchStringP!=null){
			searchString = searchStringP;
		}
		if(searchStringH!=null){
			searchString = searchStringH;
		}
		String searchCursor = req.getParameter("searchcursor");
		
		SearchResponse gr = ga.searchByString(searchString, searchCursor);
		String str = new String(Charset.forName("UTF-8").encode(gr.toJsonString()).array());
		
		out.print(str);
		out.flush();
		out.close();
	}
	
	/*
	 * *************************************************************************************
	 */

	/**
	 * @param tempFileName
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws JsonSyntaxException
	 */
	public long processPostedTempFile(String tempFileName) throws IOException,
			UnsupportedEncodingException, JsonSyntaxException {
		long assignedKeyId;
		// Read the cloud storage file
		byte[] content = null;
		String descriptor = "";
		Map<String,byte[]> tns = new HashMap<String,byte[]>();	
		InputStream is = ga.readFile(tempFileName);
		ZipInputStream zis = new ZipInputStream(is);
		ZipEntry entry;
		while((entry = zis.getNextEntry()) != null) {
		    String filename = entry.getName();		
		    if(filename.equals("contents.zip")){
		    	content = IOUtils.toByteArray(zis);			    	
		    }else if(filename.equals("descriptor.json")){
		    	BufferedReader br = new BufferedReader(new InputStreamReader(zis, "UTF-8"));
		    	String str;
		    	while ((str = br.readLine()) != null) {
		    		descriptor += str;
				}			    	
		    }else if(entry.isDirectory()){
		    	continue;			    	
		    }else if(filename.endsWith(".png")){
		    	byte[] img = IOUtils.toByteArray(zis);
		    	tns.put(filename, img);
		    }
		    zis.closeEntry();
		}
		
		try{
			if(descriptor != null &&
				!descriptor.equals("") &&
				content != null
					){
					// Analizar json
					Map<String, String> entMap = Utils.jsonToMap(descriptor);
					// Parse the map into an entity
					Entity ent = new Entity("Resource");			    
					for(String key :entMap.keySet()){
						ent.setProperty(key, entMap.get(key));
					}		    
					// Store the entity (GDS) and get the Id
					Key k = ga.storeEntity(ent);
					assignedKeyId = k.getId();
					
					// Store the contents file with the Id in the name
					ByteArrayInputStream bis = new ByteArrayInputStream(content);
					ga.storeFile(bis, assignedKeyId+".zip");
					
					// Store the thumbnails in a folder with the id as the name
					for(String key : tns.keySet()){
						ByteArrayInputStream imgs = new ByteArrayInputStream(tns.get(key)); 
						ga.storeFile(imgs, assignedKeyId+"/"+key);
					}
					// Create the Search Index Document			
					GoogleAccess.addToSearchIndex(ent, k);					
					
					// Everything went ok, so we delete the temp file
					ga.deleteFile(tempFileName);
			}else{
				assignedKeyId = 0;
			}
		}catch(Exception e){
			e.printStackTrace();
			assignedKeyId = 0;
		}
		return assignedKeyId;
	}

	/**
	 * @param req
	 * @param pr
	 * @param tempFileName
	 * @return
	 * @throws IOException
	 */
	public String storePostedTempFile(HttpServletRequest req) throws IOException {
		String tempFileName = null;
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload();
		// Set overall request size constraint: the default value of -1 indicates that there is no limit.
		upload.setSizeMax(MAX_FILE_SIZE); //the maximum allowed size, in bytes.
		// Set the UTF-8 encoding to grab the correct uploaded filename, especially for Chinese
		upload.setHeaderEncoding("UTF-8");
		
		// Parse the request
		FileItemIterator iter;
		try {
			iter = upload.getItemIterator(req);		
			while (iter.hasNext()) {
			    FileItemStream item = iter.next();
				InputStream is = item.openStream();			    
				if (!item.isFormField()) {			    	
			    	// Process a file upload
			        String fileName = item.getName();			        
			        if (fileName != null)
			        	fileName= FilenameUtils.getName(fileName);			       		        
			        else
			        	throw new IOException("The file name could not be read.");
			        if(!fileName.endsWith(".zip")){
			        	throw new IOException("The file was not a .zip file.");				        	
			        }else{
			        	//Calculate fileName
			        	tempFileName = Utils.generateTempFileName(fileName);			        	
				        // Actually store the general temporal file		       			        	
				        ga.storeFile(is, tempFileName);
			        }
			    }
				is.close();
			} //end while
		} catch (FileUploadException e) {					
			e.printStackTrace();
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return tempFileName;
	}

	

}
