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
package es.eucm.mokap.backend.reporting.tracker.ga;

import es.eucm.mokap.backend.reporting.tracker.Tracker;

import java.io.IOException;
import java.util.UUID;

/**
 * Class that interfaces between the application and the Google API
 */
public class GoogleTracker implements Tracker {

	private GoogleAnalyticsTracker gat;
	/**
	 * We get the TID from the properties
	 */
	private static final String TID = System
			.getProperty("backend.ANALYTICS_ID");

	/**
	 * Default constructor, creates a GoogleAnalyticsAPI object with the TID we
	 * obtained from the properties and generates a client ID with UUID
	 * formatting (See
	 * http://es.wikipedia.org/wiki/Universally_unique_identifier)
	 * 
	 * @throws IOException
	 */
	public GoogleTracker() throws IOException {
		gat = new GoogleAnalyticsTracker(TID);
		gat.setGoogleAnalyticsClientId(UUID.randomUUID().toString());
	}

	@Override
	public void reportDownload(String fileName) throws IOException {
		gat.trackEventToGoogleAnalytics("download", fileName, fileName, "1",
				"mokap-backend");
	}
}
