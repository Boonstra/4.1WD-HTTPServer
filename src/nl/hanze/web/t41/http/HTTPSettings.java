package nl.hanze.web.t41.http;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public final class HTTPSettings {

	static final String DEFAULT_DOC_ROOT    = new File("www").getAbsolutePath();
    static final String FILE_NOT_FOUND_FILE = "404.html";

    static private String documentRoot = DEFAULT_DOC_ROOT;

	static final int BUFFER_SIZE = 2048;

	static final int PORT_MIN         = 0;
	static final int PORT_MAX         = 65535;
	static final int DEFAULT_PORT_NUM = 4444;

    static private int portNumber = DEFAULT_PORT_NUM;

	static final HashMap<String, String> dataTypes = new HashMap<String, String>() {
        {
            // Textual
            put("html", "text/html");
            put("css" , "text/css");
            put("txt" , "text/plain");

            // Application
            put("gif" , "image/gif");
            put("png" , "image/png");
            put("jpeg", "image/jpeg");
            put("jpg" , "image/jpg");
            put("pdf" , "application/pdf");
        }
    };

	static final String[] DAYS   = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    /**
     * @return documentRoot
     */
    public static String getDocumentRoot() {

        return documentRoot;
    }

    /**
     * @param documentRoot The path to where website files can be found.
     */
    public static void setDocumentRoot(String documentRoot) {

        HTTPSettings.documentRoot = documentRoot;
    }

    /**
     * @return portNumber
     */
    public static int getPortNumber() {

        return portNumber;
    }

    /**
     * @param portNumber The number of the port to listen on.
     */
    public static void setPortNumber(int portNumber) {

        HTTPSettings.portNumber = portNumber;
    }

    /**
     * @return date
     */
	public static String getDate() {

		GregorianCalendar calendar = new GregorianCalendar();

		String rv = "";

		rv += DAYS[calendar.get(Calendar.DAY_OF_WEEK) - 1] + ", ";
		rv += calendar.get(Calendar.DAY_OF_MONTH) + " " + MONTHS[calendar.get(Calendar.MONTH)];
		rv += " " + calendar.get(Calendar.YEAR) + "\r\n";
		
		return rv;
	}
}
