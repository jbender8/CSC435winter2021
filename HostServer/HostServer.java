/* 2012-05-20 Version 2.0

Thanks John Reagan for updates to original code by Clark Elliott.

Modified further on 2020-05-19

-----------------------------------------------------------------------

Play with this code. Add your own comments to it before you turn it in.

-----------------------------------------------------------------------

NOTE: This is NOT a suggested implementation for your agent platform,
but rather a running example of something that might serve some of
your needs, or provide a way to start thinking about what YOU would like to do.
You may freely use this code as long as you improve it and write your own comments.

-----------------------------------------------------------------------

TO EXECUTE: 

1. Start the HostServer in some shell. >> java HostServer

1. start a web browser and point it to http://localhost:4242. Enter some text and press
the submit button to simulate a state-maintained conversation.

2. start a second web browser, also pointed to http://localhost:4242 and do the same. Note
that the two agents do not interfere with one another.

3. To suggest to an agent that it migrate, enter the string "migrate"
in the text box and submit. The agent will migrate to a new port, but keep its old state.

During migration, stop at each step and view the source of the web page to see how the
server informs the client where it will be going in this stateless environment.

-----------------------------------------------------------------------------------

COMMENTS:

This is a simple framework for hosting agents that can migrate from
one server and port, to another server and port. For the example, the
server is always localhost, but the code would work the same on
different, and multiple, hosts.

State is implemented simply as an integer that is incremented. This represents the state
of some arbitrary conversation.

The example uses a standard, default, HostListener port of 4242.

-----------------------------------------------------------------------------------

DESIGN OVERVIEW

Here is the high-level design, more or less:

HOST SERVER
  Runs on some machine
  Port counter is just a global integer incrememented after each assignment
  Loop:
    Accept connection with a request for hosting
    Spawn an Agent Looper/Listener with the new, unique, port

AGENT LOOPER/LISTENER
  Make an initial state, or accept an existing state if this is a migration
  Get an available port from this host server
  Set the port number back to the client which now knows IP address and port of its
         new home.
  Loop:
    Accept connections from web client(s)
    Spawn an agent worker, and pass it the state and the parent socket blocked in this loop
  
AGENT WORKER
  If normal interaction, just update the state, and pretend to play the animal game
  (Migration should be decided autonomously by the agent, but we instigate it here with client)
  If Migration:
    Select a new host
    Send server a request for hosting, along with its state
    Get back a new port where it is now already living in its next incarnation
    Send HTML FORM to web client pointing to the new host/port.
    Wake up and kill the Parent AgentLooper/Listener by closing the socket
    Die

WEB CLIENT
  Just a standard web browser pointing to http://localhost:4242 to start.

  -------------------------------------------------------------------------------*/

  //---------------------------------------------Start of new comments By Jessica Bender-----------------------------------------------------------------------------------------------------

/* 
Ran the progam and connected to http:\\localhost:4242 on Google Chrome browser.
Minor code changes I made: 
    1. added on line ___:
        System.out.println("With small edits and comments from Jessica Bender. 3/7/2021");//added this text to show that this is my comments and small changes
    2. 


Logs: 
    example with 1 chrome browser open 
        Line 
    example with 3 chrome browsers open        
        Line 
    example with my code edits :)        
        Line 
*/

/* 
Notes on imports: I simplified the imports with *. 
We only need io and net imports for this program to work.
*/
  //import java.io.BufferedReader;
  //import java.io.IOException;
 // import java.io.InputStreamReader;
 // import java.io.PrintStream;
  import java.io.*; // Added this import to simplify the other commented out imports. This allows us to have only one inport rather then 4 imports 
  //import java.net.ServerSocket;
  //import java.net.Socket;
  import java.net.*;// Also Added this import to simplify the other commented out imports. This allows us to have only one inport rather then 2 imports.
 
 /* 
Notes on AgentWorker class:
the main functionality of this class is to read what was inputed in the text box and do something with it.
For example if it was migrate it printed on the webpage: 
    We are migrating to host 3006
    View the source of this page to see how the client is informed of the new location.
Whease as if anything else was endtered it printed on the webpage: 
    We are having a conversation with state 12
And if there was an error, or incorrect input it printed on the webpage: 
    You have not entered a valid request!

This class also made a few other calls to other classes. i.e. agentHolder and AgentListener. 
This class is also made as a call in the AgentListener class
*/

  class AgentWorker extends Thread {//start of the AgentWorker class 
      
    Socket sock; //new Socket connection called sock initialized.
    agentHolder parentAgentHolder; //calls to the agentHolder class within the code. assigns it to parentAgentHolder
    int localPort;//new int localPort initialized.
    
    AgentWorker (Socket s, int prt, agentHolder ah) { //start of AgentWorker. Takes in a Socket, and int and an agentHolder
      sock = s; //assigns the Soccet s that was given in the funtion call to the Socket sock that we inilized above 
      localPort = prt; // assigns the int prt that was given in the funtion call to the int localPort that we inilized above 
      parentAgentHolder = ah;// assigns the agentHolder ah that was given in the funtion call to the agentHolder parentAgentHolder that we inilized above 
    }
    public void run() {//start of run
      
      PrintStream out = null; //new PrintStream called out assigned to null
      BufferedReader in = null;//new BufferedReader called in assigned to null
      String NewHost = "localhost"; //new Sting called NewHost assigned to localhost
      int NewHostMainPort = 4242;//port that we will be running on. Int called NewHostMainPort and assigned to 4242
      String buf = "";  //new Sting called buf assigned to an empty sting 
      int newPort; //new int called newPort initialized. 
      Socket clientSock; //new Socket called clientSock initialized. 
      BufferedReader fromHostServer; //new BufferedReader called fromHostServer initialized. 
      PrintStream toHostServer; //new PrintStream called toHostServer initialized. 
      
      try { //start of try. Will try the following code. If it fails it will jump to the catch. 
        out = new PrintStream(sock.getOutputStream()); //new PrintStream that gets the Output Stream from sock and assigns it to out. 
        in = new BufferedReader(new InputStreamReader(sock.getInputStream())); //new BufferedReader and new InputStreamReader that gets the Input Stream from sock and assigns it to in. 
        
        String inLine = in.readLine(); //reads each line of in. Assigns it to new string called inLine
        StringBuilder htmlString = new StringBuilder(); //new StringBuilder assigned to a new StringBuilder variable called htmlString. A StringBuilder is an editable string. or a seqance or chars that can be edited. 
        
        System.out.println(); //prints a blank line
        System.out.println("Request line: " + inLine); // prints Request line: + what is stored in inLine. (ie: Request line: GET /?person=YourTextInput HTTP/1.1)
        
        if(inLine.indexOf("migrate") > -1) {//if statement to see the user entered migrate
            clientSock = new Socket(NewHost, NewHostMainPort); //new Socket that takes in the the string NewHost that is assigned to localhost and an int NewHostMainPort that is assigned to 4242
            fromHostServer = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
            toHostServer = new PrintStream(clientSock.getOutputStream()); //new PrintStream that takes in the Ouput Stream of clientSock. Then assigns it to toHostServer
            toHostServer.println("Please host me. Send my port! [State=" + parentAgentHolder.agentState + "]");// prints Please host me. Send my port! [State= + the agentHolder parentAgentHolder.agentState. agentState is a int that gets added later in the code.
            //example of what would be printed: Please host me. Send my port! [State=0]
            toHostServer.flush(); //flushes PrintStream toHostServer
            for(;;) {//for statement
                buf = fromHostServer.readLine(); //read lines from fromHostServer and assign it to buf
                if(buf.indexOf("[Port=") > -1) {//checks to see if buf the index of [Port= is greater than or = to 0
                    break;//breaks if true
                }//closes if
            }//end of for
      
            String tempbuf = buf.substring( buf.indexOf("[Port=")+6, buf.indexOf("]", buf.indexOf("[Port=")) ); //new string called tempbuf 
            //tempbuf takes buf's substring of the index of port + 6, the index of ] and the index of port again. this is used to get a number from buf.
            newPort = Integer.parseInt(tempbuf); //takes the string tempbuf and pases it into an int. Sets the int = to newPort
            System.out.println("newPort is: " + newPort); //prints newPort is: + the newPort number.
            //example of what would be printed: newPort is: 3004
            htmlString.append(AgentListener.sendHTMLheader(newPort, NewHost, inLine));//apends to the StringBuilder htmlString.
            //sends the int newPort, string NewHost, and the sting inLine to AgentListener.sendHTMLheader
            htmlString.append("<h3>We are migrating to host " + newPort + "</h3> \n");//apends to the StringBuilder htmlString.
            //makes a <h3> html tag with the text We are migrating to host + the new port number. then makes a new line
            //Example on webpage: We are migrating to host 3005
            htmlString.append("<h3>View the source of this page to see how the client is informed of the new location.</h3> \n"); //apends to the StringBuilder htmlString.
            //makes a <h3> html tag with the text View the source of this page to see how the client is informed of the new location.
            htmlString.append(AgentListener.sendHTMLsubmit());//apends to the StringBuilder htmlString.
            
            System.out.println("Killing parent listening loop.");//prints Killing parent listening loop.
            ServerSocket ss = parentAgentHolder.sock; // ServerSocket called ss. that gets parentAgentHolder.sock
            ss.close();//close ServerSocket ss.      
        }//end of if migrate
        else if(inLine.indexOf("person") > -1) {//checks to see if index of person is greater than or = to 0.
            parentAgentHolder.agentState++; //adds to the agentState in the agentHolder parentAgentHolder.
            htmlString.append(AgentListener.sendHTMLheader(localPort, NewHost, inLine));//apends to the StringBuilder htmlString.
            //sends the int newPort, string NewHost, and the sting inLine to AgentListener.sendHTMLheader
            htmlString.append("<h3>We are having a conversation with state   " + parentAgentHolder.agentState + "</h3>\n");//apends to the StringBuilder htmlString.
            //makes a <h3> html tag with the text We are having a conversation with state+ the int of parentAgentHolder.agentState and makes a new line
            //Example on webpage: We are having a conversation with state 11
            htmlString.append(AgentListener.sendHTMLsubmit());//apends to the StringBuilder htmlString.
        }//clses else if  
        else {
            htmlString.append(AgentListener.sendHTMLheader(localPort, NewHost, inLine));//apends to the StringBuilder htmlString.
            //sends the int newPort, string NewHost, and the sting inLine to AgentListener.sendHTMLheader
            htmlString.append("You have not entered a valid request!\n");//apends to the StringBuilder htmlString.
            //prints on the webpage You have not entered a valid request! and then a new line
            htmlString.append(AgentListener.sendHTMLsubmit());//apends to the StringBuilder htmlString.
        }//closes else
        AgentListener.sendHTMLtoStream(htmlString.toString(), out);//send htmlString as a string and out to sendHTMLtoStream
        sock.close();//closes Socket sock
      }//end try statment 
      catch (IOException ioe) { //Catch if try fails. IOException
        System.out.println(ioe);//prints ioe if try fails
      }//end of catch
    }//end run
  }//end of AgentWorker class

/* 
Notes on agentHolder class:
This is a simple class that allows other classes to call agentHolder and takes in a ServerSocket. 
It also has a int agentState initialized. that other classes call to.
This class is used in the AgentWorker class and the AgentListener class. 
*/
  
  class agentHolder { //start of class agentHolder
    ServerSocket sock;// new ServerSocket called sock initialized.
    int agentState;//new int called agentState initialized.
    agentHolder(ServerSocket s) {//start of agentHolder call. Takes in a ServerSocket. 
        sock = s; //assigns the ServerSocket Sock to the ServerSocket s that was taken in 
    }// end of agentHolder call
  }//clases class agentHolder
  
/* 
Notes on AgentListener class:
*/

  class AgentListener extends Thread { //new class AgentListener that extends Thread
    Socket sock;//new socket called sock initialized.
    int localPort; //new int called localPort initialized.
    AgentListener(Socket As, int prt) { //start of AgentListener that takes in a Socket and an Int
      sock = As;//assigns the Socket sock to the Socket as that was taken in.
      localPort = prt; //assigns the int localPort to the int prt that was taken in.
    }//closes AgentListener
    int agentState = 0;//new int called agentState set to 0.
    public void run() {//start of run
      BufferedReader in = null; //new BufferedReader called in and set to null
      PrintStream out = null; //new PrintStream called out and set to null
      String NewHost = "localhost"; //new String called NewHost and set to localhost
      System.out.println("In AgentListener Thread");//prints In AgentListener Thread	
      try {//trys the following code. If fails then jumps to catch.
        String buf; //new string called buf initialized.
        out = new PrintStream(sock.getOutputStream()); //new PrintStream that gets the Output Stream from sock and assigns it to out. 
        in =  new BufferedReader(new InputStreamReader(sock.getInputStream())); //new BufferedReader and new InputStreamReader that gets the Input Stream from sock and assigns it to in. 
        buf = in.readLine(); //reads the lines in in and assigns them to buf
        if(buf != null && buf.indexOf("[State=") > -1) {//if buff is not null and its index of [State= is greaterthan or = to 0.
            String tempbuf = buf.substring(buf.indexOf("[State=")+7, buf.indexOf("]", buf.indexOf("[State="))); //new string tempbuf
            //tempbuf takes buf's substring of the index of port + 7, the index of ] and the index of port again. this is used to get a number from buf.
            agentState = Integer.parseInt(tempbuf); ////takes the string tempbuf and pases it into an int. Sets the int = to agentState
            System.out.println("agentState is: " + agentState); //prints agentState is: + agentState
            //example of whats printed in the teminal: agentState is: 11

        }//clase if statement 
        
        System.out.println(buf); //prints buf
        //example of whats printed in the teminal: GET /favicon.ico HTTP/1.1

        StringBuilder htmlResponse = new StringBuilder(); //new StringBuilder called htmlResponse
        htmlResponse.append(sendHTMLheader(localPort, NewHost, buf)); //apends to the StringBuilder htmlResponse.
        //sends the int newPort, string NewHost, and the sting inLine to sendHTMLheader
        htmlResponse.append("Now in Agent Looper starting Agent Listening Loop\n<br />\n"); //apends to the StringBuilder htmlResponse.
        //print statement to HTML webpage only when page is first loaded on local host
        htmlResponse.append("[Port="+localPort+"]<br/>\n");//apends to the StringBuilder htmlResponse.
        //print statement to HTML webpage only when page is first loaded on local host
        //Example:
            /*
                Now in Agent Looper starting Agent Listening Loop
                [Port=3003]
            */
        htmlResponse.append(sendHTMLsubmit());//apends to the StringBuilder htmlResponse.
        sendHTMLtoStream(htmlResponse.toString(), out); //send htmlResponse as a string and out to sendHTMLtoStream
        
        ServerSocket servsock = new ServerSocket(localPort,2); //new ServerSocket called servsock. Takes in localPort and 2.
        agentHolder agenthold = new agentHolder(servsock); //new agentHolder called agenthold. Takes in servsock
        agenthold.agentState = agentState;
        
        while(true) {//while true do the following code 
            sock = servsock.accept();//accepts servsock and assigns it to sock
            System.out.println("Got a connection to agent at port " + localPort);//print Got a connection to agent at port + localPort
            //example : Got a connection to agent at port 3002
            new AgentWorker(sock, localPort, agenthold).start();//new AgentWorker starts. takes in sock, localPort, agenthold
        }//end of while
        
      } //end of try
      catch(IOException ioe) {//catch when the try fails
        System.out.println("Either connection failed, or just killed listener loop for agent at port " + localPort);//prints this to terminal
        System.out.println(ioe);//prints ioe
        //Example:
            /*
            Either connection failed, or just killed listener loop for agent at port 3004
            java.net.SocketException: socket closed
            */
      }//end of catch
    }//end of run.
    static String sendHTMLheader(int localPort, String NewHost, String inLine) {//start of sendHTMLheader. Takes in an int localPort a String NewHost and a String inLine.
      StringBuilder htmlString = new StringBuilder();// //new StringBuilder called htmlString
      htmlString.append("<html><head> </head><body>\n"); //apends to the StringBuilder htmlString.
      //new html that starts the html, head. closes the head and starts the body. then makes a new line  
      htmlString.append("<h2>This is for submission to PORT " + localPort + " on " + NewHost + "</h2>\n");//apends to the StringBuilder htmlString.
      //html h2 tag that prints This is for submission to PORT + localPort on NewHost and makes a new line 
      //Example: 
        /*
        This is for submission to PORT 3003 on localhost
        */
     htmlString.append("<h3>You sent: "+ inLine + "</h3>");//apends to the StringBuilder htmlString.
      //html h3 tag that prints You sent: + inline
      //Example: 
        /*
        You sent: GET / HTTP/1.1
        */
      htmlString.append("\n<form method=\"GET\" action=\"http://" + NewHost +":" + localPort + "\">\n");//apends to the StringBuilder htmlString.
        //new line and the form.
      htmlString.append("Enter text or <i>migrate</i>:"); //apends to the StringBuilder htmlString.
      // text before the form Enter text or migrate with migrate being italzized 
      htmlString.append("\n<input type=\"text\" name=\"person\" size=\"20\" value=\"YourTextInput\" /> <p>\n");//apends to the StringBuilder htmlString.
      //makes the value in the box YourTextInput
      return htmlString.toString(); //retuns the htmlString as a string 
    }//end of sendHTMLheader
    static String sendHTMLsubmit() {//start of sendHTMLsubmit
      return "<input type=\"submit\" value=\"Submit\"" + "</p>\n</form></body></html>\n";//retruns this 
      //the submit button and the end of the html tags
    }//end of sendHTMLsubmit
    static void sendHTMLtoStream(String html, PrintStream out) {//start of sendHTMLtoStream
      
      out.println("HTTP/1.1 200 OK");//prints to terinal 
      out.println("Content-Length: " + html.length());
      out.println("Content-Type: text/html");//prints to terinal 
      out.println("");//prints to terinal 		
      out.println(html);
    }//end of sendHTMLtoStream
    
  }//end of AgentListener

 /* 
Notes on HostServer class:
*/
  public class HostServer {
    public static int NextPort = 3000;//
    public static void main(String[] a) throws IOException {//start of main
      int q_len = 6;
      int port = 4242;
      Socket sock;
      
      ServerSocket servsock = new ServerSocket(port, q_len);
      System.out.println("Elliott/Reagan DIA Master receiver started at port 4242.");
      System.out.println("With small edits and comments from Jessica Bender. 3/7/2021");//added this text to show that this is my comments and small changes
      System.out.println("Connect from 1 to 3 browsers using \"http:\\\\localhost:4242\"\n");
      while(true) {
        NextPort = NextPort + 1;
        sock = servsock.accept();
        System.out.println("Starting AgentListener at port " + NextPort);
        new AgentListener(sock, NextPort).start();
      }//end of while
    }//end of main
  }//end of HostServer

  
    //---------------------------------------------End of new comments By Jessica Bender-----------------------------------------------------------------------------------------------------
    
//log of what whas printed on my terminal for examples: 


//-----------------example with 1 chrome browser open--------------------------
/*
C:\Users\jessi\Documents\2020-2021 school\Winter\CSC435winter2021\HostServer>java HostServer
Elliott/Reagan DIA Master receiver started at port 4242.
Connect from 1 to 3 browsers using "http:\\localhost:4242"

Starting AgentListener at port 3001
Starting AgentListener at port 3002
In AgentListener Thread
In AgentListener Thread
GET / HTTP/1.1
Starting AgentListener at port 3003
In AgentListener Thread
GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3001
Got a connection to agent at port 3001

Request line: GET /?person=migrate HTTP/1.1
Starting AgentListener at port 3004
In AgentListener Thread
agentState is: 0
Please host me. Send my port! [State=0]
newPort is: 3004
Killing parent listening loop.
Either connection failed, or just killed listener loop for agent at port 3001
java.net.SocketException: socket closed

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3004
Got a connection to agent at port 3004

Request line: GET /?person=migrate HTTP/1.1
Starting AgentListener at port 3005
In AgentListener Thread
agentState is: 0
Please host me. Send my port! [State=0]
newPort is: 3005
Killing parent listening loop.
Either connection failed, or just killed listener loop for agent at port 3004
java.net.SocketException: socket closed

Request line: GET /favicon.ico HTTP/1.1
null
Got a connection to agent at port 3005
Got a connection to agent at port 3005

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3005
Got a connection to agent at port 3005

Request line: GET /?person=migrate HTTP/1.1
Starting AgentListener at port 3006
In AgentListener Thread
agentState is: 1
Please host me. Send my port! [State=1]
newPort is: 3006
Killing parent listening loop.
Either connection failed, or just killed listener loop for agent at port 3005
java.net.SocketException: socket closed

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3006
Got a connection to agent at port 3006

Request line: GET / HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3006
Got a connection to agent at port 3006

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3006
Got a connection to agent at port 3006

Request line: GET /?person=migrate HTTP/1.1
Starting AgentListener at port 3007
In AgentListener Thread
agentState is: 2
Please host me. Send my port! [State=2]
newPort is: 3007
Killing parent listening loop.
Either connection failed, or just killed listener loop for agent at port 3006
java.net.SocketException: socket closed

Request line: GET /favicon.ico HTTP/1.1
*/
//----------end of log--------------------------------------------------------------






//-------------example with 3 chrome browsers open------------------------------

/*

C:\Users\jessi\Documents\2020-2021 school\Winter\CSC435winter2021\HostServer>java HostServer
Elliott/Reagan DIA Master receiver started at port 4242.
Connect from 1 to 3 browsers using "http:\\localhost:4242"

Starting AgentListener at port 3001
Starting AgentListener at port 3002
In AgentListener Thread
In AgentListener Thread
GET / HTTP/1.1
Starting AgentListener at port 3003
In AgentListener Thread
GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3001
Got a connection to agent at port 3001

Request line: GET /?person=migrate HTTP/1.1
Starting AgentListener at port 3004
In AgentListener Thread
agentState is: 0
Please host me. Send my port! [State=0]
newPort is: 3004
Killing parent listening loop.
Either connection failed, or just killed listener loop for agent at port 3001
java.net.SocketException: socket closed

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3004
Got a connection to agent at port 3004

Request line: GET /?person=migrate HTTP/1.1
Starting AgentListener at port 3005
In AgentListener Thread
agentState is: 0
Please host me. Send my port! [State=0]
newPort is: 3005
Killing parent listening loop.
Either connection failed, or just killed listener loop for agent at port 3004
java.net.SocketException: socket closed

Request line: GET /favicon.ico HTTP/1.1
null
Got a connection to agent at port 3005
Got a connection to agent at port 3005

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3005
Got a connection to agent at port 3005

Request line: GET /?person=migrate HTTP/1.1
Starting AgentListener at port 3006
In AgentListener Thread
agentState is: 1
Please host me. Send my port! [State=1]
newPort is: 3006
Killing parent listening loop.
Either connection failed, or just killed listener loop for agent at port 3005
java.net.SocketException: socket closed

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3006
Got a connection to agent at port 3006

Request line: GET / HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3006
Got a connection to agent at port 3006

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3006
Got a connection to agent at port 3006

Request line: GET /?person=migrate HTTP/1.1
Starting AgentListener at port 3007
In AgentListener Thread
agentState is: 2
Please host me. Send my port! [State=2]
newPort is: 3007
Killing parent listening loop.
Either connection failed, or just killed listener loop for agent at port 3006
java.net.SocketException: socket closed

Request line: GET /favicon.ico HTTP/1.1

C:\Users\jessi\Documents\2020-2021 school\Winter\CSC435winter2021\HostServer>java HostServer
Elliott/Reagan DIA Master receiver started at port 4242.
Connect from 1 to 3 browsers using "http:\\localhost:4242"

Starting AgentListener at port 3001
Starting AgentListener at port 3002
In AgentListener Thread
In AgentListener Thread
GET / HTTP/1.1
Starting AgentListener at port 3003
In AgentListener Thread
GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=hdlew HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInputsIVH HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=JYYF HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=migrate HTTP/1.1
Starting AgentListener at port 3004
In AgentListener Thread
agentState is: 3
Please host me. Send my port! [State=3]
newPort is: 3004
Killing parent listening loop.
Either connection failed, or just killed listener loop for agent at port 3002
java.net.SocketException: socket closed

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3004
Got a connection to agent at port 3004

Request line: GET /?person=person HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
null
Got a connection to agent at port 3004
Got a connection to agent at port 3004

Request line: GET /?person= HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3004
Got a connection to agent at port 3004

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3004
Got a connection to agent at port 3004

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3004
Got a connection to agent at port 3004

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3004
Got a connection to agent at port 3004

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3004
Got a connection to agent at port 3004

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3004
Got a connection to agent at port 3004

Request line: GET /?person=migrate HTTP/1.1
Starting AgentListener at port 3005
In AgentListener Thread
agentState is: 10
Please host me. Send my port! [State=10]
newPort is: 3005
Killing parent listening loop.
Either connection failed, or just killed listener loop for agent at port 3004
java.net.SocketException: socket closed

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3005
Got a connection to agent at port 3005

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3005
Got a connection to agent at port 3005

Request line: GET /?person=migrate HTTP/1.1
Starting AgentListener at port 3006
In AgentListener Thread
agentState is: 11
Please host me. Send my port! [State=11]
newPort is: 3006
Killing parent listening loop.
Either connection failed, or just killed listener loop for agent at port 3005
java.net.SocketException: socket closed

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3006
Got a connection to agent at port 3006

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Starting AgentListener at port 3007
Starting AgentListener at port 3008
In AgentListener Thread
In AgentListener Thread
GET / HTTP/1.1
GET /favicon.ico HTTP/1.1
Starting AgentListener at port 3009
Starting AgentListener at port 3010
In AgentListener Thread
In AgentListener Thread
GET / HTTP/1.1
GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3008
Got a connection to agent at port 3008

Request line: GET /?person=dsahj HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3010
Got a connection to agent at port 3010

Request line: GET /?person=dsa HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3006
Got a connection to agent at port 3006

Request line: GET /?person=ads HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3008
Got a connection to agent at port 3008

Request line: GET /?person=da HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3010
Got a connection to agent at port 3010

Request line: GET /?person=YourTextIncput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3006
Got a connection to agent at port 3006

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3006
Got a connection to agent at port 3006

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3006
Got a connection to agent at port 3006

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3006
Got a connection to agent at port 3006

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3006
Got a connection to agent at port 3006

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3008
Got a connection to agent at port 3008

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3008
Got a connection to agent at port 3008

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3008
Got a connection to agent at port 3008

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3010
Got a connection to agent at port 3010

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1

*/

//----------end of log--------------------------------------------------------------





//-------------example with my code edits :) ------------------------------

/*
Elliott/Reagan DIA Master receiver started at port 4242.
With small edits and comments from Jessica Bender. 3/7/2021
Connect from 1 to 3 browsers using "http:\\localhost:4242"

Starting AgentListener at port 3001
Starting AgentListener at port 3002
In AgentListener Thread
In AgentListener Thread
GET / HTTP/1.1
GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Got a connection to agent at port 3002
Request line: GET /favicon.ico HTTP/1.1

Request line: GET /?person=YourTextInput HTTP/1.1
Got a connection to agent at port 3002

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3002
Got a connection to agent at port 3002

Request line: GET /?person=migrate HTTP/1.1
Starting AgentListener at port 3003
In AgentListener Thread
agentState is: 23
Please host me. Send my port! [State=23]
newPort is: 3003
Killing parent listening loop.
Either connection failed, or just killed listener loop for agent at port 3002
java.net.SocketException: socket closed

Request line: GET /favicon.ico HTTP/1.1
Got a connection to agent at port 3003
Got a connection to agent at port 3003

Request line: GET /?person=YourTextInput HTTP/1.1

Request line: GET /favicon.ico HTTP/1.1

*/
//----------end of log--------------------------------------------------------------
