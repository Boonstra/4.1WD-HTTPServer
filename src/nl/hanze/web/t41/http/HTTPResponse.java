package nl.hanze.web.t41.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HTTPResponse {

	private OutputStream outputStream;
	private HTTPRequest httpRequest;

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

		try {
			File file       = new File(HTTPSettings.getDocumentRoot(), fileName);
            fileInputStream = getFileInputStream(file);
			
			if (file.exists()) {
                outputStream.write(getHTTPHeader(fileName));
            } else {
                outputStream.write(getHTTPHeader(""));
            }
			
			int character = fileInputStream.read(bytes, 0, HTTPSettings.BUFFER_SIZE);
			while (character != -1) {
                outputStream.write(bytes, 0, character);

                character = fileInputStream.read(bytes, 0, HTTPSettings.BUFFER_SIZE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
                fileInputStream.close();
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

		/*
		 * *** OPGAVE 4: 1b ***
		 * Stuur het bestand terug wanneer het bestaat; anders het standaard 404-bestand.
		 *
		 * TODO Implement
		 */
				
		return fileInputStream;
	}

    /**
     * Returns the HTTP header.
     *
     * @param fileName The name of the file.
     *
     * @return byte[]
     */
	private byte[] getHTTPHeader(String fileName) {

		String fileType = getFileType(fileName);		
		String header   = "Date: " + HTTPSettings.getDate() + "Server: Crappy Java Server 2000\r\n";

		/*
		 *** OPGAVE 4: 1b, 1c en 1d
		   zorg voor een goede header:
		   200 als het bestand is gevonden;
		   404 als het bestand niet bestaat
		   500 als het bestand wel bestaat maar niet van een ondersteund type is
		   
		   zorg ook voor ene juiste datum en tijd, de juiste content-type en de content-length.

		   TODO Implement
		*/

		byte[] rv = header.getBytes();

		return rv;
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
     * @param respons A byte array of the response.
     */
	private void showResponse(byte[] respons) {

		StringBuffer buf = new StringBuffer(HTTPSettings.BUFFER_SIZE);

		for (int i = 0; i < respons.length; i++) {
			buf.append((char) respons[i]);
		}

		System.out.print(buf.toString());
	}
}