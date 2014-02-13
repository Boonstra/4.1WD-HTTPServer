package nl.hanze.web.t41.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HTTPHandlerImpl implements HTTPHandler {

    /**
     * Handles the request.
     *
     * @param in  The request's input stream
     * @param out The request's output stream
     */
	public void handleRequest(InputStream in, OutputStream out) {

		/*
		 ***  OPGAVE 4: 1c ***
		 stel de juiste bestand-typen in.

		 TODO Implement
		*/
		
		HTTPRequest httpRequest   = new HTTPRequest(in);
		HTTPResponse httpResponse = new HTTPResponse(out);

        httpRequest.setUri();
        httpResponse.setRequest(httpRequest);
		
		showDateAndTime();
		System.out.println(": " + httpRequest.getUri());
		
		try {
            httpResponse.sendResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
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