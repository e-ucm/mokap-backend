package es.eucm.mokap.backend.model;

import java.util.LinkedList;

/**
 * Created by mdebenito on 16/12/2014. Enum with the different featured categories contemplated by the backend
 */
public enum FeaturedCategories {
    /**
     * This represents all the categories
     */
    ALL("all"),
    FEATURED("featured");

    private String value;
    private FeaturedCategories(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
