package es.eucm.mokap.backend.model.response;

import com.google.gson.Gson;

public abstract class Response {
	public String toJsonString(){
		Gson gson = new Gson();
		String jsonString = gson.toJson(this);
		return jsonString;
	}
	
}
