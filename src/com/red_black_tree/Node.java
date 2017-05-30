package com.red_black_tree;

public class Node {

	int Key;
	
	Node leftChild = null;
	Node rightChild = null;
	Node parent = null;
	
	// is color red?
	boolean color = true;
	
	Node(int key){
		
		this.Key = key;
		
	}
	
}
