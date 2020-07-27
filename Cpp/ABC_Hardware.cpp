//
//  ABC_Hardware.cpp
//  CISC 3130
//  Homework Assignment #1 - Review
//
//  Created by Mischa G Fubler on 5/28/15.
//
#include <iostream>
#include <stdio.h>
#include <fstream>
#include <sstream>
using namespace std;

struct customer {
    int id;
    string name;
    double balance, previous;
};

struct transaction {
    char type;
    int id, transNum, amount;
    double price, payment;
    string item;
    
};

void readMaster(customer files[], ifstream& x, int& size){
    string copy;
    
    if(x.is_open())
    {
        int i = 0;
        while (!x.eof())
        {
            getline(x, copy); //read in ID
            if (copy == "0")
                break;
            files[i].id = atoi(copy.c_str()); //cast as int
            getline(x, files[i].name); //read in Name
            getline(x, copy); //read in balance
            files[i].balance = atof(copy.c_str());
            i++;
            if(x)size++; //stops extra record on 2nd running of program
        }
    }
    else cout << "Unable to open Master Record file...";
    x.close();
}

void readTrans(transaction files[], ifstream& x, int& size){
    string copy;
    
    if(x.is_open()){
        int i = 0;
        while (!x.eof()){
            getline(x, copy); //read in Type
            files[i].type = copy.at(0);
            getline(x, copy); //read in ID
            files[i].id = atoi(copy.c_str());
            getline(x, copy); //read in Trans #
            files[i].transNum = atoi(copy.c_str());
            
            if (files[i].type == 'P') {
                getline(x, copy); //read in payment
                files[i].payment = atof(copy.c_str());
            }
            else{
                getline(x, files[i].item); //read in item
                getline(x, copy); //read in Quantity
                files[i].amount = atoi(copy.c_str());
                getline(x, copy); //read in price for 1
                files[i].price = atof(copy.c_str());
            }
            i++;
            if(x)size++;
        }
    }
    else cout << "Unable to open Transaction Record file...";
    x.close();
}

void process (customer accounts[], transaction transactions[], double& total, int mSize, int tSize){
    for (int i = 0; i < mSize; i++){
        accounts[i].previous = accounts[i].balance; //record previous balance
        total += accounts[i].balance;
        
        for(int j = 0; j <tSize; j++){
            if (accounts[i].id == transactions[j].id){
                if(transactions[j].type == 'O'){
                    accounts[i].balance += (transactions[j].price * transactions[j].amount);
                    total += (transactions[j].price * transactions[j].amount);
                }
                else{
                    accounts[i].balance -= transactions[j].payment;
                    total -= transactions[j].payment;
                }
            }
        }
    }
}

void printer (customer accounts[], transaction transactions[], double& total, int aSize, int tSize){
    for (int i = 0; i < aSize; i++){
        printf ("%*s", -20, accounts[i].name.c_str()); //20 character long left justified.
        printf("%04d \n\n", accounts[i].id);
        printf("\t\t\t\tPrevious Balance: $%.2f\n\n", accounts[i].previous);
        for (int j = 0; j < tSize; j++){
            if (accounts[i].id == transactions[j].id){
                printf("%04d", transactions[j].transNum); //incase transNum starts with 0 in file
                if (transactions[j].type == 'O'){
                    printf ("\t\t%*s", -20, transactions[j].item.c_str());
                    printf(" $%.2f\n", (transactions[j].price * transactions[j].amount));
                }
                else{
                    printf("\t\tPayment             ($%.2f)\n", transactions[j].payment);
                }
            }
        }
        printf("\n\t\t\t\tBalance Due: $%.2f\n\n",accounts[i].balance);
    }
    printf("A/R Balance of ABC Company: $%.2f\n", total);
}

void update(customer accounts[], int& mSize){
    ofstream masterFile;
    masterFile.open("Master.txt", ios::trunc);
    
    for (int i = 0; i < mSize; i++){
        if (accounts[i].id == 0) break;
        masterFile <<accounts[i].id << "\n";
        masterFile << accounts[i].name << "\n";
        masterFile << accounts[i].balance << "\n";
    }
    
    masterFile.close();
    cout << mSize << " Records updated successfully.";
}

int main (){
    int masterSize, transSize; //counts number of records in each file
    double running; //running total
    
    ifstream counterFile, masterFile, transFile;
    masterFile.open("Master.txt");
    transFile.open("Transactions.txt");
    
    customer records[100];
    transaction trans[500];
    
    readMaster(records, masterFile, masterSize);
    readTrans(trans, transFile, transSize);
    process(records, trans, running, masterSize, transSize);
    printer(records, trans, running, masterSize, transSize);
    update(records, masterSize);
   
    masterFile.close();
    transFile.close();
    
    return 0;
}