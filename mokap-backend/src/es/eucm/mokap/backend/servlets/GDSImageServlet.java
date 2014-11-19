package es.eucm.mokap.backend.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;

import es.eucm.mokap.backend.model.RepoElement;
import es.eucm.mokap.backend.model.search.SearchResult;
import es.eucm.mokap.backend.utils.GDSUtils;
import es.eucm.mokap.backend.utils.JSONUtils;
/**
 * Servlet that manages the GSD repoelements requests
 * @author mario
 * 
 * 
{
"thumbnail": "test_tb",
"author":{"name":"test_author","url":"test_url_aut"},
"license": CC_BY,
"width":100,
"height":100,
"description":{"strings":[{"lang":"sp","value":"test-description" }]},
"name":{"strings":[{"lang":"sp","value":"test-name"}]},
"tags": [{"strings":[{"lang":"sp","value":"tag1"}]},{"strings":[{"lang":"sp","value":"tag2" }]}]
}
 */
public class GDSImageServlet extends HttpServlet {

	private static final long serialVersionUID = -3178885200866195963L;
	
	/**
	 * Method: GET
	 * Expected headers:
	 * 	searchstring: string to perform the search.
	 * Returns the entity search results.
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();		
		
		// Get the search string from the header
		String searchstring = req.getHeader("searchstring");
		
		// Perform the search
		SearchResult res = GDSUtils.searchGDSByString(searchstring);
		
		// Emit the response in JSON
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		
		
		String jsonString = JSONUtils.JSONfromObject(res);
		
		
		out.print(jsonString);
		out.flush();
		out.close();
		
	}

	
	
	/**
	 * Method: POST
	 * Adds a new entity to the DB and stores its data in the search index.
	 * @throws IOException 
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();				
		
		// If the content type is what we're looking for...
		String contentType = req.getContentType();		
		if(contentType.equals("application/json; charset=UTF-8")){
			
			try{
				// Read the JSON in the request into a String
				StringBuffer jb = JSONUtils.parseRequestBody(req);	
				
				long keyId = GDSUtils.insertJSONIntoGDS(jb);
				out.println("Stored with Key ["+keyId+"]");
				
			}catch(Exception e){
				out.println("ERROR: "+e.getClass()+" - "+e.getMessage()+" - "+e.getStackTrace());
				e.printStackTrace();
			}
			out.println("DONE.");
			
		}else{
			out.println("ERROR: Content type must be 'application/json' and charset must be 'UTF-8'");
		}
		
		out.close();
	}

}
