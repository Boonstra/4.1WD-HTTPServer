package nl.hanze.web.t41.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class HTTPHandlerImpl implements HTTPHandler {

    protected static final String[] SUPPORTED_FILE_TYPES = new String[] {
            // Text
            "html",
            "css",
            "text",

            // Image
            "gif",
            "png",
            "jpeg",
            "jpg",

            // Application
            "pdf"
    };

    @Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream) {

		HTTPRequest httpRequest   = new HTTPRequest(inputStream);
		HTTPResponse httpResponse = new HTTPResponse(outputStream);

        httpRequest.setUri();

        HTTPResponseFile httpResponseFile = new HTTPResponseFile(httpRequest.getUri(), this);

        httpResponse.setResponseFile(httpResponseFile);

		showDateAndTime();
		System.out.println(": " + httpRequest.getUri());
		
		try
        {
            httpResponse.sendResponse();
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}
	}

    @Override
    public boolean isFileTypeSupported(String fileType) {

        return Arrays.asList(SUPPORTED_FILE_TYPES).contains(fileType);
    }

    /**
     * Outputs the date and time.
     */
	private void showDateAndTime () {

		DateFormat format = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
		Date date         = new Date();

		System.out.print(format.format(date));
	}
}