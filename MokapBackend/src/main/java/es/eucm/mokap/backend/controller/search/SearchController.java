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
package es.eucm.mokap.backend.controller.search;

import es.eucm.mokap.backend.model.search.SearchParams;

import java.io.IOException;

/**
 * Created by mario on 03/12/2014.
 */
public interface SearchController {
	/**
	 * Performs a Datastore simple search given a string with the search terms.
	 * The search returns up to 20 results for that search. If there are more
	 * results to be retrieved, it also returns a cursor string (see
	 * 
	 * <pre>
	 * <a href="https://cloud.google.com/appengine/docs/java/datastore/queries?hl=Java_Query_cursors#Java_Query_cursors">
	 * </a>
	 * </pre>
	 * 
	 * ) to allow the client to continue iterating the search results. If
	 * searchCursor is empty, the method will assume it's a first query. The
	 * results and cursor string are embedded into a JSON String of the type
	 * es.eucm.mokap.model.response.SearchResponse.
	 * 
	 * @param sp
	 *            SearchFilters object with all the needed parameters to perform
	 *            the search
	 * @throws java.io.IOException
	 *             When the search can't access Datastore or the Search Index
	 *             for some reason
	 * @return JSON String of the type
	 *         es.eucm.mokap.model.response.SearchResponse
	 */
	String performSearch(SearchParams sp) throws IOException;
}
