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
package es.eucm.mokap.backend.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Enum with different Time Span options. Defaults to A (all) d -> Today w ->
 * this week m -> this month a -> all the time
 */
public enum TimeSpans {
	D, W, M, A;

	/**
	 * Builds a TimeSpans object form a string
	 * 
	 * @param ts
	 * @return
	 */
	public static TimeSpans buildTimeSpan(String ts) {
		switch (ts.toLowerCase()) {
		case "d":
			return TimeSpans.D;
		case "w":
			return TimeSpans.W;
		case "m":
			return TimeSpans.M;
		case "a":
			return TimeSpans.A;
		default:
			return TimeSpans.A;
		}
	}

	/**
	 * Method that calculates the initial time of a given timeSpan
	 * 
	 * @param timeSpan
	 *            period to calculate from
	 * @return formatted string with time (using formatDate method)
	 */
	public static String getInitTime(TimeSpans timeSpan) {
		Calendar cal = getToday();

		switch (timeSpan) {
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
			cal.set(Calendar.DAY_OF_YEAR, 1);
			cal.add(Calendar.YEAR, -10);
			return formatDate(cal.getTime());
		default:
			return formatDate(new Date(0));

		}
	}

	/**
	 * Method that calculates the end time of a given timeSpan
	 * 
	 * @param timeSpan
	 *            period to calculate from
	 * @return formatted string with time (using formatDate method)
	 */
	public static String getEndTime(TimeSpans timeSpan) {
		Calendar cal = getToday();

		switch (timeSpan) {
		case D:
			cal.add(Calendar.DAY_OF_YEAR, 1);
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

	/**
	 * Generates a date set at the very beginning of the current day.
	 * 
	 * @return Calendar object set to today at 00:00:00
	 */
	private static Calendar getToday() {
		// get today and clear time of day
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of
											// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		return cal;
	}

	/**
	 * Formats a date object into a String readable by humans (and standard for
	 * other APIs)
	 * 
	 * @param dt
	 *            date object
	 * @return string with the formatted date
	 */
	private static String formatDate(Date dt) {
		SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
		return dtf.format(dt);
	}

}
