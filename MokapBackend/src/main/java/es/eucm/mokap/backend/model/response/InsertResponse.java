package es.eucm.mokap.backend.model.response;

/**
 * Class that represents a response to an insert petition. It should contain the necessary data to know if the insertion
 * went as expected and a reference to the object created in online storage.
 */
public class InsertResponse extends Response{

	private long id;
	private String message;
	
	

	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}
	
	public void setError(String msg){
		this.id = 0;
		this.message = msg;
	}

}
