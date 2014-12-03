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
     * Performs a simple search given a string
     * @param searchString
     * @param searchCursor
     * @return
     */
    String searchByString(String searchString, String searchCursor) throws IOException;

    /**
     * Processes an uploaded file
     * @param fis
     * @throws Exception
     */
    String processUploadedResource(FileItemStream fis) throws Exception;

    /**
     * Launches the file download into the Servlet output Stream
     * @param fileName
     * @param outputStream
     */
    void launchFileDownload(String fileName, OutputStream outputStream) throws IOException;
}
