/**
 *  Copyright [2014] [mokap.es]
 *
 *    This file is part of the mokap community backend (MCB).
 *    MCB is licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package es.eucm.mokap.backend.data.storage;

import com.google.appengine.tools.cloudstorage.*;
import com.google.gwt.thirdparty.guava.common.io.ByteStreams;

import es.eucm.mokap.backend.controller.insert.UploadZipStructure;

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
	 * 
	 * @param bucketName
	 *            Name of the Cloud Storage bucket we're using
	 * @param downloadUrl
	 *            Base url we'll use to generate the download links
	 */
	public CloudStorageAccess(String bucketName, String downloadUrl) {
		this.bucketName = bucketName;
		this.downloadUrl = downloadUrl;
	}

	@Override
	public List<String> getTnsUrls(long keyId) throws IOException {
		List<String> urls = new LinkedList<String>();

		ListResult list = gcs.list(bucketName, new ListOptions.Builder()
				.setPrefix(keyId + "/" + UploadZipStructure.THUMBNAILS_FOLDER)
				.setRecursive(true).build());

		while (list.hasNext()) {
			ListItem item = list.next();
			urls.add(this.downloadUrl + item.getName());
		}

		return urls;
	}

	@Override
	public void storeFile(InputStream is, String fileName) throws IOException {

		GcsFilename filename = new GcsFilename(bucketName, fileName);
		GcsFileOptions options = GcsFileOptions.getDefaultInstance();
		GcsOutputChannel writeChannel = gcs.createOrReplace(filename, options);
		OutputStream os = new DataOutputStream(
				Channels.newOutputStream(writeChannel));
		ByteStreams.copy(is, os);
		os.flush();
		os.close();
		writeChannel.close();
	}

	@Override
	public InputStream readFile(String fileName) throws IOException {
		GcsFilename filename = new GcsFilename(bucketName, fileName);
		GcsInputChannel readChannel;
		readChannel = gcs.openReadChannel(filename, 0);
		InputStream bis = new BufferedInputStream(
				Channels.newInputStream(readChannel));
		return bis;
	}

	@Override
	public void deleteFile(String fileName) throws IOException {
		GcsFilename filename = new GcsFilename(bucketName, fileName);
		gcs.delete(filename);
	}
}
