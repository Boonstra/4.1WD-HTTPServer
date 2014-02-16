package nl.hanze.web.t41.http;

import java.io.OutputStream;
import java.util.Arrays;

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
	public void handleRequest(OutputStream outputStream, String uri) {

        HTTPResponseFile httpResponseFile = new HTTPResponseFile(uri, this);

        HTTPResponse httpResponse = new HTTPResponse();
        httpResponse.setResponseFile(httpResponseFile);
		
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
}