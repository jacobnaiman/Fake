package edu.yu.cs.dataStructures.fall2016.SimpleSQLParser;

import java.util.ArrayList;
//putting in a null value throws an exception, should it throw one?
public class BTree<Key extends Comparable<Key>, Value>  { // max children per B-tree node = M-1
	private static int MAX = 4; // (must be even and greater than 2) final? or not final?
	private Node root;
	private String name;
	private int height;
	private int n;  // number of total key-value pairs in the B-tree
	
    private static final class Node {
        private int M;                             // number of children
        private Entry[] children = new Entry[MAX];   // the array of children

        // create a node with k children
        private Node(int children) {
            M = children;
        }
    }

	private static class Entry {
	    @SuppressWarnings("rawtypes")
		private Comparable key;
	    private final Object val;
	    private Node next;
	    @SuppressWarnings("rawtypes")
		public Entry(Comparable key, Object val, Node next) {
	        this.key  = key;
	        this.val  = val;
	        this.next = next;
	    }
	}
    /*
     * initializes BTree of certain order
     */
	public BTree(String callMe) {
		root = new Node(0);
		name = callMe;
	}
	
	public String getBTreeName() {
		return this.name;
	}
	
	public Value get(Key key) {
		return this.get(this.root, key, this.height);
	}
	
	public void put(Key key, Value val) {
		Node newNode = this.put(this.root, key, val, this.height); 
		this.n++; //number of key value pairs in the whole BTree
		if(newNode == null) {
			return; 
		}
	//split the root:
	//Set the old root to be new root's first entry.
	//Set the node returned from the call to put to be new root's second entry 
	Node newRoot = new Node(2); //Create a new node to be the root.
	newRoot.children[0] = new Entry(this.root.children[0].key, null, this.root); 
	newRoot.children[1] = new Entry(newNode.children[0].key, null, newNode); 
	this.root = newRoot;
	//a split at the root always increases the tree height by 1 
	this.height++;
	}
		
	private Value get(Node currentNode, Key key, int height) {
		if (key == null) throw new IllegalArgumentException("argument to get() is null");
		Entry[] children = currentNode.children;
		   if (height == 0) {  //if current node is external
		     for (int j = 0; j < currentNode.M; j++) {
		    	 	if(isEqual(key, children[j].key)) { 												
		          //found desired key. Return its value
		          return (Value)children[j].val;
		    	 	}
		     }
		     //didn't find the key
		     return null;
		  }
		   else { 	//internal (height > 0)
		      for (int j = 0; j < currentNode.M; j++) {
		         //if (we are at the last key in this node OR the key we are looking for is less than the next key, i.e. the desired key must be in the subtree below the current entry), then recurse into the current entry’s child
		    	  	if (j + 1 == currentNode.M || less(key, children[j + 1].key)) {
		    	  		return this.get(children[j].next, key, height - 1); 
		    	  	}
		      }
		      	return null;
		   }
	}
	/********************
	 * GET GREATER THAN
	 *******************/
	public Value getGreaterThan(Key key) {
		return this.getGreaterThan(this.root, key, this.height);
	}
	
	private Value getGreaterThan(Node currentNode, Key key, int height) {
		if (key == null) throw new IllegalArgumentException("argument to get() is null");
		Entry[] children = currentNode.children;
		   if (height == 0) {  //if current node is external
		     for (int j = 0; j < currentNode.M; j++) {
		    	 	if(greaterThan(key, children[j].key)) { 												
		          //found desired key. Return its value
		          return (Value)children[j].val;
		    	 	}
		     }
		     //didn't find the key
		     return null;
		  }
		   else { 	//internal (height > 0)
		      for (int j = 0; j < currentNode.M; j++) {
		         //if (we are at the last key in this node OR the key we are looking for is less than the next key, i.e. the desired key must be in the subtree below the current entry), then recurse into the current entry’s child
		    	  	if (j + 1 == currentNode.M || less(key, children[j + 1].key)) {
		    	  		return this.get(children[j].next, key, height - 1); 
		    	  	}
		      }
		      	return null;
		   }
	}

	private Node put(Node currentNode, Key key, Value val, int height) {
        if (key == null) throw new IllegalArgumentException("argument key to put() is null"); 
		int j;
		Entry newEntry = new Entry(key, val, null);
		if (height == 0) { //external node
		//find index in currentNode’s entry[] to insert new entry
		      for (j = 0; j < currentNode.M; j++) {
		    	  	if (less(key, currentNode.children[j].key)){ 
		    	  		break; 
		    	  	}
		      } 
		}
		else { //internal node
			//find index in node entry array to insert the new entry
			for (j = 0; j < currentNode.M; j++) {
			//if we are at the last key in this node OR the key we are looking for is less than the next key, 
			//i.e. the desired key must be added to the subtree below the current entry),
			//then do a recursive call to put on the current entry’s child
				if ((j + 1 == currentNode.M) || less(key, currentNode.children[j + 1].key)) {  // if the key we are looking for is less than the next key
					//increment j (j++) after the call so that a new entry created by a split 
					//will be inserted in the next slot				
					Node newNode = this.put(currentNode.children[j++].next, key, val, height - 1);
						if (newNode == null) {
							return null;
						}
			//if the call to put returned a node, it means I need to add a new entry to the current node
					newEntry.key = newNode.children[0].key;
			    		newEntry.next = newNode;
			    		break; 
				}
			}
		}
			   for (int i = currentNode.M; i > j; i--) { 
				   //shift entries over one place to make room for new entry
				   currentNode.children[i] = currentNode.children[i - 1]; //moves the entries to the right 
			   }
			   currentNode.children[j] = newEntry;  //add the new entry
			   currentNode.M++;
			   if (currentNode.M < BTree.MAX) {  //no structural changes/splits needed in the tree, bc the amount of entries is less than the ORDER
			     //so just return null
			     return null;
			   }
			   else {
				//will have to create new entry in the parent due to the split, so return the new node, which is 
				//the node for which the new entry will be created 
				   return this.split(currentNode);
			   } 
	}
	
	/*************************
	 * puts a null value in for key
	 * */
	public void delete(Key key) {
		Node newNode = this.delete(this.root, key, null, this.height); 
		if(newNode == null) {
			return; 
		}
	}
	
	
	private Node delete(Node currentNode, Key key, Value val, int height) {
        if (key == null) throw new IllegalArgumentException("argument key to put() is null"); 
        int j;
        Entry newEntry = new Entry(key, null, null);
		if (height == 0) { //external node
		//find index in currentNode’s entry[] to insert new entry
		      for (j = 0; j < currentNode.M; j++) {
		    	  	if (isEqual(key, currentNode.children[j].key)){ 
		    	  		break;  //this is the j that i want to do something at now
		    	  	}
		      } 
		}
		else { //internal node
			//find index in node entry array to insert the new entry
			for (j = 0; j < currentNode.M; j++) {
			//if we are at the last key in this node OR the key we are looking for is less than the next key, 
			//i.e. the desired key must be added to the subtree below the current entry),
			//then do a recursive call to put on the current entry’s child
				if ((j + 1 == currentNode.M) || less(key, currentNode.children[j + 1].key)) {  // if the key we are looking for is less than the next key
					//increment j (j++) after the call so that a new entry created by a split 
					//will be inserted in the next slot				
					Node newNode = this.delete(currentNode.children[j++].next, key, val, height - 1);
						if (newNode == null) {
							return null;
						}
			//if the call to put returned a node, it means I need to add a new entry to the current node
					newEntry.key = newNode.children[0].key;
			    		newEntry.next = newNode;
			    		break; 
				}
			}
		}
			   
			   currentNode.children[j] = newEntry;  //add the new (null) entry
			     return null;

	}
	
		private Node split(Node currentNode) { // split node in half
			Node newNode = new Node(BTree.MAX / 2);
			//by changing currentNode.entryCount, we will treat any value 
			//at index higher than the new currentNode.entryCount as if it doesn't exist
			currentNode.M = BTree.MAX / 2;
			//now copy top half of h into t
			for (int j = 0; j < BTree.MAX / 2; j++) {
					newNode.children[j] = currentNode.children[BTree.MAX / 2 + j]; 
					currentNode.children[BTree.MAX / 2 + j] = null;     			//MYCHANGE
			}
				return newNode;
		}
		
		
	    // comparison functions - make Comparable instead of Key to avoid casts
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private boolean less(Comparable k1, Comparable k2) {
	        return k1.compareTo(k2) < 0;
	    }
	    @SuppressWarnings({ "rawtypes", "unchecked" })
		private boolean isEqual(Comparable k1, Comparable k2) {
	        return k1.compareTo(k2) == 0;
	    }
	    // comparison functions - make Comparable instead of Key to avoid casts
	  	@SuppressWarnings({ "unchecked", "rawtypes" })
	  	private boolean greaterThan(Comparable k1, Comparable k2) {
	       return k1.compareTo(k2) > 0;
	    }	    
	    
	    
	    /*
	     * returns the arrayList of rows for the key that is equal to the given value in paramters
	     * puts in the key of 4.00 (GPA) and then returns anything that the key is equal to, less than etc
	     */
	    public ArrayList<Row> getEquals(Object keyType) {
	    		String key = keyType.toString();
	    		if(Table.isInteger(key)) {
	    			Integer integer = Integer.valueOf(key);
	    			return (ArrayList<Row>) get((Key) integer);
	    		}
	    		else if(Table.isDouble(key)){
	    			Double dbl = Double.valueOf(key);
	    			return (ArrayList<Row>) get((Key) dbl);
	    		}
	    		else if(Table.isBoolean(key)) {
	    			Boolean bool = Boolean.valueOf(key);
	    			return (ArrayList<Row>) get((Key) bool);
	    		}
	    		else {
	    			return (ArrayList<Row>) get((Key) key);
	    		}
	    }
	     
	    public ArrayList<Row> getNotEquals(Object keyType) {
	    		String key = keyType.toString();
	    		if(Table.isInteger(key)) {
	    			Integer integer = Integer.valueOf(key);
	    			return (ArrayList<Row>) get((Key) integer);
	    		}
	    		else if(Table.isDouble(key)){
	    			Double dbl = Double.valueOf(key);
	    			return (ArrayList<Row>) get((Key) dbl);
	    		}
	    		else if(Table.isBoolean(key)) {
	    			Boolean bool = Boolean.valueOf(key);
	    			return (ArrayList<Row>) get((Key) bool);
	    		}
	    		else { //its a string
	    			return (ArrayList<Row>) get((Key) key);
	    		} 	
	    }

	    public static void main(String[] args) {
	    BTree<Integer, Integer> yo = new BTree<Integer, Integer>("hey");
	    yo.put(1, 2);
	    yo.put(2, 4);
	    yo.put(4, 8);
	    yo.put(8, 16);
	    System.out.println(yo.get(4));
	    yo.delete(4);
	    System.out.println(yo.get(4));
	    
	    }
	    

	}
