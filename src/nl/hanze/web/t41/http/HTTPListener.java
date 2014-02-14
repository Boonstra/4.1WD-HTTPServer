package nl.hanze.web.t41.http;

import java.net.ServerSocket;
import java.net.Socket;

public class HTTPListener {

	private int portNumber;
	private HTTPHandler httpHandler;

    private boolean listen = true;

    /**
     * Constructor.
     *
     * @param httpHandler The HTTP handler for this listener.
     *
     * @throws Exception
     */
	public HTTPListener(HTTPHandler httpHandler) throws Exception {

        portNumber = HTTPSettings.getPortNumber();

		if (portNumber < HTTPSettings.PORT_MIN || portNumber > HTTPSettings.PORT_MAX) {
			throw new Exception("Invalid TCP/IP port, out of range");
        }

		this.httpHandler = httpHandler;
	}

    /**
     * Start listening.
     *
     * TODO Zorg ervoor dat er voor elke request een nieuw proces wordt aangemaakt (een techniek die bekend staat als
     * TODO spawning of forking). Je kunt hiervoor bijvoorbeeld ProcesBuilder gebruiken.
     *
     * @throws Exception
     */
	public void startUp() throws Exception {

		ServerSocket serverSocket = new ServerSocket(portNumber);

		System.out.println("Server started");
		System.out.println("Waiting requests at port " + portNumber);
		
		while (listen) {
			Socket socket = serverSocket.accept();

			httpHandler.handleRequest(socket.getInputStream(), socket.getOutputStream());

            socket.close();
		}		
	}

    /**
     * Interrupts the listener.
     */
    public void interrupt() {

        listen = false;
    }
}
