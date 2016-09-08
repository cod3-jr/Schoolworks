/* Mischa Fubler
 * Sept 20, 2014
 * 
 * purpose: determine the number of records in a file
 * 		then read product names and quarterly sales numbers
 * 		from the file, print them and determine:
 * 		- the yearly profit for each item
 * 		- best quarterly performer and its sales number
 * 		- how many items had a profit or loss for each quarter
 * 		- if each item experienced consecutive gains, loses or variations 
 * 			for the year 
 * 
 * input: sales.txt - stored in the same project folder
 * 
 * output: print to screen
 */

import java.io.*;
import java.util.Scanner;

public class accountAnalyzer2 
{
	public static String[] products;
	public static int[][] allSales;
	public static int itemCount;
	public static int divisions = 4; //can be expanded to allow additional fractions of a year (bi-annual, monthly etc.)
	public static int[] winners;
	public static int[][]winnersCount;
	
	public static void main (String[]args) throws IOException
	{
		String fileName = "sales.txt";
		File myFile = new File (fileName); 
		Scanner iFile = new Scanner (myFile);
		
		itemCount = count();
		makeArrays(itemCount, iFile);
		winners = maxQuarter();
		winnersCount = redBlackCount();
		
		printer(itemCount);
		
		iFile.close();
	}
	
	public static int count() throws FileNotFoundException //counts number of records (if separated by \n)
	{	
		String fileName = "sales.txt"; //created new instance of the scanner because it is consumed 
		File myFile = new File (fileName); //after being read and I need to pass it to makeArrays()
		Scanner iFile = new Scanner (myFile);
		int c = 0;
		
		while (iFile.hasNext()) //send file content for counting and print file content to screen
		{
			iFile.nextLine();
			c++;
		}
		return c;
	}
	
	public static void makeArrays(int n, Scanner s) //creates arrays and calculates yearly total and average
	{
		String[] product = new String[n];
		int[][] sales = new int[n][(divisions+2)];
		double average = 0.0;
		
		while (s.hasNext())
		{
			for (int i = 0; i < n; i++) //read product names into array[]
			{
				product[i] = s.next();
				
				for (int y = 0; y < 4; y++)	//read sales numbers into array[][]
					sales[i][y] = s.nextInt();
				
				sales[i][4] = yearSales(i,sales);		//yearly sales total is sales[i][4]
				average = sales[i][4]/divisions;
				sales[i][5] = (int) Math.round(average);	//yearly sales average is sales[i][5]. 
				//Math.round returns a long, so I cast it as an int since I assumed quarterly reports
				//are more concerened with whole number values. If doubles are required by client,
				//I can create a new double[] with the yearly averages
			}
		}
		products = product;
		allSales = sales;
	
	}
	
	public static int yearSales(int i, int[][] n)
	{
		int total = 0;
		for (int y = 0; y < divisions; y++)
			total += n[i][y];
		return total;
	}
	
	public static int[] maxQuarter()
	{
		int maxValue = 0;
		int maxIndex = 0;
		int[] qWinners = new int[divisions];
		
		for (int i = 0; i < divisions; i++)
		{
			for (int y = 0; y < itemCount;y++)
			{
				if (maxValue < allSales[y][i])
					{
						maxValue = allSales[y][i];
						maxIndex = y;
					}
			}
			qWinners[i] = maxIndex;
		}
		return qWinners;
	}
	
	public static int[][] redBlackCount() //counts how many products were profits or losses in each quarter
	{
		int red = 0, black = 0;
		int[][] counts = new int[2][divisions];
		
		for (int i = 0; i < divisions; i++)
		{
			for (int y = 0; y < itemCount;y++)
			{
				if (allSales[y][i] < 0)
					red++;
				else
					black++;
			}
			counts[0][i] = black;
			counts[1][i] = red;
			black = 0;
			red = 0;
		}
		return counts;
	}
	
	public static String riseOrFall(int i) //did the product have a steady increase/decrease or fluctuate?
	{
		String analysis;
				
		if (allSales[i][0]>=allSales[i][1] && allSales[i][1]>=allSales[i][2] && allSales[i][2]>=allSales[i][3])
				analysis = "declined";
		else if (allSales[i][0]<=allSales[i][1] && allSales[i][1]<=allSales[i][2] && allSales[i][2]<=allSales[i][3])
				analysis = "grew";
		else analysis = "fluctuated";
			
		return analysis;
	}
	
	public static void printer(int count)
	{
		System.out.println("Product: \tQ1:\tQ2:\tQ3:\tQ4:\tTotal:\tAverage:"); //headings
		for (int i = 0; i < count; i++)
		{
			if (products[i].length()<8) //crude formatting. two tabs for smaller words
				System.out.print(products[i]+"\t\t");
			else System.out.print(products[i]+"\t");
			
			for (int y = 0; y < 4; y++)	//print array of sales numbers
			{
				System.out.print(allSales[i][y]+"\t");
			}
			System.out.print("| "+allSales[i][4] + "\t " + allSales[i][5] + "\t");//print total & average
			System.out.println(riseOrFall(i));
		}
		for (int i=0; i<4;i++) //print quarterly sales leader
			{
				System.out.print("Q"+(i+1)+" profit leader: "+ products[winners[i]] +" : "+allSales[winners[i]][i] +"\t");
				if ((i+1)%2==0) //print 2 quarters per line
					System.out.println();
			}
		for (int i=0; i<4;i++) //print quarterly wins and losses count
		{
			System.out.println("Q"+(i+1)+" profitable products: "+ winnersCount[0][i] +" and losses: "+winnersCount[1][i]);
		}	
	}
}

	