/* Mischa Fubler 
 * Nov 28, 2014
 * 
 * purpose: read in a file, set the text, delimited by periods, 
 * to Sentence case (first char upper, remainder to lower case.)
 * 
 * input: user defined text file
 * 
 * output: user defined text file
 */
import java.io.*;
import java.util.Scanner;

public class sentenceFilter 
{
	public static void main(String[]args) throws IOException
	{
		String inFileName, outFileName, outFile, temp = null, end = ". ";
		char firstLetter;
		File input;
		Scanner scan = new Scanner (System.in), iFile;
		
		
		System.out.println("Provide path & file name to be filtered:");
		inFileName = scan.nextLine();
		input = new File(inFileName);
		iFile = new Scanner(input);
		iFile.useDelimiter("\\. "); //sets delimiter to ". "
		
		System.out.println("Provide path & file name for completed file:");
		outFileName = scan.nextLine();
		
		PrintWriter writer = new PrintWriter(outFileName, "UTF-8");
		
		while (iFile.hasNext())
		{
			temp = iFile.next();
			firstLetter = temp.charAt(0); //selects first letter
			outFile = Character.toString(firstLetter).toUpperCase(); //sets letter to upper case
			outFile += temp.substring(1, (temp.length())).toLowerCase(); //adds remaining sentence text and sets to lower case
			
			if(iFile.hasNext())
				outFile += end;
			
			System.out.print(outFile);
			writer.print(outFile);
		}
		
		iFile.close();
		writer.close();
	}
}