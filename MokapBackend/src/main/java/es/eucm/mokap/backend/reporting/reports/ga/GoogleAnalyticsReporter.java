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
package es.eucm.mokap.backend.reporting.reports.ga;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.services.analytics.model.GaData;
import es.eucm.mokap.backend.model.TimeSpans;
import es.eucm.mokap.backend.reporting.reports.Reporter;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by reyde_000 on 16/03/2015.
 */
public class GoogleAnalyticsReporter implements Reporter {

    private static final List SCOPES = Arrays
			.asList(AnalyticsScopes.ANALYTICS_READONLY);
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final String VIEW_ID = "98801421";
    private static final String SERVICE_EMAIL = "912139491009-gmft31poia3fvm8icme0jn30nfuucs8h@developer.gserviceaccount.com";
    public static final String KEY_FILE = "test-mokap-be685b9eefa5.p12";

    @Override
	public Map<String, Integer> getMostDownloaded(TimeSpans timeSpan)
			throws GeneralSecurityException, IOException {
		Map<String, Integer> ret = new HashMap<String, Integer>();
		GoogleCredential credential = new GoogleCredential.Builder()
				.setTransport(TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId(SERVICE_EMAIL)
				.setServiceAccountScopes(SCOPES)
                .setServiceAccountPrivateKeyFromP12File(
                        new File(KEY_FILE)).build();

		Analytics analytics = new Analytics.Builder(TRANSPORT,
				JSON_FACTORY, null).setApplicationName("test-mokap")
				.setHttpRequestInitializer(credential).build();

		Analytics.Data.Ga.Get apiQuery = analytics
				.data()
				.ga()
				.get("ga:" + VIEW_ID, getInitTime(timeSpan), getEndTime(timeSpan), "ga:totalEvents")
				.setDimensions(
                        "ga:eventCategory,ga:eventAction,ga:eventLabel")
                .setSort("-ga:totalEvents")
                .setFilters("ga:eventCategory==download")
                .setMaxResults(20);

		try {
			GaData data = apiQuery.execute();

			for (List<String> row : data.getRows()) {
				String eventCat = row.get(0);
				String eventAct = row.get(1);
				String eventLbl = row.get(2);
				String eventTot = row.get(3);
				ret.put(""+eventLbl.replace(".zip",""), Integer.parseInt(eventTot));
			}
			// Success. Do something cool!

		} catch (GoogleJsonResponseException e) {
			// Catch API specific errors.
			e.printStackTrace();

		} catch (IOException e) {
			// Catch general parsing network errors.
			e.printStackTrace();
		}

		return ret;
	}

    private String getInitTime(TimeSpans timeSpan) {
        Calendar cal = getToday();

        switch (timeSpan){
            case D:
                return formatDate(cal.getTime());
            case W:
                // get start of this week in milliseconds
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                return formatDate(cal.getTime());
            case M:
                cal.set(Calendar.DAY_OF_MONTH, 1);
                return formatDate(cal.getTime());
            case A:
                cal.set(Calendar.DAY_OF_YEAR,1);
                cal.add(Calendar.YEAR,-10);
                return formatDate(cal.getTime());
            default:
                return formatDate(new Date(0));

        }
    }

    private String getEndTime(TimeSpans timeSpan) {
        Calendar cal = getToday();

        switch (timeSpan){
            case D:
                cal.add(Calendar.DAY_OF_YEAR,1);
                return formatDate(cal.getTime());
            case W:
                // get start of this week in milliseconds
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                // add 1 week
                cal.add(Calendar.DAY_OF_YEAR, 7);
                return formatDate(cal.getTime());
            case M:
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.add(Calendar.MONTH, 1);
                return formatDate(cal.getTime());
            case A:
                return formatDate(new Date());
            default:
                return formatDate(new Date());

        }
    }


    private String formatDate(Date dt){
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
        String date = dtf.format(dt);
        return date;
    }

    private Calendar getToday(){
        // get today and clear time of day
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal;
    }
}
