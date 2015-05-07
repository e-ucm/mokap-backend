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
package es.eucm.mokap.backend.controller.vote;

import es.eucm.mokap.backend.model.response.ScoreResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Controller interface for all the resource voting actions.
 */
public interface VoteController {
	/**
	 * Gets the average score for the resource included in the request under the
	 * parameter id.
	 * 
	 * @param req
	 *            Request that contains the id of the resource we're looking
	 *            into
	 * @return JSON String in ScoreResponse format
	 * @throws Exception
	 */
	String getScore(HttpServletRequest req) throws Exception;

	/**
	 * Adds a vote to the resource passed in the id field in the request, with
	 * the score passed in the score field.
	 * 
	 * @param req
	 *            Request that contains the id of the resource we're looking
	 *            into and its score
	 */
	void addVote(HttpServletRequest req) throws Exception;
}
