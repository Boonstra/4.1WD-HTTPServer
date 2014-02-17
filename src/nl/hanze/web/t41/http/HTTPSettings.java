package nl.hanze.web.t41.http;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public final class HTTPSettings {

	protected static final String DEFAULT_DOC_ROOT           = new File("www").getAbsolutePath();
    protected static final String FILE_NOT_FOUND_FILE        = "404.html";
    protected static final String INTERNAL_SERVER_ERROR_FILE = "500.html";

    private static String documentRoot = DEFAULT_DOC_ROOT;

    protected static final String CLASSPATH = "./out/production/4.1WD-HTTPServer";

	protected static final int BUFFER_SIZE = 2048;

	protected static final int PORT_MIN         = 0;
	protected static final int PORT_MAX         = 65535;
	protected static final int DEFAULT_PORT_NUM = 4444;

    private static int portNumber = DEFAULT_PORT_NUM;

    protected static final HashMap<String, String> dataTypes = new HashMap<String, String>() {
        {
            // Text
            put("html", "text/html");
            put("css" , "text/css");
            put("txt" , "text/plain");

            // Image
            put("gif" , "image/gif");
            put("png" , "image/png");
            put("jpeg", "image/jpeg");
            put("jpg" , "image/jpg");

            // Application
            put("pdf" , "application/pdf");
        }
    };

    protected static final String[] DAYS   = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	protected static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

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

        System.out.println("Document root set: " + documentRoot);

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
     * Returns the mime type of the passed file type. Returns "application/octet-stream" when no matching data type was
     * found.
     *
     * @param fileType The type of the file.
     *
     * @return mimeType
     */
    public static String getMimeType(String fileType) {

        if (dataTypes.containsKey(fileType)) {
            return dataTypes.get(fileType);
        }

        // The mime type for unknown file types
        return "application/octet-stream";
    }

    /**
     * @return date
     */
	public static String getDate() {

		GregorianCalendar calendar = new GregorianCalendar();

		String date = "";

        date += DAYS[calendar.get(Calendar.DAY_OF_WEEK) - 1] + ", ";
        date += calendar.get(Calendar.DAY_OF_MONTH) + " " + MONTHS[calendar.get(Calendar.MONTH)] + " ";
        date += calendar.get(Calendar.YEAR);
		
		return date;
	}
}
