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

/**
 * Created by mario on 09/12/2014. Parent class for the different search
 * parameter options
 */
public abstract class SearchParams {
	protected String searchCursor;

	public SearchParams(String searchCursor) {
		this.searchCursor = searchCursor;
	}

	public String getSearchCursor() {
		return searchCursor;
	}

	public void setSearchCursor(String searchCursor) {
		this.searchCursor = searchCursor;
	}

	/**
	 * Generates the query we'll use to retrieve the search results from the
	 * database
	 * 
	 * @return Query String
	 */
	public abstract String getSearchQuery();
}
