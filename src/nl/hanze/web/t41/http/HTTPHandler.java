package nl.hanze.web.t41.http;

import java.io.InputStream;
import java.io.OutputStream;

public interface HTTPHandler {

    /**
     * Handles the request.
     *
     * @param in  The request's input stream
     * @param out The request's output stream
     */
	public void handleRequest(InputStream in, OutputStream out);

    /**
     * Returns whether or not the requested file type is supported by the server.
     *
     * @param fileType The file type
     *
     * @return $isFileTypeSupported
     */
    public boolean isFileTypeSupported(String fileType);
}
