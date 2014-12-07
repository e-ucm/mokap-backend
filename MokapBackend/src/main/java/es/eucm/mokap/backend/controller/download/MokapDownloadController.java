package es.eucm.mokap.backend.controller.download;

import com.google.gwt.thirdparty.guava.common.io.ByteStreams;
import es.eucm.mokap.backend.controller.BackendController;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Controller class to manage the download petitions to the service
 */
public class MokapDownloadController extends BackendController implements DownloadController{
    /**
     * Launches the file download into the Servlet output Stream
     * @param fileName String with the name of the file we want to download (full name + path)
     * @param outputStream Stream containing the file.
     * @throws IOException If Cloud Storage is unavailable or the file does not exist.
     */
    @Override
    public void launchFileDownload(String fileName, OutputStream outputStream) throws IOException {
        InputStream bis = null;
        OutputStream bos = null;

        //Read the file
        bis = st.readFile(fileName);
        //Output the file
        bos = new BufferedOutputStream(outputStream);

        ByteStreams.copy(bis, bos);
        bos.flush();
        bis.close();
        bos.close();
    }
}
