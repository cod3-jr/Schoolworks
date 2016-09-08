import java.text.DecimalFormat;

/*Mischa Fubler
 * 
 * Employee Class
 * class miserPay
 */
public class employee implements Comparable<employee>
//implements Comparable to sort employee[] by netPay
{
	private String empName;
	private double hours, salary, grossPay, netPay, tax, taxRate, taxRateOlder, fullTime;
	private int age, taxAge;
	private DecimalFormat money = new DecimalFormat("$#,##0.00"); //format monetary values
	
	public employee(String n, double hrs, double money, int a)
	{
		empName = n;
		hours = hrs;
		salary = money;
		age = a;
		fullTime = 40;
		taxAge = 55;
		taxRate = .1;
		taxRateOlder = .5;
		basePay();
		taxCalc();
		netPayCalc();
	}
	public employee()
	{
		empName = null;
		hours = 0.0;
		salary = 0.0;
		age = 0;
		fullTime = 40;
		taxAge = 55;
		taxRate = .1;
		taxRateOlder = .5;
		basePay();
		taxCalc();
		netPayCalc();
	}
	public void setName(String n)
	{
		empName = n;
	}
	public String getName()
	{
		return empName;
	}
	public void setHours(double hrs)
	{
		hours = hrs;
	}
	public double getHours()
	{
		return hours;
	}
	public void setSalary(double money)
	{
		salary = money;
	}
	public double getSalary()
	{
		return salary;
	}
	public void setAge(int a)
	{
		age = a;
	}
	public int getAge()
	{
		return age;
	}
	public void setTaxAge(int a)
	{
		taxAge = a;
	}
	public int getTaxAge()
	{
		return taxAge;
	}
	public void setTaxRate(double a)
	{
		taxRate = a;
	}
	public double getTaxRate()
	{
		return taxRate;
	}
	public void setTaxRateOlder(double a)
	{
		taxRate = a;
	}
	public double getTaxRateOlder()
	{
		return taxRate;
	}
	public void setFullTime(int a)
	{
		fullTime = a;
	}
	public double getFullTime()
	{
		return fullTime;
	}
	public void setTax(double a)
	{
		tax = a;
	}
	public double getTax()
	{
		return tax;
	}
	public double getNetPay()
	{
		return netPay;
	}
	public void printRecord() //in case individual record info is needed.
	{
		
		System.out.println("Name: \t" + empName);
		if(empName.length()<12)
			System.out.print("\t");
		System.out.println("Hours worked: \t" + hours);
		System.out.println("Rate of Pay: \t" + money.format(salary) + " /per hour");
		System.out.println("\tAge: \t" + age);
		System.out.println("Base Pay: \t"+ money.format(grossPay));
		System.out.println("\tTax: \t" + money.format(tax) + "\nNet Pay: \t" + money.format(netPay));
	}
	public void printRecordTable()
	{
		if(empName.length()<10) //maintains tab alignment for shorter name
		{
			System.out.println(empName + "\t\t\t" + hours + "\t   " + money.format(salary) 
					+ "\t" + age + "\t" + money.format(grossPay) + "\t\t" +money.format(tax)
					+ "\t" + money.format(netPay));
		}else
		System.out.println(empName + "\t\t" + hours + "\t   " + money.format(salary) 
				+ "\t" + age + "\t" + money.format(grossPay) + "\t\t" +money.format(tax)
				+ "\t" + money.format(netPay));		
	}
	public void basePay()
	{		
		if(hours > fullTime)
		{
			grossPay = (hours - fullTime)*(salary*1.5);
			grossPay += fullTime*salary;			
		}
		else grossPay = hours*salary;
	}
	public void taxCalc()
	{
		if(age>=taxAge)
		{
			tax = grossPay*taxRateOlder;
			
		}
		else
		{
			tax = grossPay*taxRate;
		}
	}
	public void netPayCalc()
	{
		netPay = grossPay-tax;
	}
	
	@Override public int compareTo(employee o) //sets which member variable to compare. 
	//if necessary, could take extra parameter with member variable name...
	{
		return new Double(o.getNetPay()).compareTo(new Double(this.getNetPay()));
	}
	
}