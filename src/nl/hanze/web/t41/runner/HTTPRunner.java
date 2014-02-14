package nl.hanze.web.t41.runner;

import nl.hanze.web.t41.http.HTTPHandlerImpl;
import nl.hanze.web.t41.http.HTTPListener;
import nl.hanze.web.t41.http.HTTPSettings;

public class HTTPRunner {

    /**
     * Main method.
     *
     * @param args Command line arguments.
     */
	public static void main (String args[]) {

        // Get port number from arguments
        try
        {
            int portNumber = Integer.parseInt(args[0]);

            HTTPSettings.setPortNumber(portNumber);
        }
        catch (Exception e)
        {
            System.out.println("Default port number is used: " + HTTPSettings.getPortNumber());
        }

        // Get the document root from the arguments
        try
        {
            String documentRoot = args[1];

            HTTPSettings.setDocumentRoot(documentRoot);
        }
        catch (Exception e)
        {
            System.out.println("Default document is used: " + HTTPSettings.getDocumentRoot());
        }

        // Start listening
	    try
        {
	    	HTTPListener listener = new HTTPListener(new HTTPHandlerImpl());
	    	listener.startUp();
	    }
        catch (Exception e)
        {
			e.printStackTrace();
		}
	}
}
