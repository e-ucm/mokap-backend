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

import es.eucm.mokap.backend.controller.vote.MokapVoteController;
import es.eucm.mokap.backend.controller.vote.VoteController;
import es.eucm.mokap.backend.utils.ApiKeyVerifier;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet that allows the backend to process votes to resources and to request
 * the vote results
 */
public class ResourceVote extends HttpServlet {
	/**
	 * Method: GET Retrieves information on the voting score of an element,
	 * received in the request.
	 * 
	 * @param req
	 * @param resp
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		if (!ApiKeyVerifier.checkApiKey(req, resp)) {
			return;
		} else {
			VoteController cont = new MokapVoteController();
			try {
				String score = cont.getScore(req);
				// Send the response
				resp.setContentType("application/json");
				PrintWriter out = resp.getWriter();
				out.print(score);
				out.flush();
				out.close();
			} catch (Exception e) {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						e.getMessage());
			}
		}
	}

	/**
	 * Method: POST Inserts a new vote for a resource into the system
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		if (!ApiKeyVerifier.checkApiKey(req, resp)) {
			return;
		} else {
			VoteController cont = new MokapVoteController();
			try {
				cont.addVote(req);
			} catch (Exception e) {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						e.getMessage());
			}
		}
	}

}
