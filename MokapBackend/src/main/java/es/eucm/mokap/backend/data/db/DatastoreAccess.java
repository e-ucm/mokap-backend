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
package es.eucm.mokap.backend.data.db;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.search.Cursor;
import com.google.appengine.api.search.*;
import com.google.appengine.api.search.Index;

import es.eucm.ead.schemax.repo.RepoElementFields;
import es.eucm.mokap.backend.model.search.SearchParams;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class manages all the interaction between the service and Google
 * Datastore
 */
public class DatastoreAccess implements DatabaseInterface {

	private static DatastoreService ds = DatastoreServiceFactory
			.getDatastoreService();

	@Override
	public Results<ScoredDocument> searchByString(SearchParams sp)
			throws IOException {
		String cursorString = sp.getSearchCursor();
		Cursor cursor;
		if (cursorString != null) {
			cursor = Cursor.newBuilder().build(cursorString);
		} else {
			cursor = Cursor.newBuilder().build();
		}
		com.google.appengine.api.search.Query query = com.google.appengine.api.search.Query
				.newBuilder()
				.setOptions(QueryOptions.newBuilder().setCursor(cursor).build())
				.build(sp.getSearchQuery());

		IndexSpec indexSpec = IndexSpec.newBuilder().setName("Resource")
				.build();
		Index index = SearchServiceFactory.getSearchService().getIndex(
				indexSpec);
		// Perform the search
		Results<ScoredDocument> results = index.search(query);
		return results;
	}

	@Override
	public Key storeEntity(Entity ent) {
		return ds.put(ent);
	}

	@Override
	public void deleteEntity(long keyId) {
		Key k = KeyFactory.createKey("Resource", keyId);
		ds.delete(k);
	}

	@Override
	public Map<String, Object> getEntityByIdAsMap(long keyId) {
		Map<String, Object> m = new HashMap<String, Object>();

		Key k = KeyFactory.createKey("Resource", keyId);
		com.google.appengine.api.datastore.Query.Filter keyFilter = new com.google.appengine.api.datastore.Query.FilterPredicate(
				Entity.KEY_RESERVED_PROPERTY,
				com.google.appengine.api.datastore.Query.FilterOperator.EQUAL,
				k);
		com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query(
				"Resource").setFilter(keyFilter);
		Entity result = ds.prepare(q).asSingleEntity();
		if (result != null) {
			Map<String, Object> props = result.getProperties();
			for (String key : props.keySet())
				m.put(key, props.get(key));
		}
		return m;
	}

	@Override
	public void addToSearchIndex(Entity ent, Key k) {
		// Build a Document Object
		// Add all the attributes on which search can be done
		Map<String, Object> m = ent.getProperties();
		String keyString = k.getId()+"";
		addToSearchIndex(m,keyString);
	}

	/**
	 * Adds the properties in a map to a Index Document. Sets the RepoElementFields.ENTITYREF property with the string received.
	 * @param m Map with the properties
	 * @param k Id to set in the RepoElementFields.ENTITYREF property
	 */
	private void addToSearchIndex(Map<String, Object> m, String k){
		Document.Builder b = Document.newBuilder();

		for (String key : m.keySet()) {
			b.addField(Field.newBuilder().setName(key)
					.setText(m.get(key).toString()));
		}
		b.addField(Field.newBuilder().setName(RepoElementFields.ENTITYREF)
				.setText(k));
		Document newDoc = b.build();
		// Add the Document instance to the Search Index
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("Resource")
				.build();
		Index index = SearchServiceFactory.getSearchService().getIndex(
				indexSpec);

		index.put(newDoc);
	}

	@Override
	public void updateIndexDocument(Map<String, Object> m, String documentId, String entityRef) {
		Document.Builder b = Document.newBuilder();

		for (String key : m.keySet()) {
			b.addField(Field.newBuilder().setName(key)
					.setText(m.get(key).toString()));
		}
		b.addField(Field.newBuilder().setName(RepoElementFields.ENTITYREF)
				.setText(entityRef));
		b.setId(documentId);
		Document newDoc = b.build();
		// Add the Document instance to the Search Index
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("Resource")
				.build();
		Index index = SearchServiceFactory.getSearchService().getIndex(
				indexSpec);
		index.put(newDoc);
	}

	/**
	 * Removes a document from the index given its Id.
	 * @param documentId Id of the document (NOTE: Not the id of the entity)
	 */
	private void removeIndexDocument(String documentId) {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("Resource")
				.build();
		Index index = SearchServiceFactory.getSearchService().getIndex(
				indexSpec);
		index.delete(documentId);
	}


}
