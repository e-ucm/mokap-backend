package es.eucm.mokap.backend.model;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents the parameter fields available in a search request
 */
public class SearchFilters {
    private String searchString;
    private String searchCursor;
    private String publisherFilter;
    private String libraryIdFilter;
    private String categoryIdFilter;
    private String tagFilter;

    /**
     * Constructor that accepts the direct String values
     * @param searchString String to perform textual search
     * @param searchCursor Cursor needed to resume a previous search
     * @param publisherFilter Exact publisher name to filter for
     * @param libraryIdFilter Exact library Id name to filter for
     * @param categoryIdFilter Exact category name to filter for
     * @param tagFilter Exact tag name to filter for
     */
    public SearchFilters(String searchString, String searchCursor, String publisherFilter,
                         String libraryIdFilter, String categoryIdFilter, String tagFilter){
        this.searchString = searchString;
        this.searchCursor = searchCursor;
        this.publisherFilter = publisherFilter;
        this.libraryIdFilter = libraryIdFilter;
        this.categoryIdFilter = categoryIdFilter;
        this.tagFilter = tagFilter;
    }

    /**
     * Special constructor that parses the request directly, taking the values we need.
     * @param req
     */
    public SearchFilters(HttpServletRequest req){
        this.searchString = getParameterValue("q",req);
        this.searchCursor = getParameterValue("c",req);
        this.publisherFilter = getParameterValue("p",req);
        this.libraryIdFilter = getParameterValue("l",req);
        this.categoryIdFilter = getParameterValue("cat",req);
        this.tagFilter = getParameterValue("t",req);
    }


    /**
     * Gets the value of a parameter received either in the url or in a header. If it's received in both, the header value prevails.
     * @param paramName Name of the parameter to look for
     * @param req Request containing the parameter
     * @return String with the value of the parameter
     */
    private String getParameterValue(String paramName, HttpServletRequest req){
        String value = null;
        String paramHeader = req.getHeader(paramName);
        String paramUrl = req.getParameter(paramName);
        if(paramUrl!=null){
            value = paramUrl;
        }
        if(paramHeader!=null){
            value = paramHeader;
        }
        return value;
    }

    /**
     * Generates a map with the filters that are not set to null. The keys of the map are the names of the RepoElementFields constants
     * that correspond to the values of the filters we have.
     * @return The map with the active filters.
     */
    Map<String,String> getActiveFilters(){
        HashMap<String,String> activeFilters = new HashMap<String,String>();
        if(this.categoryIdFilter!=null){
            activeFilters.put(RepoElementFields.CATEGORY,categoryIdFilter);
        }
        if(this.libraryIdFilter!=null){
            activeFilters.put(RepoElementFields.LIBRARY,libraryIdFilter);
        }
        if(this.publisherFilter!=null){
            activeFilters.put(RepoElementFields.PUBLISHER,publisherFilter);
        }
        if(this.tagFilter!=null){
            activeFilters.put(RepoElementFields.TAGS,tagFilter);
        }
        return activeFilters;
    }

    /**
     * Generates a String we can use to directly perform the search. If the searchString parameter is not null, then we
     * perform a simple text search.
     * @return A String with the query filters / text to search for
     */
    public String getSearchQuery(){
        if(this.searchString!=null){
            return this.searchString;
        }
        int i = 0;
        String queryString = "";
        Map<String,String> activeFilters = getActiveFilters();
        for(String key : activeFilters.keySet()){
            if(i!=0) queryString+=" AND ";

            queryString+=key+" "+activeFilters.get(key);
        }
        return queryString;
    }


    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getSearchCursor() {
        return searchCursor;
    }

    public void setSearchCursor(String searchCursor) {
        this.searchCursor = searchCursor;
    }

    public String getPublisherFilter() {
        return publisherFilter;
    }

    public void setPublisherFilter(String publisherFilter) {
        this.publisherFilter = publisherFilter;
    }

    public String getLibraryIdFilter() {
        return libraryIdFilter;
    }

    public void setLibraryIdFilter(String libraryIdFilter) {
        this.libraryIdFilter = libraryIdFilter;
    }

    public String getCategoryIdFilter() {
        return categoryIdFilter;
    }

    public void setCategoryIdFilter(String categoryIdFilter) {
        this.categoryIdFilter = categoryIdFilter;
    }

    public String getTagFilter() {
        return tagFilter;
    }

    public void setTagFilter(String tagFilter) {
        this.tagFilter = tagFilter;
    }
}
