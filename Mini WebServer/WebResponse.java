/* WebResponse.java

Copyright (C) 2020 with all rights reserved. Clark Elliott

1.0 

Point your browser to:

http://localhost:2540/  or...
http://localhost:2540/WebAdd.fake-cgi  or...
http://localhost:2540/any/string/you/want.abc

...to get a response back. Keep in mind there may be FavIco requests.

Use the WebAdd.html form to submit a query string to WebResponse, based on
the input to the form. You can probably "click on" the file in your
directory. Locally it will have a URL of something like:

file:///C:/Users/Elliott/435/java/MiniWebserver/WebAdd.html

You should see:

Hello Browser World N
...along with some request information.

See WebAdd.html source HTML below.

To complete the MiniWebserver.java assignment, modify this file (or start your own from
scratch) so that your MiniWebserver returns HTML containing the person's
name and the sum of the two numbers.

You can use the Firefox console (control-shift E / Network / Inspector) to
see the Internet traffic. (Note: drag the top line up to give a bigger console
window.)

HTML Reference site:
https://www.w3schools.com/

You may find that including the following in your HTML header helps with
facivon problems (Thanks Thomas K.!):

<head> <link rel="icon" href="data:,"> </head>

https://stackoverflow.com/questions/1321878/how-to-prevent-favicon-ico-requests

For the MiniWebserver assignment answer these questions briefly in YOUR OWN
WORDS here in your comments:

1. How MIME-types are used to tell the browser what data is coming.

2. How you would return the contents of requested files of type HTML
(text/html)

3. How you would return the contents of requested files of type TEXT
(text/plain)



*/

import java.io.*;  // Get the Input Output libraries
import java.net.*; // Get the Java networking libraries

class ListenWorker extends Thread {    // Class definition
  Socket sock;                   // Class member, socket, local to ListnWorker.
  ListenWorker (Socket s) {sock = s;} // Constructor, assign arg s to local sock
  public void run(){
    PrintStream out = null;   // Input from the socket
    BufferedReader in = null; // Output to the socket
    try {
      out = new PrintStream(sock.getOutputStream());
      in = new BufferedReader
        (new InputStreamReader(sock.getInputStream()));

      System.out.println("Sending the HTML Reponse now: " +
			 Integer.toString(WebResponse.i) + "\n" );
           String HTMLResponse = "<html> <h1> Hello Browser World! " +
	     Integer.toString(WebResponse.i++) +  "</h1> <p><p> <hr> <p>";
      out.println("HTTP/1.1 200 OK");
      out.println("Connection: close"); // Can fool with this.
      // int Len = HTMLResponse.length();
      // out.println("Content-Length: " + Integer.toString(Len));
      out.println("Content-Length: 400"); // Lazy, so set high. Calculate later.
      out.println("Content-Type: text/html \r\n\r\n");
      out.println(HTMLResponse);

      for(int j=0; j<6; j++){ // Echo some of the request headers for fun
	out.println(in.readLine() + "<br>\n"); // Save and calculate length
      }                                        // ...if you care to.
      out.println("</html>"); 
	
      sock.close(); // close this connection, but not the server;
    } catch (IOException x) {
      System.out.println("Error: Connetion reset. Listening again...");
    }
  }
}

public class WebResponse {

  static int i = 0;

  public static void main(String a[]) throws IOException {
    int q_len = 6; /* Number of requests for OpSys to queue */
    int port = 2540;
    Socket sock;

    ServerSocket servsock = new ServerSocket(port, q_len);

    System.out.println("Clark Elliott's WebResponse running at 2540.");
    System.out.println("Point Firefox browser to http://localhost:2540/abc.\n");
    while (true) {
      // wait for the next client connection:
      sock = servsock.accept();
      new ListenWorker (sock).start();
    }
  }
}

/*
Save the following as WebAdd.html:

<HTML>
<BODY>
<H1> WebAdd </H1>

<FORM method="GET" action="http://localhost:2540/WebAdd.fake-cgi">

Enter your name and two numbers. My program will return the sum:<p>

<INPUT TYPE="text" NAME="person" size=20 value="YourName"><P>

<INPUT TYPE="text" NAME="num1" size=5 value="4"> <br>
<INPUT TYPE="text" NAME="num2" size=5 value="5"> <p>

<INPUT TYPE="submit" VALUE="Submit Numbers">

</FORM> </BODY></HTML>

*/
