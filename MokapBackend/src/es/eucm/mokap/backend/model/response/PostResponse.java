package es.eucm.mokap.backend.model.response;

public class PostResponse extends Response{

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
