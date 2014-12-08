package es.eucm.mokap.backend.data.storage;

import com.google.appengine.tools.cloudstorage.*;
import com.google.gwt.thirdparty.guava.common.io.ByteStreams;

import java.io.*;
import java.nio.channels.Channels;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that offers interaction with Google Cloud Storage
 */
public class CloudStorageAccess implements StorageInterface {
    private static GcsService gcs = GcsServiceFactory.createGcsService();
    private String bucketName;
    private String downloadUrl;

    /**
     * Constructor, takes the bucketname and the base url used for the downloads
     * @param bucketName Name of the Cloud Storage bucket we're using
     * @param downloadUrl Base url we'll use to generate the download links
     */
    public CloudStorageAccess(String bucketName, String downloadUrl) {
        this.bucketName = bucketName;
        this.downloadUrl=downloadUrl;
    }

    /**
     * Returns a list with the url's (filenames) of all the thumbnails related to the keyId present in Cloud Storage
     * @param keyId The key id of the item of whom we're retrieving the thumbnails
     * @return List of the filenames
     * @throws java.io.IOException If files cannot be found or Cloud Storage is unavailable
     */
    public List<String> getTnsUrls(String baseUrl,long keyId) throws IOException {
        List<String> urls = new LinkedList<String>();

        ListResult list = gcs.list(bucketName, new ListOptions.Builder().setPrefix(keyId+"/thumbnails/").setRecursive(true).build());

        while(list.hasNext())
        {
            ListItem item = list.next();
            urls.add(this.downloadUrl+item.getName());
        }

        return urls;
    }

    /**
     * Stores a file in Cloud Storage
     * @param is InputStream with the file to store
     * @param fileName name of the fila in Cloud Storage
     * @throws IOException If files cannot be found or Cloud Storage is unavailable
     */
    public void storeFile(InputStream is, String fileName) throws IOException {

        GcsFilename filename = new GcsFilename(bucketName, fileName);
        GcsFileOptions options = GcsFileOptions.getDefaultInstance();
        GcsOutputChannel writeChannel = gcs.createOrReplace(filename, options);
        OutputStream os = new DataOutputStream(Channels.newOutputStream(writeChannel));
        ByteStreams.copy(is, os);
        os.flush();
        os.close();
        writeChannel.close();
    }
    /**
     * Reads a file from Cloud Storage
     * @param fileName Name of the file to read
     * @throws IOException If files cannot be found or Cloud Storage is unavailable
     */
    public InputStream readFile(String fileName) throws IOException {
        GcsFilename filename = new GcsFilename(bucketName, fileName);
        GcsInputChannel readChannel;
        readChannel = gcs.openReadChannel(filename, 0);
        InputStream bis = new BufferedInputStream(Channels.newInputStream(readChannel));
        return bis;
    }
    /**
     * Deletes a file from the Cloud Storage bucket
     * @param fileName Name of the file to delete
     * @throws IOException If files cannot be found or Cloud Storage is unavailable
     */
    public void deleteFile(String fileName) throws IOException {
        GcsFilename filename = new GcsFilename(bucketName, fileName);
        gcs.delete(filename);
    }
}
