package es.eucm.mokap.backend.utils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;

public class GDSUtils {
	/**
	 * Stores the received entity in GDS and adds it to the search index.
	 * @param ent
	 */
	public static void store(Entity ent) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		addToSearchIndex(ent);
		datastore.put(ent);
	}
	/**
	 * Adds an entity to the search index.
	 * @param ent
	 */
	private static void addToSearchIndex(Entity ent) {
		//Build a Document Object
		//Add all the attributes on which search can be done
		Document newDoc = Document.newBuilder()
		.addField(Field.newBuilder().setName("title").setText(ent.getProperty("title").toString()))
		.addField(Field.newBuilder().setName("tags").setText(ent.getProperty("tags").toString()))
		.build();
		//Add the Document instance to the Search Index
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("RepoElement").build();
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		
		index.put(newDoc);
	}


}
