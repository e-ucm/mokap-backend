package es.eucm.mokap.backend.server;

import es.eucm.mokap.backend.controller.search.MokapSearchController;
import es.eucm.mokap.backend.controller.insert.MokapInsertController;
import es.eucm.mokap.backend.model.SearchFilters;
import es.eucm.mokap.backend.utils.ApiKeyVerifier;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet class for the Repository main functions (insert, search...)
 */
public class MokapBackend extends HttpServlet {
	private static final long serialVersionUID = -1883047452996950111L;

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
		MokapInsertController iCont = new MokapInsertController();
		// Check api key
		if (!ApiKeyVerifier.checkApiKey(req,resp)){
			return;
		}else {

			try {
				// Get the uploaded file stream
				FileItemStream fis = getUploadedFile(req);
				if(fis!=null){
					// Actually process the uploaded resource
					String str = iCont.processUploadedResource(fis);
					// Send the response
					PrintWriter out = resp.getWriter();
					out.print(str);
					out.flush();
					out.close();
				}else{
					resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"A file could not be found inside the request.");
				}
			} catch (Exception e) {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"There was an error: "+e.getMessage());
			}
		}
	}
	
	/**
	 * Method: GET
	 * Processes get requests to perform a search.
	 * -Requires a header/parameter called q (string to search for). It performs an index search with the keyword in that header.
	 * -Requires a valid api key to work.
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		MokapSearchController sCont = new MokapSearchController();
		// Check api key
		if (!ApiKeyVerifier.checkApiKey(req,resp)){
			return;
		}else{
			PrintWriter out = resp.getWriter();

			// Get the parameters from the header / parameter
			SearchFilters sp = new SearchFilters(req);

			String str = sCont.performSearch(sp);
			// Set the response encoding
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("application/json");
			out.print(str);
			out.flush();
			out.close();
		}
	}

	/**
	 * Iterates the whole request in search for a file. When it finds it, it creates a FileItemStream which contains it.
	 * @param req The request to process
	 * @return Returns THE FIRST file found in the upload request or null if no file could be found
	 */
	private FileItemStream getUploadedFile(HttpServletRequest req) throws IOException, FileUploadException {
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload();
		// Set the UTF-8 encoding to grab the correct uploaded filename, especially for Chinese
		upload.setHeaderEncoding("UTF-8");

		FileItemStream  file = null;

		/* Parse the request */
		FileItemIterator iter = upload.getItemIterator(req);
		while (iter.hasNext()) {
			FileItemStream item = iter.next();
			if (!item.isFormField()) {
				file=item;
				break;
			}
		}
		return file;
	}

}
