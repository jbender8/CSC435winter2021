/** File is: ReadFiles.java, Version 1.2
Thanks Kishori, dhanyu
http://www.artima.com/legacy/answers/Jul2000/messages/69.html

Use this hint file to help you promiscuously return a directory in HTML
format to the client.

ReadFiles simply reads the files in some hard-coded directory and displays
them on the console.

*/

import java.io.* ;
import java.util.* ;
class ReadFiles {
  public static void main ( String[] args ) {
    
    String filedir ;
    // Create a file object for your root directory

    // E.g. For windows:    File f1 = new File ( "C:\\temp" ) ;

    // For Unix:
    File f1 = new File ( "./" ) ;
    
    // Get all the files and directory under your diretcory
    File[] strFilesDirs = f1.listFiles ( );
    
    for ( int i = 0 ; i < strFilesDirs.length ; i ++ ) {
      if ( strFilesDirs[i].isDirectory ( ) ) 
	System.out.println ( "Directory: " + strFilesDirs[i] ) ;
      else if ( strFilesDirs[i].isFile ( ) )
	System.out.println ( "File: " + strFilesDirs[i] + 
			     " (" + strFilesDirs[i].length ( ) + ")" ) ;
    }
  }
}

