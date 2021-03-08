
import java.net.*;
import java.io.*;
import java.util.*;

public class test {
	public static void main(String[] args) throws IOException {
		
		int q_len = 6; // number of clients can connect to server
		int port = 2540; // assignment requires use of port 2540		
		Socket sock;
		
		System.out.println("======= WebServer =======");
		System.out.println("Server starting up, listening at port: " +port);
		
		ServerSocket serverSocket = new ServerSocket(port, q_len);  
		
		//loop that runs until a client requests shutdown.
		while (true) {  
			sock = serverSocket.accept(); 
			new WebServerWorker(sock).start(); //spawn a new thread for any client that connects as a new Worker
		}
	}
}

class WebServerWorker extends Thread {
	Socket sock;
	
	WebServerWorker(Socket s){ // constructor class creates a worker from the socket passed by the client that connected.
		sock = s;
	}

	
	public void run() {
		PrintStream out = null;
		BufferedReader in = null;
		try {
				// input/ output streams
				in =  new BufferedReader(new InputStreamReader(sock.getInputStream()));
				out = new PrintStream(sock.getOutputStream()); 				 
			
				String input = in.readLine(); //read the input from the browser
				String fileName = "";
				String contentType = "";
				
				StringTokenizer token = new StringTokenizer(input, " ");
				
				if (fileName.indexOf("..") >= 0) { // security concern
                    throw new FileNotFoundException();
                }
								
				if(token.nextToken().equals("GET") && token.hasMoreElements()) // check the type of http request.
				{ 
					fileName = token.nextToken();			//get the file name.		
				}
				else {
                    throw new FileNotFoundException();
                }
								
				// check for MIME type being requested.				
				if(fileName.endsWith(".txt") == true) { //if the file is of type text.
					contentType = "text/plain"; 
					displayFile(fileName, out, contentType); // call the function to process the text file.
				} else if(fileName.endsWith(".html") == true) { //if the file is of type HTML.
					contentType = "text/html";
					displayFile(fileName, out, contentType); // call the function to process the HTML file.
				} else if(fileName.contains("/cgi/addnums.fake-cgi") == true) { // if the file is of type HTML to add numbers.
					contentType = "text/html";
					addNums(fileName, out, contentType); // call the function to process the addition of numbers.
				} else if(fileName.endsWith("/") == true) { // check if it is a directory.
					contentType = "text/html"; 
					displayDir(fileName, out, contentType); // call the function to process the directory.
				} else { // process all other type of file.
					contentType = "text/plain";
					displayFile(fileName, out, contentType);  					 
				}
			sock.close(); // close the connection.
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}	
	
	/*
	 * Method to process the file and reads the content of the file and displays it on the browser
	 */
	
	public void displayFile(String fileName, PrintStream out, String contentType) {
		try {
            if (fileName.indexOf("/") == 0) {
            	fileName = fileName.substring(1);
            }
            
            InputStream file = new FileInputStream(fileName);
            File f1 = new File(fileName);
            
            long contentLength = f1.length();

            //prints the header for the file being served.
            out.print("HTTP/1.1 200 OK\r\n"
                    + "Content-Length: " + contentLength
                    + "\r\nContent-Type: " + contentType + "\r\n\r\n");

            //message on server console to let the server user know what the client is accessing
            System.out.println("\nServer sending:\ncontentType: " + contentType + "\nfilename: " + fileName + "\n");


            //writes the file to a buffer to write to the client.
            byte[] fileBytes = new byte[10000]; // array of bytes to hold the file.  10000 bytes can hold a little less than 10KB
	        int numberOfBytes = file.read(fileBytes);  // get number of bytes
	        out.write(fileBytes, 0, numberOfBytes); // write all the bytes
	        System.out.write(fileBytes, 0, numberOfBytes); // write all the bytes
	        
	        out.flush(); // flush
	        file.close(); // close
        } catch (IOException x) {
            System.out.println(x);
        }
				           
}
	
	/*
	 * Method to display the list of files present in the directory, create the dynamic hyperlinks.
	 */
	public void displayDir(String directory, PrintStream out, String contentType) throws IOException {		
		
		BufferedWriter display = new BufferedWriter(new FileWriter("Index.html")); //
		File initialFile = new File("./" + directory + "/"); // get the first file.
			
		File[] directoryFiles = initialFile.listFiles();  // get the list of files present in the directory.
		
		
		display.write("<html><head>Display Directory</head>"); 
		display.write("<br><br>");
		display.write("<body link='blue'");  		
		display.write("<font size=100><font color = #3399FF> Directory: " + directory + "</font></font>" + "<br><br>");  //Display the name of the directory showing
		display.write("<a href=\"" + "http://localhost:2540" + "/\">" + "Home" + "</a>"); //link to get back to home page.
		
		display.write("<br><br>");
		
		//loops through each file in the directory to dynamically create a file.
		for(File f: directoryFiles) { 
			String fileName = f.getName(); //get the name of the file.
			if (fileName.startsWith(".") == true || fileName.startsWith("Index.html") == true) { 
				continue; 
			}			
			if (f.isDirectory() == true) { //check if it's a directory.
				display.write("<a href=\"" + fileName + "/\">/" + fileName + "</a> <br>");  // create a hyperlink
			}
			if (f.isFile() == true) {  //check if it's a file.
				display.write("<a href=\"" + fileName + "\" >" + fileName + "</a> <br>"); // create a hyperlink
			}
		display.flush(); 
		}
			
		display.write("</body></html>");  
		File tempFile = new File("Index.html");
		
		InputStream file = new FileInputStream("Index.html");
		out.println("HTTP/1.1 200 OK" + "Content-Length: " + tempFile.length() + "Content-Type: "  + contentType + "\r\n\r\n"); 
		
		System.out.println("Server is sending directory: " + directory); // server log
	

        byte[] displayFileBytes = new byte[10000]; //array to display file bytes.
        int numberOfBytes = file.read(displayFileBytes); 
        out.write(displayFileBytes, 0, numberOfBytes); // write the display file bytes to browser.
        System.out.write(displayFileBytes, 0, numberOfBytes); // write the display file bytes to server.
        
        display.close(); 
        out.flush();
		file.close(); // close the connection
		tempFile.delete(); // drop the temp file.
	}
	
	/*
	 * Method to add the numbers, sent from the HTML file.
	 */	
	
	public void addNums(String url, PrintStream out, String contentType) throws IOException {
		
		int ind = url.indexOf("?")+1;
		String input = url.substring(ind);
		
		int indName = input.indexOf("=") +1;
		String name = input.substring(indName); //Get the user name from the URL
		
		int indno1 = name.indexOf("=") +1; // parse the input to get the first number.
		String no1 = name.substring(indno1);
		
		int indno2 = no1.indexOf("=")+1; // parse the input to fetch the second number.
		String no2 = no1.substring(indno2);
		int n2 = Integer.parseInt(no2);
		
		int index = name.indexOf("&");
		name = name.substring(0,  index); //parse the user name
		
		int indnum1 = no1.indexOf("&");
		no1 = no1.substring(0, indnum1);
		int n1 = Integer.parseInt(no1);
		
		int sum = n1 + n2;		// summation of numbers.
		
		//Display on the console and on the browser.
		out.println("Dear " +name+" the sum of "+n1+" and "+n2+" is "+sum+".");
		System.out.println("Dear " +name+" the sum of "+n1+" and "+n2+" is "+sum+".");		
	}
	
	private static void errorReport(PrintStream pout, Socket connection,String code, String title, String msg){
		pout.print("HTTP/1.0 " + code + " " + title + "\r\n" +	"\r\n" +
				"<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n" +
				"<TITLE>" + code + " " + title + "</TITLE>\r\n" +
				"</HEAD><BODY>\r\n" +"<H1>" + title + "</H1>\r\n" + msg + "<P>\r\n" +
				"<HR><ADDRESS>FileServer 1.0 at " + connection.getLocalAddress().getHostName() + 
				" Port " + connection.getLocalPort() + "</ADDRESS>\r\n" +"</BODY></HTML>\r\n");
				log(connection, code + " " + title);
		}
	
	private static void log(Socket connection, String msg)
    {
        System.err.println(new Date() + " [" + connection.getInetAddress().getHostAddress() + 
                           ":" + connection.getPort() + "] " + msg);
    }
}
