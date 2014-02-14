package nl.hanze.web.t41.http;

import java.io.*;

public class HTTPResponse {

	private OutputStream outputStream;

    private HTTPResponseFile httpResponseFile;

    /**
     * All response codes with their error messages.
     */
    public enum ResponseCode {

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
     * @param httpResponseFile The HTTP response file from which to read the data.
     */
	public void setResponseFile(HTTPResponseFile httpResponseFile) {

		this.httpResponseFile = httpResponseFile;
	}

    /**
     * Sends a response to the browser.
     *
     * @throws IOException
     */
	public void sendResponse() throws IOException {

		byte[] bytes                    = new byte[HTTPSettings.BUFFER_SIZE];
        FileInputStream fileInputStream = null;

		try
        {
            // Place the HTTP response file into a file input stream
            fileInputStream = getFileInputStream(httpResponseFile.file);

            // The file input stream may be null if both the requested file and the 404 file weren't found
            if (fileInputStream != null) {

                // Write the header
                outputStream.write(getHTTPHeader(httpResponseFile.fileType, httpResponseFile.responseCode, httpResponseFile.file.length()));

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
            outputStream.write(getHTTPHeader("html", ResponseCode.C500, exceptionStringWriter.getBuffer().length()));

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
        header += "Content-Type: " + HTTPSettings.getMimeType(fileType) + "; charset=UTF-8\r\n";
        header += "Content-Length: " + contentLength + "\r\n";
        header += "Date: " + HTTPSettings.getDate() + "\r\n";
        header += "Server: Crappy Java Server 2000\r\n\r\n";
        System.out.println(fileType + " - " + HTTPSettings.getMimeType(fileType));
        return header.getBytes();
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