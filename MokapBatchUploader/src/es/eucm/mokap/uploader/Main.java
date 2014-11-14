package es.eucm.mokap.uploader;

import java.io.IOException;

import javax.swing.JOptionPane;

import es.eucm.mokap.uploader.exceptions.FileNotUploadedException;

public class Main {

	public static void main(String[] args) {
		try {
			Uploader.beginUpload();
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (FileNotUploadedException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}

	}

}
