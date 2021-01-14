import java.io.*; 
import java.net.*; 
class Worker extends Thread { 
    Socket sock;
    Worker (Socket s) {sock = s;} 
    public void run(){
        PrintStream out = null;
        BufferedReader in = null;
        try {
            in = new BufferedReader
                (new InputStreamReader(sock.getInputStream()));
            out = new PrintStream(sock.getOutputStream());
            try {
                String name;
                name = in.readLine ();
                System.out.println("Looking up " + name);
                printRemoteAddress(name, out);
            } catch (IOException x) {
                System.out.println("Server read error"); x.printStackTrace ();
            }
            sock.close();
        } catch (IOException ioe) {System.out.println(ioe);}
    }

 static void printRemoteAddress (String name, PrintStream out) {
     try {
         out.println("Looking up " + name + "...");
         InetAddress machine = InetAddress.getByName (name);
         out.println("Host name : " + machine.getHostName ());
         out.println("Host IP : " + toText (machine.getAddress ()));
     } catch(UnknownHostException ex) {
         out.println ("Failed in atempt to look up " + name);
     }
 }

static String toText (byte ip[]) { 
    StringBuffer result = new StringBuffer ();
    for (int i = 0; i < ip.length; ++ i) {
        if (i > 0) result.append (".");
        result.append (0xff & ip[i]);
    }
    return result.toString ();
}
}
public class InetServer {
    public static void main(String a[]) throws IOException {
        int q_len = 6; 
        int port = 1581;
        Socket sock;
        ServerSocket servsock = new ServerSocket(port, q_len);
        System.out.println
 ("Jess Bender's Inet server 1.8 starting up, listening at port 1565.\n");
        while (true) {
            sock = servsock.accept(); 
            new Worker(sock).start();
        }
    }
}