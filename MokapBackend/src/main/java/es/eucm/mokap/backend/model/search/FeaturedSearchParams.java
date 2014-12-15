package es.eucm.mokap.backend.model.search;

/**
 * Created by mario on 15/12/2014. This class represents the parameters of a search for featured content.
 * We only need the topic of  the featured content group we'll be retrieving.
 */
public class FeaturedSearchParams extends SearchParams{
    private String featuredName;

    /**
     * Constructor for the featured search
     * @param featuredName Name of a featured group. The search will try to find this string in the featured field of all the elements in the index.
     */
    public FeaturedSearchParams(String featuredName) {
        super(null);
        this.featuredName = featuredName;
    }

    @Override
    public String getSearchQuery() {
        return "featured : "+featuredName; // TODO Add featured to RepoElementFields
    }
}
