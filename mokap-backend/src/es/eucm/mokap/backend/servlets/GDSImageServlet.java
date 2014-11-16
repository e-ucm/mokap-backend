package es.eucm.mokap.backend.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;

import es.eucm.mokap.backend.model.RepoElement;
import es.eucm.mokap.backend.model.search.SearchResult;
import es.eucm.mokap.backend.utils.GDSUtils;
import es.eucm.mokap.backend.utils.JSONTranslator;

public class GDSImageServlet extends HttpServlet {

	private static final long serialVersionUID = -3178885200866195963L;
	
	/**
	 * Method: GET
	 * Expected headers:
	 * 	searchstring: string to perform the search.
	 * Returns the entity search results.
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
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
			long keyId = Long.parseLong(sd.getOnlyField("sdkey").getText());
			RepoElement elm = GDSUtils.RepoElementByKey(keyId);
			res.addElement(elm);
		}
		
		// Emit the response in JSON
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		
		PrintWriter out = resp.getWriter();		
		out.print(JSONTranslator.JSONfromObject(res));
		out.flush();
		
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
			
			// Create a DS Entity with the data and store its contents.
			Entity ent = JSONTranslator.EntityFromJSON(new String(jb));			
			GDSUtils.store(ent);
			
		}else{
			out.println("ERROR: Content type must be 'application/json' and charset must be 'UTF-8'");
		}
		
		out.close();
	}

}
