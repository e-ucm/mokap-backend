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
package es.eucm.mokap.backend.server;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.google.gwt.thirdparty.guava.common.io.ByteStreams;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.channels.Channels;

/**
 * A simple servlet that proxies reads and writes to its Google Cloud Storage
 * bucket. Adapted from
 * https://github.com/GoogleCloudPlatform/appengine-gcs-client
 * /blob/master/java/example
 * /src/com/google/appengine/demos/GcsExampleServlet.java
 */
@SuppressWarnings("serial")
public class DownloadServlet extends HttpServlet {

	public static final boolean SERVE_USING_BLOBSTORE_API = false;

	/**
	 * This is where backoff parameters are configured. Here it is aggressively
	 * retrying with backoff, up to 10 times but taking no more that 15 seconds
	 * total to do so.
	 */
	private final GcsService gcsService = GcsServiceFactory
			.createGcsService(new RetryParams.Builder()
					.initialRetryDelayMillis(10).retryMaxAttempts(10)
					.totalRetryPeriodMillis(15000).build());

	/**
	 * Used below to determine the size of chucks to read in. Should be > 1kb
	 * and < 10MB
	 */
	private static final int BUFFER_SIZE = 2 * 1024 * 1024;

	/**
	 * Retrieves a file from GCS and returns it in the http response. If the
	 * request path is /gcs/Foo/Bar this will be interpreted as a request to
	 * read the GCS file named Bar in the bucket Foo.
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		GcsFilename fileName = getFileName(req);
		if (SERVE_USING_BLOBSTORE_API) {
			BlobstoreService blobstoreService = BlobstoreServiceFactory
					.getBlobstoreService();
			BlobKey blobKey = blobstoreService
					.createGsBlobKey("/gs/" + fileName.getBucketName() + "/"
							+ fileName.getObjectName());
			blobstoreService.serve(blobKey, resp);
		} else {
			resp.setContentType("application/octet-stream");
			resp.setHeader("Content-Disposition", "attachment; filename=\""
					+ fileName.getObjectName() + "\"");
			GcsInputChannel readChannel = gcsService
					.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
			ByteStreams.copy(Channels.newInputStream(readChannel),
					resp.getOutputStream());
		}
	}

	private GcsFilename getFileName(HttpServletRequest req) {
		return new GcsFilename(System.getProperty("backend.BUCKET_NAME"),
				req.getParameter("filename"));
	}
}
