package es.eucm.mokap.uploader;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import es.eucm.mokap.uploader.exceptions.FileNotUploadedException;

public class Uploader {
	/*
	 * Sets the max size of the files to test
	 */
	private static final int MAX_SIZE = 32*1024*1024;
	/*
	 * Sets the servlet we want to test
	 */
	private static final String SERVLET_URI = "http://127.0.0.1:8888/backend";
	/*
	 * Sets the location of the files
	 */
	private static final String FILES_LOCATION = "c:/test";
	
	/**
	 * Initiates the upload process
	 * @param initialSize int with the initial KByte value for the test
	 * @throws IOException 
	 * @throws FileNotUploadedException 
	 */
	public static void beginUpload() throws IOException, FileNotUploadedException{		
		System.out.println("Initiating upload...");
		File[] files = new File(FILES_LOCATION).listFiles();
		List<File> failed = new LinkedList<File>();
		
	    for (File file : files) {	    	
	        if (!file.isDirectory()) {
	          System.out.println("-Uploading file: "+file.getName());
	          if(sendUploadRequest(file, SERVLET_URI) != 200){
	        	  failed.add(file);
	          }
	        } 
	    }
	    if(failed.size() > 0){
	    	System.out.println("|| ====== "+failed.size()+" files failed to upload ===== ||");
	    	for(File f : failed){
	    		System.out.println(f.getName());
	    	}
	    }
		
	}	
	
	/**
	 * Sends a request to the GCS servlet to try and upload the file
	 * @return int Estado de la respuesta
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	private static int sendUploadRequest(File file, String uri) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost uploadFile = new HttpPost(uri);

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);
		builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, "file.zip");
		HttpEntity multipart = builder.build();

		uploadFile.setEntity(multipart);

		CloseableHttpResponse response = httpClient.execute(uploadFile);
		String myString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
		System.out.println(myString);
		//HttpEntity responseEntity = response.getEntity();
		return response.getStatusLine().getStatusCode();
	}
}
