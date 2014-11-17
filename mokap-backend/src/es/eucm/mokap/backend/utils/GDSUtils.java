package es.eucm.mokap.backend.utils;

import java.util.List;

import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;

public class GDSUtils {
	/**
	 * Stores the received entity in GDS and adds it to the search index.
	 * @param ent
	 */
	public static Key store(Entity ent) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Key k = datastore.put(ent);
		
		addToSearchIndex(ent, k);
		return k;
		
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
		.addField(Field.newBuilder().setName("namevalues").setText(ent.getProperty("namevalues").toString()))
		.addField(Field.newBuilder().setName("tagsValues").setText(ent.getProperty("tagsValues").toString()))
		.addField(Field.newBuilder().setName("descriptionvalues").setText(ent.getProperty("descriptionvalues").toString()))
		.addField(Field.newBuilder().setName("dskey").setText(k.getId()+""))
		.build();
		//Add the Document instance to the Search Index
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("RepoElement").build();
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		
		index.put(newDoc);
	}
	/**
	 * Gets the RepoElement that corresponds to the given key or null if there is none
	 * @param keyId
	 * @return
	 */
	public static Entity RepoElementByKey(long keyId) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();	
		Key k = KeyFactory.createKey("RepoElement", keyId);
		Filter keyFilter =
				  new FilterPredicate(Entity.KEY_RESERVED_PROPERTY,
				                      FilterOperator.EQUAL,
				                      k);
		Query q =  new Query("RepoElement").setFilter(keyFilter);
		Entity result = datastore.prepare(q).asSingleEntity();
		
		return result;
	}


}
