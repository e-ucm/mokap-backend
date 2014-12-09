package es.eucm.mokap.backend.model.search;

/**
 * Created by mario on 09/12/2014.
 * Class that represents the parameters needed to perform a text search in the database
 */
public class TextSearchParams extends SearchParams{
    private String searchString;

    /**
     * Default Constructor
     * @param searchString string that contains the text we are searching for
     */
    public TextSearchParams(String searchString, String searchCursor){
        super(searchCursor);
        this.searchString = searchString;
    }
    @Override
    public String getSearchQuery() {
        return this.searchString;
    }

    public String toString(){
        return "Search String: "+this.getSearchString()+ System.lineSeparator() +
                "Search Cursor: "+this.getSearchCursor() + System.lineSeparator();
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
