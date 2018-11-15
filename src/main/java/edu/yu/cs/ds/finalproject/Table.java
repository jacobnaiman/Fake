package edu.yu.cs.ds.finalproject;

import java.awt.RenderingHints.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnID;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnValuePair;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery.FunctionInstance;

public class Table {
	public List<Row> rows;
	public ColumnDescription[] cds;
	public String tableName;
	public ColumnDescription primaryKey;
	public ArrayList<Column> columns;
	public ArrayList<BTree> indexedColumns;
	
	/**
	 * This method creates a table and initializes all the fields with the input parameters
	 * @param name - table name
	 * @param cds - column descriptions
	 * @param pK - the primary key column
	 */
	public Table(String name, ColumnDescription[] cds, ColumnDescription pK) {
		this.rows = new ArrayList<Row>();
		this.cds = cds;
		this.tableName = name;
		this.primaryKey = pK;
		this.columns = this.createColumns();
		this.indexedColumns = new ArrayList<BTree>();
	}
	
	public Table(String name, ColumnDescription[] cds, ArrayList<Row> rows) {
		this.rows = new ArrayList<Row>();
		this.cds = cds;
		this.tableName = name;
		for(Row row : rows) {
			this.rows.add(row);
		}
	}
	
	/**
	 * adds a new row to a table
	 * @return row - the row being inserted
	 */
	public Row addRow() {
		Row row = new Row(this.cds.length);
		this.rows.add(row);
		return row;
	}
	
	public ArrayList<Column> createColumns() {
		ArrayList<Column> columns = new ArrayList<Column>();
		for(ColumnDescription cd : this.cds) {
			Column column = new Column(cd, this);
			columns.add(column);
		}
		return columns;
		
	}
	
	/**
	 * This method inserts the values from the column value pairs of a query into a row
	 * @param colVals
	 * @param row
	 * @return the row that the values are being inserted into
	 */
	public Row putInValues(ColumnValuePair[] colVals, Row row) {
		int counter = 0;
		this.duplicateCheck(colVals);
		for (int j = 0; j < this.cds.length; j++) {//iterate through all the columns
			for(ColumnValuePair CVP : colVals) {
				//linking the colvalpair to its column
				if (CVP.getColumnID().getColumnName().equals(this.cds[j].getColumnName())) {
					DataEntry dataEntry = new DataEntry(CVP);
					dataEntry.performValueChecks(this.cds[j]); //datatype and length
					//checks if it is a unique column or primary key which is unique
					if(this.cds[j].isUnique() || this.cds[j].equals(this.primaryKey)) {
						this.uniquenessCheck(dataEntry, j);
					}
					row.rowEntries[j] = dataEntry;
					counter++;
				}
			}
			try {//checks if it is a not null column or the primary key which is also not null
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
	
	/**
	 * makes sure the primary key does not have a default value
	 */
	public void primaryKeyChecks() {
		for (ColumnDescription cd : this.cds) {
			if (cd.equals(this.primaryKey)) {
				if (cd.getHasDefault()) {
					throw new IllegalArgumentException("Primary key cannot have a default value");
				}
			}
		}
	}
	
	/**
	 * Ensures unique and primary key columns do not have repeat values
	 * @param entry
	 * @param j - the index of the column
	 */
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
	
	/**
	 * Adds all the columns requested to be selected to the arrayList
	 * @param columnIDs
	 * @return an arraylist of column descriptions
	 */
	public ArrayList<ColumnDescription> getSelectColumns(ColumnID[] columnIDs) {
		ArrayList<ColumnDescription> selectedColumns = new ArrayList<ColumnDescription>();
		for (ColumnID selectColumn : columnIDs) {
			int i = 0;
			for (ColumnDescription cd : this.cds) {
				if (cd.getColumnName().equals(selectColumn.getColumnName())) {
					selectedColumns.add(cd);
					i++;	
				}	
			}
			if(i == 0) {
				throw new IllegalArgumentException("You tried to select a column which does not exist");
			}
		}
		return selectedColumns;
	}
	
	/**
	 * Ensures the same column was not entered twice
	 * @param CVPs
	 */
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
	
	/**
	 * recursively sorts an arraylist of integers in order
	 * merge sort algorithm taken mostly from http://www.codexpedia.com/java/java-merge-sort-implementation/
	 * @param whole
	 * @return
	 */
	public ArrayList<Integer> sortIntegers(ArrayList<Integer> whole) {
		ArrayList<Integer> left = new ArrayList<Integer>();
	    ArrayList<Integer> right = new ArrayList<Integer>();
	    int center;
	    if (whole.size() == 1) {    
	        return whole;
	    } else {
	        center = whole.size()/2;
	        for (int i=0; i<center; i++) {
	                left.add(whole.get(i));
	        }
	        for (int i=center; i<whole.size(); i++) {
	                right.add(whole.get(i));
	        }
	        left  = sortIntegers(left);
	        right = sortIntegers(right);
	        this.mergeIntegers(left, right, whole);
	    }
	    return whole;
	}
	
	/**
	 * merges the lists of sorted integers together
	 * merge sort algorithm taken mostly from http://www.codexpedia.com/java/java-merge-sort-implementation/
	 * @param whole
	 * @return
	 */
	private void mergeIntegers(ArrayList<Integer> left, ArrayList<Integer> right, ArrayList<Integer> whole) {
	    int leftIndex = 0;
	    int rightIndex = 0;
	    int wholeIndex = 0;
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
	        rest = right;
	        restIndex = rightIndex;
	    } else {
	        rest = left;
	        restIndex = leftIndex;
	    }
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
	
	/**
	 * if a select is distinct this method filters out all the rows which do not have distinct values
	 * @param selectedColumns
	 * @param returnedRows
	 * @return
	 */
	public List<Row> distinct(ArrayList<ColumnDescription> selectedColumns, List<Row> returnedRows) {
		for(ColumnDescription cd: selectedColumns) {
			for(Row row : returnedRows) {//adds the values selected onto a string to ensure only nondistinct combinations of 
				//column values are filtered out and not merely values
				int index = returnedRows.get(0).findColumnIndex(cd.getColumnName());
				row.selectString = row.selectString + row.rowEntries[index].value;
			}
		}
		ArrayList<Row> distinct = new ArrayList<Row>(returnedRows);
		for(int i = 0; i < distinct.size(); i++) {
			for(int j = distinct.size() -1; j > i; j--) {
				if(distinct.get(i).selectString.equals(distinct.get(j).selectString)) {
					distinct.remove(j);
				}
			}
		}
		return distinct;
	}
	
	/**
	 * extends the columns with only one function value to the length of all the other columns
	 * @param allSelectColumns
	 * @return
	 */
	public ArrayList<SelectColumn> extendFunctionColumns(List<SelectColumn> allSelectColumns) {
		int maxLength = 0;
		for(SelectColumn column : allSelectColumns) {
			if(!column.hasFunction()) {
				maxLength = column.getSelectColumn().size();
				break;
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
	
	
	/**
	 * Returns an arraylist of column descriptions requested in the select query
	 * @param SQ
	 * @return an arraylist of column descriptions
	 */
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
	
	/**
	 * This takes in a list of rows and removes the rows which do not meet the where condition
	 * @param SQ
	 * @param selectedColumns
	 * @return a list of rows which satisfy the where condition
	 */
	public List<Row> addSelectRows(SelectQuery SQ, ArrayList<ColumnDescription> selectedColumns) {
		ArrayList<Row> returnedRows = null;
		try {
			ColumnDescription[] tempCDS = new ColumnDescription[selectedColumns.size()];
			for(int i = 0; i < selectedColumns.size(); i++) {
				tempCDS[i] = selectedColumns.get(i);
			}
			Where where = new Where(tempCDS, this.columns, SQ, this.indexedColumns);
			List<Row> temp = new ArrayList<Row>(this.rows);
			returnedRows = where.where((ArrayList<Row>) temp, SQ.getWhereCondition());	
		}
		catch(NullPointerException e) {
			returnedRows = (ArrayList<Row>) this.rows;
		}
		if(SQ.isDistinct()) {
			returnedRows = (ArrayList<Row>) this.distinct(selectedColumns, returnedRows);
		}
		return returnedRows;
	}
	
	/**
	 * deletes the given rows from a table
	 * @param returnedRows
	 */
	public void deleteRows(List<Row> returnedRows) {
		List<Integer> indecesToBeDeleted = new ArrayList<Integer>();
		for (Row row : returnedRows) {
			indecesToBeDeleted.add(this.findIndex(row));
		}
		if(indecesToBeDeleted.size() > 1) {
			indecesToBeDeleted = this.sortIntegers((ArrayList<Integer>) indecesToBeDeleted);
		}
		int size = indecesToBeDeleted.size() - 1;
		for (int j = size; j >= 0; j--) {
			int index = indecesToBeDeleted.get(j);
			this.rows.remove(index);
		}
	}
	
	/**
	 * This method creates selectColumns from the select query, which are added to a list and returned
	 * @param SQ
	 * @param returnedRows
	 * @param selectedColumns
	 * @param columnIDs
	 * @return a list of selected columns
	 */
	public List<SelectColumn> gatherSelectColumns(SelectQuery SQ, List<Row> returnedRows, ArrayList<ColumnDescription> selectedColumns, ColumnID[] columnIDs) {
		List<SelectColumn> allSelectColumns = new ArrayList<SelectColumn>();
		if (!(SQ.getSelectedColumnNames().length == 1 && SQ.getSelectedColumnNames()[0].getColumnName().equals("*"))) {
			int iterator = 0;
			for(ColumnDescription cd : selectedColumns) {
				SelectColumn selectColumn = new SelectColumn(returnedRows, cd, cd.getColumnName(), SQ.getOrderBys(), columnIDs[iterator]);
				allSelectColumns.add(selectColumn);
				iterator++;
			}
		}
		else {
			for(ColumnDescription cd : selectedColumns) {
				SelectColumn selectColumn = new SelectColumn(returnedRows, cd, cd.getColumnName(), SQ.getFunctions(), SQ.getOrderBys());
				allSelectColumns.add(selectColumn);
			}
		}
		return allSelectColumns;
	}
	
	/**
	 * performs the functions on the columns they are supposed to be performed on
	 * @param SQ
	 * @param allSelectColumns
	 * @return
	 */
	public List<SelectColumn> performFunctions(SelectQuery SQ, List<SelectColumn> allSelectColumns) {
		for(SelectColumn SC : allSelectColumns) {
			for(FunctionInstance func : SQ.getFunctions()) {
				if (SC.getColumnID() == func.column){
					SC.performFunction(func);
					SC.setIndividualfunction(func);
				}
			}
		}
		allSelectColumns = this.extendFunctionColumns(allSelectColumns);
		return allSelectColumns;
	}

}
