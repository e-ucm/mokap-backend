package es.eucm.mokap.backend.controller.admin;

import es.eucm.mokap.backend.controller.BackendController;
import es.eucm.mokap.backend.model.search.SearchParams;

/**
 * Created by mario on 16/12/2014. Controller class for the admin jsp pages that allow to manage some aspects of the app
 */
public class MokapAdminController extends BackendController implements AdminController {

    @Override
    public String getFeaturedResourcesAsTable(SearchParams searchParams) {
        return "<table></table>";
    }
}
