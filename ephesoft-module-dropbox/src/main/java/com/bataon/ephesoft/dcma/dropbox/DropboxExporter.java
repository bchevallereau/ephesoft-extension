package com.bataon.ephesoft.dcma.dropbox;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bataon.ephesoft.dcma.dropbox.helper.DropboxHelper;
import com.dropbox.core.DbxClient;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.cmis.CMISExporter.UPLOADEXT;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;

public class DropboxExporter {

	private static final Logger LOGGER = LoggerFactory.getLogger(DropboxExporter.class);

	private Map<String, String> properties;

	private BatchSchemaService batchSchemaService;

	public DropboxExporter(Map<String, String> properties) {
		this.properties = properties;
	}

	public void exportFiles(String batchInstanceIdentifier) throws DCMAException, DCMAApplicationException {
		log(" - Dropbox export plugin: initialisation...");

		DropboxHelper helper = new DropboxHelper(properties);
		helper.setPluginName("dcma-dropbox-plugin");
		LOGGER.info("Initializing properties...");
		String isDropboxON = properties.get(DropboxProperties.DROPBOX_SWITCH.getPropertyKey());
		if (isDropboxON == null || !isDropboxON.equals("ON")) {
			log(" - Dropbox export plugin: stopping...");
			return;
		}

		String rootFolder = properties.get(DropboxProperties.DROPBOX_FOLDER.getPropertyKey());
		String fileExtension = properties.get(DropboxProperties.DROPBOX_UPLOAD_FILEEXT.getPropertyKey());
		String dropbocFilename = properties.get(DropboxProperties.DROPBOX_UPLOAD_FILENAME.getPropertyKey());
		String sFolderToBeExported = (new StringBuilder()).append(batchSchemaService.getLocalFolderLocation()).append(File.separator).append(batchInstanceIdentifier).toString();

		log(" - Dropbox export plugin: authenticating...");
		DbxClient client = helper.authenticateApp();

		// Check properties
		if (null == fileExtension || "".equals(fileExtension))
			throw new DCMAApplicationException(" - Dropbox file extension is null/empty from the database.");
		if (null == rootFolder || "".equals(rootFolder))
			throw new DCMAApplicationException(" - Dropbox destination folder is null/empty from the database.");

		// Preparing file name generation
		String fileName = fileExtension;
		String fileNameFormatArr[] = null;
		if (dropbocFilename != null && !dropbocFilename.isEmpty())
			fileNameFormatArr = dropbocFilename.split("&&");

		// Retrieving documents
		Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
		Documents documents = batch.getDocuments();
		if (documents == null)
			throw new DCMABusinessException("Document type can not be null.");
		List<Document> listOfDocuments = documents.getDocument();
		if (listOfDocuments == null)
			throw new DCMABusinessException("Document type list can not be null.");

		// Browsing documents
		for (Iterator<Document> i$ = listOfDocuments.iterator(); i$.hasNext();) {
			Document document = (Document) i$.next();

			// Generate the file name that should be used in Dropbox
			String generatedFileName = null;
			if (fileNameFormatArr != null)
				generatedFileName = getUpdatedFileName(batchInstanceIdentifier, document.getIdentifier(), fileNameFormatArr, document.getDocumentLevelFields().getDocumentLevelField());

			// Searching the appropriate document depending of the extension
			String sMultiPageDoc = null;
			String sFileExtension = null;
			if (fileName.equalsIgnoreCase(UPLOADEXT.PDF.getUploadFileExt())) {
				sMultiPageDoc = document.getMultiPagePdfFile();
				sFileExtension = FileType.PDF.getExtensionWithDot();
			} else if (fileName.equalsIgnoreCase(UPLOADEXT.TIF.getUploadFileExt())) {
				sMultiPageDoc = document.getMultiPageTiffFile();
				sFileExtension = FileType.TIF.getExtensionWithDot();
			}

			// If the document to export has been found
			if (sMultiPageDoc != null && !sMultiPageDoc.isEmpty()) {
				File fSourceFile = new File((new StringBuilder()).append(sFolderToBeExported).append(File.separator).append(sMultiPageDoc).toString());
				try {
					// Generation of the filename that should be used in Dropbox
					// with the extension
					String uploadFileName = null;

					// Create the file name using the generated file name
					if (generatedFileName != null && !generatedFileName.isEmpty())
						uploadFileName = (new StringBuilder()).append(generatedFileName).append(sFileExtension).toString();

					// If the generated file name is empty
					if (null != uploadFileName)
						uploadFileName = fSourceFile.getName();

					log(" - Dropbox export plugin: uploading...");
					helper.uploadDocument(client, fSourceFile, (new StringBuilder()).append("/").append(rootFolder).append("/").append(uploadFileName).toString());

				} catch (Exception e) {
					LOGGER.error((new StringBuilder()).append("- Problem uploading file : ").append(fSourceFile).toString(), e);
					throw new DCMAApplicationException((new StringBuilder()).append("Unable to upload the document : ").append(fSourceFile).toString(), e);
				}
			}
		}
	}

	private void log(String msg) {
		LOGGER.debug(msg);
		System.out.println(msg);
	}

	private String getUpdatedFileName(String batchInstanceID, String documentIdentifier, String fileNameFormat[], List docFieldList) {
		LOGGER.info("Entering getUpdatedFileName method.");
		StringBuffer updatedFileName = new StringBuffer();
		boolean isValidParamForFileName = false;
		String dlfValue = null;
		String arr$[] = fileNameFormat;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++) {
			String fileFormat = arr$[i$];
			fileFormat = fileFormat.trim();
			LOGGER.info((new StringBuilder()).append("Paramter : ").append(fileFormat).toString());
			if (fileFormat.startsWith("$")) {
				fileFormat = fileFormat.substring(1);
				if (fileFormat.equalsIgnoreCase("EphesoftBatchID")) {
					isValidParamForFileName = true;
					updatedFileName.append(batchInstanceID);
				} else if (fileFormat.equalsIgnoreCase("EphesoftDOCID")) {
					isValidParamForFileName = true;
					updatedFileName.append(documentIdentifier);
				} else {
					dlfValue = getDlfValue(docFieldList, fileFormat);
					if (dlfValue != null && !dlfValue.isEmpty()) {
						isValidParamForFileName = true;
						updatedFileName.append(dlfValue);
					}
				}
			} else if (isValidParamForFileName) {
				isValidParamForFileName = false;
				updatedFileName.append(fileFormat);
			}
			LOGGER.info((new StringBuilder()).append("Updated file name: ").append(updatedFileName).toString());
		}

		LOGGER.info("Exiting getUpdatedFileName method.");
		return updatedFileName.toString();
	}

	private String getDlfValue(List docFieldList, String dlfName) {
		String dlfValue;
		boolean dlfFound;
		label0: {
			LOGGER.info("Entering getDlfValue method.");
			dlfValue = null;
			dlfFound = false;
			LOGGER.info((new StringBuilder()).append("Get value for dlf: ").append(dlfName).toString());
			if (docFieldList == null)
				break label0;
			Iterator i$ = docFieldList.iterator();
			DocField docField;
			do {
				if (!i$.hasNext())
					break label0;
				docField = (DocField) i$.next();
			} while (docField.getName() == null || !docField.getName().equalsIgnoreCase(dlfName));
			String value = docField.getValue();
			dlfFound = true;
			if (value != null && !value.trim().isEmpty()) {
				LOGGER.info((new StringBuilder()).append("Dlf found, Value for Dlf: ").append(value).toString());
				dlfValue = value.trim();
			}
		}
		LOGGER.info((new StringBuilder()).append("Dlf found: ").append(dlfFound).toString());
		LOGGER.info("Exiting getDlfValue method.");
		return dlfValue;
	}

	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	public void setBatchSchemaService(BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

}
