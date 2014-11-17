package es.eucm.mokap.backend.utils;

import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.eucm.mokap.backend.model.I18NString;
import es.eucm.mokap.backend.model.I18NStrings;
import es.eucm.mokap.backend.model.RepoAuthor;
import es.eucm.mokap.backend.model.RepoElement;
import es.eucm.mokap.backend.model.RepoLibrary;
import es.eucm.mokap.backend.model.RepoLicense;

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
	/**
	 * Parses a RepoElement from a GDS Entity.
	 * @param ent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static RepoElement repoElementFromEntity(Entity ent) {
		RepoElement elm = new RepoElement();
		
		RepoAuthor aut = new RepoAuthor();
		aut.setName(ent.getProperty("authorname").toString());
		aut.setUrl(ent.getProperty("authorurl").toString());
		elm.setAuthor(aut);
		
		RepoLicense lic = RepoLicense.fromValue(ent.getProperty("license").toString());
		elm.setLicense(lic);
		
		elm.setHeight((double)ent.getProperty("height"));
		elm.setWidth((double)ent.getProperty("width"));
		
		elm.setThumbnail(ent.getProperty("thumbnail").toString());
		
		I18NStrings desc = new I18NStrings();
		List<String> descLangs = (List<String>)ent.getProperty("descriptionlangs");
		List<String> descVals = (List<String>)ent.getProperty("descriptionvalues");		
		for(int i = 0; i<descLangs.size();i++){
			I18NString s = new I18NString();
			s.setLang(descLangs.get(i));
			s.setValue(descVals.get(i));
			desc.addString(s);
		}
		elm.setDescription(desc);
		
		I18NStrings name = new I18NStrings();
		List<String> nameLangs = (List<String>)ent.getProperty("namelangs");
		List<String> nameVals = (List<String>)ent.getProperty("namevalues");		
		for(int i = 0; i<nameLangs.size();i++){
			I18NString s = new I18NString();
			s.setLang(nameLangs.get(i));
			s.setValue(nameVals.get(i));
			name.addString(s);
		}
		elm.setDescription(name);
		
		List<I18NStrings> tags = new LinkedList<I18NStrings>();
		List<String> tagLangs = (List<String>)ent.getProperty("tagsLangs");
		List<String> tagVals = (List<String>)ent.getProperty("tagsValues");
		List<Long> tagStrCount = (List<Long>)ent.getProperty("tagStringCounts");
		//lista general de strings
		List<I18NString> strings = new LinkedList<I18NString>();
		for(int i=0;i<tagLangs.size();i++){
			I18NString s = new I18NString();
			s.setLang(tagLangs.get(i));
			s.setValue(tagVals.get(i));
			strings.add(s);
		}
		//separamos por distintos tags
		int i = 0;
		for(long numStrings : tagStrCount){
			I18NStrings tS = new I18NStrings();
			for(int j = 0; j < numStrings; j++){
				tS.addString(strings.get(i));
				i++;
			}
			tags.add(tS);
		}
		elm.setTags(tags);
		
		
		return elm;
	}

}
