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
		
		HTTPRequest request = new HTTPRequest(in);
		HTTPResponse respons = new HTTPResponse(out);
		
		request.setUri();						
		respons.setRequest(request);
		
		showDateAndTime();
		System.out.println(": " + request.getUri());
		
		try {
			respons.sendResponse();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showDateAndTime () {

		DateFormat format = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
		Date date         = new Date();

		System.out.print(format.format(date));
	}
}
