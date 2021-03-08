/* MyListener

Listen for incoming browser (or other Internet) requests on port 2540.

This is "quick and dirty" utility code that leaves workers lying around. But you get the idea.

In your browser use http://localhost:2540/some/string.html or with WebAdd.html (or
similar). Edit ports / IP address as needed.

After connecting from a browser you should get something like the following on your console:

> java MyListener
Clark Elliott's Port listener running at 2540.


GET /SomeString/WebAdd.cgi/?person=YourName&num1=4&num2=5 HTTP/1.1
Host: localhost:2540
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp, ...;q=0.8
Accept-Language: en-US,en;q=0.5
Accept-Encoding: gzip, deflate
Connection: keep-alive
Upgrade-Insecure-Requests: 1

For fun, you can communicate with MyListener from MyTelnet.

*/


import java.io.*;  // Get the Input Output libraries
import java.net.*; // Get the Java networking libraries

class ListenWorker extends Thread {    // Class definition
    Socket sock;                   // Class member, socket, local to ListnWorker.
    ListenWorker (Socket s) {sock = s;} // Constructor, assign arg s
    //to local sock
    public void run(){
	// Get I/O streams from the socket:
	PrintStream out = null;
	BufferedReader in = null;
	try {
	    out = new PrintStream(sock.getOutputStream());
      in = new BufferedReader
	  (new InputStreamReader(sock.getInputStream()));
      String sockdata;
      while (true) {
	  sockdata = in.readLine ();
	  if (sockdata != null) System.out.println(sockdata);
	  System.out.flush ();
      }
      //sock.close(); // close this connection, but not the server;
	} catch (IOException x) {
	    System.out.println("Connetion reset. Listening again...");
	}
    }
}

public class MyListener {

    public static boolean controlSwitch = true;

    public static void main(String a[]) throws IOException {
	int q_len = 6; /* Number of requests for OpSys to queue */
	int port = 2540;
	Socket sock;

	ServerSocket servsock = new ServerSocket(port, q_len);

	System.out.println("Clark Elliott's Port listener running at 2540.\n");
	while (controlSwitch) {
	    // wait for the next client connection:
	    sock = servsock.accept();
	    new ListenWorker (sock).start(); // Uncomment to see shutdown bug:
	    // try{Thread.sleep(10000);} catch(InterruptedException ex) {}
	}
    }
}
