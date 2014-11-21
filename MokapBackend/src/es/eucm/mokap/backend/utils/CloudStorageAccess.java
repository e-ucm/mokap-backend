package es.eucm.mokap.backend.utils;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.gwt.thirdparty.guava.common.io.ByteStreams;

public class CloudStorageAccess {
	private String bucketName;
	
	public CloudStorageAccess(String bucketName){
		this.bucketName = bucketName;
	}
	/**
	 * Stores a file in Cloud Storage
	 * @param is InputStream with the file to store
	 * @param fileName name of the fila in Cloud Storage
	 * @param bucketName name of the Cloud Storage bucket
	 * @throws IOException
	 */
	public void storeFile(InputStream is, String fileName) throws IOException {
		GcsService gcsService = GcsServiceFactory.createGcsService();
		GcsFilename filename = new GcsFilename(bucketName, fileName);
		GcsFileOptions options = GcsFileOptions.getDefaultInstance();
		GcsOutputChannel writeChannel = gcsService.createOrReplace(filename, options);						
		OutputStream os = new DataOutputStream(Channels.newOutputStream(writeChannel));
		ByteStreams.copy(is, os);						
		os.flush();
		os.close();
		writeChannel.close();
	}
	/**
	 * Reads a file from Cloud Storage
	 * @param fileName Name of the file to read
	 * @param bucketName Name of the bucket
	 * @return InputStream of the file
	 * @throws IOException
	 */
	public InputStream readFile(String fileName) throws IOException {
		GcsFilename filename = new GcsFilename(bucketName, fileName);
		GcsService gcsService = GcsServiceFactory.createGcsService();		
		GcsInputChannel readChannel = null;
		readChannel = gcsService.openReadChannel(filename, 0);
		InputStream bis = new BufferedInputStream(Channels.newInputStream(readChannel));
		return bis;
	}
	/**
	 * Deletes a file from the bucket
	 * @param fileName
	 * @throws IOException
	 */
	public void deleteFile(String fileName) throws IOException {
		GcsFilename filename = new GcsFilename(bucketName, fileName);
		GcsService gcsService = GcsServiceFactory.createGcsService();
		gcsService.delete(filename);		
	}
}
