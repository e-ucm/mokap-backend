package es.eucm.mokap.backend.utils;

import java.util.Map;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Document.Builder;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;

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
		Document newDoc = b.build();
		//Add the Document instance to the Search Index
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("Resource").build();
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);		
		
		index.put(newDoc);
	}
	
	
}
