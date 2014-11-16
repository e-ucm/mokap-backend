package es.eucm.mokap.backend.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

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
import com.google.appengine.api.search.SearchServiceFactory;

public class GDSImageServlet extends HttpServlet {

	private static final long serialVersionUID = -3178885200866195963L;
	
	/**
	 * Method: GET
	 * Returns the entity search results.
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO 
		PrintWriter out = resp.getWriter();
		out.print("<p>Error: The request method <code>"+req.getMethod()+"</code> is not yet implemented. <code>"+req.getRequestURI()+"</code></p>");
		out.close();
	}
	
	/**
	 * Method: POST
	 * Adds a new entity to the DB and stores its data in the search index.
	 * @throws IOException 
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO
		
		PrintWriter out = resp.getWriter();				
		
		
		out.close();
	}

}
