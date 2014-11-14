package es.eucm.mokap.backend.server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gwt.thirdparty.guava.common.io.ByteStreams;

import es.eucm.mokap.backend.utils.GoogleAccess;

public class ResourceDownload extends HttpServlet {

	private static final long serialVersionUID = 5191318392003026466L;
	private static GoogleAccess csa = new GoogleAccess(System.getProperty("backend.BUCKET_NAME"));	
	
	/**
	 * Method: GET
	 * Retrieves the file specified in the parameter filename
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String msg = "";
		//Get the filename from the parameters
		String fileName = req.getParameter("filename");
		if(!fileName.equals("") && fileName != null){
			//Set the header
			resp.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
			InputStream bis = null;
			OutputStream bos = null;
			try{
				//Read the file			
				bis = csa.readFile(fileName);				
				//Output the file
				bos = new BufferedOutputStream(resp.getOutputStream());
				ByteStreams.copy(bis, bos);
				bos.flush();
				
			}catch(Exception e){
				e.printStackTrace();
				msg+="ERROR: "+fileName+" does not exist."+System.lineSeparator();
			}finally{
				bis.close();
				bos.close();
			}
		}else{
			PrintWriter o = resp.getWriter();
			msg+="ERROR: \""+fileName+"\" is not a valid file name."+System.lineSeparator();
			o.println(msg);
			o.close();			
		}	
	}

}
