package edu.yu.cs.ds.finalproject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnID;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnValuePair;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery;

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
	
	public Table(String name, ColumnDescription[] cds, ArrayList<Row> rows) {
		this.rows = new ArrayList<Row>();
		this.cds = cds;
		this.tableName = name;
		for(Row row : rows) {
			this.rows.add(row);
		}
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
	
	public ArrayList<ColumnDescription> getSelectColumns(ColumnID[] columnIDs) {
		ArrayList<ColumnDescription> selectedColumns = new ArrayList<ColumnDescription>();
		int i = 0;
		for (ColumnID selectColumn : columnIDs) {
			System.out.println("times");
			for (ColumnDescription cd : this.cds) {
				if (cd.getColumnName().equals(selectColumn.getColumnName())) {
					selectedColumns.add(cd);
					i++;
					
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
			if(this.rows.get(i).equals(row, index)) {
				index = i;
			}
		}
		return index;
	}
	
	public ArrayList<Integer> sortIntegers(ArrayList<Integer> whole) {
		ArrayList<Integer> left = new ArrayList<Integer>();
	    ArrayList<Integer> right = new ArrayList<Integer>();
	    int center;
	 
	    if (whole.size() == 1) {    
	        return whole;
	    } else {
	        center = whole.size()/2;
	        // copy the left half of whole into the left.
	        for (int i=0; i<center; i++) {
	                left.add(whole.get(i));
	        }
	 
	        //copy the right half of whole into the new arraylist.
	        for (int i=center; i<whole.size(); i++) {
	                right.add(whole.get(i));
	        }
	 
	        // Sort the left and right halves of the arraylist.
	        left  = sortIntegers(left);
	        right = sortIntegers(right);
	 
	        // Merge the results back together.
	        this.mergeIntegers(left, right, whole);
	    }
	    return whole;
	}
	
	private void mergeIntegers(ArrayList<Integer> left, ArrayList<Integer> right, ArrayList<Integer> whole) {
	    int leftIndex = 0;
	    int rightIndex = 0;
	    int wholeIndex = 0;
	    // As long as neither the left nor the right ArrayList has
	    // been used up, keep taking the smaller of left.get(leftIndex)
	    // or right.get(rightIndex) and adding it at both.get(bothIndex).
	    while (leftIndex < left.size() && rightIndex < right.size()) {
	        if ((left.get(leftIndex).compareTo(right.get(rightIndex))) < 0) {
	            whole.set(wholeIndex, left.get(leftIndex));
	            leftIndex++;
	        } else {
	            whole.set(wholeIndex, right.get(rightIndex));
	            rightIndex++;
	        }
	        wholeIndex++;
	    }
	    ArrayList<Integer> rest;
	    int restIndex;
	    if (leftIndex >= left.size()) {
	        // The left ArrayList has been use up...
	        rest = right;
	        restIndex = rightIndex;
	    } else {
	        // The right ArrayList has been used up...
	        rest = left;
	        restIndex = leftIndex;
	    }
	    // Copy the rest of whichever ArrayList (left or right) was not used up.
	    for (int i=restIndex; i<rest.size(); i++) {
	        whole.set(wholeIndex, rest.get(i));
	        wholeIndex++;
	    }
	}

	public boolean hasWhere(SelectQuery SQ) {
		try {
			SQ.getWhereCondition();
			return true;
		}
		catch(NullPointerException e) {
			return false;
		}
	}
	
	public ArrayList<Row> sortAscending(ArrayList<Row> whole, int columnIndex) {
		ArrayList<Row> left = new ArrayList<Row>();
	    ArrayList<Row> right = new ArrayList<Row>();
	    int center;
	 
	    if (whole.size() == 1) {    
	        return whole;
	    } else {
	        center = whole.size()/2;
	        // copy the left half of whole into the left.
	        for (int i=0; i<center; i++) {
	                left.add(whole.get(i));
	        }
	 
	        //copy the right half of whole into the new arraylist.
	        for (int i=center; i<whole.size(); i++) {
	                right.add(whole.get(i));
	        }
	 
	        // Sort the left and right halves of the arraylist.
	        left  = sortAscending(left, columnIndex);
	        right = sortAscending(right, columnIndex);
	 
	        // Merge the results back together.
	        this.mergeAscending(left, right, whole, columnIndex);
	    }
	    return whole;
	}
	
	private void mergeAscending(ArrayList<Row> left, ArrayList<Row> right, ArrayList<Row> whole, int columnIndex) {
	    int leftIndex = 0;
	    int rightIndex = 0;
	    int wholeIndex = 0;
	    // As long as neither the left nor the right ArrayList has
	    // been used up, keep taking the smaller of left.get(leftIndex)
	    // or right.get(rightIndex) and adding it at both.get(bothIndex).
	    while (leftIndex < left.size() && rightIndex < right.size()) {
	        if ((left.get(leftIndex).rowEntries[columnIndex].value.compareTo(right.get(rightIndex).rowEntries[columnIndex].value)) < 0) {
	            whole.set(wholeIndex, left.get(leftIndex));
	            leftIndex++;
	        } else {
	            whole.set(wholeIndex, right.get(rightIndex));
	            rightIndex++;
	        }
	        wholeIndex++;
	    }
	    ArrayList<Row> rest;
	    int restIndex;
	    if (leftIndex >= left.size()) {
	        // The left ArrayList has been use up...
	        rest = right;
	        restIndex = rightIndex;
	    } else {
	        // The right ArrayList has been used up...
	        rest = left;
	        restIndex = leftIndex;
	    }
	    // Copy the rest of whichever ArrayList (left or right) was not used up.
	    for (int i=restIndex; i<rest.size(); i++) {
	        whole.set(wholeIndex, rest.get(i));
	        wholeIndex++;
	    }
	}

	public ArrayList<Row> sortDescending(ArrayList<Row> whole, int columnIndex) {
		ArrayList<Row> left = new ArrayList<Row>();
	    ArrayList<Row> right = new ArrayList<Row>();
	    int center;
	 
	    if (whole.size() == 1) {    
	        return whole;
	    } else {
	        center = whole.size()/2;
	        // copy the left half of whole into the left.
	        for (int i=0; i<center; i++) {
	                left.add(whole.get(i));
	        }
	 
	        //copy the right half of whole into the new arraylist.
	        for (int i=center; i<whole.size(); i++) {
	                right.add(whole.get(i));
	        }
	 
	        // Sort the left and right halves of the arraylist.
	        left  = sortAscending(left, columnIndex);
	        right = sortAscending(right, columnIndex);
	 
	        // Merge the results back together.
	        this.mergeDescending(left, right, whole, columnIndex);
	    }
	    return whole;
	}
	
	private void mergeDescending(ArrayList<Row> left, ArrayList<Row> right, ArrayList<Row> whole, int columnIndex) {
	    int leftIndex = 0;
	    int rightIndex = 0;
	    int wholeIndex = 0;
	    // As long as neither the left nor the right ArrayList has
	    // been used up, keep taking the smaller of left.get(leftIndex)
	    // or right.get(rightIndex) and adding it at both.get(bothIndex).
	    while (leftIndex < left.size() && rightIndex < right.size()) {
	        if ((left.get(leftIndex).rowEntries[columnIndex].value.compareTo(right.get(rightIndex).rowEntries[columnIndex].value)) > -1) {
	            whole.set(wholeIndex, left.get(leftIndex));
	            leftIndex++;
	        } else {
	            whole.set(wholeIndex, right.get(rightIndex));
	            rightIndex++;
	        }
	        wholeIndex++;
	    }
	    ArrayList<Row> rest;
	    int restIndex;
	    if (leftIndex >= left.size()) {
	        // The left ArrayList has been use up...
	        rest = right;
	        restIndex = rightIndex;
	    } else {
	        // The right ArrayList has been used up...
	        rest = left;
	        restIndex = leftIndex;
	    }
	    // Copy the rest of whichever ArrayList (left or right) was not used up.
	    for (int i=restIndex; i<rest.size(); i++) {
	        whole.set(wholeIndex, rest.get(i));
	        wholeIndex++;
	    }
	}

	public List<Row> distinct(ArrayList<ColumnDescription> selectedColumns, List<Row> returnedRows) {
		for(ColumnDescription cd: selectedColumns) {
			for(Row row : returnedRows) {
				int index = returnedRows.get(0).findColumnIndex(cd.getColumnName());
				row.selectString = row.selectString + row.rowEntries[index].value;
			}
		}
		for(int i = 0; i < returnedRows.size(); i++) {
			for(int j = returnedRows.size() -1; j > i; j--) {
				if(returnedRows.get(i).selectString.equals(returnedRows.get(j).selectString)) {
					returnedRows.remove(j);
				}
			}
		}
		return returnedRows;
	}

	public ArrayList<SelectColumn> extendFunctionColumns(List<SelectColumn> allSelectColumns) {
		int maxLength = 0;
		for(SelectColumn column : allSelectColumns) {
			if(column.getSelectColumn().size() > maxLength) {
				maxLength = column.getSelectColumn().size();
			}
		}
		if(maxLength > 1) {
			for(SelectColumn column : allSelectColumns) {
				if(column.hasFunction()) {
					DataEntry entry = column.getSelectColumn().get(0);
					for(int i = 1; i < maxLength; i++) {
						column.getSelectColumn().add(entry);
					}
				}
			}
		}
		return (ArrayList<SelectColumn>) allSelectColumns;
	}

	public ArrayList<ColumnDescription> addSelectedColumns(SelectQuery SQ) {
		ArrayList<ColumnDescription> selectedColumns = null;
		if (SQ.getSelectedColumnNames().length == 1 && SQ.getSelectedColumnNames()[0].getColumnName().equals("*")) {
			selectedColumns = new ArrayList<ColumnDescription>();
			for (int i = 0; i < this.cds.length; i++) {
				selectedColumns.add(this.cds[i]);
			}
		}
		else {
			selectedColumns = this.getSelectColumns(SQ.getSelectedColumnNames());
		}
		return selectedColumns;
	}

	public List<Row> addSelectRows(SelectQuery SQ, ArrayList<ColumnDescription> selectedColumns) {
		ArrayList<Row> returnedRows = null;
		try {
			ColumnDescription[] tempCDS = new ColumnDescription[selectedColumns.size()];
			for(int i = 0; i < selectedColumns.size(); i++) {
				tempCDS[i] = selectedColumns.get(i);
			}
			Where where = new Where(tempCDS);
			List<Row> temp = new ArrayList<Row>(this.rows);
			returnedRows = where.where((ArrayList<Row>) temp, SQ.getWhereCondition());	
		}
		catch(NullPointerException e) {
			returnedRows = (ArrayList<Row>) this.rows;
		}
		return returnedRows;
	}
}
