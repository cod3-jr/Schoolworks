//
//  main.cpp
//  CISC 3130
//
//  Created by Mischa G Fubler on 5/26/15.
//  Copyright (c) 2015 Mischa G Fubler. All rights reserved.
//
#include <iostream>
#include <fstream>
#include <new>
using namespace std;



int main(int argc, const char * argv[]) {
    // insert code here...
    
    int * p, * q;
    int x;
    
    p = new int; //new instance of p.
    *p = 3; //pointing to new p.
    q = p;
    
    cout<< *q << " " << *p << "\n";
    
    x = 7;
    *q = x;
    cout<< *q << " " << *p << "\n";
    
    *p = 5;
    cout<< *q << " " << *p << "\n";

    
    std::cout << "Hello, World!\n";
    
    ifstream masterFile ("Master.txt");
    
    if (masterFile.is_open()) {
        cout << "success!";
    }
    
    
    return 0;
}
