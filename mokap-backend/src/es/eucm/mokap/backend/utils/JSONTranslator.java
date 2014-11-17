package es.eucm.mokap.backend.utils;

import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.eucm.mokap.backend.model.I18NString;
import es.eucm.mokap.backend.model.I18NStrings;
import es.eucm.mokap.backend.model.RepoElement;

public class JSONTranslator {
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
	 * Converts a RepoElement Object in a GDS entity
	 * @param relm
	 * @return
	 */
	public static Entity entityFromRepoElement(RepoElement relm){			
		Entity ent = new Entity("RepoElement");	
		ent.setProperty("thumbnail",relm.getThumbnail());
		ent.setProperty("authorname", relm.getAuthor().getName());
		ent.setProperty("authorurl", relm.getAuthor().getUrl());		
		ent.setProperty("license", relm.getLicense().toString());
		ent.setProperty("width", relm.getWidth());
		ent.setProperty("height", relm.getHeight());
		
		List<String> descriptionLangs = new LinkedList<String>();		
		List<String> descriptionValues = new LinkedList<String>();
		for(I18NString s : relm.getDescription().getStrings()){
			descriptionLangs.add(s.getLang());
			descriptionValues.add(s.getValue());
		}					
		ent.setProperty("descriptionlangs", descriptionLangs);
		ent.setProperty("descriptionvalues", descriptionValues);
		
		List<String> nameLangs = new LinkedList<String>();
		List<String> nameValues = new LinkedList<String>();
		for(I18NString s : relm.getName().getStrings()){
			nameLangs.add(s.getLang());
			nameValues.add(s.getValue());		
		}			
		ent.setProperty("namelangs",nameLangs);
		ent.setProperty("namevalues",nameValues);
		
		List<String> tagLangs = new LinkedList<String>();
		List<String> tagValues = new LinkedList<String>();
		List<Integer> tagStringCounts = new LinkedList<Integer>();
		for(I18NStrings tag : relm.getTags()){	
			int i = 0;
			for(I18NString t : tag.getStrings()){
				tagLangs.add(t.getLang());
				tagValues.add(t.getValue());	
				i++;
			}	
			tagStringCounts.add(i);
		}
		ent.setProperty("tagsLangs", tagLangs);
		ent.setProperty("tagsValues", tagValues);
		ent.setProperty("tagStringCounts", tagStringCounts);
	
		return ent;
	}
	
	/**
	 * Converts a JSON string into a RepoElement and then parses it into a GDS Entity.
	 * @param string
	 * @return
	 */
	public static Entity EntityFromJSON(String string){		
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
