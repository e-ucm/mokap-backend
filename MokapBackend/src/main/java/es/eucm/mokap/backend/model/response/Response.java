package es.eucm.mokap.backend.model.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Parent class for all the responses. It features a quick method to convert them to Json
 */
public abstract class Response {
	public String toJsonString(){
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();				
		String jsonString = gson.toJson(this);
		return jsonString;
	}
	
}
