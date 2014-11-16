package es.eucm.mokap.backend.utils;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;

import es.eucm.mokap.backend.model.RepoElement;

public class GDSUtils {
	/**
	 * Stores the received entity in GDS and adds it to the search index.
	 * @param ent
	 */
	public static void store(Entity ent) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Key k = datastore.put(ent);
		
		addToSearchIndex(ent, k);
		
	}
	/**
	 * Adds an entity to the search index.
	 * @param ent
	 * @param k 
	 */
	private static void addToSearchIndex(Entity ent, Key k) {
		//Build a Document Object
		//Add all the attributes on which search can be done
		Document newDoc = Document.newBuilder()
		.addField(Field.newBuilder().setName("title").setText(ent.getProperty("title").toString()))
		.addField(Field.newBuilder().setName("tags").setText(ent.getProperty("tags").toString()))
		.addField(Field.newBuilder().setName("dskey").setText(k.getId()+""))
		.build();
		//Add the Document instance to the Search Index
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("RepoElement").build();
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		
		index.put(newDoc);
	}
	public static RepoElement RepoElementByKey(long keyId) {
		// TODO Auto-generated method stub
		return null;
	}


}
