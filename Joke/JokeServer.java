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
// All Jokes were taken from google. here is the link used https://www.rd.com/list/short-jokes/
// All Proverbs were taken from google. here is the link used https://lemongrad.com/proverbs-with-meanings-and-examples/ 
// Code Starts Here:
import java.io.*;// importing all packages in java io.
import java.net.*;// importing all packages in java net.
import java.util.HashMap; //importing hashmaps to use to store jokes and proverbs
class User{ //start user class. This class will see who the use is so they dont get the same jokes.
    String users; // makes a string varable to get and store the usename
    int[] jokes; //makes a int array for the used jokes
    int[] proverbs; //makes a int array for the used proverbs
    jokes = {0,0,0,0}; //adding 4 zeros into joke array for the 4 allowed jokes.
    proverbs = {0,0,0,0}; //adding 4 zeros into proverbs array for the 4 allowed proverbs.

    public String getuser(){//method to get the username
        return users;//returns the username
    }//closes getuser method

    public int[] getjoke(){//method to get the jokes we aleady used
        return jokes;//returns the jokes we have already used
    }//closes getjoke method

    public int[] getproverb(){//method to get the provers we aleady used
        return proverbs;//returns the proverbs we have already used
    }//closes getproverb method



    public void setuser(String users){//method to set the username
        this.users = users;//sets user
    }//closes setuser method

    public void setjoke(int[] jokes){//method to set the jokes we aleady used
        this.jokes = jokes;//sets jokes
    }//closes setjoke method

    public void setproverb(int[] proverbs){//method to set the provers we aleady used
        this.proverbs = proverbs;//sets proverbs
    }//closes setproverb method

}//end user class

class Worker extends Thread{ //start Worker class. This class will 
    Socket sock; //makes a new Socket called sock. 
    Worker (Socket s) {sock = s;} 
    public void run(){
        PrintStream out = null; //assigns a PrintStream variable called out to null. PrintStream is a directory in the java.io package that writes output data.
        BufferedReader in = null; //assigns a BufferedReader variable called in to null. BufferedReader is a directory in the java.io package that reads text from an input including sockets
        try { //will try the things within the try otherwise will skip to catch
            in = new BufferedReader // assigns in to a new BufferedReader
                (new InputStreamReader(sock.getInputStream()));
            out = new PrintStream(sock.getOutputStream()); // assigns out to a new PrintStream that gets its data from the Socket sock .getInputStream() allows us to get that output
            try { //will try the things within the try otherwise will skip to catch
                String name;//makes a new string called name
                name = in.readLine ();//assigns the string variable name to what was read in the the varaible in. 
                System.out.println("Looking up your Joke or Proverb " + name); //prints Lokking up and whatever was saved into the variable name to the terminal.
                printJoke(name, out); //calls the static void printJoke a few lines down and sets the String to whatever the name vaible was assigned to and the PrintStream to whatever the out vaible was assigned to.
            }//closes second try
            catch (IOException x) { //catches IOExeption when try fails
                System.out.println("Server read error"); x.printStackTrace (); //prints Server read error and printStackTrace witch prints details about the error including the line number where the error occurred.
            } //closes catch
            sock.close(); // closes the Socket called sock
        } //closes first try
        catch (IOException ioe) {System.out.println(ioe);} //catches IOExeption when try fails and prints the error
    } // closes run()

    public printJoke(String name, PrintStream out){//Start printJoke method
        Text text = Next
        try{ //will try the things within the try otherwise will skip to catch

        }//end try
        catch(UnknownHostException ex) { //catches UnknownHostException when try fails
            out.println ("Failed to get joke for " + name); //prints Failed in atempt to look up and whatever was stored in the name variable. 
        }//closes catch

    }//end printJoke method


}//closes worker class 
public class JokeServer { // start of the JokeServer class
    static String choice ="proverb";//makes a static varable to see if the user is on joke or proverb. I am also initizing it as a proverb

    public static String joke(String haha){ //start of joke method take in a string haha
        HashMap<String, String> Jokes = new HashMap<String, String>(); //making a new hashmap to store my jokes. First string in the hashmap will be for the joke numbers ie JA, JB, JC, and JD. the second string will be the actual joke.
        Jokes.put("JA", "Why did the nurse need a red pen at work? In case she needed to draw blood."); //first joke.
        Jokes.put("JB", "I invented a new word! Plagiarism!"); //Second joke.
        Jokes.put("JC", "Yesterday I saw a guy spill all his scrabble letter on the road. I asked him, 'what's the word on the street?'"); //Third joke.
        Jokes.put("JD", "Hear about the new resturant called Karma? There's no menu: You get what you deserve."); //Fouth joke.

        return haha+":" + Jokes.get(haha);
    }
    public static String proverb(String wisdom){ //start of joke method take in a string haha
        HashMap<String, String> Proverbs = new HashMap<String, String>(); //making a new hashmap to store my jokes. First string in the hashmap will be for the joke numbers ie JA, JB, JC, and JD. the second string will be the actual joke.
        Proverbs.put("PA", "A bad workman always blames his tools."); //first Proverb.
        Proverbs.put("PB", "A journey of thousand miles begins with a single step."); //Second Proverb.
        Proverbs.put("PC", "Beggars can’t be choosers."); //Third Proverb.
        Proverbs.put("PD", "Beauty is in the eye of the beholder."); //Fouth Proverb.

        return wisdom+":" + Proverbs.get(wisdom);

    }

    public static void main(String[] args) {// start of main method
        int q_len = 6; //initialize q_len as an int and assigns it to 6
        int port = 1581;//initialize port as an int and assigns a port number(can be changed)
        Socket sock; //makes a Socket called sock
        ServerSocket servsock = new ServerSocket(port, q_len); //makes a new ServerSocket called servsock and puts the port number and q_len assigned above.
        System.out.println("Jess Bender's Joke Server, Version 2.\n");//prints statement on terminal.
        System.out.println("Listening at port 1581.\n");//prints that text on the termial
        while (true) { //keeps running while its true
            sock = servsock.accept(); //assignes sock to an accepted servsock.
            new Worker(sock).start(); //calls on worker class with the sock assigned in it.
        } //closes while
    }// end of main method



}// End of JokeServer class