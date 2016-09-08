/* Mischa Fubler
*
* December 11, 2014
*
* memberType class for bookstore.java
*/

import java.text.DecimalFormat;
import com.sun.tools.javac.util.List;

public class memberType
{
	private String[] name;
	private int memID, bookCount;
	private double amtSpent, discount;
	private final DecimalFormat money = new DecimalFormat("$#,##0.00");
	
	public memberType()
	{
		name = new String[2];
		memID = 0;
		bookCount = 0;
		amtSpent = 0.0;	
	}
	
	public memberType(String first, String last, int id, int count, double spent)
	{
		name = new String[2];
		name[0] = last;
		name[1] = first;
		memID = id;
		bookCount = count;
		amtSpent = spent;
	}
	
	public void setName(String first, String last)
	{
		name[0] = last;
		name[1] = first;
	}
	
	public String getName()
	{
		String n = name[1] + " " + name[0]; 
		return n;
	}
	
	public String getNameFirst()
	{
		return name[1];
	}
	
	public String getNameLast()
	{
		return name[0];
	}
	
	public void setMemID(int id)
	{
		memID = id;
	}
	
	public int getMemID()
	{
		return memID;
	}
	
	public void setBookCount(int book)
	{
		bookCount = book;
	}
	
	public int getBookCount()
	{
		return bookCount;
	}
	
	public void setAmtSpent(double d)
	{
		amtSpent = d;
	}
	
	public double getAmtSpent()
	{
		return amtSpent;
	}
	
	public double purchases(int items, List<Double> receipt, int counter) //considered storing purchases in a list<booType> for printing of receipt
	{
		//int counter = 0;
		double disc = 0.0;
		
		if(bookCount + items >= 11)
		{
			while(counter < receipt.length())
			{
				disc += (Double) receipt.get(counter);
				if(counter%10 == 0)
					break;
			}
			discount += disc/10;
			bookCount = (bookCount + items) - 11;
			
			if(bookCount > 10)
				purchases(bookCount, receipt, counter);
			
			return discount;
		}
		else
		{
			bookCount += items;
			
			return discount;
		}
		  
	}
	
	public double purchase(int items, double price)
	{
		discount = 0.0;
		
		if (bookCount + items < 11)
		{
			bookCount += items;
			amtSpent += (items*price);
		}
			
		
		if (bookCount + items >= 11)
		{
			
			discount = (amtSpent/10);
			System.out.println("Discount applied: " + discount);
		}
		return discount;
	}
	
	public void printRecordTable()
	{
		System.out.println("Member Name: " + this.getName() + "\nMember ID: " + this.getMemID() 
				+ "\nBook Count: " + this.getBookCount() + "\nAmount Spent: " + money.format(this.getAmtSpent()));
	}
}