package edu.yu.cs.ds.finalproject;

public class Row {
	public DataEntry[] rowEntries;
	
	public Row(int numOfColumns) {
		this.rowEntries = new DataEntry[numOfColumns];
	}
}
