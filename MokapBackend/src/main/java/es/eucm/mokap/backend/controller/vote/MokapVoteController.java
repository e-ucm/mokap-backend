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
import es.eucm.mokap.backend.reporting.reports.Reporter;
import es.eucm.mokap.backend.reporting.reports.ga.GoogleAnalyticsReporter;
import es.eucm.mokap.backend.reporting.tracker.ga.GoogleTracker;
import es.eucm.mokap.backend.server.ServerReturnMessages;
import es.eucm.mokap.backend.utils.Utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller class for all the resource voting actions
 */
public class MokapVoteController implements VoteController {
	private String resourceId;

	public MokapVoteController() {

	}

	@Override
	public String getScore(HttpServletRequest req) throws Exception {
		this.resourceId = Utils.getParameterValue("id", req);
		if (this.resourceId != null) {
			Reporter rep = new GoogleAnalyticsReporter();
			double score = rep.getEventAvgValueByAction(this.resourceId);

			ScoreResponse resp = new ScoreResponse(resourceId, score);
			return resp.toJsonString();

		} else {
			throw new Exception(ServerReturnMessages.INVALID_RESOURCE_ID);
		}
	}

	@Override
	public void addVote(HttpServletRequest req) throws Exception {
		this.resourceId = Utils.getParameterValue("id", req);
		String score = Utils.getParameterValue("score", req);
		if (this.resourceId != null) {
			GoogleTracker track = new GoogleTracker();
			track.castVote(resourceId, score);

		} else {
			throw new Exception(ServerReturnMessages.INVALID_RESOURCE_ID);
		}
	}
}
