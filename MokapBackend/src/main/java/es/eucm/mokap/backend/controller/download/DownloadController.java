package es.eucm.mokap.backend.controller.download;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by mario on 05/12/2014.
 */
public interface DownloadController {
    /**
     * Launches the file download into the Servlet output Stream
     * @param fileName String with the name of the file we want to download (full name + path)
     * @param outputStream Stream containing the file.
     * @throws IOException If Cloud Storage is unavailable or the file does not exist.
     */
    void launchFileDownload(String fileName, OutputStream outputStream) throws IOException;
}
