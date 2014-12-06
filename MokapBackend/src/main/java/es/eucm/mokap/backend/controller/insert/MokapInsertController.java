package es.eucm.mokap.backend.controller.insert;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.gson.JsonSyntaxException;
import es.eucm.mokap.backend.controller.BackendController;
import es.eucm.mokap.backend.model.response.InsertResponse;
import es.eucm.mokap.backend.utils.Utils;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by mario on 05/12/2014.
 */
public class MokapInsertController extends BackendController implements InsertController{
    /**
     * Processes an uploaded file:
     * -It temporarily stores the file in Google Cloud Storage
     * -Then it analyzes its contents
     * -Processes the descriptor.json file and stores the entity in Datastore
     * -Finally it stores the thumbnails and contents.zip in Cloud Storage
     * @param fis FileItemStream containing the file uploaded by the client
     * @throws java.io.IOException When Cloud Storage is unavailable
     * @return Returns a JSON string of the type es.eucm.mokap.model.response.InsertResponse, containing all the RepoElement information and the reference to the entity in Datastore
     */
    @Override
    public String processUploadedResource(FileItemStream fis) throws IOException {
        String tempFileName = storeUploadedTempFile(fis);
        long storedId = processUploadedTempFile(tempFileName);
        InsertResponse ir = new InsertResponse();
        ir.setId(storedId);
        ir.setMessage("OK");
        return ir.toJsonString();
    }

    /**
     * Stores an uploaded file with a temporal file name.
     * @param fis Stream with the file
     * @return Name of the created temporal file
     * @throws IOException
     */
    private String storeUploadedTempFile(FileItemStream fis) throws IOException{
        String tempFileName;
        // Let's process the file
        String fileName = fis.getName();
        if (fileName != null)
            fileName= FilenameUtils.getName(fileName);
        else
            throw new IOException("The file name could not be read.");
        if(!fileName.toLowerCase().endsWith(".zip")){
            throw new IOException("The file was not a .zip file.");
        }else{
            InputStream is = fis.openStream();
            //Calculate fileName
            tempFileName = Utils.generateTempFileName(fileName);
            // Actually store the general temporal file
            st.storeFile(is, tempFileName);
            is.close();
        }
        return tempFileName;
    }

    /**
     * Processes a temp file stored in Cloud Storage:
     * -It analyzes its contents
     * -Processes the descriptor.json file and stores the entity in Datastore
     * -Finally it stores the thumbnails and contents.zip in Cloud Storage
     * @param tempFileName name of the temp file we are going to process
     * @return The Datastore Key id for the entity we just created (entityRef in RepoElement)
     * @throws IOException If the file is not accessible or Cloud Storage is not available
     */
    private long processUploadedTempFile(String tempFileName) throws IOException,
            JsonSyntaxException {
        long assignedKeyId;
        // Read the cloud storage file
        byte[] content = null;
        String descriptor = "";
        Map<String,byte[]> tns = new HashMap<String,byte[]>();
        InputStream is = st.readFile(tempFileName);
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry;
        while((entry = zis.getNextEntry()) != null) {
            String filename = entry.getName();
            if(filename.equals("contents.zip")){
                content = IOUtils.toByteArray(zis);
            }else if(filename.equals("descriptor.json")){
                BufferedReader br = new BufferedReader(new InputStreamReader(zis, "UTF-8"));
                String str;
                while ((str = br.readLine()) != null) {
                    descriptor += str;
                }
            }else if(entry.isDirectory()){
                continue;
            }else if(filename.toLowerCase().endsWith(".png")){
                byte[] img = IOUtils.toByteArray(zis);
                tns.put(filename, img);
            }
            zis.closeEntry();
        }

        try{
            if(descriptor != null &&
                    !descriptor.equals("") &&
                    content != null
                    ){
                // Analizar json
                Map<String, Object> entMap = Utils.jsonToMap(descriptor);
                // Parse the map into an entity
                Entity ent = new Entity("Resource");
                for(String key :entMap.keySet()){
                    ent.setProperty(key, entMap.get(key));
                }
                // Store the entity (GDS) and get the Id
                Key k = db.storeEntity(ent);
                assignedKeyId = k.getId();

                // Store the contents file with the Id in the name
                ByteArrayInputStream bis = new ByteArrayInputStream(content);
                st.storeFile(bis, assignedKeyId+".zip");

                // Store the thumbnails in a folder with the id as the name
                for(String key : tns.keySet()){
                    ByteArrayInputStream imgs = new ByteArrayInputStream(tns.get(key));
                    st.storeFile(imgs, assignedKeyId+"/"+key);
                }
                // Create the Search Index Document
                db.addToSearchIndex(ent, k);

                // Everything went ok, so we delete the temp file
                st.deleteFile(tempFileName);
            }else{
                assignedKeyId = 0;
            }
        }catch(Exception e){
            e.printStackTrace();
            assignedKeyId = 0;
            //Force rollback if anything failed
            try{
                st.deleteFile(tempFileName);
                for(String key : tns.keySet()){
                    ByteArrayInputStream imgs = new ByteArrayInputStream(tns.get(key));
                    st.deleteFile(assignedKeyId+"/"+key);
                }
                db.deleteEntity(assignedKeyId);
            }catch(Exception ex){}
        }
        return assignedKeyId;
    }
}
