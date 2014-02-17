package nl.hanze.web.t41.http;

import nl.hanze.web.t41.helper.JavaProcessBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class HTTPListener implements Runnable {

	private int portNumber;

    private boolean listen;

    private ProcessSocketManager processSocketManager;

    /**
     * Constructor.
     *
     * @throws Exception
     */
	public HTTPListener() throws Exception {

        listen = true;

        portNumber = HTTPSettings.getPortNumber();

		if (portNumber < HTTPSettings.PORT_MIN || portNumber > HTTPSettings.PORT_MAX) {
			throw new Exception("Invalid TCP/IP port, out of range");
        }

        processSocketManager = new ProcessSocketManager();

        new Thread(processSocketManager).start();
	}

    @Override
    public void run() {

        ServerSocket serverSocket = null;

        try
        {
            // Start a server on the defined port number
            serverSocket = new ServerSocket(portNumber);

            System.out.println("Server started");
            System.out.println("Waiting requests at port " + portNumber);

            while(listen) {
                // Wait for a request
                Socket socket = serverSocket.accept();

                // Parse the request
                HTTPRequest httpRequest = new HTTPRequest(socket.getInputStream());
                httpRequest.setUri();

                // Show request information
                showDateAndTime();
                System.out.println(": " + httpRequest.getUri());

                // Build a new listener process
                JavaProcessBuilder javaProcessBuilder = new JavaProcessBuilder("nl.hanze.web.t41.http.HTTPListener", ".");

                javaProcessBuilder.addClasspathEntry(HTTPSettings.CLASSPATH);
                javaProcessBuilder.addArgument(httpRequest.getUri());

                Process process = javaProcessBuilder.startProcess();

                processSocketManager.add(process, socket);
            }
        }
        catch (IOException e)
        {
            if (serverSocket != null) {
                try
                {
                    serverSocket.close();
                }
                catch (IOException e2)
                {
                    e2.printStackTrace();
                }
            }

            e.printStackTrace();
        }
    }

    /**
     * Stop listening.
     */
    public void interrupt() {

        listen = false;
    }

    /**
     * Called by the listener when a new listener process needs to be created.
     *
     * @param args Command line arguments.
     */
    public static void main(String args[]) {

        HTTPHandler httpHandler = new HTTPHandlerImpl();

        // Get port number from arguments
        try
        {
            httpHandler.handleRequest(System.out, args[0]);
        }
        catch (Exception e)
        {
            System.out.println("No request URI received");
        }

        System.exit(0);
    }

    /**
     * Outputs the date and time.
     */
    private void showDateAndTime () {

        DateFormat format = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
        Date date         = new Date();

        System.out.print(format.format(date));
    }

    /**
     * Manages the connection between the process and the client-server socket connection.
     */
    private class ProcessSocketManager implements Runnable {

        private final HashMap<Process, Socket> processSocketHashMap = new HashMap<Process, Socket>();

        /**
         * @param process The process to handle.
         * @param socket  The socket to output the process' output to.
         */
        public void add(Process process, Socket socket) {

            synchronized (processSocketHashMap) {
                processSocketHashMap.put(process, socket);
            }
        }

        @Override
        public void run() {

            while(listen || !processSocketHashMap.isEmpty()) {

                synchronized (processSocketHashMap) {
                    Iterator iterator = processSocketHashMap.entrySet().iterator();

                    while (iterator.hasNext()) {

                        Map.Entry pair = (Map.Entry) iterator.next();

                        Process process = (Process) pair.getKey();
                        Socket  socket  = (Socket)  pair.getValue();

                        try
                        {
                            // Read process output
                            Scanner scanner      = new Scanner(process.getInputStream()).useDelimiter("\\A");
                            String processOutput = scanner.hasNext() ? scanner.next() : "";

                            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

                            printWriter.print(processOutput);
                            printWriter.flush();

                            // Throws an exception when the process has not yet exited
                            process.exitValue();

                            // Clean up
                            iterator.remove();
                            socket.close();
                        }
                        // When the process is still running, this exception will be thrown and nothing should be done.
                        catch (IllegalThreadStateException ignored) {
                            System.out.println("Process is still running"); }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

                Thread.yield();
            }
        }
    }
}
