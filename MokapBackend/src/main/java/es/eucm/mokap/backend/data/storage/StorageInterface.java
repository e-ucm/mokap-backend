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
package es.eucm.mokap.backend.data.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * This interface offers some methods to allow the service to interact with the
 * storage
 */
public interface StorageInterface {
	/**
	 * Returns a list with the url's (filenames) of all the thumbnails related
	 * to the keyId present in Cloud Storage
	 * 
	 * @param keyId
	 *            The key id of the item of whom we're retrieving the thumbnails
	 * @return List of the filenames
	 * @throws java.io.IOException
	 *             If files cannot be found or Cloud Storage is unavailable
	 */
	List<String> getTnsUrls(long keyId) throws IOException;

	/**
	 * Returns the length of the contents.zip file associated to the given
	 * entity id (keyId).
	 * 
	 * @param keyId
	 *            The key id of the item of whom contents.zip length is to be
	 *            calculated
	 * @return Total length of contents.zip, in bytes
	 * @throws java.io.IOException
	 *             If files cannot be found or Cloud Storage is unavailable
	 */
	long getContentsSize(long keyId) throws IOException;

	/**
	 * Reads a file from Cloud Storage
	 * 
	 * @param fileName
	 *            Name of the file to read
	 * @throws IOException
	 *             If files cannot be found or Cloud Storage is unavailable
	 */
	InputStream readFile(String fileName) throws IOException;

	/**
	 * Stores a file in Cloud Storage
	 * 
	 * @param is
	 *            InputStream with the file to store
	 * @param fileName
	 *            name of the fila in Cloud Storage
	 * @throws IOException
	 *             If files cannot be found or Cloud Storage is unavailable
	 */
	void storeFile(InputStream is, String fileName) throws IOException;

	/**
	 * Deletes a file from the Cloud Storage bucket
	 * 
	 * @param fileName
	 *            Name of the file to delete
	 * @throws IOException
	 *             If files cannot be found or Cloud Storage is unavailable
	 */
	void deleteFile(String fileName) throws IOException;
}
