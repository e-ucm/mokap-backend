package es.eucm.mokap.backend.model.search;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mario on 10/12/2014.
 * Factory class for the SearchParams classes
 */
public class SearchParamsFactory {
    /**
     * Creates a SearchParams child object of the type that corresponds to the parameters we get in the request
     * @param req request to analyze and extract the params
     * @return SearchParams object
     */
    public static SearchParams create(HttpServletRequest req){
        if (getParameterValue("q",req)!=null){
            return new TextSearchParams(getParameterValue("q",req),getParameterValue("c",req));
        } else {
            return new FilteredSearchParams(getParameterValue("p",req), getParameterValue("l",req), getParameterValue("cat",req), getParameterValue("t",req), getParameterValue("c",req));
        }
    }

    /**
     * Gets the value of a parameter received either in the url or in a header. If it's received in both, the header value prevails.
     * @param paramName Name of the parameter to look for
     * @param req Request containing the parameter
     * @return String with the value of the parameter
     */
    private static  String getParameterValue(String paramName, HttpServletRequest req){
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
}
