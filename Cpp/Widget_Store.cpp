//
//  Widget_Store.cpp
//  CISC 3130
//
//  Created by Mischa G Fubler on 6/26/15.
//
using namespace std;

#include <iostream>
#include <stdio.h>
#include <fstream>
#include <sstream>

struct record {
	char type;
	int amt = 0;
	double price = 0.0;
	record *next;
};


void fillList (ifstream& x, record *transaction){
	string copy;
	
	if (x.is_open() && !(x.eof())) {
		
		getline(x, copy, '\t'); //read in Type
		transaction -> type = copy[0];
		getline(x, copy, '\t');//clear tab
		
		if (transaction -> type == 'S' || transaction -> type == 'P') {
			getline(x, copy); //read in quantity
			transaction ->amt = atoi(copy.c_str());
		}
		else if (transaction ->type == 'R'){ // if receipt
			getline(x, copy, '\t'); //read in quantity
			transaction ->amt = atoi(copy.c_str());
			getline(x, copy);
			transaction ->price = atof(copy.c_str());
		}
		
		transaction -> next = NULL;
	}
	else cout << "End of File";
	
}


void received (record *p, int& stock){
	cout << "Received: " << p -> amt << "\t@ ";  //Print Receipt
	printf("\t$%.2f\n\n", p -> price);
	stock += p -> amt;
}

void promotion (record *p, bool& discount, int& disCounter){
	discount = true; //activate discount flag
	disCounter = 2;
	cout << "Next 2 customers receive a " << p -> amt << "% discount\n\n";
}

void sale (record *p, int& stock, double& tSales, double& markup, int& order, bool& discount, int& disCounter, record *list, record *pDiscount){
	record *traverse;
	double discAmt = 0.0, sales = 0.0;
	int sold = 0, remain = 0;
	traverse = list;
	
	remain = p -> amt - stock;
	
	if (traverse -> amt >= p -> amt) { //if enough in one node to fill order
		traverse -> amt -= p -> amt;
		stock -= p -> amt;
		sales = traverse -> price * markup;
		tSales += sales * p -> amt;
		
		cout << p -> amt << " Widgets sold\n" << p -> amt;
		printf(" at $%.2f each\t\tSales: $%.2f\n", sales, tSales);

	}
	else if (traverse -> amt < p -> amt) { //if insufficient in node
		order = p -> amt;
		if (stock >= p -> amt)
			cout << p -> amt << " Widgets sold\n";
		else cout << stock << " Widgets sold\n";
		
			while (traverse -> next != p -> next && traverse -> next != NULL) { //traverse list
				if (traverse -> amt == 0 || traverse -> type != 'R') {
					traverse = traverse -> next;
					continue;
				}
				if (order < traverse -> amt){ //if surplus in node
					traverse -> amt -= order;
					stock -= order;
					sold = order;
					order = 0;
				}
				else {
					order -= traverse -> amt;
					sold = traverse -> amt;
					stock -= traverse -> amt;
					traverse -> amt = 0;
				}
				
				sales = traverse -> price * markup;
				printf("%d\t at $%.2f each\t\tSales: $%.2f\n", sold, sales, sales * sold);
				tSales += sales * sold;
				traverse = traverse -> next;
				if (order == 0)
					break;
			}
	}

	
	if (discount == false) { //calculate total sale if no discount
		printf("\t\tTotal Sales: $%.2f\n", tSales);
	}
	else { //total sale if discount
		disCounter--;
		cout << "\tDiscount amount: " << pDiscount -> amt <<"%\n";
		discAmt = (pDiscount -> amt) /100.00;
		discAmt = discAmt * tSales;
		tSales -= discAmt;
		printf("\tDiscounted Sales: $%.2f\n", tSales);
		if (disCounter == 0)
			discount = false;
		}
	if (remain > 0)
		cout << "remainder of " << remain << " Widgets not available.\n\n";
	if (remain <= 0)
		cout << "\n";
	
	order = 0;
	tSales = 0;
	sold = 0;
	remain = 0;
}

void endPrint(record *p){
	
}

int main ()
{
	record *list, *p, *pDiscount = NULL;
	
	bool discount = false;
	string copy;
	int stock = 0, order = 0, disCounter = 0;
	double sales = 0.0, markup = 1.3;
	ifstream transFile;
	transFile.open("Widgets.txt");
	
	list = new record;
	list -> next = NULL;
	p = list;
	
	for (int i = 0; i < 3; i++) //clear file headings
		getline(transFile, copy);
	
	while (!(transFile.eof())){ //fill list
		fillList(transFile, p);
		p -> next = new record;
		p = p -> next;
	}
	
	p = list;
	while (p -> next != NULL) { //process list
		if (p -> type == 'R')
			received(p,stock);
		else if (p -> type == 'P'){
			promotion(p, discount, disCounter);
			pDiscount = p; //point to discount node
		}
		else if (p -> type == 'S')
			sale(p, stock, sales, markup, order, discount, disCounter, list, pDiscount);
		
		p = p -> next;
	}
	
	cout << "------\nWidgets Remaining\n";
	p = list;
	
	while (p -> next != NULL) {
		if (p -> type != 'R' || p -> amt == 0){
			p = p -> next;
			continue;
		}
		cout << p -> amt;
		printf("\t@\t$%.2f\n", p -> price);
		p = p -> next;
	}
	
	transFile.close(); 
	return 0;
}