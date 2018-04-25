package edu.yu.cs.ds.finalproject;

import java.util.ArrayList;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery.OrderBy;

public class OrderByTable {
	public ArrayList<Row> rows;
	public ArrayList<SelectColumn> SCs;
	public OrderBy[] orderBys;
	public OrderByTable(ArrayList<Row> rows, ArrayList<SelectColumn> SCs, OrderBy[] OBs) {
		this.rows = rows;
		this.SCs = SCs;
		this.orderBys = OBs;
	}
	
	public void doOrderBys() {
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
			for(int j = 0; j < this.rows.size(); j++) {
				this.rows.get(j).orderByString = this.rows.get(j).orderByString + this.rows.get(j).rowEntries[pastindex].value;
			}
		}
	}
	
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
				//SelectColumn SC = new SelectColumn(this.rows, this.SCs.get(q).getCD());
				pastindex = this.rows.get(0).findColumnIndex(orderBys[i].getColumnID().getColumnName());
			}
		}
		return pastindex;
	}
	
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
			SelectColumn newColumn = new SelectColumn(this.rows, SC.getCD());
			allSelectColumns.add(newColumn);
		}
		this.SCs = allSelectColumns;
		return allSelectColumns;
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
}
