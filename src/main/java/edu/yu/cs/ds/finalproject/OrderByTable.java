package edu.yu.cs.ds.finalproject;

import java.util.ArrayList;
import java.util.List;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery.OrderBy;

public class OrderByTable {
	public ArrayList<Row> rows;
	public ArrayList<Row> selectRowsInOrder;
	public ArrayList<SelectColumn> SCs;
	public OrderBy[] orderBys;
	
	/**
	 * constructs an orderby table and initializes the fields
	 * @param rows
	 * @param SCs - select columns
	 * @param OBs - orderbys
	 */
	public OrderByTable(ArrayList<Row> rows, ArrayList<SelectColumn> SCs, OrderBy[] OBs) {
		this.rows = rows;
		this.SCs = SCs;
		this.orderBys = OBs;
	}
	
	/**
	 * Does all the orderBys on the columns
	 */
	public void doOrderBys() {
		if(this.rows.size() == 0) {
			return;
		}
		int pastindex = 0;
		for(int i = 0; i < this.orderBys.length; i++) {
			if(i == 0) {
				pastindex = this.firstOrderBy(i);
			}
			else {
				int currentindex = this.rows.get(0).findColumnIndex(orderBys[i].getColumnID().getColumnName());
				this.otherOrderBys(pastindex, currentindex, i);
				pastindex = currentindex;
			}
			for(int j = 0; j < this.rows.size(); j++) {//uses a string of already ordered elements to determine which are equal
				this.rows.get(j).orderByString = this.rows.get(j).orderByString + this.rows.get(j).rowEntries[pastindex].value;
			}
		}
	}
	
	/**
	 * Completes the first orderby in the array which applies to all rows
	 * @param i - index of orderBy
	 * @return the previous index of the orderBy array
	 */
	private int firstOrderBy(int i) {
		int pastindex =0;
		if(this.orderBys[i].isAscending()) {
			this.rows = this.sortAscending(this.rows, this.rows.get(0).findColumnIndex(orderBys[i].getColumnID().getColumnName()));
		}
		else {
			this.rows = this.sortDescending(this.rows, this.rows.get(0).findColumnIndex(orderBys[i].getColumnID().getColumnName()));
		}
		for(int q = 0; q < this.SCs.size(); q++) {
			if(this.SCs.get(q).getColumnName().equals(this.orderBys[i].getColumnID().getColumnName())) {
				pastindex = this.rows.get(0).findColumnIndex(orderBys[i].getColumnID().getColumnName());
			}
		}
		return pastindex;
	}
	
	/**
	 * completes the subsequent orderBys after the first one by determining if the orderby string is equal
	 * @param pastindex
	 * @param currentindex
	 * @param i
	 */
	private void otherOrderBys(int pastindex, int currentindex, int i) {
		for(int j = 0; j < this.SCs.get(pastindex).getSelectColumn().size(); j++) {
			for(int k = j + 1; k < this.SCs.get(pastindex).getSelectColumn().size(); k++) {
				if(this.rows.get(j).orderByString.equals(this.rows.get(k).orderByString)) {
					if(this.rows.get(j).rowEntries[currentindex].value.compareTo(this.rows.get(k).rowEntries[currentindex].value) > 0 && this.orderBys[i].isAscending()) {
						Row rowj = this.rows.get(j);
						Row rowk = this.rows.get(k);
						this.rows.remove(j);
						this.rows.add(j, rowk);
						this.rows.remove(k);
						this.rows.add(k, rowj);
					}
					else if (this.rows.get(j).rowEntries[currentindex].value.compareTo(this.rows.get(k).rowEntries[currentindex].value) < 0 && !this.orderBys[i].isAscending()) {
						Row tempRow = this.rows.get(j);
						this.rows.add(j, this.rows.get(k));
						this.rows.remove(j + 1);
						this.rows.add(k, tempRow);
						this.rows.remove(k + 1);
						
					}
				}
			}
		}
	}
	
	public ArrayList<SelectColumn> getSelectColumns() {
		ArrayList<SelectColumn> allSelectColumns = new ArrayList<SelectColumn>();
		for(SelectColumn SC : this.SCs) {
			SelectColumn newColumn = new SelectColumn(this.selectRowsInOrder, SC.getCD(), SC.getColumnName(), SC.getIndividualfunction());
			allSelectColumns.add(newColumn);
		}
		this.SCs = allSelectColumns;
		return allSelectColumns;
	}
	
	/**
	 * recursively sorts an arraylist of rows in ascending order in a given column
	 * merge sort algorithm taken mostly from http://www.codexpedia.com/java/java-merge-sort-implementation/
	 * @param whole
	 * @param columnIndex
	 * @return
	 */
	public ArrayList<Row> sortAscending(ArrayList<Row> whole, int columnIndex) {
		ArrayList<Row> left = new ArrayList<Row>();
	    ArrayList<Row> right = new ArrayList<Row>();
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
	        left  = sortAscending(left, columnIndex);
	        right = sortAscending(right, columnIndex);
	        this.mergeAscending(left, right, whole, columnIndex);
	    }
	    return whole;
	}
	
	
	/**
	 * merges the lists of sorted rows together
	 * merge sort algorithm taken mostly from http://www.codexpedia.com/java/java-merge-sort-implementation/
	 * @param whole
	 * @return
	 */
	private void mergeAscending(ArrayList<Row> left, ArrayList<Row> right, ArrayList<Row> whole, int columnIndex) {
	    int leftIndex = 0;
	    int rightIndex = 0;
	    int wholeIndex = 0;
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
	
	
	/**
	 * recursively sorts an arraylist of rows in descending order in a given column
	 * merge sort algorithm taken mostly from http://www.codexpedia.com/java/java-merge-sort-implementation/
	 * @param whole
	 * @param columnIndex
	 */
	public ArrayList<Row> sortDescending(ArrayList<Row> whole, int columnIndex) {
		ArrayList<Row> left = new ArrayList<Row>();
	    ArrayList<Row> right = new ArrayList<Row>();
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
	        left  = sortDescending(left, columnIndex);
	        right = sortDescending(right, columnIndex);
	        this.mergeDescending(left, right, whole, columnIndex);
	    }
	    return whole;
	}
	
	/**
	 * merges the lists of sorted rows together
	 * merge sort algorithm taken mostly from http://www.codexpedia.com/java/java-merge-sort-implementation/
	 * @param whole
	 */
	private void mergeDescending(ArrayList<Row> left, ArrayList<Row> right, ArrayList<Row> whole, int columnIndex) {
	    int leftIndex = 0;
	    int rightIndex = 0;
	    int wholeIndex = 0;
	    while (leftIndex < left.size() && rightIndex < right.size()) {
	        if ((left.get(leftIndex).rowEntries[columnIndex].value.compareTo(right.get(rightIndex).rowEntries[columnIndex].value)) > 0) {
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
	
	/**
	 * puts the selected rows in the same order that the rows of the table were in after they were sorted into the proper order
	 * @param returnedRows
	 * @return the ordered selected rows
	 */
	public List<Row> putInOrder(List<Row> returnedRows) {
		ArrayList<Row> selectRowsInOrder = new ArrayList<Row>();
		for(Row row : this.rows) {
			for(int i = 0; i < returnedRows.size(); i++) {
				if(row.compare(returnedRows.get(i))) {
					selectRowsInOrder.add(returnedRows.get(i));
					break;
				}
			}
		}
		this.selectRowsInOrder = selectRowsInOrder;
		return selectRowsInOrder;
	}	
}
