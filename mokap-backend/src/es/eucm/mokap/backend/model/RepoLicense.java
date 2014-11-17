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

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public enum RepoLicense {

	PUBLIC_DOMAIN("public-domain"), CC_BY("cc-by"), CC_BY_ND("cc-by-nd"), CC_BY_SA(
			"cc-by-sa"), CC_BY_NC("cc-by-nc"), CC_BY_ND_NC("cc-by-nd-nc"), CC_BY_SA_NC(
			"cc-by-sa-nc"), LINK_AUTHOR("link-author"), LEARNING_ONLY(
			"learning-only"), UNDEFINED("undefined");
	private String value;
	private static Map<String, RepoLicense> constants = new HashMap<String, RepoLicense>();

	static {
		for (RepoLicense c : values()) {
			constants.put(c.value, c);
		}
	}

	private RepoLicense(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public static RepoLicense fromValue(String value) {
		RepoLicense constant = constants.get(value);
		if (constant == null) {
			throw new IllegalArgumentException(value);
		} else {
			return constant;
		}
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
}
