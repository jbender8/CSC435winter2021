/*--------------------------------------------------------

1. Name / Date: Jessica Bender / version 2 - 1/24/2021

2. Java version used, if not the official version for the class:

My Java:
java version "9.0.4"
Java(TM) SE Runtime Environment (build 9.0.4+11)
Java HotSpot(TM) 64-Bit Server VM (build 9.0.4+11, mixed mode)

3. Precise command-line compilation examples / instructions:


> javac JokeClientAdmin.java


4. Precise examples / instructions to run this program:

e.g.:

In separate shell windows:

> java JokeServer
> java JokeClient
> java JokeClientAdmin

All acceptable commands are displayed on the various consoles.

This runs across machines, in which case you have to pass the IP address of
the server to the clients. For exmaple, if the server is running at
140.192.1.22 then you would type:

> java JokeClient 140.192.1.22
> java JokeClientAdmin 140.192.1.22

5. List of files needed for running the program.

 a. checklist.html
 b. JokeServer.java
 c. JokeClient.java
 d. JokeClientAdmin.java

5. Notes:

For this to run since it is a little buggy put joke in JokeClientAdmin, but a username into Jokeclient then type switch to get jokes. I dont think it will give you any perverbs and it also jsut keeps running. I ran out of time and wanted to get some credit

----------------------------------------------------------*/

// Referanced Inet homework assignment from January 2021. Some comments may overlap from JokeClient, JokeClientAdmin and JokeServer.
// Code Starts Here:
import java.io.*;// importing all packages in java io.
import java.net.*;// importing all packages in java net.


public class JokeClientAdmin { // start of the JokeClientAdmin class
    public static void main(String[] args) {
        String serverName; //initialize serverName as a String varable. 
        if (args.length < 1) serverName = "localhost";//checks to see if args is less than one. If true, serverName is set equal to localhost.
        else serverName = args[0];//otherwise serverName is set equal to args[0].
        System.out.println("Jess Bender's Joke Client Admin, Version 2.\n");//prints statement on terminal.
        System.out.println("Using server: " + serverName + ", Port: 1881"); //prints Using server: plus whatever is saved in serverName plus the port.
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));// new BufferedReader named in
        try {//trys the following code, if fails jumps to catch.
            String or;
            do {
                System.out.print("This is the Admin Client, Please enter weather you want a 'joke' or 'perverb' or 'quit' to end program: ");//prints statement on terminal.
                System.out.flush ();
                or = in.readLine ();//assigns the text from the BufferedReader in to or.
                if (or.indexOf("quit") < 0)//checks to see if or = quit
                    getRemoteAddress(or, serverName);//calls function getRemoteAddress below and puts in or and serverName for the 2 string varables.
            }//closes do 
            while (or.indexOf("quit") < 0);// keep doing the do above until or = quit
            System.out.println ("Leaving Program......GoodBye!");//when or = quit print this statement.
        }//closes try
        catch (IOException x) {x.printStackTrace ();} //catches IOExeption when try fails and prints the error.
    }//closes main
    
    static void getRemoteAddress (String or, String serverName){ //// gets it or and serverName variables from above in the do statement
        Socket sock; //makes Socket variable called sock.
        BufferedReader fromServer; //makes BufferedReader variable called fromServer.
        PrintStream toServer; //makes PrintStream variable called toServer.
       
        try{//trys the following code, if fails jumps to catch.
            sock = new Socket(serverName, 2571); //assigns sock toa new Socket with serverName and the port 1881
            fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream())); //assigns fromServer to a new BufferedReader that gets the input from sock
            toServer = new PrintStream(sock.getOutputStream()); //assigns toServer to a new PrintStream that writes the input from sock
            toServer.println(or); toServer.flush(); 
            System.out.println(fromServer.readLine()); //checks to make sure textFromServer is not null/ empty and id so prints whatever was in textFromServer.
        } //closes try
        catch (IOException x) { //catches IOExeption when try fails.
            System.out.println ("Socket error.");
            x.printStackTrace ();//prints Socket error and printStackTrace witch prints details about the error including the line number where the error occurred.
        }//closes catch
    }//closes getRemoteAddress
}// End of JokeClientAdmin class