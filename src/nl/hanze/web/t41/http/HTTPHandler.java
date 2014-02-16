package nl.hanze.web.t41.http;

import java.io.OutputStream;

public interface HTTPHandler {

    /**
     * Handles the request.
     *
     * @param outputStream The output stream to write to
     * @param uri          The request's URI
     */
	public void handleRequest(OutputStream outputStream, String uri);

    /**
     * Returns whether or not the requested file type is supported by the server.
     *
     * @param fileType The file type
     *
     * @return $isFileTypeSupported
     */
    public boolean isFileTypeSupported(String fileType);
}
