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

/**
 * Enum with different Time Span options. Defaults to A (all) d -> Today w ->
 * this week m -> this month a -> all the time
 */
public enum TimeSpans {
	D, W, M, A;

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

}
