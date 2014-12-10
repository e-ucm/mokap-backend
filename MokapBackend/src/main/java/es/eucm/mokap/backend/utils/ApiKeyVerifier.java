package es.eucm.mokap.backend.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.eucm.mokap.backend.server.ServerError;
import es.eucm.mokap.backend.server.ServerReturnMessages;

/**
 * Simple tool used to verify if a given API Key belongs to a registered application
 * @author jtorrente
 *
 */
public class ApiKeyVerifier {
	
	private static Properties properties=null;
	
	private static final String VALID_KEYS_FILE = "keys.api";
	
	private static void loadValidKeys(){
		if (properties==null){
			properties = new Properties();
			try {
				properties.load(new FileInputStream(VALID_KEYS_FILE));
			} catch (IOException e) {
				properties = null;
				throw new ServerError(ServerReturnMessages.INTERNAL_ERROR_APIKEYSFILE_NOTFOUND);
			}
		}
	}

	/**
	 * Convenient method to be invoked by any servlet of the mokap backend at the beginning of any doGet() or doPost(). 
	 * It checks that the api key provided as a parameter in the request is present and valid
	 * @param request	The request received, should have a parameter k=API_KEY
	 * @param resp The response object, needed to throw the 401 error (Unauthorized)
	 * @throws IOException If no valid api key is provided (401 error)
	 * @return True if api key was valid, false otherwise
	 */
	public static boolean checkApiKey (HttpServletRequest request, HttpServletResponse resp) throws IOException{
		String apiKey = request.getParameter("k");
		if (apiKey==null || !isValidKey(apiKey)){
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, ServerReturnMessages.m(ServerReturnMessages.INVALID_APIKEY, apiKey));
			return false;
		}
		return true;
	}
	
	/**
	 * Returns true if the {@param key} provided belongs to a registered application client, false otherwise
	 * @param key	Api key provided as an argument by the client that queried the backend (apikey="XXXXX")
	 * @return	True if valid, false otherwise
	 * @throws RuntimeException if the file where valid api keys are stored cannot be loaded
	 */
	private static boolean isValidKey(String key){
		loadValidKeys();
		return (properties!=null && properties.containsKey(key));
	}
	
	/**
	 * Returns the name of the application registered to the given api key, or @null if it is not registered to any app.
	 * @param key	Api key provided as an argument by the client that queried the backend (apikey="XXXXX")
	 * @return The name of the app registered to the api key provided, null if no app is registered to that key
	 * @throws RuntimeException if the file where valid api keys are stored cannot be loaded
	 */
	public static String getClientName(String key){
		loadValidKeys();
		if (properties==null){
			return null;
		}
		return properties.getProperty(key);
	}

}
