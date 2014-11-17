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

import javax.annotation.Generated;

/**
 * An internationalized String (One language only)
 * 
 */
@Generated("org.jsonschema2pojo")
public class I18NString {

	/**
	 * en_EN / es_ES / en_UK ...
	 * 
	 */
	private String lang;
	/**
	 * The value of the string for this specific language
	 * 
	 */
	private String value;

	/**
	 * en_EN / es_ES / en_UK ...
	 * 
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * en_EN / es_ES / en_UK ...
	 * 
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * The value of the string for this specific language
	 * 
	 */
	public String getValue() {
		return value;
	}

	/**
	 * The value of the string for this specific language
	 * 
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
