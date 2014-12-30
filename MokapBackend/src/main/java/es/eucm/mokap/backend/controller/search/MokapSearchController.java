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

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;

import es.eucm.ead.schemax.repo.RepoElementFields;
import es.eucm.mokap.backend.controller.BackendController;
import es.eucm.mokap.backend.controller.insert.UploadZipStructure;
import es.eucm.mokap.backend.model.response.SearchResponse;
import es.eucm.mokap.backend.model.search.SearchParams;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Specific controller class for the resource search service.
 */
public class MokapSearchController extends BackendController implements
		SearchController {

	/**
	 * Performs a Datastore simple search given a string with the search terms.
	 * The search returns up to 20 results for that search. If there are more
	 * results to be retrieved, it also returns a cursor string (
	 * 
	 * <pre>
	 * <a href="https://cloud.google.com/appengine/docs/java/datastore/queries?hl=Java_Query_cursors#Java_Query_cursors">
	 * https://cloud.google.com/appengine/docs/java/datastore/queries?hl=Java_Query_cursors#Java_Query_cursors
	 * </a>
	 * </pre>
	 * 
	 * ) to allow the client to continue iterating the search results. If
	 * searchCursor is empty, the method will assume it's a first query. The
	 * results and cursor string are embedded into a JSON String of the type
	 * es.eucm.mokap.model.response.SearchResponse.
	 * 
	 * @param sp
	 *            Object containing all the parameters we need to make the
	 *            search
	 * @throws java.io.IOException
	 *             When the search can't access Datastore or the Search Index
	 *             for some reason
	 * @return JSON String of the type
	 *         es.eucm.mokap.model.response.SearchResponse
	 */
	@Override
	public String performSearch(SearchParams sp) throws IOException {
		SearchResponse gr = new SearchResponse();
		Results<ScoredDocument> results = db.searchByString(sp);
		if (results.getCursor() != null)
			gr.setSearchCursor(results.getCursor().toWebSafeString());
		gr.setTotal(results.getNumberFound());
		gr.setSearchString(sp.getSearchQuery());

		// Iterate the results and find the corresponding entities
		fillResults(sp.getWidth(), sp.getHeight(), gr, results);
		String str = gr.toJsonString();
		return str;
	}

	/**
	 * Fills a SearchResponse with a set of search results In case of error in
	 * the addition of one entity, we just skip it and substract one from the
	 * total so it's coherent.
	 * 
	 * @param gr
	 *            SearchResponse object we need to fill
	 * @param results
	 *            List of results we're processing
	 */
	private void fillResults(String width, String height, SearchResponse gr,
			Results<ScoredDocument> results) {
		for (ScoredDocument sd : results) {
			try {
				long keyId = Long.parseLong(sd.getOnlyField(
						RepoElementFields.ENTITYREF).getText());
				Map<String, Object> ent = db.getEntityByIdAsMap(keyId);
				prepareResponseEntity(keyId, width, height, ent);

				gr.addResult(ent);
			} catch (Exception e) {
				gr.setTotal(gr.getTotal() - 1);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Calculates and adds the missing fields to the entity we're sending to the
	 * user See
	 * 
	 * <pre>
	 * <a href="https://cloud.google.com/appengine/docs/java/javadoc/com/google/appengine/api/datastore/Entity">
	 * https://cloud.google.com/appengine/docs/java/javadoc/com/google/appengine/api/datastore/Entity
	 * </a> 
	 * and
	 * <a href="https://cloud.google.com/appengine/docs/java/javadoc/com/google/appengine/api/datastore/Key">
	 * https://cloud.google.com/appengine/docs/java/javadoc/com/google/appengine/api/datastore/Key</a>
	 * </pre>
	 * 
	 * @param keyId
	 *            Key id of the entity we're modifying
	 * @param ent
	 *            Entity we're modifying already converted to hashmap
	 * @throws IOException
	 */
	private void prepareResponseEntity(long keyId, String width, String height,
			Map<String, Object> ent) throws IOException {
		ent.put(RepoElementFields.ENTITYREF, keyId + "");
		String contentsUrl = DOWNLOAD_URL + keyId
				+ UploadZipStructure.ZIP_EXTENSION;
		ent.put(RepoElementFields.CONTENTSURL, contentsUrl);
		List<String> tnsUrls = st.getTnsUrls(keyId);
		long contentBytes = st.getContentsSize(keyId);
		float contentMegabytes = contentBytes / (1024F * 1024F);

		ent.put(RepoElementFields.THUMBNAILURL,
				pickThumbnail(tnsUrls, width, height));
		ent.put(RepoElementFields.CONTENTSSIZE, contentMegabytes);
	}

	/**
	 * Picks the best thumbnail available from the list
	 * 
	 * @param tnsUrls
	 *            for the given preferred
	 * @param width
	 *            and
	 * @param height
	 *            .
	 * @return A thumbnail url from the list where its area (width x height)
	 *         meets the next criteria: (1) Covers the preferred area
	 *         (width>=preferredWidth && height>=preferredHeight) and (2) The
	 *         difference with the preferred area (width*height -
	 *         preferredWidth*preferredHeight) is minimum
	 * 
	 *         If no thumbnail meets condition (1), then the thumbnail with
	 *         closest area to the preferred thumbnail is returned
	 */
	public String pickThumbnail(List<String> tnsUrls, String width,
			String height) {
		int preferredWidth = Integer.parseInt(width);
		int preferredHeight = Integer.parseInt(height);
		if (tnsUrls.size() == 0) {
			return null;
		}

		SelectedThumbnail selectedThumbnail = new SelectedThumbnail();
		SelectedThumbnail option = new SelectedThumbnail();
		selectedThumbnail.set(tnsUrls.get(0), preferredWidth, preferredHeight);
		for (int i = 1; i < tnsUrls.size(); i++) {
			option.set(tnsUrls.get(i), preferredWidth, preferredHeight);
			if (!selectedThumbnail.coversArea
					&& option.coversArea
					|| selectedThumbnail.coversArea == option.coversArea
					&& Math.abs(option.areaDifference) < Math
							.abs(selectedThumbnail.areaDifference)) {
				selectedThumbnail.copy(option);
			}
		}

		return selectedThumbnail.url;
	}

	private static class SelectedThumbnail {
		String url;
		int width;
		int height;
		long areaDifference;
		boolean coversArea;

		public void set(String url, int preferredWidth, int preferredHeight) {
			this.url = url;
			String fileName = url.substring(url.lastIndexOf("/") + 1,
					url.lastIndexOf("."));
			String w = fileName.substring(0, fileName.toLowerCase()
					.indexOf("x"));
			String h = fileName.substring(
					fileName.toLowerCase().indexOf("x") + 1, fileName.length());
			width = Integer.parseInt(w);
			height = Integer.parseInt(h);

			coversArea = width >= preferredWidth && height >= preferredHeight;
			areaDifference = width * height - preferredWidth * preferredHeight;
		}

		public void copy(SelectedThumbnail otherThumbnail) {
			this.url = otherThumbnail.url;
			this.width = otherThumbnail.width;
			this.height = otherThumbnail.height;
			this.areaDifference = otherThumbnail.areaDifference;
			this.coversArea = otherThumbnail.coversArea;
		}
	}
}
