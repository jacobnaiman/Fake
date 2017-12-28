package edu.yu.cs.dataStructures.fall2016.SimpleSQLParser;

public class Row {
	private Object[] row;
	
	public Row(int entrySize) {
		row = new Object[entrySize];
	}
	
	/*
	 * Adds a piece of data to the entry at index i
	 */
	public void addToRow(Object type, int i) { 
		row[i] = type;
	}
	
	/*
	 *@param index i
	 *@return Object at position i
	 */
	public Object get(int i) {
		return row[i];
	}
	
	/*
	 *@return length of row
	 */
	public int rowSize() {
		return row.length;
	}
}