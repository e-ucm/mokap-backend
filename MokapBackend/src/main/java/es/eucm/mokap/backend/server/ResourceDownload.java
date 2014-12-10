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

import es.eucm.mokap.backend.controller.download.MokapDownloadController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to process download requests.
 */
public class ResourceDownload extends HttpServlet {

	private static final long serialVersionUID = 5191318392003026466L;
	// private static BackendController controller = new
	// MokapBackendController();
	private MokapDownloadController dCont;

	/**
	 * Method: GET Retrieves the file specified in the parameter filename
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		dCont = new MokapDownloadController();
		// Get the filename from the parameters
		String fileName = req.getParameter("filename");
		if (!fileName.equals("") && fileName != null) {
			// Set the header
			resp.setHeader("Content-Disposition", "attachment; filename=\""
					+ fileName + "\"");
			try {
				dCont.launchFileDownload(fileName, resp.getOutputStream());
			} catch (Exception e) {
				resp.sendError(
						HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						ServerReturnMessages
								.m(ServerReturnMessages.INVALID_DOWNLOAD_FILENNOTFOUND,
										fileName, e.getMessage()));
			}
		} else {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
					ServerReturnMessages.INVALID_DOWNLOAD_FILENAMENULL);
		}
	}

}
