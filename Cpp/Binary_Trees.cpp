//
//  Binary_Trees.cpp
//  CISC 3130
//
//  Created by Mischa G Fubler on 6/30/15.
//  Copyright (c) 2015 Mischa G Fubler. All rights reserved.
//
using namespace std;

#include <iostream>
#include <stdio.h>
#include <fstream>
#include <sstream>

typedef struct node *nodePtr;

struct node {
	int info = NULL;
	nodePtr left, right, father;
};

nodePtr makeTree (int x){
	nodePtr p;
	
	p = new node;
	p -> info = x;
	p -> left = NULL;
	p -> right = NULL;
	p -> father = NULL;
	
	return p;
}

nodePtr getNode ( ){
	nodePtr p;
	
	p = new node;
	return p;
}

void setLeft (nodePtr p, int x){
	nodePtr q;
	
	q = getNode();
	q -> info = x;
	q -> left = NULL;
	q -> right = NULL;
	q -> father = p;
	p -> left = q;
}

void setRight (nodePtr p, int x){
	nodePtr q;
	
	q = getNode();
	q -> info = x;
	q -> left = NULL;
	q -> right = NULL;
	q -> father = p;
	p -> right = q;
}

void prePrint (nodePtr tree){
	if (tree != NULL) {
		cout << tree -> info << " ";
		
		prePrint(tree -> left);
		prePrint(tree -> right);
	}
}

void inPrint (nodePtr tree){
	if (tree != NULL) {
		prePrint(tree -> left);
		
		cout << tree -> info << " ";
		
		prePrint(tree -> right);
	}
}

void postPrint (nodePtr tree){
	if (tree != NULL) {
		prePrint(tree -> left);
		prePrint(tree -> right);
		
		cout << tree -> info << " ";
	}
}

void printer (nodePtr tree){
	cout << "\nPre:\t";
	prePrint(tree);
	cout << "\nIn:\t\t";
	inPrint(tree);
	cout << "\nPost:\t";
	postPrint(tree);
}

void count (nodePtr tree, int& num){
	if (tree != NULL) {
		count(tree -> left, num);
		num++;
		count(tree -> right, num);
	}
}

void children (nodePtr tree){
	
	if (tree != NULL) {
		children(tree -> left);
		
		if (tree -> left != NULL && tree -> right != NULL)
			cout << 2 << " ";
		else if (tree -> left != NULL || tree -> right != NULL)
			cout << 1 << " ";
		else cout << 0 << " ";
		
		children(tree -> right);
	}
}

/*void insertNode(nodePtr tree, int num){ //return
	nodePtr p, q;
	
	p = q = tree;
	
	if (p -> info == -999) { //if tree is empty
		tree -> info = num;
	}
	else if (p -> info == num) //if duplicate number
		printf("duplicate number %d excluded\n", num);
	else if (p -> info > num && tree -> left == NULL) //if left node empty
		setLeft(tree, num);
	else if (p -> info < num && tree -> right == NULL) //if right node empty
		setRight(tree, num);
	
	else  {
		if (p -> info > num && tree -> left -> info < num){ //if num fits between tree and left node
			q = tree -> left;
			setLeft(tree, num);
			tree -> left  -> left = q;
			q -> father = tree -> left;
		}
		else if (p -> info < num && tree -> right -> info > num){ //if num fits between tree and left node
			q = tree -> right;
			setRight(tree, num);
			tree -> right -> right = q;
			q -> father = tree -> right;
		}
	}
	//insertNode(p -> left, num);
	
	//insertNode(p -> right, num);
}*/

void insertNode(nodePtr tree, int num){
	nodePtr p, q, r, insert;
	
	p = q = tree;
	
	while (num != p -> info && q != NULL) {
		p = q;
		if (num < p -> info)
			q = p -> left;
		else
			q = p -> right;
	}
	if (num == p -> info)
		printf(" duplicate number %d excluded from insertion ", num);
	else if (num < p -> info && num > p -> left -> info){
		r = p -> left; //remember next level
		setLeft(p, num);
		p -> left -> left = r; //join lower level to new node
	}
	else setRight(p, num);
}

void deleteNode(nodePtr tree, int& num){
	nodePtr p, q;
	
	deleteNode(tree -> left, num);
	
	deleteNode(tree -> right, num);
	
	if (tree -> info == num) {
		if (tree -> left != NULL) {
			p = tree -> left;
			tree -> father -> left = p;
		}
		if (tree -> right != NULL) {
			q = tree -> right;
			tree -> father -> right = q;
		}
	}
	else if (tree ->left == NULL && tree -> right == NULL)
		printf("number %d not found in tree\n", num);
}

int main( ){
	
	
	nodePtr tree, p, q;
	ifstream data;
	string copy;
	int num = 0, counter = 0;
	data.open("TreeData.txt");

	if (data.is_open()) {
		
		getline(data, copy, '\t');
		cout << copy << "\t";
		data >> num; //First read
		
		do {
			tree = makeTree(num);
			
			while (num != -999) { //while not end of line
				data >> num;
				if (num == -999)
					break;
				
				p = q = tree;
				
				while (num != p -> info && q != NULL) { //fill tree
					p = q;
					if (num < p -> info)
						q = p -> left;
					else
						q = p -> right;
				}
				if (num == p -> info)
					printf(" duplicate number %d excluded ", num);
				else if (num < p -> info)
					setLeft(p, num);
				else setRight(p, num);
			}
			
			printer(tree); //print tree et all after adjustment
			count(tree, counter);
			printf("\nnodes: %d\n", counter);
			counter = 0;
			cout << "child inCount:\t";
			children(tree);
			
			cout << "\n";
			copy = "0";
			while (copy[0] != 'S') { //read instruction
				data >> copy >> num;
				if (copy[0] == 'S' || data.eof())
					break;
				
				if (copy == "Insert")
					//cout << "insert ";
					insertNode(tree, num);	//fix this
				
				else if (copy == "Delete")
					cout << "delete ";
					//deleteNode(tree, num);	//fix this
				
				else cout << "Invalid Instruction";
			}
			cout << "\n\nupdated tree\n";
			printer(tree);
			count(tree, counter);
			printf("\nnodes: %d\n", counter);
			counter = 0;
			cout << "child inCount:\t";
			children(tree);
			
			if (!data.eof()) //stop printing if end of file
				cout << "\n\n" << copy << "\n"; //print next Set#
			delete tree; //free tree
		}while (!data.eof());
	}
	
	
	
	data.close();
	return 0;
}