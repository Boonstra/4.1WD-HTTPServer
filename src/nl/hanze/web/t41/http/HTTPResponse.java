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
		String fileName                 = httpRequest.getUri();
        FileInputStream fileInputStream = null;

		try {//String test = null; test.equals("");
            // Get the file by file name and put it into a file input stream
			File file       = new File(HTTPSettings.getDocumentRoot(), fileName);
            fileInputStream = getFileInputStream(file);

            // The file input stream can be null if both the file and the 404 file weren't found
            if (fileInputStream != null) {

                // Write the header
                outputStream.write(getHTTPHeader(file, fileName));

                // Write the content
                int character = fileInputStream.read(bytes, 0, HTTPSettings.BUFFER_SIZE);
                while (character != -1) {
                    outputStream.write(bytes, 0, character);

                    character = fileInputStream.read(bytes, 0, HTTPSettings.BUFFER_SIZE);
                }
            }
		} catch (Exception e) {
            // Put the exception stack trace into a string writer
            StringWriter exceptionStringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(exceptionStringWriter));

            // Write the exception stack trace
            PrintStream exceptionPrintStream = new PrintStream(outputStream);
            exceptionPrintStream.print(exceptionStringWriter.toString());
		} finally {
			if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException ioException) {
                    System.out.println("An error occurred while trying to close the file input stream");
                }
            }
		}
	}

    /**
     * @param file The file to create a file input stream of.
     *
     * @return The file input stream
     */
	private FileInputStream getFileInputStream(File file) {

        FileInputStream fileInputStream = null;

        // Try read the passed file into an input stream. If the file is not found, try to read the 404 file into the input stream
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException passedFileNotFoundException) {
            try {
                fileInputStream = new FileInputStream(new File(HTTPSettings.getDocumentRoot(), HTTPSettings.FILE_NOT_FOUND_FILE));
            } catch (FileNotFoundException notFoundFileNotFoundException) {
                System.out.println("A 404 error occurred while looking for the 404 file. Errorception.");
            }
        }
				
		return fileInputStream;
	}

    /**
     * Returns the HTTP header.
     *
     * @param file     The file.
     * @param fileName The file name.
     *
     * @return byte[]
     */
	private byte[] getHTTPHeader(File file, String fileName) {

		String fileType = getFileType(fileName);

        ResponseCode responseCode;

        // Determine response code
        if (file.exists()) {
            if (isFileTypeSupported(fileType)) {
                responseCode = ResponseCode.C200;
            } else {
                responseCode = ResponseCode.C500;
            }
        } else {
            responseCode = ResponseCode.C404;
        }

        // Build header
		String header = "";

        header += "HTTP/1.0 " + responseCode.code + " " + responseCode.description + "\r\n";
        header += "Connection: keep-alive\r\n";
        header += "Content-Type: " + getMimeType(fileType) + "; charset=UTF-8\r\n";
        header += "Content-Length: " + file.length() + "\r\n";
        header += "Date: " + HTTPSettings.getDate() + "\r\n";
        header += "Server: Crappy Java Server 2000\r\n\r\n";

        return header.getBytes();
	}

    /**
     * Returns the type of the file searched for by file name.
     *
     * @param fileName The name of the file.
     *
     * @return fileType
     */
	private String getFileType(String fileName) {

		int i      = fileName.lastIndexOf(".");
		String ext = "";

		if (i > 0 && i < fileName.length() - 1) {
			ext = fileName.substring(i + 1);
		}

		return ext;
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