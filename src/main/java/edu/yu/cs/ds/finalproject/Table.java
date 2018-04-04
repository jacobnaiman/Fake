package edu.yu.cs.ds.finalproject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnID;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnValuePair;

public class Table {
	public List<Row> rows;
	public ColumnDescription[] cds;
	public String tableName;
	public ColumnDescription primaryKey;
	
	public Table(String name, ColumnDescription[] cds, ColumnDescription pK) {
		this.rows = new ArrayList<Row>();
		this.cds = cds;
		this.tableName = name;
		this.primaryKey = pK;
	}
	
	public Row addRow() {
		Row row = new Row(this.cds.length);
		this.rows.add(row);
		return row;
	}
	
	public Row putInValues(ColumnValuePair[] colVals, Row row) {
		int counter = 0;
		this.duplicateCheck(colVals);
		//iterate through all the columns
		for (int j = 0; j < this.cds.length; j++) {
			//adding the actual data from the colvalpairs
			for(ColumnValuePair CVP : colVals) {
				//linking the colvalpair to its column
				if (CVP.getColumnID().getColumnName().equals(this.cds[j].getColumnName())) {
					DataEntry dataEntry = new DataEntry(CVP);
					dataEntry.performValueChecks(this.cds[j]);//datatype and length
					//checks if it is a unique column or primary key which is unique
					if(this.cds[j].isUnique() || this.cds[j].equals(this.primaryKey)) {
						this.uniquenessCheck(dataEntry, j);
					}
					row.rowEntries[j] = dataEntry;
					counter++;
				}
			}
			//checks if it is a not null column or the primary key which is also not null
			try {
			if((this.cds[j].isNotNull() || this.cds[j].equals(this.primaryKey)) && row.rowEntries[j].value.equals(null)) {
				throw new IllegalArgumentException("You did not put a value into a nonnull row");
			}} 
			catch (NullPointerException e) {
				throw new IllegalArgumentException("You did not put a value into a nonnull row");
			}
		}
		if (colVals.length != counter) {
			throw new IllegalArgumentException("You tried to insert something into a column which does not exist");
		}
		return row;
	}
	
	public void primaryKeyChecks() {
		for (ColumnDescription cd : this.cds) {
			if (cd.equals(this.primaryKey)) {
				if (cd.getHasDefault()) {
					throw new IllegalArgumentException("Primary key cannot have a default value");
				}
			}
		}
	}
	
	public void uniquenessCheck(DataEntry entry, int j) {
		for (Row row : this.rows) {
			try {
				if (row.rowEntries[j].value.equals(entry.value)) {
					//if goes through then its not null and throws the exception
					throw new IllegalArgumentException("this column requires unique input");
				}
			}
			catch (NullPointerException e) {
				continue;
			}
		}
	}
	
	public List<ColumnDescription> getSelectColumns(ColumnID[] columnIDs) {
		List<ColumnDescription> selectedColumns = new ArrayList<ColumnDescription>();
		for (ColumnDescription cd : this.cds) {
			for (ColumnID selectColumn : columnIDs) {
				if (cd.getColumnName().equals(selectColumn.getColumnName())) {
					selectedColumns.add(cd);
				}
			}
		}
		return selectedColumns;
	}
	
	private void duplicateCheck(ColumnValuePair[] CVPs) {
		Set<String> tempSet = new HashSet<String>();
		List<String> tempList = new ArrayList<String>();
		for (ColumnValuePair CVP : CVPs) {
			tempSet.add(CVP.getColumnID().getColumnName());
			tempList.add(CVP.getColumnID().getColumnName());
		}
		if (tempSet.size() != tempList.size()) {
			throw new IllegalArgumentException("You tried to enter into the same column twice");
		}
	}
	
	public Integer findIndex(Row row) {
		int index = 0;
		int size = this.rows.size();
		for(int i = 0; i < size; i++) {
			if(this.rows.get(i).equals(row)) {
				index = i;
			}
		}
		return index;
	}
}
