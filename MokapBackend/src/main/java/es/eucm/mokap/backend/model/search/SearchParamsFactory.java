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

import javax.servlet.http.HttpServletRequest;

import es.eucm.ead.schemax.repo.RepoRequestFields;
import es.eucm.ead.schemax.repo.SearchRequestFields;
import es.eucm.mokap.backend.utils.Utils;

/**
 * 
 * Created by mario on 10/12/2014. Factory class for the SearchParams classes
 */
public class SearchParamsFactory {

	/**
	 * Creates a SearchParams child object of the type that corresponds to the
	 * parameters we get in the request
	 * 
	 * @param req
	 *            request to analyze and extract the params
	 * @return SearchParams object
	 */
	public static SearchParams create(HttpServletRequest req) {
		String width = Utils.getParameterValue(RepoRequestFields.W, req);
		String height = Utils.getParameterValue(RepoRequestFields.H, req);
		if (width == null) {
			width = SearchParams.DEFAULT_WIDTH;
		}
		if (height == null) {
			height = SearchParams.DEFAULT_HEIGHT;
		}

		if (Utils.getParameterValue("f", req) != null) { // TODO Add f to
			// SearchRequestFields or
			// RepoRequestFields
			return new FeaturedSearchParams(Utils.getParameterValue("f", req),
					width, height);
		} else if (Utils.getParameterValue("id", req) != null) { // TODO Add id
																	// to
			// SearchRequestFields,
			// Control the
			// exception in case
			// we get a String
			// that is not valid
			long id = Long.parseLong(Utils.getParameterValue("id", req));
			return new IdSearchParams(id, width, height);
		} else if (Utils.getParameterValue("md", req) != null) { // Most
																	// Downloaded
			return new MostDownloadedSearchParams(Utils.getParameterValue("md",
					req)); // with
							// parameter
							// for
							// the
							// time
							// span
							// we're
							// analyzing
		} else {
			return new TextSearchParams(Utils.getParameterValue(
					SearchRequestFields.Q, req), Utils.getParameterValue(
					RepoRequestFields.P, req), Utils.getParameterValue(
					RepoRequestFields.L, req), Utils.getParameterValue(
					RepoRequestFields.CAT, req), Utils.getParameterValue(
					RepoRequestFields.T, req), Utils.getParameterValue(
					SearchRequestFields.C, req), width, height);
		}
	}

	/**
	 * Creates a SearchParams subclass with information to retrieve the featured
	 * elements tagged as the filter provided
	 * 
	 * @param filter
	 *            Tag in the featured field of the entity
	 * @return SearchParams object
	 */
	public static SearchParams createFeaturedSearch(String filter) {
		return new FeaturedSearchParams(filter, SearchParams.DEFAULT_WIDTH,
				SearchParams.DEFAULT_HEIGHT);
	}

	/**
	 * Creates a SearchParams subclass with information to retrieve an element
	 * by its id
	 * 
	 * @param id
	 *            Id of the element to retreive
	 * @return SearchParams object
	 */
	public static SearchParams createIdSearch(long id) {
		return new IdSearchParams(id, SearchParams.DEFAULT_WIDTH,
				SearchParams.DEFAULT_HEIGHT);
	}

}
