package edu.yu.cs.ds.finalproject;

import java.awt.RenderingHints.Key;
import java.util.ArrayList;

import javax.management.Query;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition.Operator;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.DeleteQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SQLQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.UpdateQuery;

public class BTree<Key extends Comparable<Key>, Value> {
	public Node root;
	public int height;
	public int n;
	public final int M = 4;
	public Column column;
	/**
	 * Constructs a btree
	 * @param column
	 */
	public BTree(Column column) {
		this.root = new Node(0);
		this.column = column;
	}
	
	/**
	 * deletes all values from the btree when a given key is put in
	 * @param value
	 */
    public void delete(Key value) {
    		this.put(value, null, true);
    }
    
    
	/**
	 * public get method which takes the key as input and returns the value
	 * @param key
	 * @return an arraylist of rows
	 */
	public ArrayList<Row> get(Key key) { 
		return this.get(this.root, key, this.height);
	}
	
	/**
	 * private recursive get method which goes through the btree and finds the value based on the key
	 * @param currentNode
	 * @param key
	 * @param height
	 * @return the value
	 */
	private ArrayList<Row> get(Node currentNode, Key key, int height) {
		Entry[] entries = currentNode.entries;
		   //current node is external (i.e. height == 0)  
		if (height == 0) {
			 //if(operator.equals(Condition.Operator.EQUALS)) {
				 for (int j = 0; j < currentNode.numberOfChildren; j++) {
					   if(isEqual(key, entries[j].key)) {
						   //found desired key. Return its value
						   return currentNode.entries[j].val;
					   }
				   }
		     //didn't find the key
		     return null;
		   }
		 //current node is internal (height > 0)
		   else {
		      for (int j = 0; j < currentNode.numberOfChildren; j++) {
		         //if (we are at the last key in this node OR the key we
		         //are looking for is less than the next key, i.e. the
		         //desired key must be in the subtree below the current entry),
		         //then recurse into the current entry’s child
		    	  	if (j + 1 == currentNode.numberOfChildren || less(key, entries[j + 1].key)) {
		    	  		return this.get(entries[j].child, key, height - 1); 
		    	  		}
		      } 
		   }
		return null;
	}
	
	/**
	 * gets all entries less than a given key
	 * @param key
	 * @return value of a list of rows
	 */
	public ArrayList<Row> getLess(Key key) {   
		return this.getLess(this.root, key, this.height);
	}
	
	/**
	 * private recursive get method which goes through the btree and finds the value based on the key
	 * @param currentNode
	 * @param key
	 * @param height
	 * @return the value
	 */
	private ArrayList<Row> getLess(Node currentNode, Key key, int height) {
		Entry[] entries = currentNode.entries;
		ArrayList<Row> returnedRows = new ArrayList<Row>();  
		if (height == 0) {
			for (int j = 0; j < currentNode.numberOfChildren; j++) {
				if(isGreater(key, entries[j].key)) {
					returnedRows = this.merge(returnedRows, currentNode.entries[j].val);
				}
				else if(isEqual(key, entries[j].key)) {
					break;
				}
			}
			return returnedRows;
		}
		else {
			for (int j = 0; j < currentNode.numberOfChildren; j++) {
				if (isGreater(key, entries[j].key) || j + 1 == currentNode.numberOfChildren) {
					returnedRows = this.merge(returnedRows, this.getLess(entries[j].child, key, height - 1));
				}
			} 
		}
		return returnedRows;
	}
	
	/**
	 * gets all entries less than or equal to a given key
	 * @param key
	 * @return value of a list of rows
	 */
	public ArrayList<Row> getLessOrEqual(Key key) {   
		return this.getLessOrEqual(this.root, key, this.height);
	}
	
	/**
	 * private recursive get method which goes through the btree and finds the value based on the key
	 * @param currentNode
	 * @param key
	 * @param height
	 * @return the value
	 */
	private ArrayList<Row> getLessOrEqual(Node currentNode, Key key, int height) {
		Entry[] entries = currentNode.entries;
		ArrayList<Row> returnedRows = new ArrayList<Row>();  
		if (height == 0) {
			for (int j = 0; j < currentNode.numberOfChildren; j++) {
				if(isGreater(key, entries[j].key) || isEqual(key, entries[j].key)) {
					returnedRows = this.merge(returnedRows, currentNode.entries[j].val);
				}
				else if(less(key, entries[j].key)) {
					break;
				}
			}
			return returnedRows;
		}
		else {
			for (int j = 0; j < currentNode.numberOfChildren; j++) {
				if (isGreater(key, entries[j].key) || isEqual(key, entries[j].key) || j + 1 == currentNode.numberOfChildren) {
					returnedRows = this.merge(returnedRows, this.getLessOrEqual(entries[j].child, key, height - 1));
				}
			} 
		}
		return returnedRows;
	}
	
	/**
	 * gets all entries greater than or equal to a given key
	 * @param key
	 * @return value of a list of rows
	 */
	public ArrayList<Row> getGreaterOrEqual(Key key) {   
		return this.getGreaterOrEqual(this.root, key, this.height);
	}
	
	/**
	 * private recursive get method which goes through the btree and finds the value based on the key
	 * @param currentNode
	 * @param key
	 * @param height
	 * @return the value
	 */
	private ArrayList<Row> getGreaterOrEqual(Node currentNode, Key key, int height) {
		Entry[] entries = currentNode.entries;
		ArrayList<Row> returnedRows = new ArrayList<Row>();  
		if (height == 0) {
			for (int j = currentNode.numberOfChildren - 1; j >= 0; j--) {
				if(less(key, entries[j].key) || isEqual(key, entries[j].key)) {
					returnedRows = this.merge(returnedRows, currentNode.entries[j].val);
				}
				else if(isGreater(key, entries[j].key)) {
					break;
				}
			}
			return returnedRows;
		}
		else {
			for (int j = currentNode.numberOfChildren - 1; j >= 0; j--) {
				if(j == currentNode.numberOfChildren - 1) {
					if (less(key, entries[j].key) || isEqual(key, entries[j].key) || j + 1 == currentNode.numberOfChildren) {
						returnedRows = this.merge(returnedRows, this.getGreaterOrEqual(entries[j].child, key, height - 1));
					}
				}
				else {
					if (less(key, entries[j].key) || isEqual(key, entries[j].key) || (isGreater(key, entries[j].key) && less(key, entries[j+1].key))) {
						returnedRows = this.merge(returnedRows, this.getGreaterOrEqual(entries[j].child, key, height - 1));
					}
				}
			} 
		}
		return returnedRows;
	}
	
	/**
	 * gets all entries greater than a given key
	 * @param key
	 * @return value of a list of rows
	 */
	public ArrayList<Row> getGreater(Key key) {   
		return this.getGreater(this.root, key, this.height);
	}
	
	/**
	 * private recursive get method which goes through the btree and finds the value based on the key
	 * @param currentNode
	 * @param key
	 * @param height
	 * @return the value
	 */
	private ArrayList<Row> getGreater(Node currentNode, Key key, int height) {
		Entry[] entries = currentNode.entries;
		ArrayList<Row> returnedRows = new ArrayList<Row>();  
		if (height == 0) {
			for (int j = currentNode.numberOfChildren - 1; j >= 0; j--) {
				if(less(key, entries[j].key)) {
					returnedRows = this.merge(returnedRows, currentNode.entries[j].val);
				}
				else if(isEqual(key, entries[j].key)) {
					break;
				}
			}
			return returnedRows;
		}
		else {
			for (int j = currentNode.numberOfChildren - 1; j >= 0; j--) {
				if(j == currentNode.numberOfChildren - 1) {
					if (less(key, entries[j].key) || isEqual(key, entries[j].key) || j + 1 == currentNode.numberOfChildren) {
						returnedRows = this.merge(returnedRows, this.getGreater(entries[j].child, key, height - 1));
					}
				}
				else {
					if (less(key, entries[j].key) || isEqual(key, entries[j].key) || (isGreater(key, entries[j].key) && less(key, entries[j+1].key))) {
						returnedRows = this.merge(returnedRows, this.getGreater(entries[j].child, key, height - 1));
					}
				}
			} 
		}
		return returnedRows;
	}
    
	/**
	 * merges the values of two arraylists of rows into one
	 * @param returnedRows
	 * @param val
	 * @return
	 */
	private ArrayList<Row> merge(ArrayList<Row> returnedRows, ArrayList<Row> val) {
		if(val == null) {
			return returnedRows;
		}
		for(int i = 0; i < val.size(); i++) {
			
			returnedRows.add(val.get(i));
		}
		for(int i = returnedRows.size() - 1; i >= 0; i--) {
			if(returnedRows.get(i) == null) {
				returnedRows.remove(i);
			}
			
		}
		return returnedRows;
	}
	
	/**
	 * returns true if the first key is greater than the second
	 * @param key
	 * @param key2
	 * @return boolean
	 */
	private boolean isGreater(Key key, Comparable key2) {
		return key.compareTo((Key) key2) > 0;
	}
	
	/**
	 * returns true if the first key is equal to the second
	 * @param key
	 * @param key2
	 * @return boolean
	 */
	public boolean isEqual(Key key, Comparable key2) {
		return key.compareTo((Key) key2) == 0;
	}
	
	/**
	 * returns true if the first key is less than the second
	 * @param key
	 * @param key2
	 * @return boolean
	 */
	public boolean less(Key key, Comparable key2) {
		return key.compareTo((Key) key2) < 0;
	}
	
	
	
	/**
	 * Takes a key and value and inserts them into the btree
	 * @param key
	 * @param val
	 * @param isDelete
	 */
	public void put(Key key, Row val, boolean isDelete) {
		Node newNode = this.put(this.root, key, val, this.height, isDelete);
		this.n++;
		if(newNode == null) {
			return; 
		}
		//split the root:
		//Create a new node to be the root.
		//Set the old root to be new root's first entry.
		//Set the node returned from the call to put to be new root's second entry 
		Node newRoot = new Node(2);
		newRoot.entries[0] = new Entry(this.root.entries[0].key, null, this.root); 
		newRoot.entries[1] = new Entry(newNode.entries[0].key, null, newNode); 
		this.root = newRoot;
		//a split at the root always increases the tree height by 1 
		this.height++;
	}
	
	/**
	 * recurses through the btree and places the key value pair in the proper place
	 * @param currentNode
	 * @param key
	 * @param val
	 * @param height
	 * @param isDelete
	 * @return
	 */
	private Node put(Node currentNode, Key key, Row val, int height, boolean isDelete) {
		int j;
		Entry entry = new Entry(key, val, null);
		Entry newEntry = entry; //external node
		if (height == 0) {
		//find index in currentNode’s entry[] to insert new entry
			for (j = 0; j < currentNode.numberOfChildren; j++) {
		    	  	if (less(key, currentNode.entries[j].key)) {
		    	  		break;
		    	  	}
		    	  	else if(isEqual(key, currentNode.entries[j].key)) {
		    	  		if(isDelete) {
		    	  			currentNode.entries[j].val = null;
		    	  		}
		    	  		else {
		    	  			try {
		    	  				currentNode.entries[j].val.add(val);
		    	  				return null;
		    	  			}
		    	  			catch(NullPointerException e) {
		    	  				currentNode.entries[j].val = new ArrayList<Row>();
		    	  				currentNode.entries[j].val.add(val);
		    	  				return null;
		    	  			}
		    	  			
		    	  		}
		    	  	}
			}
		}
		else { 
			for (j = 0; j < currentNode.numberOfChildren; j++) {
			//if (we are at the last key in this node OR the key we
			//are looking for is less than the next key, i.e. the
			//desired key must be added to the subtree below the current entry),
			//then do a recursive call to put on the current entry’s child
				if ((j + 1 == currentNode.numberOfChildren) || less(key, currentNode.entries[j+1].key)) {
					//increment j (j++) after the call so that a new entry created by a split 
					//will be inserted in the next slot
					Node newNode = this.put(currentNode.entries[j++].child, key, val, height - 1, isDelete);
			    		if (newNode == null) {
			    			return null;
			    		}
			    		//if the call to put returned a node, it means I need to add a new entry to 
			    		//the current node
			    		newEntry.key = newNode.entries[0].key;
			    		newEntry.child = newNode;
			    		break; 
				}
			}
		}
		return this.putValuesIn(j, currentNode, newEntry);
	}
	
	/**
	 * puts the entry into the place in the btree where it belongs
	 * @param j
	 * @param currentNode
	 * @param newEntry
	 * @return
	 */
	private Node putValuesIn(int j, Node currentNode, Entry newEntry) {
		for (int i = currentNode.numberOfChildren; i > j; i--) {
			currentNode.entries[i] = currentNode.entries[i - 1];
		}
			currentNode.entries[j] = newEntry;
			currentNode.numberOfChildren++;
		if (currentNode.numberOfChildren < this.M) {
		     //no structural changes needed in the tree
		     //so just return null
		     return null;
		}
		else {
			return this.split(currentNode);
		} 
		//will have to create new entry in the parent due 
		//to the split, so return the new node, which is 
		//the node for which the new entry will be created
	}
	
	/**
	 * splits the node into 2 nodes if maximum capacity is reached
	 * @param currentNode
	 * @return
	 */
	private Node split(Node currentNode) {
		Node newNode = new Node(this.M / 2);
		//by changing currentNode.entryCount, we will treat any value 
		//at index higher than the new currentNode.entryCount as if 
		//it doesn't exist
		currentNode.numberOfChildren = this.M / 2;
		//copy top half of currentNode into newNode
		for (int j = 0; j < this.M / 2; j++) {
			newNode.entries[j] = currentNode.entries[this.M / 2 + j]; 
		}
		return newNode;
	}
	
	/**
	 * keeps the btree updated when there is a delete query run
	 * @param rows
	 * @param query
	 * @param conditionColumns
	 */
	public void delete(ArrayList<Row> rows, SQLQuery query, ArrayList<String> conditionColumns) {
		if(rows.size() == 0) {
			return;
		}
		int btreeIndex = rows.get(0).findColumnIndex(this.column.cd.getColumnName());//index of the column of this btree
		for(String conditionColumn : conditionColumns) {
			int index = rows.get(0).findColumnIndex(conditionColumn);//the index of the column with the condition
			if(this.column.cd.getColumnName().equals(conditionColumn)) {
				for(Row row : rows) {
					this.delete((Key) row.rowEntries[index].value);
				}
			}
			else {
				for(Row row : rows) {
					if(this.get((Key) row.rowEntries[btreeIndex].value) == null) {
						continue;
					}
					for(int i = this.get((Key) row.rowEntries[btreeIndex].value).size() - 1; i >= 0; i--) {
						if(this.get((Key) row.rowEntries[btreeIndex].value).get(i).compare(row)) {
							this.get((Key) row.rowEntries[btreeIndex].value).remove(i);
						}
					}
				}
			}
		}
	}
	
	/**
	 * keeps the btree updated when there is an update query run
	 * @param rows
	 * @param conditionColumns
	 * @param query
	 */
	public void update(ArrayList<Row> rows, ArrayList<String> conditionColumns, UpdateQuery query) {
		if(rows.size() == 0) {
			return;
		}
		int btreeIndex = rows.get(0).findColumnIndex(this.column.cd.getColumnName());//index of the column of this btree
		for(String conditionColumn : conditionColumns) {
			int index = rows.get(0).findColumnIndex(conditionColumn);//the index of the column with the condition
			if(this.column.cd.getColumnName().equals(conditionColumn)) {
				for(Row row : rows) {
					row = this.column.table.putInValues(((UpdateQuery) query).getColumnValuePairs(), row);
				}
			}
		}
	}

	/**
	 * removes all values from the btree when all rows are deleted
	 */
	public void clear(ArrayList<Row> rows) {
		if(rows.size() == 0) {
			return;
		}
		int index = rows.get(0).findColumnIndex(this.column.cd.getColumnName());
		for(Row row : rows) {
			this.delete((Key) row.rowEntries[index].value);
		}
	}
	
	/**
	 * updates all rows in a btree when all need to be updated
	 * @param UQ
	 */
	public void updateAll(UpdateQuery UQ) {
		ArrayList<Row> allRows = this.getGreaterOrEqual((Key) this.root.entries[0].key);
		for(Row row : allRows) {
			row = this.column.table.putInValues(UQ.getColumnValuePairs(), row);
		}
	}
}
  

