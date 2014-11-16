package es.eucm.mokap.backend.utils;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.eucm.mokap.backend.model.RepoElement;

public class JSONTranslator {
	/**
	 * Converts a JSON string into a RepoElement and then parses it into a GDS Entity.
	 * @param string
	 * @return
	 */
	public static Entity EntityFromJSON(String string) {
		Gson gson = new GsonBuilder().create();
		RepoElement relm = gson.fromJson(string,RepoElement.class);
		
		// TODO rellenar entidad
		Entity ent = new Entity("RepoElement");
		ent.setProperty("title",relm.getName().getStrings().get(0).getValue() );
		
		return ent;
	}
	/**
	 * Converts any object to a JSON string
	 * @param res
	 * @return
	 */
	public static String JSONfromObject(Object res) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(res);
		return jsonString;
	}

}
