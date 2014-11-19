/**
 * eAdventure is a research project of the
 *    e-UCM research group.
 *
 *    Copyright 2005-2014 e-UCM research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          CL Profesor Jose Garcia Santesmases 9,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.mokap.backend.model;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Generated;

import com.google.appengine.api.datastore.Entity;

/**
 * A simple editor component for elements that are meant to be shared and reused
 * through the repository.
 * 
 */
@Generated("org.jsonschema2pojo")
public class RepoElement {

	/**
	 * Relative url where the thumbnail for this element is placed
	 * 
	 */
	private String thumbnail;
	/**
	 * Information related to an author
	 * 
	 */
	private RepoAuthor author;
	/**
	 * Information associated to a license applicable to this resource or
	 * library. Only creative commons and public domain licenses supported.
	 * 
	 */
	private RepoLicense license;
	private double width;
	private double height;
	/**
	 * An internationalized String (support for multiple languages)
	 * 
	 */
	private I18NStrings description;
	/**
	 * An internationalized String (support for multiple languages)
	 * 
	 */
	private I18NStrings name;
	/**
	 * A list of tags - useful for searching and grouping resources
	 * 
	 */
	private List<I18NStrings> tags = new LinkedList<I18NStrings>();

	/**
	 * Relative url where the thumbnail for this element is placed
	 * 
	 */
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * Relative url where the thumbnail for this element is placed
	 * 
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * Information related to an author
	 * 
	 */
	public RepoAuthor getAuthor() {
		return author;
	}

	/**
	 * Information related to an author
	 * 
	 */
	public void setAuthor(RepoAuthor author) {
		this.author = author;
	}

	/**
	 * Information associated to a license applicable to this resource or
	 * library. Only creative commons and public domain licenses supported.
	 * 
	 */
	public RepoLicense getLicense() {
		return license;
	}

	/**
	 * Information associated to a license applicable to this resource or
	 * library. Only creative commons and public domain licenses supported.
	 * 
	 */
	public void setLicense(RepoLicense license) {
		this.license = license;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double d) {
		this.width = d;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double d) {
		this.height = d;
	}

	/**
	 * An internationalized String (support for multiple languages)
	 * 
	 */
	public I18NStrings getDescription() {
		return description;
	}

	/**
	 * An internationalized String (support for multiple languages)
	 * 
	 */
	public void setDescription(I18NStrings description) {
		this.description = description;
	}

	/**
	 * An internationalized String (support for multiple languages)
	 * 
	 */
	public I18NStrings getName() {
		return name;
	}

	/**
	 * An internationalized String (support for multiple languages)
	 * 
	 */
	public void setName(I18NStrings name) {
		this.name = name;
	}

	/**
	 * A list of tags - useful for searching and grouping resources
	 * 
	 */
	public List<I18NStrings> getTags() {
		return tags;
	}

	/**
	 * A list of tags - useful for searching and grouping resources
	 * 
	 */
	public void setTags(List<I18NStrings> tags) {
		this.tags = tags;
	}
	
	/*
	 * Translator methods
	 */
	/**
	 * Generates a GDS Entity from the contents of the RepoElement
	 * @return
	 */
	public Entity toGDSEntity(){
		Entity ent = new Entity("RepoElement");	
		ent.setProperty("thumbnail",this.getThumbnail());
		ent.setProperty("authorname", this.getAuthor().getName());
		ent.setProperty("authorurl", this.getAuthor().getUrl());		
		ent.setProperty("license", this.getLicense().toString());
		ent.setProperty("width", this.getWidth());
		ent.setProperty("height", this.getHeight());
		
		List<String> descriptionLangs = new LinkedList<String>();		
		List<String> descriptionValues = new LinkedList<String>();
		for(I18NString s : this.getDescription().getStrings()){
			descriptionLangs.add(s.getLang());
			descriptionValues.add(s.getValue());
		}					
		ent.setProperty("descriptionlangs", descriptionLangs);
		ent.setProperty("descriptionvalues", descriptionValues);
		
		List<String> nameLangs = new LinkedList<String>();
		List<String> nameValues = new LinkedList<String>();
		for(I18NString s : this.getName().getStrings()){
			nameLangs.add(s.getLang());
			nameValues.add(s.getValue());		
		}			
		ent.setProperty("namelangs",nameLangs);
		ent.setProperty("namevalues",nameValues);
		
		List<String> tagLangs = new LinkedList<String>();
		List<String> tagValues = new LinkedList<String>();
		List<Integer> tagStringCounts = new LinkedList<Integer>();
		for(I18NStrings tag : this.getTags()){	
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
	 * Generates a RepoElement from a given GDS Entity
	 * @param ent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static RepoElement fromGDSEntity(Entity ent){
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
