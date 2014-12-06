package es.eucm.mokap.backend.data.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by mario on 05/12/2014.
 */
public interface StorageInterface {
    /**
     * Returns a list with the url's (filenames) of all the thumbnails related to the keyId present in Cloud Storage
     * @param keyId The key id of the item of whom we're retrieving the thumbnails
     * @return List of the filenames
     * @throws java.io.IOException If files cannot be found or Cloud Storage is unavailable
     */
    List<String> getTnsUrls(String downloadUrl, long keyId) throws IOException;
    /**
     * Reads a file from Cloud Storage
     * @param fileName Name of the file to read
     * @throws IOException If files cannot be found or Cloud Storage is unavailable
     */
    InputStream readFile(String fileName) throws IOException;
    /**
     * Stores a file in Cloud Storage
     * @param is InputStream with the file to store
     * @param fileName name of the fila in Cloud Storage
     * @throws IOException If files cannot be found or Cloud Storage is unavailable
     */
    void storeFile(InputStream is, String fileName) throws IOException;
    /**
     * Deletes a file from the Cloud Storage bucket
     * @param fileName Name of the file to delete
     * @throws IOException If files cannot be found or Cloud Storage is unavailable
     */
    void deleteFile(String fileName) throws IOException;
}
