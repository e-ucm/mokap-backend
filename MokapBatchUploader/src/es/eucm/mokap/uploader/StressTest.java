package es.eucm.mokap.uploader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;

import es.eucm.mokap.uploader.exceptions.FileNotUploadedException;

@SuppressWarnings("deprecation")
public class StressTest {
	/*
	 * Sets the max size of the files to test
	 */
	private static int MAX_SIZE = 32*1024*1024;
	/*
	 * Sets the servlet we want to test
	 */
	private static String SERVLET_URI = "http://1-dot-ultimate-rig-753.appspot.com/upload"; 
	/**
	 * Creates a dummy file with the specified size
	 * @param size in KBytes
	 * @param directory Directory in which the file will be created
	 * @return File of the desired size
	 * @throws IOException
	 */
	public static File createRandomFile(int size, File directory) throws IOException{		
		File f = new File(directory,"dummy.file");
		int kbs = size*1024;
		try {	          
	          BufferedWriter output = new BufferedWriter(new FileWriter(f));
	          for(int i=0; i<kbs; i++){
	        	  output.append('0');
	  		  }	          
	          output.close();
        } catch ( IOException e ) {
           e.printStackTrace();
        }
		return f;
		
	}	
	
	/**
	 * Initiates the test with a defined initial value
	 * @param initialSize int with the initial KByte value for the test
	 * @throws IOException 
	 * @throws FileNotUploadedException 
	 */
	public static void test(int initialSize) throws IOException, FileNotUploadedException{
		int currentSize = initialSize;
		System.out.println("Initiating test with initial size = "+initialSize);
		do{
			System.out.println("Creating dummy file...");
			File file = createRandomFile(currentSize, new File("")); 
			System.out.println("File created ["+currentSize+" KBytes].");
			if(file.isFile()){
				System.out.println("Sending POST message...");
				int status = sendUploadRequest(file, SERVLET_URI);
				System.out.println("Message sent.");
				if(status >199 && status < 300){ //file was uploaded successfully
					System.out.println("Received response: ["+status+"].");
					System.out.println("Deleting file...");
					file.delete();
					System.out.println("File deleted.");
				}else{
					throw new FileNotUploadedException(status,currentSize);
				}
			}
			currentSize = currentSize*2;
		}while(currentSize <= MAX_SIZE);
	}	
	/**
	 * Initiates the test with a default value of 1 KByte
	 * @throws IOException 
	 * @throws FileNotUploadedException 
	 */
	public static void test() throws IOException, FileNotUploadedException{
		test(1);
	}
	/**
	 * Sends a request to the GCS servlet to try and upload the file
	 * @return int Estado de la respuesta
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	private static int sendUploadRequest(File file, String uri) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost uploadFile = new HttpPost(uri);

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);
		builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, "file.ext");
		HttpEntity multipart = builder.build();

		uploadFile.setEntity(multipart);

		CloseableHttpResponse response = httpClient.execute(uploadFile);
		String myString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
		System.out.println(myString);
		//HttpEntity responseEntity = response.getEntity();
		return response.getStatusLine().getStatusCode();
	}
}
