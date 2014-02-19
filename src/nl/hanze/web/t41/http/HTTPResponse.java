package nl.hanze.web.t41.http;

import nl.hanze.web.t41.helper.Base64Converter;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPResponse {

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
        String response                 = "";
        FileInputStream fileInputStream = null;

		try
        {
            // Place the HTTP response file into a file input stream
            fileInputStream = getFileInputStream(httpResponseFile.file);

            // The file input stream may be null if both the requested file and the 404 file weren't found
            if (fileInputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

                StringBuilder stringBuilder = new StringBuilder();

                String line;

                // Read file into line
                while((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append('\n');
                }

                String builtString = stringBuilder.toString();

                if (httpResponseFile.fileType.equals("html")) {
                    // Look for image sources
                    Matcher matcher = Pattern.compile("(?<=img src=\")(?:.*)(?=\")").matcher(builtString);

                    StringBuffer stringBuffer = new StringBuffer();

                    // Convert image sources to base64 blobs
                    while(matcher.find()) {
                        Base64Converter base64Converter = new Base64Converter(matcher.group());

                        if (base64Converter.fileExists()) {
                            matcher.appendReplacement(stringBuffer, "data:image/jpg;base64," + base64Converter.encodeToBase64());
                        }
                    }

                    matcher.appendTail(stringBuffer);

                    builtString = stringBuffer.toString();
                }

                // Write the header
                response = getHTTPHeader(httpResponseFile.fileType, httpResponseFile.responseCode, builtString.length());

                response += builtString;
            } else {
                response = "404: 404 File Not Found";
            }
        }
        catch (Exception e)
        {
            // Put the exception stack trace into a string writer
            StringWriter exceptionStringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(exceptionStringWriter));

            // Write the exception header
            response = getHTTPHeader("html", ResponseCode.C500, exceptionStringWriter.getBuffer().length());

            // Write the exception stack trace
            response += exceptionStringWriter.toString();
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
                    response = "An error occurred while trying to close the file input stream";
                }
            }
		}

        System.out.print(response);
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
	private String getHTTPHeader(String fileType, ResponseCode responseCode, long contentLength) {

        // Build header
		String header = "";

        header += "HTTP/1.1 " + responseCode.code + " " + responseCode.description + "\r\n";
        header += "Connection: keep-alive\r\n";
        header += "Content-Type: " + HTTPSettings.getMimeType(fileType) + "; charset=UTF-8\r\n";
        header += "Content-Length: " + (contentLength + 2L) + "\r\n";
        header += "Date: " + HTTPSettings.getDate() + "\r\n";
        header += "Server: Crappy Java Server 2000\r\n\r\n";

        return header;
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