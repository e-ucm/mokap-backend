package es.eucm.mokap.backend.controller;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.gson.JsonSyntaxException;
import com.google.gwt.thirdparty.guava.common.io.ByteStreams;
import es.eucm.mokap.backend.model.response.InsertResponse;
import es.eucm.mokap.backend.model.response.SearchResponse;
import es.eucm.mokap.backend.utils.GoogleAccess;
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
 * Created by mario on 03/12/2014.
 */
public class MokapBackendController implements BackendController{
    private static String BUCKET_NAME = System.getProperty("backend.BUCKET_NAME");
    private static int MAX_FILE_SIZE = Integer.parseInt(System.getProperty("backend.MAX_FILE_SIZE"));
    private static GoogleAccess ga = new GoogleAccess(BUCKET_NAME);

    @Override
    public String searchByString(String searchString, String searchCursor) throws IOException {
        SearchResponse gr = ga.searchByString(searchString, searchCursor);
        String str = gr.toJsonString();
        return str;
    }

    @Override
    public String processUploadedResource(FileItemStream fis) throws IOException {
        String tempFileName = storeUploadedTempFile(fis);
        long storedId = processUploadedTempFile(tempFileName);
        InsertResponse ir = new InsertResponse();
        ir.setId(storedId);
        ir.setMessage("OK");
        return ir.toJsonString();
    }

    @Override
    public void launchFileDownload(String fileName, OutputStream outputStream) throws IOException {
        InputStream bis = null;
        OutputStream bos = null;

        //Read the file
        bis = ga.readFile(fileName);
        //Output the file
        bos = new BufferedOutputStream(outputStream);

        ByteStreams.copy(bis, bos);
        bos.flush();
        bis.close();
        bos.close();
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
            ga.storeFile(is, tempFileName);
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
            UnsupportedEncodingException, JsonSyntaxException {
        long assignedKeyId;
        // Read the cloud storage file
        byte[] content = null;
        String descriptor = "";
        Map<String,byte[]> tns = new HashMap<String,byte[]>();
        InputStream is = ga.readFile(tempFileName);
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
                Key k = ga.storeEntity(ent);
                assignedKeyId = k.getId();

                // Store the contents file with the Id in the name
                ByteArrayInputStream bis = new ByteArrayInputStream(content);
                ga.storeFile(bis, assignedKeyId+".zip");

                // Store the thumbnails in a folder with the id as the name
                for(String key : tns.keySet()){
                    ByteArrayInputStream imgs = new ByteArrayInputStream(tns.get(key));
                    ga.storeFile(imgs, assignedKeyId+"/"+key);
                }
                // Create the Search Index Document
                GoogleAccess.addToSearchIndex(ent, k);

                // Everything went ok, so we delete the temp file
                ga.deleteFile(tempFileName);
            }else{
                assignedKeyId = 0;
            }
        }catch(Exception e){
            e.printStackTrace();
            assignedKeyId = 0;
            //Force rollback if anything failed
            try{
                ga.deleteFile(tempFileName);
                for(String key : tns.keySet()){
                    ByteArrayInputStream imgs = new ByteArrayInputStream(tns.get(key));
                    ga.deleteFile(assignedKeyId+"/"+key);
                }
                ga.deleteEntity(assignedKeyId);
            }catch(Exception ex){}
        }
        return assignedKeyId;
    }
}
