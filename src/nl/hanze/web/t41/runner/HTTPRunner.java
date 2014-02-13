package nl.hanze.web.t41.runner;

import nl.hanze.web.t41.http.HTTPHandlerImpl;
import nl.hanze.web.t41.http.HTTPListener;
import nl.hanze.web.t41.http.HTTPSettings;

public class HTTPRunner {
	public static void main (String args[]) {



		/* 
		  *** OPGAVE 1.1 ***
		  zorg ervoor dat het port-nummer en de basis-directory vanuit de command-line kunnen worden meegegeven.
		  LET OP: de default-waarden moet je nog wel instellen in de Settings-klasse.
		*/
		
		int portnumber;


        try {
            portnumber = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            portnumber = HTTPSettings.PORT_NUM;
        }
		
	    try {
	    	HTTPListener listener = new HTTPListener (portnumber, new HTTPHandlerImpl());
	    	listener.startUp();	    	
	    } catch (Exception e) {
			e.printStackTrace();
		}
	}
}
