package es.eucm.mokap.backend.controller.insert;

import org.apache.commons.fileupload.FileItemStream;

import java.io.IOException;

/**
 * Created by mario on 05/12/2014.
 */
public interface InsertController {
    /**
     * Processes an uploaded file:
     * -It temporarily stores the file in Google Cloud Storage
     * -Then it analyzes its contents
     * -Processes the descriptor.json file and stores the entity in Datastore
     * -Finally it stores the thumbnails and contents.zip in Cloud Storage
     * @param fis FileItemStream containing the file uploaded by the client
     * @throws java.io.IOException When Cloud Storage is unavailable
     * @return Returns a JSON string of the type es.eucm.mokap.model.response.InsertResponse, containing all the RepoElement information and the reference to the entity in Datastore
     */
    String processUploadedResource(FileItemStream fis) throws IOException;
}
