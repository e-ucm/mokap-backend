package es.eucm.mokap.backend.data.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by mario on 05/12/2014.
 */
public interface StorageInterface {
    List<String> getTnsUrls(String downloadUrl, long keyId) throws IOException;

    InputStream readFile(String fileName) throws IOException;

    void storeFile(InputStream is, String tempFileName) throws IOException;

    void deleteFile(String tempFileName) throws IOException;
}
