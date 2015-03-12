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
package es.eucm.mokap.backend.controller.download;

import com.google.gwt.thirdparty.guava.common.io.ByteStreams;
import es.eucm.mokap.backend.controller.BackendController;
import es.eucm.mokap.backend.reporting.Reporter;
import es.eucm.mokap.backend.reporting.ga.GoogleReporter;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Controller class to manage the download petitions to the service
 */
public class MokapDownloadController extends BackendController implements
		DownloadController {
	/**
	 * Launches the file download into the Servlet output Stream
	 * 
	 * @param fileName
	 *            String with the name of the file we want to download (full
	 *            name + path)
	 * @param outputStream
	 *            Stream containing the file.
	 * @throws IOException
	 *             If Cloud Storage is unavailable or the file does not exist.
	 */
	@Override
	public void launchFileDownload(String fileName, OutputStream outputStream)
			throws IOException {
		InputStream bis = null;
		OutputStream bos = null;

		// Read the file
		bis = st.readFile(fileName);

        // Track the download in Analytics
        Reporter rep = new GoogleReporter();
        rep.reportDownload(fileName);

		// Output the file
		bos = new BufferedOutputStream(outputStream);

		ByteStreams.copy(bis, bos);
		bos.flush();
		bis.close();
		bos.close();

	}
}
