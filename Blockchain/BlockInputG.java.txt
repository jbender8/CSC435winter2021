/* BlockInputG.java

Version 3.0 2020-02-24, combines BlockingPriorityQueue.

To compile and run (may have to update the file name):

javac -cp "gson-2.8.2.jar" BlockInputG.java
java -cp ".;gson-2.8.2.jar" BlockInputG

RunBlockInput.bat:
java -cp ".;gson-2.8.2.jar" m %1

Example for process two:

> RunBlockInput 2

Author: Clark Elliott, with ample help from the below web sources.

You are free to use this code in your assignment, but you MUST add
your own comments. Leave in the web source references.


This utility program shows one method of reading data into a linked list of unverified blocks from an input data file.
The specific data file / Process ID is determined by argment passed to Java at runtime. 
The list is shuffled. Blocks are also written into a priority queue with TimeStamp priority which
demonstrates how the priority queue works.

The shuffled list is marshaled (written) to disk in JSON format.

----------------------------

Requires three data files:

BlockInput0.txt:

John Smith 1996.03.07 123-45-6789 Chickenpox BedRest aspirin
Joe  Blow  1996.03.07 123-45-6888 Smallpox BedRest Whiskey
Julie Wilson 1996.03.07 123-45-6999 Insomnia Exercise HotPeppers
Wayne Blaine 1942.07.07 123-45-6777 Measles WaitToGetBetter CodLiverOil 

BlockInput1.txt:

Rita Vita  1992.01.31 999-456-789 ObessivePersonality TryToRelax Ibuprofen
Wei  Xu  1996.03.22 123-456-333 Shingles WaitForRelief Zovirax
Sally McCutty 1970.01.01 123-456-999 Migraine IcePack Almotriptan
Bruce Lee 1940.11.27 456-789-123 SoreStomach LessCombat Vicodine

BlockInput2.txt:

Helen Keller 1880.06.27 666-45-6789 Arthritis WarmCloths Aspirin
Abraham Lincoln 1809.02.12 444-45-6888 GreviousWound Surgery Whiskey
John Kennedy 1917.05.29  333-45-6999 AddisonsDisease DrugTherapy Steroids
Joe DiMaggio 1914.11.25  111-22-3333 SoreKnees RestFromSports Aspirin


------------

The web sources:

Reading lines and tokens from a file:
http://www.fredosaurus.com/notes-java/data/strings/96string_examples/example_stringToArray.html
Good explanation of linked lists:
https://beginnersbook.com/2013/12/linkedlist-in-java-with-example/
Priority queue:
https://www.javacodegeeks.com/2013/07/java-priority-queue-priorityqueue-example.html

-----------------------------------------------------------------------*/

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.Reader;

import java.util.*;
/* import java.util.Date;
   import java.util.Random;
   import java.util.regex.*;
   import java.util.StringTokenizer;
*/

import java.io.StringWriter;
import java.io.StringReader;
import java.io.BufferedReader;
import java.text.*;

class BlockRecord{
  /* Examples of block fields. You should pick, and justify, your own set: */
  String BlockID;
  String TimeStamp;
  String VerificationProcessID;
  String PreviousHash; // We'll copy from previous block
  UUID uuid; // Just to show how JSON marshals this binary data.
  String Fname;
  String Lname;
  String SSNum;
  String DOB;
  String RandomSeed; // Our guess. Ultimately our winning guess.
  String WinningHash;
  String Diag;
  String Treat;
  String Rx;

  /* Examples of accessors for the BlockRecord fields: */
  public String getBlockID() {return BlockID;}
  public void setBlockID(String BID){this.BlockID = BID;}

  public String getTimeStamp() {return TimeStamp;}
  public void setTimeStamp(String TS){this.TimeStamp = TS;}

  public String getVerificationProcessID() {return VerificationProcessID;}
  public void setVerificationProcessID(String VID){this.VerificationProcessID = VID;}
  
  public String getPreviousHash() {return this.PreviousHash;}
  public void setPreviousHash (String PH){this.PreviousHash = PH;}
  
  public UUID getUUID() {return uuid;} // Later will show how JSON marshals as a string. Compare to BlockID.
  public void setUUID (UUID ud){this.uuid = ud;}

  public String getLname() {return Lname;}
  public void setLname (String LN){this.Lname = LN;}
  
  public String getFname() {return Fname;}
  public void setFname (String FN){this.Fname = FN;}
  
  public String getSSNum() {return SSNum;}
  public void setSSNum (String SS){this.SSNum = SS;}
  
  public String getDOB() {return DOB;}
  public void setDOB (String RS){this.DOB = RS;}

  public String getDiag() {return Diag;}
  public void setDiag (String D){this.Diag = D;}

  public String getTreat() {return Treat;}
  public void setTreat (String Tr){this.Treat = Tr;}

  public String getRx() {return Rx;}
  public void setRx (String Rx){this.Rx = Rx;}

  public String getRandomSeed() {return RandomSeed;}
  public void setRandomSeed (String RS){this.RandomSeed = RS;}
  
  public String getWinningHash() {return WinningHash;}
  public void setWinningHash (String WH){this.WinningHash = WH;}

}

public class BlockInputG{

  private static String FILENAME;

  Queue<BlockRecord> ourPriorityQueue = new PriorityQueue<>(4, BlockTSComparator);
  

  /* Token indexes for input: */
  private static final int iFNAME = 0;
  private static final int iLNAME = 1;
  private static final int iDOB = 2;
  private static final int iSSNUM = 3;
  private static final int iDIAG = 4;
  private static final int iTREAT = 5;
  private static final int iRX = 6;

  public static void main(String argv[]) {
    BlockInputG s = new BlockInputG(argv);
    s.run(argv);
  }

  public static Comparator<BlockRecord> BlockTSComparator = new Comparator<BlockRecord>()
    {
     @Override
     public int compare(BlockRecord b1, BlockRecord b2)
     {
      String s1 = b1.getTimeStamp();
      String s2 = b2.getTimeStamp();
      if (s1 == s2) {return 0;}
      if (s1 == null) {return -1;}
      if (s2 == null) {return 1;}
      return s1.compareTo(s2);
     }
    };

  
  public BlockInputG(String argv[]) {
    System.out.println("In the constructor...");
  }
  
  public void run(String argv[]) {
    
    System.out.println("Running now\n");
    try{
      ListExample(argv);
    } catch (Exception x) {};
  }

  public void ListExample(String args[]) throws Exception {
  
     LinkedList<BlockRecord> recordList = new LinkedList<BlockRecord>();

    /* CDE: Process numbers and port numbers to be used: */
    int pnum;
    int UnverifiedBlockPort;
    int BlockChainPort;

    /* CDE If you want to trigger bragging rights functionality... */
    if (args.length > 1) System.out.println("Special functionality is present \n");

    if (args.length < 1) pnum = 0;
    else if (args[0].equals("0")) pnum = 0;
    else if (args[0].equals("1")) pnum = 1;
    else if (args[0].equals("2")) pnum = 2;
    else pnum = 0; /* Default for badly formed argument */
    UnverifiedBlockPort = 4710 + pnum;
    BlockChainPort = 4820 + pnum;
    
    System.out.println("Process number: " + pnum + " Ports: " + UnverifiedBlockPort + " " + 
		       BlockChainPort + "\n");

    switch(pnum){
    case 1: FILENAME = "BlockInput1.txt"; break;
    case 2: FILENAME = "BlockInput2.txt"; break;
    default: FILENAME= "BlockInput0.txt"; break;
    }

    System.out.println("Using input file: " + FILENAME);

    try {
      BufferedReader br = new BufferedReader(new FileReader(FILENAME));
      String[] tokens = new String[10];
      String InputLineStr;
      String suuid;
      UUID idA;
      BlockRecord tempRec;
      
      StringWriter sw = new StringWriter();
      
      int n = 0;
      
      while ((InputLineStr = br.readLine()) != null) {
	
	BlockRecord BR = new BlockRecord(); // Careful

	/* CDE For the timestamp in the block entry: */
	try{Thread.sleep(1001);}catch(InterruptedException e){}
      	Date date = new Date();
	//String T1 = String.format("%1$s %2$tF.%2$tT", "Timestamp:", date);
	String T1 = String.format("%1$s %2$tF.%2$tT", "", date);
	String TimeStampString = T1 + "." + pnum; // No timestamp collisions!
	System.out.println("Timestamp: " + TimeStampString);
	BR.setTimeStamp(TimeStampString); // Will be able to priority sort by TimeStamp

	
	/* CDE: Generate a unique blockID. This would also be signed by creating process: */
	suuid = new String(UUID.randomUUID().toString());
	BR.setBlockID(suuid);
	/* CDE put the file data into the block record: */
	tokens = InputLineStr.split(" +"); // Tokenize the input
	BR.setFname(tokens[iFNAME]);
	BR.setLname(tokens[iLNAME]);
	BR.setSSNum(tokens[iSSNUM]);
	BR.setDOB(tokens[iDOB]);
	BR.setDiag(tokens[iDIAG]);
	BR.setTreat(tokens[iTREAT]);
	BR.setRx(tokens[iRX]);
	
	recordList.add(BR);
	n++;
      }
      System.out.println(n + " records read." + "\n");
      System.out.println("Records in the linked list:");

      // Show names from records read into the linked list:
      Iterator<BlockRecord> iterator = recordList.iterator();
      while(iterator.hasNext()){
	tempRec = iterator.next();
	System.out.println(tempRec.getTimeStamp() + " " + tempRec.getFname() + " " + tempRec.getLname());
      } 
      System.out.println("");
      
      iterator=recordList.iterator();
      
      System.out.println("The shuffled list:"); // Shuffle the list to later demonstrate the priority queue.
      Collections.shuffle(recordList);
      while(iterator.hasNext()){
	tempRec = iterator.next();
	System.out.println(tempRec.getTimeStamp() + " " + tempRec.getFname() + " " + tempRec.getLname());
      } 
      System.out.println("");

      iterator=recordList.iterator();

      System.out.println("Placing shuffled records in our priority queue...\n");
      while(iterator.hasNext()){
	ourPriorityQueue.add(iterator.next());
      } 
      
      System.out.println("Priority Queue (restored) Order:");

      while(true){ // Queue will be in TimeStamp order. (In this case, the original order.)
	// Can't iterate here. poll() removes and returns the head of the queue.
	tempRec = ourPriorityQueue.poll(); // For consumer thread you'll want .take() which blocks while waiting.
	if (tempRec == null) break;
	System.out.println(tempRec.getTimeStamp() + " " + tempRec.getFname() + " " + tempRec.getLname());
      } 
      System.out.println("\n\n");

    } catch (Exception e) {e.printStackTrace();}

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    // Convert the Java object to a JSON String:
    String json = gson.toJson(recordList);
    
    System.out.println("\nJSON (suffled) String list is: " + json);
    
    // Write the JSON object to a file:
    try (FileWriter writer = new FileWriter("myList.json")) {
      gson.toJson(recordList, writer);
    } catch (IOException e) {e.printStackTrace();}
  }
}
