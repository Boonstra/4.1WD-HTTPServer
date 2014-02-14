package nl.hanze.web.t41.http;

import java.io.*;

public class HTTPResponse {

	private OutputStream outputStream;

	private HTTPRequest httpRequest;

    /**
     * All response codes with their error messages.
     */
    private enum ResponseCode {

        C200(200, "OK"), C404(404, "Not Found"), C500(500, "Internal Server Error");

        private int    code;
        private String description;

        private ResponseCode(int code, String description) {

            this.code        = code;
            this.description = description;
        }
    }

    /**
     * Constructor
     *
     * @param outputStream The response.
     */
	public HTTPResponse(OutputStream outputStream) {

		this.outputStream = outputStream;
	}

    /**
     * @param httpRequest The HTTP request to handle.
     */
	public void setRequest(HTTPRequest httpRequest) {

		this.httpRequest = httpRequest;
	}

    /**
     * Sends a response to the browser.
     *
     * @throws IOException
     */
	public void sendResponse() throws IOException {

		byte[] bytes                    = new byte[HTTPSettings.BUFFER_SIZE];
		String fileName                 = getFileName(httpRequest.getUri());
        String fileType                 = getFileType(fileName);
        FileInputStream fileInputStream = null;

		try
        {
            File file;
            ResponseCode responseCode;

            // Determine response code and file to display
            if (!isFileTypeSupported(fileType)) {
                responseCode = ResponseCode.C500;

                file     = new File(HTTPSettings.getDocumentRoot(), HTTPSettings.INTERNAL_SERVER_ERROR_FILE);
                fileName = HTTPSettings.INTERNAL_SERVER_ERROR_FILE;
                fileType = getFileType(fileName);
            } else {
                file = new File(HTTPSettings.getDocumentRoot(), fileName);

                if (file.exists()) {
                    responseCode = ResponseCode.C200;
                } else {
                    responseCode = ResponseCode.C404;

                    file = new File(HTTPSettings.getDocumentRoot(), HTTPSettings.FILE_NOT_FOUND_FILE);
                    fileName = HTTPSettings.FILE_NOT_FOUND_FILE;
                    fileType = getFileType(fileName);
                }
            }

            // Place the file into a file input stream
            fileInputStream = getFileInputStream(file);

            // The file input stream can be null if both the file and the 404 file weren't found
            if (fileInputStream != null) {

                // Write the header
                outputStream.write(getHTTPHeader(fileType, responseCode, file.length()));

                // Write the content byte by byte
                int character = fileInputStream.read(bytes, 0, HTTPSettings.BUFFER_SIZE);
                while (character != -1) {
                    outputStream.write(bytes, 0, character);

                    character = fileInputStream.read(bytes, 0, HTTPSettings.BUFFER_SIZE);
                }
            } else {
                new PrintStream(outputStream).print("404: 404 File Not Found");
            }
        }
        catch (Exception e)
        {
            // Put the exception stack trace into a string writer
            StringWriter exceptionStringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(exceptionStringWriter));

            // Write the exception header
            outputStream.write(getHTTPHeader(getFileType("500.html"), ResponseCode.C500, exceptionStringWriter.getBuffer().length()));

            // Write the exception stack trace
            new PrintStream(outputStream).print(exceptionStringWriter.toString());
		}
        finally
        {
			if (fileInputStream != null) {
                try
                {
                    fileInputStream.close();
                }
                catch (IOException ioException)
                {
                    System.out.println("An error occurred while trying to close the file input stream");
                }
            }
		}
	}

    /**
     * @param file The file to create a file input stream of.
     *
     * @return The file input stream, or null if the input stream could not be created.
     */
	private FileInputStream getFileInputStream(File file) {

        // Try read the passed file into an input stream. If the file is not found, try to read the 404 file into the input stream
        try
        {
            return new FileInputStream(file);
        }
        catch (FileNotFoundException passedFileNotFoundException)
        {
            return null;
        }
	}

    /**
     * Returns the HTTP header.
     *
     * @param fileType      The file name.
     * @param responseCode  The status code of the response.
     * @param contentLength The length of the output content.
     *
     * @return byte[]
     */
	private byte[] getHTTPHeader(String fileType, ResponseCode responseCode, long contentLength) {

        // Build header
		String header = "";

        header += "HTTP/1.1 " + responseCode.code + " " + responseCode.description + "\r\n";
        header += "Connection: keep-alive\r\n";
        header += "Content-Type: " + getMimeType(fileType) + "; charset=UTF-8\r\n";
        header += "Content-Length: " + contentLength + "\r\n";
        header += "Date: " + HTTPSettings.getDate() + "\r\n";
        header += "Server: Crappy Java Server 2000\r\n\r\n";

        return header.getBytes();
	}

    /**
     * Returns the file name from the HTTP request URI. If the file name is not set or ends with a slash, the index file
     * will be returned as the file name.
     *
     * @param uri The URI of the HTTP request.
     *
     * @return fileName
     */
    private String getFileName(String uri) {

        if (uri.length() > 0) {
            if (uri.substring(uri.length() - 1).equals("/")) {
                uri += "index.html";
            }
        } else {
            uri = "index.html";
        }

        return uri;
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

    /**
     * Returns the mime type of the passed file type.
     *
     * @param fileType The file type.
     *
     * @return mimeType
     */
    private String getMimeType(String fileType) {

        if (HTTPSettings.dataTypes.containsKey(fileType)) {
            return HTTPSettings.dataTypes.get(fileType);
        }

        // The mime type for unknown file types
        return "application/octet-stream";
    }

    /**
     * Returns whether or not the requested file type is supported by the server.
     *
     * @param fileType The file type
     *
     * @return $isFileTypeSupported
     */
    private boolean isFileTypeSupported(String fileType) {

        return HTTPSettings.dataTypes.containsKey(fileType);
    }

    /**
     * @param response A byte array of the response.
     */
	private void showResponse(byte[] response) {

		StringBuilder stringBuilder = new StringBuilder(HTTPSettings.BUFFER_SIZE);

        for (byte responseByte : response) {
            stringBuilder.append((char) responseByte);
        }

		System.out.print(stringBuilder.toString());
	}
}