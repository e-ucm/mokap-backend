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

import es.eucm.mokap.backend.model.FeaturedCategories;

/**
 * Created by mario on 15/12/2014. This class represents the parameters of a
 * search for admin content. We only need the topic of the admin content group
 * we'll be retrieving.
 */
public class FeaturedSearchParams extends SearchParams {
	private String featuredName;

	/**
	 * Constructor for the admin search
	 * 
	 * @param featuredName
	 *            Name of a admin group. The search will try to find this string
	 *            in the admin field of all the elements in the index.
	 */
	public FeaturedSearchParams(String featuredName) {
		super(null);
		this.featuredName = featuredName;
	}

	@Override
	public String getSearchQuery() {
		if (featuredName.equals(FeaturedCategories.ALL.toString())) {
			String q = "";
			int i = 0;
			for (FeaturedCategories s : FeaturedCategories.values()) {
				if (s != FeaturedCategories.ALL) {
					if (i != 0) {
						q += " OR ";
					}
					q += "featured : " + s.toString();
					i++;
				}
			}
			return q;
		}
		return "featured : " + featuredName; // TODO Add admin to
												// RepoElementFields
	}
}
