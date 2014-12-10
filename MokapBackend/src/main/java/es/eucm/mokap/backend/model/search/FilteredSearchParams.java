package es.eucm.mokap.backend.model.search;

import es.eucm.ead.schemax.repo.RepoElementFields;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mario on 09/12/2014.
 * Class that contains all the parameters you can use to filter a search in the database
 */
public class FilteredSearchParams extends SearchParams {
    private String publisherFilter;
    private String libraryIdFilter;
    private String categoryIdFilter;
    private String tagFilter;

    /**
     * Default constructor for the class
     * @param publisherFilter name of the publisher of the resource
     * @param libraryIdFilter id string of the library
     * @param categoryIdFilter category of the resource
     * @param tagFilter a tag you want to filter for
     */
    public FilteredSearchParams(String publisherFilter, String libraryIdFilter, String categoryIdFilter, String tagFilter, String searchCursor){
        super(searchCursor);
        this.publisherFilter = publisherFilter;
        this.libraryIdFilter = libraryIdFilter;
        this.categoryIdFilter = categoryIdFilter;
        this.tagFilter = tagFilter;
    }

    @Override
    public String getSearchQuery() {
        int i = 0;
        String queryString = "";
        Map<String,String> activeFilters = getActiveFilters();
        for(String key : activeFilters.keySet()){
            if(i!=0) queryString+=" AND ";

            queryString+=key+" = "+activeFilters.get(key);
            i++;
        }
        return queryString;
    }

    /**
     * Generates a map with the filters that are not set to null. The keys of the map are the names of the RepoElementFields constants
     * that correspond to the values of the filters we have.
     * @return The map with the active filters.
     */
    Map<String,String> getActiveFilters(){
        HashMap<String,String> activeFilters = new HashMap<String,String>();
        if(this.categoryIdFilter!=null){
            activeFilters.put(RepoElementFields.CATEGORYLIST,categoryIdFilter);
        }
        if(this.libraryIdFilter!=null){
            activeFilters.put(RepoElementFields.LIBRARYID,libraryIdFilter);
        }
        if(this.publisherFilter!=null){
            activeFilters.put(RepoElementFields.PUBLISHER,publisherFilter);
        }
        if(this.tagFilter!=null){
            activeFilters.put(RepoElementFields.TAGLIST,tagFilter);
        }
        return activeFilters;
    }

}
