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
import es.eucm.mokap.backend.utils.JSONTranslator;
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
		SearchResult res = new SearchResult();
		// Get the search string from the header
		String searchstring = req.getHeader("searchstring");
		// Select the search index to use				
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("RepoElement").build();
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		
		Results<ScoredDocument> results = index.search(searchstring);
		res.setNumResults(results.getNumberFound());
		
		// Iterate the results and find the corresponding entities
		for(ScoredDocument sd : results){
			long keyId = Long.parseLong(sd.getOnlyField("dskey").getText());
			
			Entity ent = GDSUtils.RepoElementByKey(keyId);
			if(ent != null){
				RepoElement elm = JSONTranslator.repoElementFromEntity(ent);
				res.addElement(elm);
			}
		}
		
		// Emit the response in JSON
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		
		
		String jsonString = JSONTranslator.JSONfromObject(res);
		
		
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
			// We parse the JSON text in the body of the request
			StringBuffer jb = new StringBuffer();
			String line = null;
			try {
			    BufferedReader reader = req.getReader();
			    while ((line = reader.readLine()) != null)
			      jb.append(line);
			} catch (Exception e) { out.println(e.getMessage()); }
			
			try{
			// Create a DS Entity with the data and store its contents.
			out.println("Translating JSON...");
			Entity ent = JSONTranslator.EntityFromJSON(new String(jb));		
			out.println("Storing Object...");
			Key k = GDSUtils.store(ent);
			out.println("Stored with Key ["+k.getId()+"]");
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
