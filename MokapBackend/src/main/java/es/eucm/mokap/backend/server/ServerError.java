package es.eucm.mokap.backend.server;

/**
 * Simple class to enclose server errors that can be propagated and handled. 
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
	 * Creates an error of the given type and customizes the message with the given params.
	 * @param errorType	The error type (see {@link ServerReturnMessages} for different options).
	 * @param params	The parameters to customize the error message using {@link ServerReturnMessages#m(String, String...)}.
	 */
	public ServerError(String errorType, String...params){
		super(ServerReturnMessages.m(errorType, params));
		this.errorType = errorType;
	}

	/**
	 * @return The error type (see {@link ServerReturnMessages} for different options) set up when the object was built.
	 */
	public String getErrorType() {
		return errorType;
	}
}
