package es.eucm.mokap.backend.controller;

import org.apache.commons.fileupload.FileItemStream;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by mario on 03/12/2014.
 */
public interface BackendController {
    /**
     * Performs a Datastore simple search given a string with the search terms. The search returns up to 20 results for that search.
     * If there are more results to be retrieved, it also returns a cursor string (https://cloud.google.com/appengine/docs/java/datastore/queries?hl=Java_Query_cursors#Java_Query_cursors)
     * to allow the client to continue iterating the search results. If searchCursor is empty, the method will assume it's a first query.
     * The results and cursor string are embedded into a JSON String of the type es.eucm.mokap.model.response.SearchResponse.
     * @param searchString Terms to search for
     * @param searchCursor WebSafeString for resuming a search
     * @throws java.io.IOException When the search can't access Datastore or the Search Index for some reason
     * @return JSON String of the type es.eucm.mokap.model.response.SearchResponse
     */
    String searchByString(String searchString, String searchCursor) throws IOException;

    /**
     * Processes an uploaded file:
     * -It temporarilly stores the file in Google Cloud Storage
     * -Then it analyzes its contents
     * -Processes the descriptor.json file and stores the entity in Datastore
     * -Finally it stores the thumbnails and contents.zip in Cloud Storage
     * @param fis FileItemStream containing the file uploaded by the client
     * @throws IOException When Cloud Storage is unavailable
     * @return Returns a JSON string of the type es.eucm.mokap.model.response.InsertResponse, containing all the RepoElement information and the reference to the entity in Datastore
     */
    String processUploadedResource(FileItemStream fis) throws IOException;

    /**
     * Launches the file download into the Servlet output Stream
     * @param fileName String with the name of the file we want to download (full name + path)
     * @param outputStream Stream containing the file.
     * @throws IOException If Cloud Storage is unavailable or the file does not exist.
     */
    void launchFileDownload(String fileName, OutputStream outputStream) throws IOException;
}
