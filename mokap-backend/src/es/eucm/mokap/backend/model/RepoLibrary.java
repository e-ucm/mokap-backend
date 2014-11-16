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

/**
 * A simple editor component to describe libraries of elements that are meant to
 * be shared together and reused through the repository.
 * 
 */
@Generated("org.jsonschema2pojo")
public class RepoLibrary{

	/**
	 * Relative path of the zip file in the server that contains the library
	 * 
	 */
	private String path;
	/**
	 * Relative url where the thumbnail for this resource library is placed
	 * 
	 */
	private String thumbnail;
	/**
	 * Information related to an author
	 * 
	 */
	private RepoAuthor author;
	private List<RepoLicense> licenses = new LinkedList<RepoLicense>();
	/**
	 * The number of resources (objects, characters, etc.) the library contains
	 * 
	 */
	private float numberOfElements;
	/**
	 * The downloadable amount of mega bytes of the zip file associated to the
	 * library. If -1, the size is unknown
	 * 
	 */
	private float size;
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
	 * Relative path of the zip file in the server that contains the library
	 * 
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Relative path of the zip file in the server that contains the library
	 * 
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Relative url where the thumbnail for this resource library is placed
	 * 
	 */
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * Relative url where the thumbnail for this resource library is placed
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

	public List<RepoLicense> getLicenses() {
		return licenses;
	}

	public void setLicenses(List<RepoLicense> licenses) {
		this.licenses = licenses;
	}

	/**
	 * The number of resources (objects, characters, etc.) the library contains
	 * 
	 */
	public float getNumberOfElements() {
		return numberOfElements;
	}

	/**
	 * The number of resources (objects, characters, etc.) the library contains
	 * 
	 */
	public void setNumberOfElements(float numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	/**
	 * The downloadable amount of mega bytes of the zip file associated to the
	 * library. If -1, the size is unknown
	 * 
	 */
	public float getSize() {
		return size;
	}

	/**
	 * The downloadable amount of mega bytes of the zip file associated to the
	 * library. If -1, the size is unknown
	 * 
	 */
	public void setSize(float size) {
		this.size = size;
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

}
