package es.eucm.mokap.uploader.exceptions;

public class FileNotUploadedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8741492435618146423L;

	public FileNotUploadedException(int status, int currentSize) {
		super("ERROR: The file failed to upload.\n -Code: "+status+"\n -Size of the file: "+currentSize+" KBytes");
	}

}
