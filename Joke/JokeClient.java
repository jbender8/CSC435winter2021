/*--------------------------------------------------------

1. Name / Date: Jessica Bender / version 2 - 1/24/2021

2. Java version used, if not the official version for the class:

e.g. build 1.5.0_06-b05: 

My Java:
java version "9.0.4"
Java(TM) SE Runtime Environment (build 9.0.4+11)
Java HotSpot(TM) 64-Bit Server VM (build 9.0.4+11, mixed mode)

3. Precise command-line compilation examples / instructions:
> javac JokeClient.java

4. Precise examples / instructions to run this program:
In separate shell windows:

> java JokeServer
> java JokeClient
> java JokeClientAdmin



5. List of files needed for running the program.

e.g.:

 a. checklist.html
 b. JokeServer.java
 c. JokeClient.java
 d. JokeClientAdmin.java

6. Notes:

For this to run since it is a little buggy put joke in JokeClientAdmin, but a username into Jokeclient then type switch to get jokes. I dont think it will give you any perverbs and it also jsut keeps running. I ran out of time and wanted to get some credit


----------------------------------------------------------*/

// Referanced Inet homework assignment from January 2021. Some comments may overlap from JokeClient, JokeClientAdmin and JokeServer.
// Code Starts Here:
import java.io.*;// importing all packages in java io.
import java.net.*;// importing all packages in java net.

public class JokeClient { // start of the JokeClient class
    public static void main(String[] args) {// start of main method
        String serverName; //initialize serverName as a String varable. 
        if (args.length < 1) serverName = "localhost";//checks to see if args is less than one. If true, serverName is set equal to localhost.
        else serverName = args[0];//otherwise serverName is set equal to args[0].
        System.out.println("Jess Bender's Joke Client, Version 2.\n");//prints statement on terminal.
        System.out.println("Using server: " + serverName + ", Port: 1581"); //prints Using server: plus whatever is saved in serverName plus the port.
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));// new BufferedReader named in
        try {//trys the following code, if fails jumps to catch.
            String name; //assigns a string varable to get the users username.
            String another; //Assigns a string varable to get the usersinput if they want anther joke or not
            int JokeNum; //Assigns a int varable to randomize the jokes
            int max; //Assigns a int varable to randomize the jokes max number
            int min; //Assigns a int varable to randomize the jokes min number
            max = 100; //Assigning max to 100 for now not sure what the number should be yet. Maybe 4? since there are 4 Jokes and 4 Perverbs.
            min = 1; //Also sasigning min to 1 for now not sure what the number should be yet. Think it should be 1 or 0?
            JokeNum =  (int)Math.random() * (max - min + 1) + min; //getting a random number for jokeNum using the Math.random function. However since math.random gives a double I multiplied it by int so it would give me an int
            do{//start do statement
                System.out.print("Welcome to the JokeServer! Enter a username to get your first Joke or Proverb or type 'quit' to exit program: ");//prints statement on terminal. Saying welcome and Asking for the users username
                System.out.flush ();
                name = in.readLine ();//assigns the text from the BufferedReader in to name.
                if(name.length()>=1){
                    System.out.print("Hello "+ name + ",\nLet's get your first Joke!\n");//prints statement on terminal. Saying welcome
                }else if(name.length()<1){
                    System.out.print("Im sorry I did not catch your name.\n");//prints statement on terminal. Saying ne name was entered
                }else{
                    System.out.println ("Exiting program. Come back Soon!");//when another = end print this statement. saying they are exiting the program and tell them to come back soon!
                }
                }//close do
                while (name.indexOf("quit") < 0 && name.length()<1);// keep doing the do above until another = end
                do {//Start 2nd do statement
                    System.out.print("To receive your next Joke or Proverb enter 'next'! To swich from Joke to Proverb or vise versa enter 'switch'! To end program type 'end': ");//prints statement on terminal. Asking the user if they want another joke or not
                    System.out.flush ();
                    another = in.readLine ();//assigns the text from the BufferedReader in to another.
                    if (another.indexOf("end") < 0 ){//checks to see if another = end and switch
                        getRemoteAddress(another, serverName);//calls function getRemoteAddress below and puts in name and serverName for the second string varables.
                    }//close if
                }
                //closes 2nd do 
                while (another.indexOf("end") < 0);// keep doing the do above until another = end
                System.out.println ("Exiting program. Come back for more Jokes and Perverbs soon!");//when another = end print this statement. saying they are exiting the program and tell them to come back soon!
            }//closes try
        catch (IOException x) {x.printStackTrace ();} //catches IOExeption when try fails and prints the error.
    }//closes main
    

    //not sure if I need the toText methond from Inet will leave here for now tho
    static String toText (byte ip[]) { //new method
        StringBuffer result = new StringBuffer (); //new StringBuffer called result.
        for (int i = 0; i < ip.length; ++ i) {
            if (i > 0) result.append ("."); // add . to result if i is larger than 0.
            result.append (0xff & ip[i]); // if not greater than 0 0xff & ip[i] gets added to result.
        }//closes for
        return result.toString ();// returns result as a string
    }//closes toText


    
    static void getRemoteAddress (String name, String serverName){ //// gets it name and serverName string variables and JokeNum int varable from above in the do statement 
        Socket sock; //makes Socket variable called sock.
        BufferedReader fromServer; //makes BufferedReader variable called fromServer.
        PrintStream toServer; //makes PrintStream variable called toServer.
        String textFromServer; //makes String variable called textFromServer.
        
        try{//trys the following code, if fails jumps to catch.
            sock = new Socket(serverName, 1591); //assigns sock toa new Socket with serverName and the port 1581
            fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream())); //assigns fromServer to a new BufferedReader that gets the input from sock
            toServer = new PrintStream(sock.getOutputStream()); //assigns toServer to a new PrintStream that writes the input from sock
            toServer.println(name); toServer.flush(); //Prints the name and the JokeNum
            
            for (int i = 1; i <=3; i++){
                textFromServer = fromServer.readLine();//assigns textFromServer to the text in fromServer.
                if (textFromServer != null) System.out.println(textFromServer); //checks to make sure textFromServer is not null/ empty and id so prints whatever was in textFromServer.
            }//closes for
            sock.close();//closes sock
        } //closes try
        catch (IOException x) { //catches IOExeption when try fails.
            System.out.println ("Socket error.");
            x.printStackTrace ();//prints Socket error and printStackTrace witch prints details about the error including the line number where the error occurred.
        }//closes catch
    }//closes getRemoteAddress

}// End of JokeClient class