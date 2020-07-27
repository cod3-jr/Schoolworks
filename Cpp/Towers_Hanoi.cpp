//
//  Towers_Hanoi.cpp
//  CISC 3130
//
//  Created by Mischa G Fubler on 6/4/15.
//  Copyright (c) 2015 Mischa G Fubler. All rights reserved.
//

#include <stdio.h>
#include <iostream>
#include <fstream>
#include <new>
using namespace std;

int main ()
{
    
    
    return 0;
}

Towers (int n, char front, char top, char aux) //example
{
    if (n == 1)
    {
        cout << "move disk 1 from " << from << " to " << to;
        return;
    }
        Towers (n-1, from, aux, top);
        cout << "move disk " << n << " from " << from << " to " << top;
        
        Towers (n-1, aux, top, from);
}