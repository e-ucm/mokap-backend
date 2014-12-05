package es.eucm.mokap.backend.controller.search;

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import es.eucm.mokap.backend.controller.BackendController;
import es.eucm.mokap.backend.model.response.SearchResponse;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by mario on 03/12/2014.
 */
public class MokapSearchController extends BackendController implements SearchController {


    @Override
    public String searchByString(String searchString, String searchCursor) throws IOException {
        SearchResponse gr = new SearchResponse();
        Results<ScoredDocument> results = db.searchByString(searchString, searchCursor);
        if(results.getCursor()!=null)
            gr.setSearchCursor(results.getCursor().toWebSafeString());
        gr.setCount(results.getNumberReturned());
        gr.setTotal(results.getNumberFound());

        // Iterate the results and find the corresponding entities
        fillResults(gr, results);
        String str = gr.toJsonString();
        return str;
    }




    /**
     * Fills a SearchResponse with a set of search results
     * @param gr SearchResponse object we need to fill
     * @param results List of results we're processing
     */
    private void fillResults(SearchResponse gr, Results<ScoredDocument> results) {
        for(ScoredDocument sd : results){
            String debug = "";
            try{
                long keyId = Long.parseLong(sd.getOnlyField("entityRef").getText());

                Map<String, Object> ent = db.getEntityByIdAsMap(keyId);
                prepareResponseEntity(keyId, ent);

                gr.addResult(ent);
            }catch(Exception e){
                if(gr.getCount()>0)
                    gr.setCount(gr.getCount()-1);
                if(gr.getTotal()>0)
                    gr.setTotal(gr.getTotal()-1);
                e.printStackTrace();
                gr.setMessage("ERROR: "+debug);
            }
        }
    }

    /**
     * Calculates and adds the missing fields to the entity we're sending to the user
     * See https://cloud.google.com/appengine/docs/java/javadoc/com/google/appengine/api/datastore/Entity
     * See https://cloud.google.com/appengine/docs/java/javadoc/com/google/appengine/api/datastore/Key
     * @param keyId Key id of the entity we're modifying
     * @param ent Entity we're modifying already converted to hashmap
     * @throws IOException
     */
    private void prepareResponseEntity(long keyId,
                                       Map<String, Object> ent) throws IOException {
        ent.put("entityRef", keyId+"");
        ent.put("contentsUrl", DOWNLOAD_URL+keyId+".zip");
        List<String> tnsUrls = st.getTnsUrls(DOWNLOAD_URL, keyId);
        List<Integer> tnsWidths = new LinkedList<Integer>();
        List<Integer> tnsHeights = new LinkedList<Integer>();
        for(String tn : tnsUrls){
            int lastSeparator = tn.lastIndexOf("/")+1;
            String end = tn.substring(lastSeparator);
            String res = end.split("\\.")[0];
            String[] resolutionParams = res.split("x");
            tnsWidths.add(Integer.parseInt(resolutionParams[0]));
            tnsHeights.add(Integer.parseInt(resolutionParams[1]));
        }

        ent.put("thumbnailUrlList", tnsUrls);
        ent.put("thumbnailWidthList", tnsWidths);
        ent.put("thumbnailHeightList", tnsHeights);
    }
}
