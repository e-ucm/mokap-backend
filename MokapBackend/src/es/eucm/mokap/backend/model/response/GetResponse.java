package es.eucm.mokap.backend.model.response;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GetResponse extends Response {
	private int count;
	private long total;
	private int start;
	private String message;
	private List<Map<String,String>> results = new LinkedList<Map<String,String>>();
	
	public void addResult(Map<String, String> res){
		this.results.add(res);
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<Map<String, String>> getResults() {
		return results;
	}
	public void setResults(List<Map<String, String>> results) {
		this.results = results;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long l) {
		this.total = l;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
