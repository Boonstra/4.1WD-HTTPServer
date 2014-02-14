package nl.hanze.web.t41.http;

import java.io.InputStream;

public class HTTPRequest {

	private String uri = "";

	private InputStream inputStream;

    /**
     * Constructor.
     *
     * @param inputStream The request's input stream
     */
	public HTTPRequest(InputStream inputStream) {

		this.inputStream = inputStream;
	}

    /**
     * @return uri
     */
	public String getUri() {

		return uri;
	}

    /**
     * Set the uri.
     */
	public void setUri() {

		int index1, index2;

		String request = parseRequest();
		index1         = request.indexOf(' ');

		if (index1 != -1) {
			index2 = request.indexOf(' ', index1 + 1);

			if (index2 > index1) {
				uri = request.substring(index1 + 1, index2);
            }
		}
	}

    /**
     * Outputs the request.
     */
	public void showRequest() {

		System.out.print(parseRequest());
	}

    /**
     * @return request
     */
	private String parseRequest() {

		StringBuilder stringBuilder = new StringBuilder(HTTPSettings.BUFFER_SIZE);

		int i;

		byte[] buffer = new byte[HTTPSettings.BUFFER_SIZE];

		try
        {
			i = inputStream.read(buffer);
		}
        catch (Exception e)
        {
			e.printStackTrace();
			i = -1;
		}

		for (int j = 0; j < i; j++) {
            stringBuilder.append((char) buffer[j]);
		}

		return stringBuilder.toString();
	}
}