
/* Mischa Fubler
* December 11, 2014
* 
* bookType Class for bookstore.java
*/

public class bookType implements Comparable<bookType>
//implements Comparable to sort employee[] by netPay
{
	private String [][] author;
	private String title, publisher, isbn;
	private int pubYear, stock, numAuth;
	private double price;
	
	
	
	public bookType(String t, String[][] auth, String pub, int year, String num, double cost, int count)
	//constructor authors already in String[][]
	{
		this.title = t;
		author = auth;
		publisher = pub;
		pubYear = year;
		isbn = num;
		price = cost;
		stock = count;
		numAuth = auth.length;
	}

	public bookType() 
	//default constructor
	//consider importing a function that returns the current year
	{
		title = null;
		author  = new String[4][2];
			for(int i = 0; i<4; i++)
			{
				author[i][0] = "0";
				author[i][1] = null;
			}
		publisher = null;
		pubYear = 0000;
		isbn = "0000000000000";
		price = 0.00;
		stock = 0;
		numAuth = 0;
	}
	
	public void setTitle(String t)
	{
		title = t;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	
	public void setAuth(int x, int y, String name) 
	//set author or authors by passing String variable
	{
		author[x][y] = name;
	}
	
	
	public String[][] getAuth() //returns entire auth[][]
	{
		return author;
	}
	
	public String getAuth(int x, int y) // returns specific auth[][] element
	{
		return author[x][y];
	}
	
	public String getAuthFull(int x)
	{
		return (this.getAuth(x, 0) + ", " + this.getAuth(x, 1));
	}
	
	public void setNumAuth(int x)
	{
		numAuth = x;
	}
	
	public int getNumAuth()
	{
		return numAuth;
	}
	
	public void setPub(String p)
	{
		publisher = p;
	}
	
	public String getPub()
	{
		return publisher;
	}
	
	public void setIsbn(String i)
	{
		isbn = i;
	}
	
	public String getIsbn()
	{
		return isbn;
	}
	
	public void setPubYear(int yr)
	{
		pubYear = yr;
	}
	
	public int getPubYear()
	{
		return pubYear;
	}
	
	public void setCount(int c)
	{
		stock = c;
	}
	
	public int getCount()
	{
		return stock;
	}
	
	public void setPrice(double p)
	{
		price = p;
	}
	
	public double getPrice()
	{
		return price;
	}
	
	public double purchase(int i)
	{
		double value = 0.0;
		
		value = this.getPrice() * i;
		this.setCount(getCount()-i);
		if(this.getCount() <= 1)
			System.out.println("NOTICE! There is " + this.getCount() + " copy remaining in stock.");
		else System.out.println("There are " + this.getCount() + " copies remaining in stock.");
		return value;
	}
	public int confirmTitle(String t)
	//possibly switch IgnoreCase with toUpper 
	//if errors encountered while testing
	{
		int temp = t.compareToIgnoreCase(title);
		
		return temp;
	}

	public void printRecordTable()
	{
		
		System.out.println("Title: " + this.getTitle() + "\n" +"Authors: "+ this.getAuth(0, 0) + ", " + this.getAuth(0, 1) + "; " + this.getAuth(1, 0) + ", " 
				+ this.getAuth(1, 1) + "\n\t" + this.getAuth(2, 0) + ", " + this.getAuth(2, 1) + "; " + this.getAuth(3, 0) + ", " + this.getAuth(3, 1) + "\n"
				+"Publisher: "+ this.getPub() + "\n" +"Year: "+ this.getPubYear() + "\n" +"ISBN: "+ this.getIsbn() + "\nPrice: $" + this.getPrice() 
				+ "\n" +"Stock: "+ this.getCount() + " books");		
	}

	@Override
	public int compareTo(bookType b) 
	{
		return (this.getIsbn()).compareTo(b.getIsbn());
	}
		
} 