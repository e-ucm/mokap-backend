/**
 *  Copyright [2014] [mokap.es]
 *
 *    This file is part of the mokap community backend (MCB).
 *    MCB is licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package es.eucm.mokap.backend.model.search;

import es.eucm.ead.schemax.repo.RepoElementFields;

/**
 * Created by mario on 15/12/2014. This class represents the parameters needed
 * to retrieve an element by its Id
 */
public class IdSearchParams extends SearchParams {

	private long id;

	/**
	 * Standard constructor for an id based search
	 * 
	 * @param id
	 *            Id of the element we want to retrieve
	 */
	public IdSearchParams(long id, String width, String height) {
		super(null);
		this.id = id;
		this.width = width;
		this.height = height;
	}

	@Override
	public String getSearchQuery() {
		return RepoElementFields.ENTITYREF + " = " + id;
	}
}
