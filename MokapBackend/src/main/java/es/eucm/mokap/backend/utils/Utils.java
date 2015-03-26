/**
 *  Copyright [2014] [mokap.es]
 *
 *    This file is part of the mokap community backend (MCB).
 *    MCB is licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package es.eucm.mokap.backend.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Class with some useful static methods.
 */
public class Utils {
	/**
	 * Generates a pseudo-random file name from a given file name. It uses the
	 * timestamp of the creation of the file name and adds a random number and
	 * the original file name.
	 * 
	 * @return The temp new file name
	 */
	public static String generateTempFileName(String fileName) {
		long timestamp = new Date().getTime();
		double rnd = 1 + Math.random() * 9999;
		return (timestamp + rnd + fileName).replace('.', '_');

	}

	/**
	 * Converts a Json String into a Map<String,String>
	 * 
	 * @param jsonString
	 *            Stirng that contains the Json to convert
	 * @return HashMap with the processed Json's data
	 * @throws JsonSyntaxException
	 *             If the JSON is malformed
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonToMap(String jsonString)
			throws JsonSyntaxException {
		// Convert the JSON into a Map
		Gson gson = new GsonBuilder().create();
		Map<String, Object> entMap = new HashMap<String, Object>();
		entMap = (HashMap<String, Object>) gson.fromJson(jsonString,
				entMap.getClass());

		return entMap;
	}
}
