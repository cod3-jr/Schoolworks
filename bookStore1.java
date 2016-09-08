/* Mischa Fubler
*
* December 14, 2014
*
* Purpose: user interface for managing bookType & memberType classes. 
* Contains methods for: 
* 	adding members
* 	searching for members 
* 	recording purchases
* 	applying applicable discounts
* 
* input: user driven menu
* 
* 	
* 	
*/

import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class bookStore1
{
	public static String input = "0";
	public static Scanner keyboard = new Scanner(System.in);
	public static int booksCount = 0, memberCount = 0, bookCurrent = 0, memberCurrent = 0, currentYear = Calendar.getInstance().get(Calendar.YEAR);
	public static bookType[] library = new bookType[100];
	public static memberType[] members = new memberType[50];
	//date format
	public static final String formattedDate = "yyyy-MM-dd HH:mm:ss";
	public static final DecimalFormat money = new DecimalFormat("$#,##0.00");
	 
	
	public static void main(String[]args)throws IOException
	{
				
		System.out.println("Welcome");
		for(int i = 0; i < library.length; i++)
			 library[i] = new bookType();
		for(int i = 0; i < members.length; i++)
			 members[i] = new memberType();
		System.out.println("Array of bookType["+ library.length +"] and memberType["+ members.length +"] created.");
		
		mainMenu();
		System.out.println("Goodbye...");
	}
	
	public static void mainMenu() throws IOException// Main menu text
	{
		while (input.equalsIgnoreCase("exit") != true )
		{
			System.out.println("Type desired option number to continue:\n(type 'exit' or '0' to end program)\n");
			System.out.println("1 - Member Menu\t 2 - Book Menu\t3 - Process Purchases");
		
			input = keyboard.nextLine();
			
			if(input.equalsIgnoreCase("exit") == true)
				break;
			
			switch (Integer.parseInt(input))
			{
				case 1: memberMenu();
						break;
				case 2: bookMenu();
						break;
				case 3: purchases();
						break;
				case 0: break;
				default: System.out.println("Invalid key");
			}
			
		}
	}
	
	public static void memberMenu() throws FileNotFoundException, IOException 
	{
		while (input.equalsIgnoreCase("back") != true )
		{
			System.out.println("Type desired option number to continue:\n(type 'back'to return to main menu)\n");
			System.out.println("1 - Add Member \t 2 - Search Member \t 3 - Update Member \n"
							+ "4 - Import Member \t 5 - Export Member");
		
			input = keyboard.nextLine();
			
			if(input.equalsIgnoreCase("back") == true)
				break;
		
			switch (Integer.parseInt(input))
			{
				case 1: addMem(0);
						break;
				case 2: memberCurrent = searchMem();
						if(memberCurrent != -1)
						members[memberCurrent].printRecordTable();
						System.out.println("\n\n");
						break;
				case 3: updateMem();
						break;
				case 4: importMem();
						break;
				case 5: exportMem();
						break;
				case 0: break;
				default: System.out.println("Invalid key");
			}
		}
	}
	
	public static void bookMenu() throws FileNotFoundException, IOException //book actions menu
	{
		while (input.equalsIgnoreCase("back") != true )
		{
			System.out.println("Type desired option number to continue:\n(type 'back' to return to main menu)\n");
			System.out.println("1 - Add Book \t 2 - Search Book \t 3 - Update Book \n"
							+ "4 - Import Books\t 5 - Export Books\t");
		
			input = keyboard.nextLine();
			
			if(input.equalsIgnoreCase("back") == true)
				break;
			
			switch (Integer.parseInt(input))
			{
				case 1: addBook(0);
						break;
				case 2: bookCurrent = searchBook();
						if(bookCurrent != -1)
						library[bookCurrent].printRecordTable();
						System.out.println("\n\n");
						break;
				case 3: updateBook();
						break;
				case 4: importBook();
						break;
				case 5: exportBook();
						break;
				case 0: break;
				default: System.out.println("Invalid key");
			}
			
		}
	}
	
	public static  void purchases() //calculates total sale; calls bookType & memberType purchase methods
	{
		boolean mem = false;
		double price = 0.0, discount = 0.0, total = 0.0;
		System.out.println("Is customer a member?");
		input = keyboard.nextLine().toLowerCase();
		
		if(input.charAt(0) == 'y')
			mem = true;
		memberCurrent = searchMem();
		members[memberCurrent].printRecordTable();
		System.out.println("\n\n");
		
		while(!(input.equalsIgnoreCase("done")))
		{
			bookCurrent = searchBook();
			if(input.equalsIgnoreCase("done"))
				break;
			if(bookCurrent != -1)
			{
				System.out.println("Enter Quantity");
				input = keyboard.nextLine();
				price = library[bookCurrent].purchase(Integer.parseInt(input));
				if(mem)
				discount = members[memberCurrent].purchase(Integer.parseInt(input), price);
				
				total += (price - discount);
				
				if(discount < 0)
				{
					members[memberCurrent].setAmtSpent(0.0);
					members[memberCurrent].setBookCount(0);
				}
			}
			
			System.out.println("type 'done' to display total sale and return to main menu\n or ENTER to continue purchases");
			input = keyboard.nextLine();
		}
		
		System.out.println("Total Sale = " + money.format(total) + "\n\n\n-------------------------");
	}
	
	public static void addMem(int x) // adds memberType to members[]; creates new ID number
	{
		String f = null, l = null, temp = null;
		int tempID = 1, tempSearch = 1, tempi = 0;
		double tempD = 0.0;
		
		if(x == 0)
			x = memberCount;
		
		
		while(tempSearch != -1)
		{
			tempSearch = searchMem(tempID);
			tempID ++;
		}
	
		members[x].setMemID(tempID);
		System.out.println("New Member ID: " + tempID);
		
		System.out.println("Type the member's First Name:");
		f = keyboard.nextLine();
		System.out.println("Type the member's Last Name:");
		l = keyboard.nextLine();
		if(!(f.isEmpty()) && !(l.isEmpty()))
		members[x].setName(f, l);
		if (x == memberCount)
		{
			members[x].setBookCount(0);
			members[x].setAmtSpent(0.0);
			memberCount++;
		}else{
			System.out.println("Set books purchased:");
			temp = keyboard.nextLine();
			
			if(!(temp.isEmpty()))
			{
				tempi = Integer.parseInt(temp);
				members[x].setBookCount(tempi);
			}
			
			
			System.out.println("Set amount spent:");
			temp = keyboard.nextLine();
			
			if(!(temp.isEmpty()))
			{
				tempD = Double.parseDouble(temp);
				members[x].setAmtSpent(tempD);
			}
		}
		
		members[x].printRecordTable();
		System.out.println("library now contains: " + memberCount + " members");
	}
	
	public static int searchMem() // searches members[] for memberType
	{
		String temp = null;
		int index = 0;
		
		System.out.println("Enter Member ID");
		temp = keyboard.nextLine();
	
		
		for(int i=0; i < memberCount; i++)
		{
			if(members[i].getMemID() == Integer.parseInt(temp))
					{
						index = i;
						System.out.println("member found at : " + index);
						return index;
					}
		}
		
		System.out.println("member not found...");
		return -1;
	}
	
	public static int searchMem(int x)
	{
		int index = 0;
		
		for(int i=0; i < memberCount; i++)
		{
			if(members[i].getMemID() == x)
					{
						index = i;
						return index;
					}
		}
		
		return -1;
	}
	
	public static void updateMem() // calls searchMem and addMem to add member to specified array index
	{
		int temp = searchMem();
		addMem(temp);
	}
	
	public static void importMem() throws FileNotFoundException //import members file
	{
		String inFile = "members.txt";
		File input = new File(inFile);
		Scanner iFile = new Scanner(input);
		
		String first = null, last = null;
		int memID = 0, bookCount = 0;
		double amountSpent = 0.0;
		
		while(iFile.hasNext())
		{
			first = iFile.nextLine();
			last = iFile.nextLine();
			memID = iFile.nextInt();
			iFile.nextLine();
			bookCount = iFile.nextInt();
			iFile.nextLine();
			amountSpent = iFile.nextDouble();
			iFile.nextLine();
			members[memberCount] = new memberType(first, last, memID, bookCount, amountSpent);
			members[memberCount].printRecordTable();
			
			if (iFile.hasNext())
				System.out.println();
			
			memberCount++;
		}
		System.out.println("\n\ncurrent member count: " + memberCount);
	}
	
	public static void exportMem() throws IOException //exports member array to file
	{
		String outfile = "backup - " + now();
		PrintWriter writer = new PrintWriter(outfile, "UTF-8");
		
		for(int i = 0; i < memberCount; i++)
		{
			writer.println(members[i].getNameFirst());
			writer.println(members[i].getNameLast());
			writer.println(members[i].getMemID());
			writer.println(members[i].getBookCount());
			writer.println(members[i].getAmtSpent());
		}
		System.out.println(memberCount +" records successfully exported");
		writer.close();
	}

	public static void addBook(int x) //adds bookType element to Library[] at end of elements in array or specified index
	{
		String temp = null;
		int tempi = 0;
		
		if(x == 0)
			x = booksCount;
		
		System.out.println("Enter book title:");
		temp = keyboard.nextLine();
		
		if(!(temp.isEmpty())) //allows updateBook to call addBook
		library[x].setTitle(temp);
		
		temp = "1"; //ensures author loop runs if Title is unchanged
		System.out.println("Enter book Author / Authors: \n (4 maximum. Press Enter on blank line to confirm final author)");
		for(int i = 0;  !(temp.isEmpty()) && i < 4; i++)
		{
			System.out.print("Enter author " + (i+1) + "'s FIRST name: ");
			temp = keyboard.nextLine();
			library[x].setAuth(i, 1, temp);
			if(temp.isEmpty())
				break;
			
			System.out.print("Enter author " + (i+1) + "'s LAST name: ");
			temp = keyboard.nextLine();
			library[x].setAuth(i, 0, temp);
			
			library[x].setNumAuth(i+1);
		}
		//sorts array[][] of author's names by author last name auth[x][0] then by auth[x][1]
		//if last names are the same.
		Arrays.sort(library[x].getAuth(), new Comparator<String[]>() {
	            @Override
	            public int compare(final String[] entry1, final String[] entry2) {
	                final String last1 = entry1[0];
	                final String last2 = entry2[0];
	                
	                if(last1.compareTo(last2) != 0)
	                	return last1.compareTo(last2);
	                else
	                {
	                	final String first1 = entry1[1];
		                final String first2 = entry2[1];
		                return first1.compareTo(first2);
	                }
	            }
	        });
		//test sorting of authors
		for(int i = 0; i < library[x].getAuth().length; i++)
		{
			System.out.println(library[x].getAuth(i, 0) + " " +library[x].getAuth(i, 1));
		}
		
		System.out.println("Enter Publisher: ");
		temp = keyboard.nextLine();
		if(!(temp.isEmpty()))
			library[x].setPub(temp);
		
		System.out.println("Enter Year Published: ");
		temp = keyboard.nextLine();
		
		try{
			tempi = Integer.parseInt(temp);
		}catch (NumberFormatException e){
			tempi = library[x].getPubYear();
		}
		
		while (tempi > currentYear)
		{
			System.out.println("ERROR: Please enter a valid year \n (enter a negative number for BC years)");
			temp = keyboard.nextLine();
		}
		if(!(temp.isEmpty()) && !(temp.equals( "")))
			library[x].setPubYear(tempi);
			
		
		
		System.out.println("Enter ISBN ");
		temp = keyboard.nextLine();
		
		while (!(temp.equals("")) && (Long.parseLong(temp) > 9999999999999L || Long.parseLong(temp) < 0))
		{
			System.out.println("ERROR: Please enter a valid ISBN");
			temp = keyboard.nextLine();
		}
		if(temp.isEmpty())
			temp = library[x].getIsbn();
		
		if(!(temp.isEmpty()))
		library[x].setIsbn(temp);
		
		System.out.println("Enter Price: ");
		temp = keyboard.nextLine();
		while(!(temp.equals("")) && Double.parseDouble(temp) < 0.0)
		{
			System.out.println("ERROR: Please enter a valid Price, don't add the '$'");
			temp = keyboard.nextLine();
		}
		if(!(temp.isEmpty()) && !(temp.equals("")))
		library[x].setPrice(Double.parseDouble(input));
		
		System.out.println("Enter Amount of books: ");
		temp = keyboard.nextLine();
		while (!(temp.equals("")) && Integer.parseInt(temp) <= 0) //chose 0 incase adding book that is not instock yet
		{
			System.out.println("ERROR: Please enter a valid book stock amount: ");
			temp = keyboard.nextLine();
		}
		if(!(temp.isEmpty()) && !(temp.equals("")))
		library[x].setCount(Integer.parseInt(temp));
		
		if(x == booksCount)
			booksCount ++;
		
		library[x].printRecordTable();
		System.out.println("library now contains: " + booksCount + " books");
	}
	
	public static int searchBook() 		//Search books by ISBN 
	{
		String temp = null;
		int index = 0;
		
		System.out.println("Enter ISBN");
		temp = keyboard.nextLine();
		
		for(int i=0; i < booksCount; i++)
		{
			if(library[i].getIsbn().equals(temp))
					{
						index = i;
						System.out.println("book found at : " + index);
						return index;
					}
		}
		
		System.out.println("book not found...");
		return -1;
		
	}
	
	public static void updateBook() 	// calls search and addBook methods based on index of bookType in Library
	{
		int x = 0;
		x = searchBook();
		System.out.println("Leave field blank to leave it unchanged\n\n");
		library[x].printRecordTable();
		addBook(x);
		
	}
	
	public static void importBook() throws FileNotFoundException //Import books file
	{
		String inFile = "books.txt";
		File input = new File(inFile);
		Scanner iFile = new Scanner(input);
		
		String title = null, pub = null, isbn = null, temp = null;
		String[][] auths = new String[4][2];
		int pubY = 0, count = 0;
		double pr = 0.0;
		while(iFile.hasNext())
		{
			title = iFile.nextLine();
			for(int i = 0; i<4; i++)
			{
				auths[i][0] = iFile.nextLine();
				auths[i][1] = iFile.nextLine();
			}
			
			pub = iFile.nextLine();
			pubY = iFile.nextInt();
			iFile.nextLine();
			isbn = iFile.nextLine();
			temp = iFile.nextLine();
			pr = Double.parseDouble(temp);
			temp = iFile.nextLine();
			count = Integer.parseInt(temp);
			library[booksCount] = new bookType(title, auths, pub, pubY, isbn, pr, count);
			
			//Sorth Authors by last name
			Arrays.sort(library[booksCount].getAuth(), new Comparator<String[]>() {
	            @Override
	            public int compare(final String[] entry1, final String[] entry2) {
	                final String last1 = entry1[0];
	                final String last2 = entry2[0];
	                
	                if(last1.compareTo(last2) != 0)
	                	return last1.compareTo(last2);
	                else
	                {
	                	final String first1 = entry1[1];
		                final String first2 = entry2[1];
		                return first1.compareTo(first2);
	                }
	            }
	        });
			
			library[booksCount].printRecordTable();
			
			if (iFile.hasNext())
			System.out.println();
			
			booksCount++;
			
		
		}
		System.out.println("current book count: " + booksCount);
	}
	
	public static void exportBook() throws IOException //Save File
	{
		String outfile = "backup - " + now();
		PrintWriter writer = new PrintWriter(outfile, "UTF-8");
		
		for(int i = 0; i < booksCount-1; i++)
		{
			
			writer.println(library[i].getTitle());
			for(int y = 0; y < 4; y++)
			{
				String temp = library[i].getAuthFull(y);
				writer.println(temp);
			}
			writer.println(library[i].getPub());
			writer.println(library[i].getPubYear());
			writer.println(library[i].getIsbn());
			writer.println(library[i].getPrice());
			writer.println(library[i].getCount());
		}
		
		System.out.println(booksCount +" records successfully exported");
		writer.close();
	}
	
	public static String now() 	//time stamp for output file
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(formattedDate);
		return sdf.format(cal.getTime());
	}
	
		
	
}