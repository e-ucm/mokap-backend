package es.eucm.mokap.backend.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class Utils {
	/**
	 * Generates a pseudo-random file name from a given file name
	 * @return
	 */
	public static String generateTempFileName(String fileName){
		long timestamp = new Date().getTime();
    	double rnd = 1+Math.random()*9999;
    	String tempFileName = new String(timestamp+rnd+fileName).replace('.', '_');
    	return tempFileName;
	}
	/**
	 * Converts a Json String into a Map<String,String>
	 * @param jsonString
	 * @return
	 * @throws JsonSyntaxException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonToMap(String jsonString)
			throws JsonSyntaxException {
		// Convert the JSON into a Map
		Gson gson = new GsonBuilder().create();		
		Map<String,Object> entMap = new HashMap<String,Object>();
		entMap = (HashMap<String,Object>) gson.fromJson(jsonString, entMap.getClass() );		
		
		return entMap;
	}
}
