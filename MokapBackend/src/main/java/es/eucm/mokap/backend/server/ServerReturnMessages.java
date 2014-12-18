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
package es.eucm.mokap.backend.server;

import es.eucm.mokap.backend.controller.insert.UploadZipStructure;
import es.eucm.mokap.backend.model.FeaturedCategories;

/**
 * All return messages the server responds to any request. Messages can contain
 * parameters to get customized messages using the placeholder pattern {number}
 * 
 * @author jtorrente
 * 
 */
public class ServerReturnMessages {

	/**
	 * Standard return message for any request that is handled with no error
	 */
	public static final String OK = "Ok";

	/**
	 * Generic error message for internal server errors (unknown cause).
	 * Accompanied by return code 500 (Internal Server Error)
	 */
	public static final String GENERIC_INTERNAL_ERROR = "There was an error: {0}";

	/**
	 * Error message returned when the server cannot find or load the registered
	 * api keys. Accompanied by a 500 (internal error) return code.
	 */
	public static final String INTERNAL_ERROR_APIKEYSFILE_NOTFOUND = "An error occurred while loading the supported keys";

	/**
	 * Error message returned by for either GET or POST requests that do not
	 * include a valid api key. Accompanied by a 401 (Unauthorized) error code.
	 */
	public static final String INVALID_APIKEY = "API Key was missing or not valid: {0}";

	/**
	 * Error message returned after POST request when a file is not present in
	 * the stream's request. Accompanied by a 400 (Bad request) error
	 */
	public static final String INVALID_UPLOAD_FILENOTFOUND = "A file could not be found inside the request.";

	/**
	 * Error message returned after POST request when a new resource file is
	 * posted but the filename is null
	 */
	public static final String INVALID_UPLOAD_FILENAMEISNULL = "The file name could not be read (is null).";

	/**
	 * Error message returned after POST request when the extension of the zip
	 * file is not valid
	 */
	public static final String INVALID_UPLOAD_EXTENSION = "The file was not a .zip file.";

	/**
	 * Error message returned after POST request when the zip file uploaded does
	 * not contain a {@link UploadZipStructure#DESCRIPTOR_FILE} file
	 */
	public static final String INVALID_UPLOAD_DESCRIPTOR = "The internal structure of the zip file uploaded is not valid: "
			+ UploadZipStructure.DESCRIPTOR_FILE + " was not found.";

	/**
	 * Error message returned after POST request when the zip file uploaded does
	 * not contain a {@link UploadZipStructure#CONTENTS_FILE} file
	 */
	public static final String INVALID_UPLOAD_CONTENT = "The internal structure of the zip file uploaded is not valid: "
			+ UploadZipStructure.CONTENTS_FILE + " was not found.";

	/**
	 * Error message returned after POST request when the zip file uploaded
	 * contains a thumbnail that does not match the widthXheight.png filename
	 * pattern
	 */
	public static final String INVALID_UPLOAD_THUMBNAIL_NAMEPATTERN = "Thumbnail image {0} does not match widthXheight.png";

	/**
	 * Error message returned after POST request when the zip file uploaded
	 * contains a thumbnail with invalid extension
	 */
	public static final String INVALID_UPLOAD_THUMBNAIL_EXTENSION = "Thumbnail image {0} does not have a supported extension";

	/**
	 * Error message returned after POST request when an expected thumbnail (any
	 * file that is not a directory or the descriptor or the contents file) is
	 * not in the expected {@link UploadZipStructure#THUMBNAILS_FOLDER}.
	 */
	public static final String INVALID_UPLOAD_THUMBNAIL_FOLDER = "Thumbnail image {0} is not in required folder";

	/**
	 * Error message returned after GET request in content download service,
	 * when the file requested is not found or cannot be retrieved. Accompanied
	 * by a 500 return error.
	 */
	public static final String INVALID_DOWNLOAD_FILENNOTFOUND = "ERROR processing \"filename\" param \"{0}\": {1}";

	/**
	 * Error message returned after GET request in content download service,
	 * when the filename param is null or blank. Accompanied by a 400 (Bad
	 * Request) return error.
	 */
	public static final String INVALID_DOWNLOAD_FILENAMENULL = "ERROR: \"filename\" param is null.\"";

	/**
	 * Error message returned when trying to add a featured element in a
	 * category that doesn't exist.
	 */
	public static final String INVALID_FEATURE_CATEGORY_NAME = "ERROR: The category specified is not valid."
			+ System.lineSeparator()
			+ "The valid categories are: "
			+ FeaturedCategories.getCategories();

	/**
	 * Error message returned when trying to add a featured element with an id
	 * that doesn't exist or is not correctly formatted.
	 */
	public static final String INVALID_FEATURE_ID = "ERROR: The id supplied does not exist or is invalid.";

	/**
	 * Creates a return message of the given type with the given parameters.
	 * Parameters are placed into messageType in the appropriate place holder.
	 * For example, given that messageType is
	 * "Error message:{0} under conditions {1}"
	 * ServerReturnMessages.m(messageType, "Customized error",
	 * "Strange conditions"); returns: Error message:Customized error under
	 * conditions Strange conditions
	 * 
	 * @param messageType
	 *            The pattern of the message to produce. Can be any of the
	 *            static fields declared in this class.
	 * @param params
	 *            The array of parameters {0}, {1}, {2}...
	 * @return The parsed message
	 */
	public static String m(String messageType, String... params) {
		String message = messageType;
		for (int i = 0; i < params.length; i++) {
			message = message.replace("{" + i + "}", params[i]);
		}
		return message;
	}
}
