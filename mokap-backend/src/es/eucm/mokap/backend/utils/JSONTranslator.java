package es.eucm.mokap.backend.utils;

import java.io.StringReader;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import es.eucm.mokap.backend.model.RepoElement;

public class JSONTranslator {
	/**
	 * Parses a JSON string into a RepoElement Object
	 * @param jsonString
	 * @return
	 */
	public static RepoElement repoElementFromJSON(String jsonString){
		Gson gson = new GsonBuilder().create();
		JsonReader reader = new JsonReader(new StringReader(jsonString));
		reader.setLenient(true);
		RepoElement relm = gson.fromJson(reader,RepoElement.class);	
		return relm;
	}
	/**
	 * Converts a RepoElement Object in a GDS entity
	 * @param relm
	 * @return
	 */
	public static Entity entityFromRepoElement(RepoElement relm){			
		Entity ent = new Entity("RepoElement");	
		ent.setProperty("thumbnail",relm.getThumbnail());
		ent.setProperty("authorname", relm.getAuthor().getName());
		ent.setProperty("authorurl", relm.getAuthor().getUrl());		
		ent.setProperty("license", relm.getLicense());
		ent.setProperty("width", relm.getWidth());
		ent.setProperty("height", relm.getHeight());
		ent.setProperty("description", relm.getDescription().getStrings());
		ent.setProperty("name",relm.getName().getStrings());
		ent.setProperty("tags", relm.getTags());
	
		return ent;
	}
	
	/**
	 * Converts a JSON string into a RepoElement and then parses it into a GDS Entity.
	 * @param string
	 * @return
	 */
	public static Entity EntityFromJSON(String string) throws Exception{
		
		
		RepoElement relm = repoElementFromJSON(string);
		
		return entityFromRepoElement(relm);
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

}
