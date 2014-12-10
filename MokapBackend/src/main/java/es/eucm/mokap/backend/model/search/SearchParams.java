package es.eucm.mokap.backend.model.search;

/**
 * Created by mario on 09/12/2014.
 * Parent class for the different search parameter options
 */
public abstract class SearchParams {
    protected String searchCursor;

    public SearchParams(String searchCursor){
        this.searchCursor = searchCursor;
    }

    public String getSearchCursor() {
        return searchCursor;
    }

    public void setSearchCursor(String searchCursor) {
        this.searchCursor = searchCursor;
    }

    /**
     * Generates the query we'll use to retrieve the search results from the database
     * @return Query String
     */
    public abstract String getSearchQuery();
}
