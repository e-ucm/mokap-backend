package es.eucm.mokap.backend.model.search;

import es.eucm.ead.schemax.repo.RepoElementFields;

/**
 * Created by mario on 15/12/2014. This class represents the parameters needed to retrieve an element by its Id
 */
public class IdSearchParams extends SearchParams{

    private long id;

    /**
     * Standard constructor for an id based search
     * @param id Id of the element we want to retrieve
     */
    public IdSearchParams(long id) {
        super(null);
        this.id = id;
    }

    @Override
    public String getSearchQuery() {
        return RepoElementFields.ENTITYREF+" = "+id;
    }
}
