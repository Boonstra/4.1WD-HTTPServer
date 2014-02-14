package nl.hanze.web.t41.http;

import nl.hanze.web.t41.http.HTTPResponse.ResponseCode;

import java.io.File;

public class HTTPResponseFile {

    File file;

    String fileName;
    String fileType;

    ResponseCode responseCode;

    HTTPHandler httpHandler;

    /**
     * Constructor.
     *
     * @param fileName The path and name of the file.
     */
    public HTTPResponseFile(String fileName, HTTPHandler httpHandler) {

        this.fileName = getFileName(fileName);
        this.fileType = getFileType(this.fileName);

        this.httpHandler = httpHandler;

        this.file = getFile(this.fileName);
    }

    /**
     * Looks up the file with the file name. If the file does not exist, the 404 file will be returned. If the file does
     * exist, but is of an unsupported file type, the 500 file will be returned.
     *
     * If the 404 file or the 500 file cannot be found, the returned file will be equal to null.
     *
     * @param fileName the path and name of the file.
     *
     * @return file
     */
    private File getFile(String fileName) {

        file         = new File(HTTPSettings.getDocumentRoot(), fileName);
        responseCode = ResponseCode.C200;

        // Determine response code and file to display
        if (file.exists()) {
            if (!httpHandler.isFileTypeSupported(fileType)) {
                responseCode = ResponseCode.C500;

                // 500: File found, but is of an unsupported file type
                file     = new File(HTTPSettings.getDocumentRoot(), HTTPSettings.INTERNAL_SERVER_ERROR_FILE);
                fileName = HTTPSettings.INTERNAL_SERVER_ERROR_FILE;
                fileType = getFileType(fileName);
            }
        } else {
            responseCode = ResponseCode.C404;

            // 404: File not found
            file = new File(HTTPSettings.getDocumentRoot(), HTTPSettings.FILE_NOT_FOUND_FILE);
            fileName = HTTPSettings.FILE_NOT_FOUND_FILE;
            fileType = getFileType(fileName);
        }

        return file;
    }

    /**
     * Returns the file name from the HTTP request URI. If the file name is not set or ends with a slash, the index file
     * will be returned as the file name.
     *
     * @param fileName The path and name of the file.
     *
     * @return fileName
     */
    private String getFileName(String fileName) {

        if (fileName.length() > 0) {
            if (fileName.substring(fileName.length() - 1).equals("/")) {
                fileName += "index.html";
            }
        } else {
            fileName = "index.html";
        }

        return fileName;
    }

    /**
     * Returns the type of the file searched for by file name.
     *
     * @param fileName The name of the file.
     *
     * @return fileType
     */
    private String getFileType(String fileName) {

        int i            = fileName.lastIndexOf(".");
        String extension = "";

        if (i > 0 && i < fileName.length() - 1) {
            extension = fileName.substring(i + 1);
        }

        return extension;
    }
}
