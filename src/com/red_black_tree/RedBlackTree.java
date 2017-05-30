package com.red_black_tree;

import java.util.Random;

public class RedBlackTree {
	
	static Node root;
	
	static void addNode(int key)  {
		
		Node node = new Node(key);
		
		Node y = null;
		Node x = root;
		
		while(x != null) {
			y = x;
			if(node.Key == x.Key) {
				System.out.println("Duplicate: "+node.Key);
				return;
			}
			else if(node.Key < x.Key)
				x = x.leftChild;
			else
				x = x.rightChild;
		}
		
		node.parent = y;
		
		if(y == null) {
			root = node;
			root.color = false;
			return;
		}
		else if(node.Key < y.Key)
			y.leftChild = node;
		else
			y.rightChild = node;
		
		node.color = true;
		insert2(node);
		
	}
	
	static void insert1(Node node) {
		if (root == null || node == root || node.parent == null){
			root = node;
			root.color = false;
			return;
		} else
			insert2(node);
	}
	
	static void insert2(Node node) {
		if(node.parent.color == false)
			return;
		else
			insert3(node);
	}
	
	static void insert3(Node node) {
		Node uncle = getUncle(node);
		if (uncle != null && uncle.color){
			node.parent.color = false;
			node.parent.parent.color = true;
			uncle.color = false;
			node.color = true;
			insert1(node.parent.parent);
		} else
			insert4(node);
	}
	
	static void insert4(Node node) {
		
		Node gparent = getGrandparent(node);
		
		if((node == node.parent.rightChild) && (node.parent == gparent.leftChild)) {
			rotate_left(node.parent);
			node = node.leftChild;
		} else if((node == node.parent.leftChild) && (node.parent == gparent.rightChild)){
			rotate_right(node.parent);
			node = node.rightChild;
		}
		insert5(node);		
	}
	
	static void insert5(Node node) {
		
		Node gparent = getGrandparent(node);
		
		node.parent.color = false;
		gparent.color = true;
		if(node == node.parent.leftChild)
			rotate_right(gparent);
		else
			rotate_left(gparent);
		
	}
	
	static void deleteNode(int nodeKey) {
		
		Node node = findNode(nodeKey);
		
		if(node == null){
			System.out.println("No match found for "+nodeKey+" to delete.");
			return;
		} else {
			System.out.println("Match found for "+nodeKey+" to delete.");
		}
		
		delete1(node);			
		
	}
	
	static void delete1(Node node) {
		if(root == node) {
			if(node.leftChild != null) {
				Node temp = node.leftChild;
				while(temp.rightChild != null)
					temp = temp.rightChild;
				node.Key = temp.Key;
				
				if(!node.leftChild.color && node.leftChild == temp) {
					rotate_left(node);
					node.parent.color = false;
					if(node.parent.rightChild != null)
						node.parent.rightChild.color = false;
					else
						node.color = true;
				}
				removeNode(temp);

			} else if (node.rightChild != null) {
				node.Key = node.rightChild.Key;
				
				if(!node.rightChild.color) {
					rotate_right(node);
					node.parent.color = false;
					if(node.parent.leftChild != null)
						node.parent.leftChild.color = false;
					else
						node.color = true;
				}
				removeNode(node.rightChild);
			} else
				removeNode(node);
				
		} else
			delete2(node);
	}
	
	static void delete2(Node node) {
		if(node.leftChild == null || node.rightChild == null) {
			
			// If node is red both children are null and can be deleted
			if(node.color)
				removeNode(node);	
			else {
				// node is black and has one red child
				if(node.leftChild != null) {
					node.Key = node.leftChild.Key;
					removeNode(node.leftChild);
				} else if(node.rightChild != null) {
					node.Key = node.rightChild.Key;
					removeNode(node.rightChild);
				// if black node has no children 
				} else
					delete3(node);
			}			
			
		} else
			delete3(node);	
	}
	
	static void delete3(Node node) {
		
		// if red must have two blacks
		if(node.color) {
			Node temp = node.leftChild;
			while(temp.rightChild != null)
				temp = temp.rightChild;
			node.Key = temp.Key;
			delete1(temp);
		} else
			delete4(node);
		
	}
	
	static void delete4(Node node) {
		
		Node sibling = getSibling(node);
		
		// if sibling is black and at least one of its children is red
		if(!sibling.color && ((sibling.leftChild != null && sibling.leftChild.color) || (sibling.rightChild != null && sibling.rightChild.color)) ) {
		
			// Sibling is left child and is black with a red left child
			if(node.parent.leftChild == sibling) {

				if(sibling.leftChild == null) {
					sibling.color = true;
					sibling.rightChild.color = false;
					rotate_left(sibling);
				}
				
				rotate_right(node.parent);
				node.parent.parent.color = true;
				node.parent.parent.leftChild.color = false;
				node.parent.parent.rightChild.color = false;
				removeNode(node);
				
			} else if (node.parent.rightChild == sibling) {
				
				if(sibling.rightChild == null) {
					sibling.color = true;
					sibling.leftChild.color = false;
					rotate_right(sibling);
				}
				
				rotate_left(node.parent);
				node.parent.parent.color = true;
				node.parent.parent.leftChild.color = false;
				node.parent.parent.rightChild.color = false;
				removeNode(node);
				
			}
		
		} else
			delete5(node);
		
	}
	
	static void delete5(Node node) {
		
		Node sibling = getSibling(node);
		
		if(!sibling.color) {

			// double red is prevented from delete2
			sibling.color = true;
			removeNode(node);
			
			if(sibling.parent.color)
				sibling.parent.color = false;
			else
				delete1(sibling.parent);
			
		} else
			delete6(node);
		
	}
	
	static void delete6(Node node) {
		
		Node sibling = getSibling(node);
		// sibling must be red and have two black children
		
		sibling.color = false;
		removeNode(node);
		
		if(sibling.parent.rightChild == sibling) {
			 sibling.leftChild.color = true;
			 rotate_left(sibling.parent);
		} else {
			sibling.rightChild.color = true;
			 rotate_right(sibling.parent);
		}
		
	}
	
	static void removeNode(Node node) {
		if(node == root)
			root = null; 
		//else if(node == node.parent.leftChild) {
		else {
			
			if(node.leftChild != null) {
				Node temp = node.leftChild;
				while(temp.rightChild != null)
					temp = temp.rightChild;
				node.Key = temp.Key;
				
				if(temp != node.leftChild && temp.leftChild != null) {
					temp.parent.rightChild = temp.leftChild;
					temp.leftChild.parent = temp.parent;
				} else if(temp != node.leftChild) {
					temp.parent.rightChild = null;
				} else {
					node.leftChild = temp.leftChild;
					if(temp.leftChild != null)
						temp.leftChild.parent = node;
				}
				
				temp = null;
				
			} else if (node.rightChild != null) {
				node.Key = node.rightChild.Key;
				node.rightChild = null;
			} else {
				if(node == node.parent.leftChild)
					node.parent.leftChild = null;
				else
					node.parent.rightChild = null;
				node = null;
			}
			
		}	
	}
	
	
	static void rotate_left(Node node){
		
		if(node.parent == null)
			root = node.rightChild;
		else if(node == node.parent.leftChild)
			node.parent.leftChild = node.rightChild;
		else
			node.parent.rightChild = node.rightChild;
		
		node.rightChild.parent = node.parent;
		node.parent = node.rightChild;
		node.rightChild = node.parent.leftChild;
		if(node.rightChild != null)
			node.rightChild.parent = node;
		node.parent.leftChild = node;
		
	}
	
	
	static void rotate_right(Node node) {
	
		if(node.parent == null)
			root = node.leftChild;
		else if(node == node.parent.leftChild)
			node.parent.leftChild = node.leftChild;
		else
			node.parent.rightChild = node.leftChild;
		
		node.leftChild.parent = node.parent;
		node.parent = node.leftChild;
		node.leftChild = node.parent.rightChild;
		if(node.leftChild != null)
			node.leftChild.parent = node;
		node.parent.rightChild = node;
		
	}
	
	
	static Node findNode(int nodeKey){

		Node node = root;
		
		while(node != null) {
			if(node.Key == nodeKey)
				return node;
			
			if(nodeKey > node.Key)
				node = node.rightChild;
			else
				node = node.leftChild;
		}
		
		return node;
	}
	
	
	static void printBinaryTree(Node node, int level){
		
		// function taken from user http://stackoverflow.com/users/2438880/anurag-agarwal
		if(root == null)
			System.out.println("Tree is empty.");
	    if(node == null)
	         return;
//	    try {
//	        Thread.sleep(100);
//	    } catch(InterruptedException ex) {
//	        Thread.currentThread().interrupt();
//	    }
	    printBinaryTree(node.rightChild, level+1);
	    
	    if(level!=0){
	    	
	    	String color;
	    	if(node.color)
	    		color = "Red";
	    	else
	    		color = "Black";
	    	
	        for(int i=0;i<level-1;i++)
	            System.out.print("|\t");
	            System.out.println("|-------"+node.Key + " : " + color);
	    }
	    else
	        System.out.println(node.Key + " : Black");
	    
	    printBinaryTree(node.leftChild, level+1);
	} 
	
	static void deleteTree(int[] nodes) {
		for(int x = 0; x < nodes.length; x++){
			
			Random rand = new Random();
			int y;
			if(x > 0)
				y = rand.nextInt(x);
			else
				y = 0;
			
			deleteNode(nodes[y]);
			printBinaryTree(root, 0);
			System.out.println("\n");
		}
	}
	
	static Node getSibling(Node node){
		
		if(node == null || node.parent == null){
			return null;
		}
		if(node == node.parent.leftChild)
			return node.parent.rightChild;
		else
			return node.parent.leftChild;
		
	}
	
	static Node getGrandparent(Node node){
		
		if(node.parent != null && node.parent.parent != null)
			return node.parent.parent;
		else 
			return null;
		
	}
	
	
	static Node getUncle(Node node){
		
		Node grandparent = getGrandparent(node);

		if(grandparent == null)
			return null;
		
		if(node.parent == grandparent.rightChild)
			return grandparent.leftChild;
		else
			return grandparent.rightChild;
			
	}
	
	
	static int[] genRandomNodes(int num){
		
		int[] nodeList = new int[num];
		
		for(int x = 0; x < nodeList.length; x++){
			Random rand = new Random();
			int y = rand.nextInt(num*num);
			nodeList[x] = y;
		}
		
		return nodeList;
		
	}
	
	public static void main(String[] args) {
				
		int[] nodes = {2,10,1,4,5,9,3,6,7};
		//int[] nodes = genRandomNodes(5000);
		
		for(int x = 0; x < nodes.length; x++){
			System.out.println(x + " - Added Node: " + nodes[x]);
			addNode(nodes[x]);
		}
		
		System.out.println("\n--INSERTIONS DONE--\n");
		printBinaryTree(root, 0);
		System.out.println("\n--PRINT DONE--\n");
		
//		deleteTree(nodes);
		
//		deleteNode(10);
//		deleteNode(2);

		printBinaryTree(root, 0);
	}
}
