//
//  Warehouse_tracker.cpp
//  CISC 3130
//
//  Created by Mischa G Fubler on 6/18/15.
//

#include <iostream>
#include <stdio.h>
#include <fstream>
#include <sstream>
using namespace std;

int cities (string x){ //match city name to array index
	int cityIndex;
	
	if (x == "Chicago")
		cityIndex = 0;
	else if (x == "Houston")
		cityIndex = 1;
	else if (x == "Los Angeles")
		cityIndex = 2;
	else if (x == "Miami")
		cityIndex = 3;
	else if (x == "New York")
		cityIndex = 4;
	else cityIndex = -1;
	return cityIndex;
}

bool searchCities (int amt, int city1, int item, int records[][3], string cityNames[], double prices[],double& extraCost){
	int most = -1, city2 = -1, needed = 0;
	
	for (int i = 0; i < 3; i++) {
		if (i == city1) continue; //if same city as passed don't update i or most.
		if (records[i][item] > most) {
			most = records[i][item];
			city2 = i;
		}
	}
	if ((most + records[city1][item]) < amt || city2 == -1){ //if insufficient amount at any other warehouse
		cout <<"\n\tItem" << (item+1) << " Order Unfulfilled";
		return false;
	}
	else{ //process movement of items and
		needed = (amt - records[city1][item]);
		records[city2][item] -= needed;
		cout << "\n\t"<< needed << " of Item" << (item+1) << " shipped from " << cityNames[city2] << " to " << cityNames[city1];
		records[city1][item] += needed;
		cout << "\n\t" <<cityNames[city2] << "\t"; //print out city2 update
		if (cityNames[city2].length() < 8) cout << "\t"; //allignment tab
		cout << records[city2][0] << "\t" << records[city2][1] << "\t" << records[city2][2];
		extraCost += ((prices[item]*1.1) * needed); //calculate extra cost
		extraCost += (prices[item] * (amt - needed)); //calculate cost of remaining units from city1
		return true;
	}
}

void readPrices (ifstream& x, double costs[]){
    string copy;
    
    if (x.is_open()) {
        int i = 0;
        
        while (!x.eof()) {
            getline(x, copy, '$'); //read in garbage (price1 =$)
            
            getline(x, copy, '\t'); //read in price
            costs[i] = atof(copy.c_str());
            if(x)i++;
        }
    }
}

void processOrderCards (ifstream& x, int stuff[][3], string city[], double priceArr[]){
    string copy, type = "0";
	int cityIndex, size = 3, orderAmts[3] = { };//can alter number of items warehouse stores with size. orderAmts needs to be reset too
	double price = 0.0;
	bool fulfilled = false;
	
    if (x.is_open())
    {
		while (!x.eof()) {
			getline(x, type, '\t');
			getline(x, copy, '\t');
			cityIndex = cities(copy);
			cout << type << "\t" << city[cityIndex] << "\t"; //print order type & city name
			if (city[cityIndex].length() < 8) cout << "\t";//allignment tab
			
			for (int i = 0; i < (size-1); i++) { //read in order amounts
				getline(x, copy, '\t');
				orderAmts[i] = atoi(copy.c_str());
			}
			getline(x, copy); //kludge. '\t' consumes order type char.
			orderAmts[(size-1)] = atoi(copy.c_str());
			
			for (int i= 0; i < (size); i++) { //print order amounts
				cout << orderAmts[i] << "\t";
			}
			
			if (type[0] == 's'){ //if shipment
				for (int i = 0; i < size; i++) {
				stuff[cityIndex][i] += orderAmts[i];
				}
			}
			
			else { //else order
				for (int i = 0; i < size; i++) { //process orders
					if (orderAmts[i] > stuff[cityIndex][i])//if insufficient amount, call searchCities, move
						fulfilled = searchCities(orderAmts[i], cityIndex, i, stuff, city, priceArr, price); //fix 10% surcharge calc error.
					
					if (orderAmts[i] <= stuff[cityIndex][i]) {//if sufficient amount and charge not already calculated, process rest of order
						stuff[cityIndex][i] -= orderAmts[i];
						if (!(fulfilled)) //if movement between warehouses, update price
						price += (orderAmts[i] * priceArr[i]);
					}
				}
			}
			
			cout << "\n\t" << city[cityIndex] << "\t"; //printing updated warehouse info
			if (city[cityIndex].length() < 8) cout << "\t";//allignment tab
			cout << stuff[cityIndex][0] << "\t" << stuff[cityIndex][1] << "\t" << stuff[cityIndex][2];
			if(type[0] == 'o')cout << "\nPrice of Order: $" << price << "\n\n\n";
			else cout << "\n\n";
			
			price = 0.0;
			fulfilled = false;
		}
    }
}

int main ()
{
    string warehouses[5] = {"Chicago", "Houston", "Los Angeles", "Miami", "New York"};
                        //     0            1           2           3          4
	double prices[3] = { };
	int inventory[5][3] = { };
		
    ifstream priceFile, orderFile;
    priceFile.open("ItemPrices.txt");
    orderFile.open("OrderCards.txt");
    
    readPrices(priceFile, prices);
    processOrderCards(orderFile, inventory, warehouses, prices); //processes shipments and calls processOrders
    
    return 0;
}