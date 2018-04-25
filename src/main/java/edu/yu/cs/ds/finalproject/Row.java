package edu.yu.cs.ds.finalproject;

import java.util.ArrayList;
import java.util.Objects;

public class Row {
	public DataEntry[] rowEntries;
	public String selectString;
	public String orderByString;
	
	public Row(int numOfColumns) {
		this.rowEntries = new DataEntry[numOfColumns];
		this.orderByString = "";
	}
	
	public ArrayList<Integer> findColumnIndeces(ArrayList<SelectColumn> columns) {
		ArrayList<Integer> indeces = new ArrayList<Integer>();
		for(SelectColumn SC : columns) {
			for(int i = 0; i < this.rowEntries.length; i++) {
				if(this.rowEntries[i].columnName.equals(SC.getColumnName())) {
					indeces.add(i);
				}
			}
		}
		return indeces;
	}
	
	public int findColumnIndex(String name) {
		int index = -1;
		for(int i = 0; i < this.rowEntries.length; i++) {
			if(this.rowEntries[i].columnName.equals(name)) {
				return i;
				
			}
		}
		return index;
	}
	
	public void setSelectString(ArrayList<Integer> indeces) {
		String value = "";
		for(Integer i : indeces) {
			value = value + this.rowEntries[i].value;
		}
		this.selectString = value;
	}
	
	public boolean equals(Object o, int index) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (getClass() != o.getClass()) {
			return false;
		}
		Row otherRow= (Row) o;
		return Objects.equals(rowEntries, otherRow.rowEntries);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (getClass() != o.getClass()) {
			return false;
		}
		Row otherRow= (Row) o;
		return Objects.equals(selectString, otherRow.selectString);
	}
}
