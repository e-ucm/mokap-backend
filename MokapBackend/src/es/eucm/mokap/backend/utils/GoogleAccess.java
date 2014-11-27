package es.eucm.mokap.backend.utils;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
import com.google.appengine.api.search.Cursor;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.Document.Builder;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.ListItem;
import com.google.appengine.tools.cloudstorage.ListOptions;
import com.google.appengine.tools.cloudstorage.ListResult;
import com.google.gwt.thirdparty.guava.common.io.ByteStreams;

import es.eucm.mokap.backend.model.response.SearchResponse;

public class GoogleAccess {
	private String bucketName;
	private DatastoreService ds;
	private GcsService gcs;
	
	static final String DOWNLOAD_URL = System.getProperty("backend.BASE_URL")+"download?filename=";
	
	public GoogleAccess(String bucketName){
		this.bucketName = bucketName;
		this.ds = DatastoreServiceFactory.getDatastoreService();
		this.gcs = GcsServiceFactory.createGcsService();
	}
	/**
	 * Stores a file in Cloud Storage
	 * @param is InputStream with the file to store
	 * @param fileName name of the fila in Cloud Storage
	 * @param bucketName name of the Cloud Storage bucket
	 * @throws IOException
	 */
	public void storeFile(InputStream is, String fileName) throws Exception {
		
		GcsFilename filename = new GcsFilename(bucketName, fileName);
		GcsFileOptions options = GcsFileOptions.getDefaultInstance();
		GcsOutputChannel writeChannel = gcs.createOrReplace(filename, options);						
		OutputStream os = new DataOutputStream(Channels.newOutputStream(writeChannel));
		ByteStreams.copy(is, os);						
		os.flush();
		os.close();
		writeChannel.close();
	}
	/**
	 * Reads a file from Cloud Storage
	 * @param fileName Name of the file to read
	 * @param bucketName Name of the bucket
	 * @return InputStream of the file
	 * @throws IOException
	 */
	public InputStream readFile(String fileName) throws IOException {
		GcsFilename filename = new GcsFilename(bucketName, fileName);				
		GcsInputChannel readChannel = null;
		readChannel = gcs.openReadChannel(filename, 0);
		InputStream bis = new BufferedInputStream(Channels.newInputStream(readChannel));		
		return bis;
	}
	/**
	 * Deletes a file from the bucket
	 * @param fileName
	 * @throws IOException
	 */
	public void deleteFile(String fileName) throws IOException {
		GcsFilename filename = new GcsFilename(bucketName, fileName);		
		gcs.delete(filename);		
	}
	
	
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
	 * @throws IOException 
	 */
	public SearchResponse searchByString(String searchString) throws IOException{
		SearchResponse gr = new SearchResponse();
		
		Cursor cursor = Cursor.newBuilder().build();
		
		com.google.appengine.api.search.Query query = 
				com.google.appengine.api.search.Query.newBuilder().setOptions(
			     QueryOptions.newBuilder().setCursor(cursor).build()).build(searchString);
	
		
		// Select the search index to use	
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("Resource").build();
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		// Perform the search
		Results<ScoredDocument> results = index.search(query);
		gr.setSearchCursor(results.getCursor().toWebSafeString());
		gr.setCount(results.getNumberReturned());
		gr.setTotal(results.getNumberFound());	
		
		// Iterate the results and find the corresponding entities
		fillResults(gr, results);
		return gr;
		
	}
	
	public SearchResponse searchByString(String searchString, String cursorString) throws IOException {
		if(cursorString != null){	
			SearchResponse gr = new SearchResponse();
			// Select the search index to use	
		
			Cursor cursor = Cursor.newBuilder().build(cursorString);
			
			com.google.appengine.api.search.Query query = 
					com.google.appengine.api.search.Query.newBuilder().setOptions(
				     QueryOptions.newBuilder().setCursor(cursor).build()).build(searchString);
		
			IndexSpec indexSpec = IndexSpec.newBuilder().setName("Resource").build();
			Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
			// Perform the search
			Results<ScoredDocument> results = index.search(query);
			Cursor resCursor = results.getCursor();
			gr.setSearchCursor(resCursor.toWebSafeString());
			gr.setCount(results.getNumberReturned());
			gr.setTotal(results.getNumberFound());
			
			// Iterate the results and find the corresponding entities
			fillResults(gr,results);
		
			return gr;
		}else{
			return searchByString(searchString);
		}
	}
	/**
	 * Fills a SearchResponse with a set of search results
	 * @param gr
	 * @param results
	 */
	private void fillResults(SearchResponse gr, Results<ScoredDocument> results) {
		for(ScoredDocument sd : results){
			String debug = "";
			try{
			long keyId = Long.parseLong(sd.getOnlyField("entityRef").getText());
			
			Map<String, String> ent = getEntityById(keyId);
			prepareResponseEntity(keyId, ent);
			
			gr.addResult(ent);
			}catch(Exception e){
				if(gr.getCount()>0)
				gr.setCount(gr.getCount()-1);
				if(gr.getTotal()>0)
				gr.setTotal(gr.getTotal()-1);
				e.printStackTrace();
				gr.setMessage("ERROR: "+debug);
			}
		}
	}
	/**
	 * Calculates and adds the missing fields to the entity we're sending to the user
	 * @param keyId
	 * @param ent
	 * @throws IOException
	 */
	private void prepareResponseEntity(long keyId,
			Map<String, String> ent) throws IOException {
		ent.put("entityRef", keyId+"");
		ent.put("contentsUrl", DOWNLOAD_URL+keyId+".zip");
		List<String> tnsUrls = getTnsUrls(keyId);			
		List<String> tnsWidths = new LinkedList<String>();
		List<String> tnsHeights = new LinkedList<String>();
		for(String tn : tnsUrls){
			int lastSeparator = tn.lastIndexOf("/")+1;							
			String end = tn.substring(lastSeparator);			
			String res = end.split("\\.")[0];			
			String[] resolutionParams = res.split("x");			
			tnsWidths.add(resolutionParams[0]);
			tnsHeights.add(resolutionParams[1]);			
		}
		
		ent.put("thumbnailUrlList", tnsUrls.toString());
		ent.put("thumbnailWidthList", tnsWidths.toString());
		ent.put("thumbnailHeightList", tnsHeights.toString());
		
	}
	/**
	 * Returns a list with the url's (filenames) of all the thumbnails related to the keyId
	 * @param keyId
	 * @return
	 * @throws IOException 
	 */
	private List<String> getTnsUrls(long keyId) throws IOException {
		List<String> urls = new LinkedList<String>();
		
		ListResult list = gcs.list(bucketName, new ListOptions.Builder().setPrefix(keyId+"/thumbnails/").setRecursive(true).build());

	    while(list.hasNext())
	    {
	      ListItem item = list.next();
	      urls.add(DOWNLOAD_URL+item.getName());
	    }		
		
		return urls;
	}
	/**
	 * Returns the entity referenced by the keyId in Datastore
	 * @param keyId
	 * @return
	 */
	public Map<String,String> getEntityById(long keyId) {
		Map<String,String> m = new HashMap<String,String>();
		
		Key k = KeyFactory.createKey("Resource", keyId);
		Filter keyFilter =
				  new FilterPredicate(Entity.KEY_RESERVED_PROPERTY,
				                      FilterOperator.EQUAL,
				                      k);
		Query q =  new Query("Resource").setFilter(keyFilter);		
		Entity result = ds.prepare(q).asSingleEntity();
		if(result != null){
			Map<String,Object> props = result.getProperties();
			for(String key : props.keySet())
				m.put(key, props.get(key).toString());
		}
		return m;
	}
	/**
	 * Stores an entity in Google Datastore
	 * @param ent
	 * @return
	 */
	public Key storeEntity(Entity ent) {
		return ds.put(ent);
	}
}
