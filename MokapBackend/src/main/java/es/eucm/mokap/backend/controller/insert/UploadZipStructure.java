package es.eucm.mokap.backend.controller.insert;

import es.eucm.ead.schema.editor.components.repo.RepoElement;
import es.eucm.mokap.backend.server.ServerError;
import es.eucm.mokap.backend.server.ServerReturnMessages;

/**
 * Simple utility class to describe the internal structure of the zip file the server
 * expects to encapsulate any resource uploaded to the backend.
 * Basically, the file contains (assuming file is named 'fileuploaded.zip'):
 * 
 * fileuploaded.zip
 *     |_ contents.zip     // File containing all the element's assets (json files, images,
 *     |                   // sounds, etc.
 *     |_ descriptor.json  // File containing the metadata entry for the backend catalog
 *     |                   // (represented by class {@link RepoElement}
 *     |_ thumbnails/          // Folder containing n versions of the thumbnail for the
 *           |_ 256x256.png    // Element. Each thumbnail filename must match 
 *           |_ 512x256.png    // imageWidthximageHeight.png
 *           |  
 *          ...
 *           |_ widthxheight.png              
 *     
 * @author jtorrente
 *
 */
public class UploadZipStructure {
	
	/**
	 * Subfile that contains all element's assets
	 */
	public static final String CONTENTS_FILE="contents.zip";
	
	/**
	 * Subfile that contains the {@link RepoElement} metadata catalog entry
	 */
	public static final String DESCRIPTOR_FILE ="descriptor.json";
	
	/**
	 * Subfolder that contains the thumbnails for the element
	 */
	public static final String THUMBNAILS_FOLDER = "thumbnails/";
	
	/**
	 * Extension of a valid thumbnail
	 */
	public static final String THUMBNAIL_EXTENSION = ".png";
	
	/**
	 * Valid extension for compressed uploaded files
	 */
	public static final String ZIP_EXTENSION = ".zip";
	
	/**
	 * Determines if the given entry name corresponds to the contents file
	 * of an uploaded element. Comparison ignores cases.
	 * @param entryName	The name of the zip entry to compare to. If it is {@code null},
	 * 					no exception is thrown. 
	 * @return	True if entryName equals to (case insensitive comparison) {@link #CONTENTS_FILE}.
	 */
	public static boolean isContentsFile(String entryName){
		return isResource(entryName, CONTENTS_FILE);
	}
	
	/**
	 * Determines if the given entry name corresponds to the descriptor file
	 * of an uploaded element. Comparison ignores cases.
	 * @param entryName	The name of the zip entry to compare to. If it is {@code null},
	 * 					no exception is thrown. 
	 * @return	True if entryName equals to (case insensitive comparison) {@link #DESCRIPTOR_FILE}.
	 */
	public static boolean isDescriptorFile(String entryName){
		return isResource(entryName, DESCRIPTOR_FILE);
	}
	
	/**
	 * Checks that the given entry name corresponds to a valid thumbnail. The next rules apply (case insensitive):
	 * - File extension is {@link #THUMBNAIL_EXTENSION}
	 * - File must be in folder {@link #THUMBNAILS_FOLDER}
	 * - File name must match widthXheight.png \d*X\d*\.png
	 * @param entryName	The entry name to be checked (e.g thumbnail/256x256.png)
	 * @return	True if it is a valid thumbnail, false otherwise.
	 * @throws IllegalArgumentException if {@param entryName} is {@code null},
	 *         ServerError if any of the conditions above are not met.
	 */
	public static boolean checkThumbnailImage(String entryName){
		if (entryName==null){
			throw new IllegalArgumentException("Entry name cannot be null");
		}
		
		entryName=entryName.toLowerCase();
		if (!entryName.endsWith(THUMBNAIL_EXTENSION)){
			throw new ServerError(ServerReturnMessages.INVALID_UPLOAD_THUMBNAIL_EXTENSION, entryName);
		}
		
		if (!entryName.startsWith(THUMBNAILS_FOLDER) && !entryName.startsWith("/"+THUMBNAILS_FOLDER)){
			throw new ServerError(ServerReturnMessages.INVALID_UPLOAD_THUMBNAIL_FOLDER, entryName);
		}
		
		String imageName = entryName.substring(entryName.lastIndexOf("/")+1, entryName.lastIndexOf("."));
		boolean validNamePattern = true;
		if (!imageName.contains("x")){
			validNamePattern = false;
		} else {
			String widthStr = imageName.substring(0, imageName.indexOf("x"));
			String heightStr = imageName.substring(imageName.indexOf("x")+1, imageName.length());
			try { 
				Integer.parseInt(widthStr);
				Integer.parseInt(heightStr);
			} catch (NumberFormatException e){
				validNamePattern = false;
			}
		}
		
		if (!validNamePattern){
			throw new ServerError(ServerReturnMessages.INVALID_UPLOAD_THUMBNAIL_NAMEPATTERN, entryName);
		}
		
		return true;
	}
	
	private static boolean isResource(String entryName, String resourceType){
		return resourceType.equalsIgnoreCase(entryName);
	}
}
