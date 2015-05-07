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
package es.eucm.mokap.backend.reporting.reports;

import es.eucm.mokap.backend.model.TimeSpans;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * Interface that provides methods to get Analytic reports
 */
public interface Reporter {
	/**
	 * Method that returns a map with the most downloaded files (by file name)
	 * and the times they have been downloaded in the time span provided.
	 * 
	 * @param timeSpan
	 *            TimeSpans object, represents the period of time to analyze
	 * @return map with file names and times downloaded
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	Map<String, Integer> getMostDownloaded(TimeSpans timeSpan)
			throws GeneralSecurityException, IOException;

	/**
	 * Gets the average value of the Action field in the events with the eventId
	 * supplied.
	 * 
	 * @param eventId
	 * @return
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	double getEventAvgValueByAction(String eventId)
			throws GeneralSecurityException, IOException;
}
