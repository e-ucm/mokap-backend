package es.eucm.mokap.backend.utils;

import java.util.HashMap;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Document.Builder;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;

import es.eucm.mokap.backend.model.response.GetResponse;

public class GoogleUtils {
	/**
	 * Adds an entity to the search index.
	 * @param ent
	 * @param k 
	 */
	public static void addToSearchIndex(Entity ent, Key k) {
		//Build a Document Object
		//Add all the attributes on which search can be done
		Builder b = Document.newBuilder();
		
		Map<String,Object> m = ent.getProperties();
		
		for(String key : m.keySet()){
			b.addField(Field.newBuilder().setName(key).setText(m.get(key).toString()));
		} 
		b.addField(Field.newBuilder().setName("entityRef").setText(k.getId()+""));
		Document newDoc = b.build();
		//Add the Document instance to the Search Index
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("Resource").build();
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);		
		
		index.put(newDoc);
	}
	/**
	 * Searches the index for occurrences of the string received 
	 * @param searchString
	 * @return A GetResponse object
	 */
	public static GetResponse searchByString(String searchString) {
		GetResponse gr = new GetResponse();
		// Select the search index to use		
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("Resource").build();
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		// Perform the search
		Results<ScoredDocument> results = index.search(searchString);
		gr.setCount(results.getNumberReturned());
		
		// Iterate the results and find the corresponding entities
		for(ScoredDocument sd : results){
			long keyId = Long.parseLong(sd.getOnlyField("entityRef").getText());
			
			Map<String, String> ent = getEntityById(keyId);
			gr.addResult(ent);
		}
		return gr;
	}
	
	/**
	 * Returns the entity referenced by the keyId in Datastore
	 * @param keyId
	 * @return
	 */
	public static Map<String,String> getEntityById(long keyId) {
		Map<String,String> m = new HashMap<String,String>();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();	
		Key k = KeyFactory.createKey("RepoElement", keyId);
		Filter keyFilter =
				  new FilterPredicate(Entity.KEY_RESERVED_PROPERTY,
				                      FilterOperator.EQUAL,
				                      k);
		Query q =  new Query("Resource").setFilter(keyFilter);
		Entity result = datastore.prepare(q).asSingleEntity();
		Map<String,Object> props = result.getProperties();
		for(String key : props.keySet())
			m.put(key, props.get(key).toString());
		return m;
	}
	
	
}
