package es.eucm.mokap.backend.model.search;

import java.util.List;

import es.eucm.mokap.backend.model.RepoElement;

public class SearchResult {
	private long numResults;
	private List<RepoElement> results;
	public long getNumResults() {
		return numResults;
	}
	public void setNumResults(long l) {
		this.numResults = l;
	}
	public List<RepoElement> getResults() {
		return results;
	}
	public void setResults(List<RepoElement> results) {
		this.results = results;
	}
}
