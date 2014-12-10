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

/**
 * Simple class to enclose server errors that can be propagated and handled.
 * 
 * @author jtorrente
 * 
 */
public class ServerError extends RuntimeException {
	/**
	 * Required
	 */
	private static final long serialVersionUID = 1L;

	private String errorType;

	/**
	 * Creates an error of the given type and customizes the message with the
	 * given params.
	 * 
	 * @param errorType
	 *            The error type (see {@link ServerReturnMessages} for different
	 *            options).
	 * @param params
	 *            The parameters to customize the error message using
	 *            {@link ServerReturnMessages#m(String, String...)}.
	 */
	public ServerError(String errorType, String... params) {
		super(ServerReturnMessages.m(errorType, params));
		this.errorType = errorType;
	}

	/**
	 * @return The error type (see {@link ServerReturnMessages} for different
	 *         options) set up when the object was built.
	 */
	public String getErrorType() {
		return errorType;
	}
}
