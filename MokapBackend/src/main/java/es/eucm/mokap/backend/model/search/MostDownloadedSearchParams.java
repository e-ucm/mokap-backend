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
package es.eucm.mokap.backend.model.search;

import es.eucm.mokap.backend.model.TimeSpans;
import es.eucm.mokap.backend.reporting.reports.Reporter;
import es.eucm.mokap.backend.reporting.reports.ga.GoogleAnalyticsReporter;

import java.util.Map;

/**
 * Created by reyde_000 on 12/03/2015.
 */
public class MostDownloadedSearchParams extends SearchParams {
	private TimeSpans timeSpan;

	/**
	 * Search Params for the Most Downloaded search
	 * 
	 * @param ts
	 *            The time span we're analyzing: d -> Today w -> this week m ->
	 *            this month a -> all the time
	 */
	public MostDownloadedSearchParams(String ts) {
		super(null);
		this.timeSpan = TimeSpans.buildTimeSpan(ts);
	}

	@Override
	public String getSearchQuery() throws Exception {
		// TODO Check API for a list of most downloaded files
		Reporter rep = new GoogleAnalyticsReporter();
		Map<String, Integer> md = rep.getMostDownloaded(this.timeSpan);
		String q = "NoEventsToShow";
		// TODO Create reports with the list
		if (md.keySet().size() > 0) {
			q = "";
			int i = 0;
			for (String id : md.keySet()) {
				if (i != 0)
					q += " OR ";
				q += "(" + id + ")";
				i++;
			}
		}
		return q;
	}
}
