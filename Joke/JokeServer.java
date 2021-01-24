/*--------------------------------------------------------

1. Name / Date: Jessica Bender / Version 2 1/24/2021

2. Java version used, if not the official version for the class:

My Java:
java version "9.0.4"
Java(TM) SE Runtime Environment (build 9.0.4+11)
Java HotSpot(TM) 64-Bit Server VM (build 9.0.4+11, mixed mode)


3. Precise command-line compilation examples / instructions:

> javac JokeServer.java


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

e.g.:

 a. checklist.html
 b. JokeServer.java
 c. JokeClient.java
 d. JokeClientAdmin.java

5. Notes:

e.g.:

I faked the random number generator. I have a bug that comes up once every
ten runs or so. If the server hangs, just kill it and restart it. You do not
have to restart the clients, they will find the server again when a request
is made.

----------------------------------------------------------*/

// Referanced Inet homework assignment from January 2021. Some comments may overlap from JokeClient, JokeClientAdmin and JokeServer.
// Code Starts Here:
import java.io.*;// importing all packages in java io.
import java.net.*;// importing all packages in java net.

public class JokeServer { // start of the JokeServer class
    public static String joke(String haha){ //start of joke method take in a string haha

    }
    public static String proverb(String wisdom){ //start of joke method take in a string haha

    }

    public static void main(String[] args) {// start of main method
        int q_len = 6; //initialize q_len as an int and assigns it to 6
        int port = 1581;//initialize port as an int and assigns a port number(can be changed)
        Socket sock; //makes a Socket called sock
        ServerSocket servsock = new ServerSocket(port, q_len); //makes a new ServerSocket called servsock and puts the port number and q_len assigned above.
        System.out.println("Jess Bender's Joke server 4.2 starting up, listening at port 1581.\n");//prints that text on the termial
        while (true) { //keeps running while its true
            sock = servsock.accept(); //assignes sock to an accepted servsock.
            new Worker(sock).start(); //calls on worker class with the sock assigned in it.
        } //closes while
    }// end of main method

}// End of JokeServer class