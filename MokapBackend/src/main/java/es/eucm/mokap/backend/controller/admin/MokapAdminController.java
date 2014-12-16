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
package es.eucm.mokap.backend.controller.admin;

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.users.User;
import es.eucm.ead.schemax.repo.RepoElementFields;
import es.eucm.mokap.backend.controller.BackendController;
import es.eucm.mokap.backend.model.FeaturedCategories;
import es.eucm.mokap.backend.model.search.SearchParams;
import es.eucm.mokap.backend.model.search.SearchParamsFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by mario on 16/12/2014. Controller class for the admin jsp pages that
 * allow to manage some aspects of the app
 */
public class MokapAdminController extends BackendController implements
		AdminController {

	@Override
	public String getFeaturedResourcesAsTable()
			throws IOException {
		String tblStr = "<table><tr><td>Category</td><td>ID</td><td>Title</td></tr>";
		for(FeaturedCategories val : FeaturedCategories.values()){
			String value = val.getValue();
			SearchParams sp = SearchParamsFactory.createFeaturedSearch(value);

			Results<ScoredDocument> results = db.searchByString(sp);

			for (ScoredDocument sd : results) {
				try {
					long keyId = Long.parseLong(sd.getOnlyField(
							RepoElementFields.ENTITYREF).getText());
					Map<String, Object> ent = db.getEntityByIdAsMap(keyId);
					tblStr += "<tr>";
					tblStr += value;
					tblStr += "<td>" + ent.get(RepoElementFields.ENTITYREF)
							+ "</td>";
					tblStr += "<td>" + ent.get(RepoElementFields.NAMELIST)
							+ "</td>";

					tblStr += "</tr>";

				} catch (Exception e) {
					e.printStackTrace();
				}
			}



		}






		return tblStr += "</table>";
	}

	@Override
	public boolean checkAllowedUser(User user) {
		return true;
	}
}
