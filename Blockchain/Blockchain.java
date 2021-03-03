/*--------------------------------------------------------

1. Name / Date: Jessica Bender / version 1 - 2/22/2021

2. Java version used, if not the official version for the class:

My Java:
java version "9.0.4"
Java(TM) SE Runtime Environment (build 9.0.4+11)
Java HotSpot(TM) 64-Bit Server VM (build 9.0.4+11, mixed mode)

3. Precise command-line compilation examples / instructions:
> javac Blockchain.java

4. Precise examples / instructions to run this program:
In separate shell windows:

> java Blockchain 0
> java Blockchain 1
> java Blockchain 2

5. List of files needed for running the program.

 a. Blockchain.Java

6. Notes:

Ample help from provided code given in class and the below web sources that were given in the sample code.
The web sources:

https://mkyong.com/java/how-to-parse-json-with-gson/
http://www.java2s.com/Code/Java/Security/SignatureSignAndVerify.htm
https://www.mkyong.com/java/java-digital-signatures-example/ (not so clear)
https://javadigest.wordpress.com/2012/08/26/rsa-encryption-example/
https://www.programcreek.com/java-api-examples/index.php?api=java.security.SecureRandom
https://www.mkyong.com/java/java-sha-hashing-example/
https://stackoverflow.com/questions/19818550/java-retrieve-the-actual-value-of-the-public-key-from-the-keypair-object
https://www.java67.com/2014/10/how-to-pad-numbers-with-leading-zeroes-in-Java-example.html 

----------------------------------------------------------*/

//Code Starts Here:
import java.text.*;
import java.security.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Blockchain{

    public static void main(String[] args) {
        int ProcessID = 0;
        String InputFile = "";
        if (args[0].equals("0")){
            ProcessID = 0;
            Ports.set(ProcessID);
            InputFile = "BlockInput0";
        }
        else if (args[0].equals("1")){
            ProcessID = 1;
            Ports.set(ProcessID);
            InputFile = "BlockInput1";
        }
        else if (args[0].equals("2")){
            ProcessID = 2;
            Ports.set(ProcessID);
            InputFile = "BlockInput2";
            new Thread(new threada()).start();

        }
        else{
            ProcessID = 0;
            Ports.set(ProcessID);
            InputFile = "BlockInput0";
        }
        System.out.println("Process ID #: "+ProcessID);
        System.out.println("Public Key Port: "+Ports.getkeyPort());
        System.out.println("Unverified Block Port: "+Ports.getunverifiedPort());
        System.out.println("Updated Blockchain Port: "+Ports.getupdatedPort()); 
        System.out.println(InputFile); 
    }
}

class Ports{
    static int keyPort = 4710;
    static int unverifiedPort = 4820;
    static int updatedPort  = 4930;
    public static void set(int ID){
        keyPort = keyPort + ID;
        unverifiedPort = unverifiedPort + ID;
        updatedPort = updatedPort + ID;
    }
    public static int getkeyPort(){
        return keyPort;
    }
    public static int getunverifiedPort(){
        return unverifiedPort;
    }
    public static int getupdatedPort(){
        return updatedPort;
    }

}

class BlockRecord {
    String BlockID;
    String VerificationProcessID;
    String PreviousHash; 
    UUID uuid;
    String Fname;
    String Lname;
    String SSNum;
    String DOB;
    String Diag;
    String Treat;
    String Rx;
    String RandomSeed; 
    String WinningHash;

    public String getBlockID() {
        return BlockID;
    }

    public void setBlockID(String BID) {
        this.BlockID = BID;
    }

    public String getVerificationProcessID() {
        return VerificationProcessID;
    }

    public void setVerificationProcessID(String VID) {
        this.VerificationProcessID = VID;
    }

    public String getPreviousHash() {
        return this.PreviousHash;
    }

    public void setPreviousHash(String PH) {
        this.PreviousHash = PH;
    }

    public UUID getUUID() {
        return uuid;
    } 

    public void setUUID(UUID ud) {
        this.uuid = ud;
    }

    public String getLname() {
        return Lname;
    }

    public void setLname(String LN) {
        this.Lname = LN;
    }

    public String getFname() {
        return Fname;
    }

    public void setFname(String FN) {
        this.Fname = FN;
    }

    public String getSSNum() {
        return SSNum;
    }

    public void setSSNum(String SS) {
        this.SSNum = SS;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String RS) {
        this.DOB = RS;
    }

    public String getDiag() {
        return Diag;
    }

    public void setDiag(String D) {
        this.Diag = D;
    }

    public String getTreat() {
        return Treat;
    }

    public void setTreat(String Tr) {
        this.Treat = Tr;
    }

    public String getRx() {
        return Rx;
    }

    public void setRx(String Rx) {
        this.Rx = Rx;
    }

    public String getRandomSeed() {
        return RandomSeed;
    }

    public void setRandomSeed(String RS) {
        this.RandomSeed = RS;
    }

    public String getWinningHash() {
        return WinningHash;
    }

    public void setWinningHash(String WH) {
        this.WinningHash = WH;
    }

}

class threada implements Runnable{
    public void run(){
        Socket sock;
        ServerSocket seversock;
    
        try{//trys the following code, if fails jumps to catch.
            seversock = new ServerSocket(1591,100); //assigns sock toa new Socket with serverName and the port 1581
            while(true){
                sock = seversock.accept();
            }
        }
        catch (IOException x) {x.printStackTrace ();} //catches IOExeption when try fails and prints the error.
    }
}