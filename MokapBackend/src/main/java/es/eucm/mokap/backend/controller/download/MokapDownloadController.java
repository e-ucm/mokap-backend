package es.eucm.mokap.backend.controller.download;

import com.google.gwt.thirdparty.guava.common.io.ByteStreams;
import es.eucm.mokap.backend.controller.BackendController;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by mario on 05/12/2014.
 */
public class MokapDownloadController extends BackendController implements DownloadController{
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
