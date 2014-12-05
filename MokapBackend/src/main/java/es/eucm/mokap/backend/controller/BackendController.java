package es.eucm.mokap.backend.controller;

import es.eucm.mokap.backend.data.db.DatabaseInterface;
import es.eucm.mokap.backend.data.db.DatastoreAccess;
import es.eucm.mokap.backend.data.storage.CloudStorageAccess;
import es.eucm.mokap.backend.data.storage.StorageInterface;

/**
 * Created by mario on 05/12/2014.
 */
public abstract class BackendController {
    protected static String BUCKET_NAME = System.getProperty("backend.BUCKET_NAME");
    protected static int MAX_FILE_SIZE = Integer.parseInt(System.getProperty("backend.MAX_FILE_SIZE"));
    protected static final String DOWNLOAD_URL = System.getProperty("backend.BASE_URL")+"download?filename=";
    // private static GoogleAccess ga = new GoogleAccess(BUCKET_NAME);
    protected static DatabaseInterface db = new DatastoreAccess();
    protected static StorageInterface st = new CloudStorageAccess(BUCKET_NAME,DOWNLOAD_URL);
}
