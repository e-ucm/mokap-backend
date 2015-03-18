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
// [START tracking_code]
package es.eucm.mokap.backend.reporting.tracker.ga;

import com.google.appengine.api.urlfetch.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Class that actually interacts with GA via HTTP requests, using the
 * Measurement protocol.
 */
public class GoogleAnalyticsTracker {
	/**
	 * Url of the analytics service.
	 */
	private static final String GA_URL_ENDPOINT = "www.google-analytics.com/collect";
	/**
	 * Required header
	 */
	private static final HTTPHeader CONTENT_TYPE_HEADER = new HTTPHeader(
			"Content-Type", "application/x-www-form-urlencoded");
	/**
	 * Tracking id for our APP
	 */
	private final String gaTrackingId; // Tracking ID / Web property / Property
	/**
	 * Client Id, 555 is anonymous
	 */
	private String gaClientId = "555"; // Anonymous Client ID.

	private URLFetchService urlFetchService = URLFetchServiceFactory
			.getURLFetchService();

	/**
	 * Default constructor that receives the TID from analytics as a parameter
	 * 
	 * @param gaTrackingId
	 *            A string with the analytics tracking ID
	 * @throws IOException
	 */
	public GoogleAnalyticsTracker(String gaTrackingId) throws IOException {
		if (gaTrackingId == null) {
			throw new IllegalArgumentException(
					"Can't set gaTrackingId to a null value.");
		}
		this.gaTrackingId = gaTrackingId;
	}

	/**
	 * Sets a specific clientId to process the data with sessions
	 * 
	 * @param gaClientId
	 *            string with the client ID
	 * @return The GA object whose clientId was set
	 * @throws IOException
	 */
	public GoogleAnalyticsTracker setGoogleAnalyticsClientId(String gaClientId)
			throws IOException {
		if (gaClientId == null) {
			throw new IllegalArgumentException(
					"Can't set gaClientId to a null value.");
		}
		this.gaClientId = gaClientId;
		return this;
	}

	/**
	 * Posts an Event Tracking message to Google Analytics.
	 * 
	 * @param category
	 *            the required event category
	 * @param action
	 *            the required event action
	 * @param label
	 *            the optional event label
	 * @param value
	 *            the optional value
	 * @param appName
	 *            the name of the app calling this method
	 * @return true if the call succeeded, otherwise false
	 * @exception java.io.IOException
	 *                if the URL could not be posted to
	 */
	public int trackEventToGoogleAnalytics(String category, String action,
			String label, String value, String appName) throws IOException {

		String url = GA_URL_ENDPOINT + "?" + "v=1&" + "tid=" + gaTrackingId
				+ "&" + "cid=" + gaClientId + "&" + "t=event&" + "ec="
				+ encode(category, true) + "&" + "ea=" + encode(action, true)
				+ "&" + "el=" + encode(label, true) + "&" + "ev="
				+ encode(value, true) + "&" + "an=" + encode(appName, false)
				+ "&" + "z="
				+ encode(((Math.floor(Math.random() * 10000))) + "", false);

		HTTPRequest request = new HTTPRequest(new URL("http", url, ""),
				HTTPMethod.POST);
		request.addHeader(CONTENT_TYPE_HEADER);

		HTTPResponse httpResponse = urlFetchService.fetch(request);
		// TODO TESTING
		System.out.println(url);
		System.out.println(httpResponse.getResponseCode());

		// Return True if the call was successful.
		return httpResponse.getResponseCode();
	}

	/**
	 * Encodes the string to be able to be passed inside a URL (UTF-8)
	 * 
	 * @param value
	 *            value of the String
	 * @param required
	 *            true if it is a required parameter
	 * @return encoded string
	 * @throws UnsupportedEncodingException
	 */
	private static String encode(String value, boolean required)
			throws UnsupportedEncodingException {
		if (value == null) {
			if (required) {
				throw new IllegalArgumentException(
						"Required parameter not set.");
			}
			return "";
		}
		return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
	}
}
