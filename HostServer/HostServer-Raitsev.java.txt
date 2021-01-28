/* James Raitsev  2012-05-20. Improvements to Elliott. */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Host Server class. Main responsibility is to accept connections on predefined
 * port , figure out what next available port is and pass the request to the
 * AgentListener class, that will do all the work
 */
public class HostServer {

	/**
	 * Statically defined starting position
	 */
	public static int			nextPort	= 30000;

	public static final int		PORT		= 2525;
	public static final String	HOST		= "localhost";

	public static void main(String a[]) throws IOException {

		int q_len = 6; // Queue length

		Socket sock;
		ServerSocket servsock = new ServerSocket(PORT, q_len);
		System.out.println("James's DIA Master receiver started at " + PORT + "\n");

		/**
		 * Now until the end of time, Host Server will listen for incoming
		 * requests (which will come only on port 2565) in order to delegate
		 * handling of this request to the AgentListener (new Thread), which
		 * would would handle incoming connections on a new post.
		 * 
		 * We hardcode a starting location at port 30,000. Each subsequent
		 * connection will start off with that number incremented by 1. This
		 * guarantees that each AgentListener will start off using unique port.
		 */
		while (true) {
			nextPort = nextPort + 1;
			sock = servsock.accept();
			System.out.println("Starting an AgentListener at port " + nextPort);
			new AgentListener(sock, nextPort).start();
		}
	}
}

class AgentWorker extends Thread {
	Socket		sock;
	int			port;

	AgentHolder	parentAgentHolder;

	/**
	 * Constructor used to set the state of the worker
	 * 
	 * @param sock Socket, on which connection was accepted by the Listener
	 * @param port HTTP port passed from the Listener (and Host Server before
	 *            that)
	 * @param agentHolder AhentHolder contains current application state. This
	 *            is how state is passed around from the listener to worker
	 */
	AgentWorker(Socket sock, int port, AgentHolder agentHolder) {
		this.sock = sock;
		this.port = port;
		this.parentAgentHolder = agentHolder;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		System.out.println("In agentWorker Thread for agent.");

		PrintStream out = null;
		BufferedReader in = null;

		// int NewHostMainPort = 1565;
		int newPort;

		Socket clientSock;

		BufferedReader fromHostServer;
		PrintStream toHostServer;

		try {
			out = new PrintStream(sock.getOutputStream());
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String inLine = in.readLine();

			/**
			 * If client request a migration, we need to effectively recognize
			 * that migration will be asked of the Host Server. This means that
			 * we'll need to connect to the Host Server, pretending as if
			 * request came from outside
			 */
			if (inLine.toLowerCase().contains("migrate")) {

				System.out.println("Migration request received");

				/**
				 * Go back to the Host Server and ask for a new connection.
				 * 
				 * By going so, effectively, we're asking for the new cycle of
				 * HostServer->AgentListener->AgentWorker
				 * 
				 */
				clientSock = new Socket(HostServer.HOST, HostServer.PORT);
				fromHostServer = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));

				// Get new PrintStream to write interesting things to server
				toHostServer = new PrintStream(clientSock.getOutputStream());

				// Plz host me!
				toHostServer.println("Please host me. Send my port! [State=" + parentAgentHolder.agentState
						+ "]");

				toHostServer.flush();

				/*
				 * We really only needed the port. The HTML was sent as
				 * convenience for starting with an initial request to the
				 * HostServer from a web client: Read until we find the port
				 * number...
				 */
				String message = "";

				while (true) {
					message = fromHostServer.readLine();
					if (message.indexOf("[Port=") > -1) {
						break;
					}
				}

				// String message received from the host server
				String tempbuf = message.substring(message.indexOf("[Port=") + 6,
						message.indexOf("]", message.indexOf("[Port=")));

				// new port to be used (Ultimately given out by the Host Server)
				newPort = Integer.parseInt(tempbuf);

				System.out.println("newPort is: " + newPort);

				// Friendly message to be sent back to browser
				out.println("<h3> We are Migrating to " + HostServer.HOST + " " + newPort + "</h3>");
				AgentListener.sendHTMLsubmit(out);

				System.out.println("Killing parent listening loop.");

				// Close socket which was originally assigned to Worker (prior
				// to migration)
				parentAgentHolder.sock.close();

			} else {
				// Update state
				parentAgentHolder.agentState++;
				AgentListener.sendHTMLheader(port, HostServer.HOST, inLine, out);

				// Print a friendly message back to client
				out.println("<h3> We are playing the animal game with state " + parentAgentHolder.agentState
						+ "</h3>");
				AgentListener.sendHTMLsubmit(out);
			}

			// close this connection, but not the server
			sock.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/**
 * Minion of HostServer. Will accept connection on the port (hidden from user),
 * which it was given and will handle communication between
 */
class AgentListener extends Thread {
	Socket	sock;
	int		port;

	int		agentState	= 0;

	/**
	 * @param sock Socket created by Host Server
	 * @param port Post this AgentListener will receive requests on
	 */
	public AgentListener(Socket sock, int port) {
		this.sock = sock;
		this.port = port;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {

		BufferedReader fromBrowser = null;
		PrintStream toBrowser = null;

		// Host server is running on. In our example it'll be localhost
		String host = "localhost";

		try {
			String httpRequest;
			toBrowser = new PrintStream(sock.getOutputStream());

			// Reader used to get requests from Browser
			fromBrowser = new BufferedReader(new InputStreamReader(sock.getInputStream()));

			// Actual request we got
			httpRequest = fromBrowser.readLine();

			/**
			 * If http request contains word [State=, we'll figure out what it
			 * is (numeric value assigned to it) and print it out.
			 * 
			 * It is important to recognize that likely sender of this message
			 * is AgentWorker (see migration code in AgentWorker)
			 */
			if (httpRequest.indexOf("[State=") > -1) {
				System.out.println(" ------>     State message received!");
				String tempbuf = httpRequest.substring(httpRequest.indexOf("[State=") + 7,
						httpRequest.indexOf("]", httpRequest.indexOf("[State=")));
				agentState = Integer.parseInt(tempbuf);
				System.out.println("AgentState is: " + agentState);
			}

			System.out.println("httpRequest:" + httpRequest);

			// Send HTML header to the browser to be displayed
			sendHTMLheader(port, host, httpRequest, toBrowser);

			// Additional text to be displayed on the browser
			toBrowser.println("Now in Agent Looper starting Agent Listening Loop\n<br>");
			toBrowser.println("[Port=" + port + "]<br>");

			// Sent HTML code for submit button
			sendHTMLsubmit(toBrowser);

			// Create new socket connection (with queue of 2), on the port we
			// got (from Host Server).
			ServerSocket servsock = new ServerSocket(port, 2);

			// Initialize new Agent Holder with the servSock (to hold the state)
			AgentHolder agenthold = new AgentHolder(servsock);

			// Advise the agent of current state
			System.out.println("\nAgent Holder knows of state: " + agentState);
			agenthold.agentState = agentState;

			/**
			 * As long as we're on this Agent Listener, we may accept requests
			 * on whatever port we were given (by Host Server). Once new request
			 * comes int we'll spawn a worker thread to do things for us.
			 * 
			 * Example of a request will be user hitting the submit button on
			 * the html form and passing us some stuff
			 */
			while (true) {
				sock = servsock.accept();
				System.out.println("Got a connection to agent at port " + port);
				new AgentWorker(sock, port, agenthold).start();
			}

		} catch (IOException e) {
			System.err.println("Either connection failed, or just killed Listener Loop for agent at port "
					+ port);
			e.printStackTrace();
		}
	}

	/**
	 * Sends HTML Header back to the out stream
	 * 
	 * @param localPort Port on which we're running locally
	 * @param host Host on which we're running
	 * @param message Message we got
	 * @param out PrintStream to which we write
	 */
	static void sendHTMLheader(int localPort, String host, String message, PrintStream out) {
		out.println("HTTP/1.1 200 OK");
		// Actual length does not matter
		out.println("Content-Length: 700");
		out.println("Content-Type: text/html");
		out.println("");
		out.println("<html><head> </head><body> ");
		out.println("<h2> This is for submission to PORT " + localPort + " on " + host + "</h2>");
		out.println("<h3> You sent:" + message + "</h3>");
		out.println("<FORM method=\"GET\" action=\"http://" + host + ":" + localPort + "\">");
		out.println("Enter text or <i>migrate</i>:");
		out.println("<INPUT TYPE=\"text\" NAME=\"person\" size=20 value=\"YourTextInput\"><P>");

		out.flush();
	}

	/**
	 * Sends Html submit form to the browser
	 * 
	 * @param out PrintStream to which we write
	 */
	static void sendHTMLsubmit(PrintStream out) {
		out.println("<INPUT TYPE=\"submit\" VALUE=\"Submit Text String\">");
		out.println("</form></body></html>");

		out.flush();
	}

}

/**
 * The purpose of this class is to hold numeric representation of state and the
 * socket used
 */
class AgentHolder {

	ServerSocket	sock;
	int				agentState;

	public AgentHolder(ServerSocket servsock) {
		sock = servsock;
	}
}
