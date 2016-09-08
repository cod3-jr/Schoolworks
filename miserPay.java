/* Mischa Fubler
 * Nov. 8, 2014
 * 
 * Purpose: calculate payroll from employee records based on hours worked
 * and hourly rate. Included in this calculation is overtime hours,
 * overtime pay, and tax deduction based on age. 
 *
 *input: file containing employee record.
 *
 *output: print to screen - Name: x hours: y age: z
 */
import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.Arrays;

public class miserPay
{
	//public static employee[] empRecords;
	
	public static void main (String[]args)throws IOException
	{
		String fileName = "employees.txt";
		File myFile = new File (fileName); 
		Scanner iFile = new Scanner (myFile);
		
		File counterFile = new File (fileName);
		Scanner countFile = new Scanner (counterFile);
		int arraySize = counter(countFile);
		
		employee[] employees = new employee[arraySize];
		employee temp = new employee();
		
		System.out.println("\t\tMiser Corporation Payroll\n");
		System.out.println("emp Name: \tHours worked \tHourly pay \tage \tGross Pay \tTax \tNet Pay");
		employees = empRecs(iFile, arraySize); //initializes, assigns, & prints employee records
		
		temp = findOldest(employees, arraySize); //print oldest
		System.out.println("Oldest Employee: \t" +temp.getName() + " " + temp.getAge()+ " years old");
		
		temp = findYoungest(employees, arraySize); //print youngest
		System.out.println("Youngest Employee: \t" +temp.getName() + " " + temp.getAge()+ " years old");
		
		temp = mostTax(employees, arraySize); //print youngest
		System.out.println("\nPaid most tax: \t" +temp.getName());
		System.out.println("Tax Amount: \t$" +temp.getTax());
		
		empSort(employees);
		netPaySort(employees);
		
		System.out.println("\n**Payroll program complete**");
		
		iFile.close();
	}
	
	public static int counter(Scanner count) //counts number of records (if separated by \n)
	{	
		int c = 0;
		
		//send file content for counting and returns record count
		while (count.hasNext()) 
		{
			count.nextLine();
			c++;
		}
		return (c/2);
	}

	public static employee[] empRecs (Scanner file, int size)
	{
		employee[] empRecs = new employee[size]; //array of employee class objects
		String name = null;
		double hours = 0.0, salary = 0.0;
		int age = 0;
		
		while (file.hasNext()) //checks if the file still contains content
		{
			for (int i = 0; i < (size); i++) //populates employee class member variables
			{
				name = file.nextLine();
				hours = file.nextDouble();
				salary = file.nextDouble();
				age = file.nextInt();
				
				empRecs[i] = new employee(name, hours, salary, age);
				empRecs[i].printRecordTable();
				
				if (file.hasNext()) //fixes "no line found" error thrown when reading multiple records of unknown length
					file.nextLine();
				System.out.println();
				
			}
		}
		return empRecs;
	}
	
	public static employee findOldest(employee[] emp, int size)
	{
		employee temp = emp[0]; 
		
		for(int i = 1; i<size; i++)
		{
			if(temp.getAge() < emp[i].getAge())
			{
				temp = emp[i];
			}
		}
		return temp;
	}
	
	public static employee findYoungest(employee[] emp, int size)
	{
		employee temp = emp[(size-1)]; 
		
		for(int i = 1; i<size; i++)
		{
			if(temp.getAge() > emp[i].getAge())
			{
				temp = emp[i];
			}
		}
		return temp;
	}
	
	public static employee mostTax(employee[] emp, int size)
	{
		employee temp = emp[0]; 
		
		for(int i = 1; i<size; i++)
		{
			if(temp.getTax() < emp[i].getTax())
			{
				temp = emp[i];
			}
		}
		return temp;
	}
	
	public static void empSort(employee[] emp) 
	//copied emp name to temp[] because Arrays.sort() would not sort emp.getName()
	{
		String [] temp = new String[emp.length];
		
		for(int i = 0; i < emp.length; i++)
			temp[i] = emp[i].getName();
		
		Arrays.sort(temp);
		
		System.out.println("\nEmployees Sorted by Name:");
		for(int i = 0; i < emp.length; i++)
			System.out.println(temp[i]);
	}
	
	public static void netPaySort(employee[] emp)
	{
		DecimalFormat money = new DecimalFormat("$#,##0.00"); //format monetary values
		Arrays.sort(emp);
		//used @Overide to set which member element to compareTo
		//could take extra parameter in method to choose which 
		System.out.println("\nEmployees Sorted by Net Pay:");
		for(int i = 0; i < emp.length; i++)
			System.out.println(emp[i].getName() + " : " + money.format(emp[i].getNetPay()));
				
	}
	
}
