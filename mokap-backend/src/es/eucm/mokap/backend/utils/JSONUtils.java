package es.eucm.mokap.backend.utils;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.eucm.mokap.backend.model.RepoElement;

public class JSONUtils {
	/**
	 * Parses a JSON string into a RepoElement Object
	 * @param jsonString
	 * @return
	 */
	public static RepoElement repoElementFromJSON(String jsonString){
		Gson gson = new GsonBuilder().create();			
		RepoElement relm = gson.fromJson(jsonString,RepoElement.class);	
		return relm;
	}	
	
	/**
	 * Converts a JSON string into a RepoElement and then parses it into a GDS Entity.
	 * @param string
	 * @return
	 */
	public static Entity EntityFromJSON(String string){		
		RepoElement relm = repoElementFromJSON(string);		
		return relm.toGDSEntity();
	}
	/**
	 * Converts any object to a JSON string
	 * @param res
	 * @return
	 */
	public static String JSONfromObject(Object res) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(res);
		return jsonString;
	}	
	
	/**
	 * Parses an HTTP request body into a String
	 * @param req
	 * @return
	 * @throws IOException
	 */
	public static StringBuffer parseRequestBody(HttpServletRequest req)
			throws IOException {
		// We parse the JSON text in the body of the request
		StringBuffer jb = new StringBuffer();
		String line = null;
		
		BufferedReader reader = req.getReader();
		while ((line = reader.readLine()) != null)
		jb.append(line);
		return jb;
	}

}
