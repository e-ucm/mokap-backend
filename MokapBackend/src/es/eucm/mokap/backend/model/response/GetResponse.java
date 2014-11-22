package es.eucm.mokap.backend.model.response;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GetResponse extends Response {
	private int count;
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
}
