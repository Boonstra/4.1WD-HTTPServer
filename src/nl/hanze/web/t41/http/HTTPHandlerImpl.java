package nl.hanze.web.t41.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class HTTPHandlerImpl implements HTTPHandler {

	public void handleRequest(InputStream in, OutputStream out) {
		/*
		 ***  OPGAVE 4: 1c ***
		 stel de juiste bestand-typen in.
		*/
		
		HTTPRequest request = new HTTPRequest(in);
		HTTPRespons respons = new HTTPRespons(out);	
		
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
		Date date = new Date();
		System.out.print(format.format(date));
		
	}
}
