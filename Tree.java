import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.Vector;

/**
 * OOP Fall 2015
 * Brooklyn College
 * Professor Bukhari
 * 
 * @author Jonathan S. Kwiat
 * 
 * Simulation of the linux 'tree' command.
 * 
 * Box Drawing characters at: https://en.wikipedia.org/wiki/Box-drawing_character
 * 
 * How to set PrinterWriter to UTF-8: http://stackoverflow.com/questions/1001540/how-to-write-a-utf-8-file-with-java
 * 
 * When I open the resulting file with VIM the lines are seamless but in Eclipse every time I open the file the lines are broken.
 * I think it's using plain ASCII encoding and is not decoding by unicode characters correctly.
 * 
 * To run this file properly from root you need to invoke it with sudo privledges on a MAC or LINUX OS or if you run it without
 * sudo the DirFilter will see to it that it doesn't try to enter directories it does not have access to.
 *
 */
public class Tree {
	public static long working = 0;
	public static boolean indent =false;

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String startDir;
		File dirStart;
		
		System.out.print("Please enter absolute path of a starting directory upon which to invoke program: ");
		startDir = input.next();
		dirStart = new File(startDir);
		Vector<StringBuilder> output = new Vector<StringBuilder>();
		output.add(new StringBuilder(""));
		dirTree(dirStart, output);
		// System.out.printf("ouput.size() = %d%n", output.size());
		printDirTree(output);

		}
	
	
	
	public static int dirTree(File dir, Vector<StringBuilder> lines) {
		int lineCount = 0;
		int subDirLines = 0;
		// dirTree must always return at least one so parent directory makes space for child.
		int dirLines =0;
		lineCount = lines.size() - 1;
		/*
		System.out.printf("In File dir = %s, lineCount = %d%n", dir.getName(), lineCount);
		*/
		// Every Branch terminates in a directory name and a newline
		lines.get(lineCount).append(dir.getName() + "\n");
		// Capture line directory is at.
		
		//Returns all the  sub-directories using implementation of abstract class
		File[] subDirs = null;
		subDirs = dir.listFiles(new DirFilter());
		// Capture how many lines is needed for the directories in this directory
		dirLines = subDirs.length;
		
		// System.out.printf("In File dir = %s, subDirs.length = %d, subDirLines %d%n", dir.getName(), subDirs.length, subDirLines);
	
		
		for (int i = 0; i < subDirs.length ; i++) {
			// System.out.println("Bottom of the for: dir = " + dir);
			// │ branch only triggered if recursive call already called, this is not adding a new line but adding to an already
			// existing line.
			for (int j = 0; j < subDirLines; j++) {
				// System.out.printf("Inside vertical pipe | %n");
				lines.get(++lineCount).insert(0, "\u2503   ");   
			}
			// ├ branch
			if ( i < (subDirs.length-1) ) {
				// System.out.printf("Inside T branch %n");
				lines.add(new StringBuilder("\u2523\u2501\u2501 "));
				lineCount++;
			}
			// └ branch
			else {
				// System.out.printf("Inside L branch %n");
				lines.add(new StringBuilder("\u2517\u2501\u2501 "));
				lineCount++;
				
			}
			// Recursive call:
			
			 // System.out.printf("About to make recursive call in File dir = %s, subDirs.length = %d%n", dir.getName(), subDirs.length);
			 // System.out.printf("i= %d subDir passing in %s%n",  i, subDirs[i].getName());
			 working+=1;
			 if ((working % 10_000) == 0) {
				 if(indent){
					 System.out.println("\tWorking...");
					  indent = false;
					 
				 }
				 else{
					 System.out.println("Working..");
					 indent = true;
				 }
			 }
			subDirLines = dirTree( subDirs[i], lines);
			dirLines += subDirLines;
			// System.out.printf(" After return from recursive call: dir.getName() = %s subDirLines = %d%n", dir.getName(), subDirLines);
			
			
		}
		//Pad out the rest of the space with whitespace
		for(int i = 0; i < subDirLines; i++) {
			// System.out.printf(" White space padding triggered subDirLines = %d%n", subDirLines);
			lines.get(++lineCount).insert(0, "    ");
		}
		
		// System.out.printf(" Returning value is  dirLines = %d%n", dirLines);
		return (dirLines ) ;
		
	}
	/*
	 * It seems BufferedWriter needs a lot more try catch blocks then a simple PrintStream object which I used previously.
	 * I was hoping that if I went for a UTF-8 encoding scheme I might get character boxing | that merged into each other.
	 * 
	 * This totally did not work but I leave it because it is cool. With this you can pick your output encoding. 
	 */
	public static void printDirTree(Vector<StringBuilder> output) {
		
		File dir_tree = null;
		dir_tree = new File("dir_tree.txt");
		 BufferedWriter out =null;
		try {
			out = new BufferedWriter(new OutputStreamWriter( new FileOutputStream (dir_tree), Charset.forName("UTF-8")));
		} catch (FileNotFoundException e) {
			System.err.println("Unable to open OutPut stream to file");
			e.printStackTrace();
		}
		for (int i = 0; i < output.size(); i ++) {
			// System.out.println("Inside writer loop");
			try {
				out.write((output.get(i)).toString());
			} catch (IOException e) {
				System.err.println("BufferedWriter write operation failed.");
				e.printStackTrace();
			}
		}
		try {
			out.flush();
		} catch (IOException e) {
			System.err.println("Attempt to flush BufferedWriter failed.");
			e.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e) {
			System.err.println("Atetemp to close system resource BufferedWriter failed.");
			e.printStackTrace();
		}
		
		return;
	}

}


