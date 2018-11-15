package edu.yu.cs.ds.finalproject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnID;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery.FunctionInstance;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery.OrderBy;

public class SelectColumn {
	private ArrayList<DataEntry> selectColumn;
	private String columnName;
	private ColumnDescription cd;
	private boolean hasFunction = false;
	private boolean hasSelect;
	private OrderBy orderBy;
	private FunctionInstance individualfunction;
	private boolean hasOrderBy;
	protected ColumnID columnID;
	
	/**
	 * constructs a select column and initializes the fields
	 * @param returnedRows - the list of rows to be displayed
	 * @param cd - a column description
	 * @param name - name of the column
	 * @param functions
	 * @param orderBys
	 * @param columnID
	 */
	public SelectColumn(List<Row> returnedRows, ColumnDescription cd, String name, SelectQuery.OrderBy[] orderBys, ColumnID columnID) {
		this.columnName = name;
		this.setColumnID(columnID);
		this.cd = cd;
		this.selectColumn = this.trimRows((ArrayList<Row>) returnedRows);
		if (orderBys.length == 0) {
			this.setHasOrderBy(false);
		}
		for(OrderBy OB : orderBys) {
			if (OB.getColumnID().getColumnName().equals(this.columnName)) {
				this.setOrderBy(OB);
				this.setHasOrderBy(true);
			}
		}
	}
	
	

	public SelectColumn(ArrayList<Row> rows, ColumnDescription cd, String string, FunctionInstance functionInstance) {
		this.cd = cd;
		this.columnName = string;
		this.individualfunction = functionInstance;
		this.selectColumn = this.trimRows(rows);
	}
	
	/**
	 * constructs a select column and initializes the fields - used for a case where selecting all columns
	 * @param returnedRows - the list of rows to be displayed
	 * @param cd - a column description
	 * @param name - name of the column
	 * @param functions
	 * @param orderBys
	 */
	public SelectColumn(List<Row> returnedRows, ColumnDescription cd, String name, ArrayList<FunctionInstance> functions, OrderBy[] orderBys) {
		this.columnName = name;
		this.cd = cd;
		this.selectColumn = this.trimRows((ArrayList<Row>) returnedRows);
		if (orderBys.length == 0) {
			this.setHasOrderBy(false);
		}
		for(OrderBy OB : orderBys) {
			if (OB.getColumnID().getColumnName().equals(this.columnName)) {
				this.setOrderBy(OB);
				this.setHasOrderBy(true);
			}
		}
	}
	
	public SelectColumn(boolean successful) {
		this.selectColumn = new ArrayList<DataEntry>();
		this.selectColumn.add(new DataEntry(((Boolean) successful).toString(), null));
	}

	/**
	 * Takes the rows and condenses them to only the selected entries
	 * @param rows
	 * @return returns an arraylist of data entries
	 */
	public ArrayList<DataEntry> trimRows(ArrayList<Row> rows) {
		ArrayList<DataEntry> entries = new ArrayList<DataEntry>();
		for(Row row : rows) {
			entries.add(row.rowEntries[this.findIndex(row)]);
		}
		return entries;	
	}
	
	/**
	 * Finds the index of the row within the select column and returns it
	 * @param row
	 * @return index of row with the select column
	 */
	public int findIndex(Row row) {
		int index = -1;
		for(int i = 0; i < row.rowEntries.length; i++) {
			if(row.rowEntries[i].columnName.equals(this.cd.getColumnName())) {
				
				index = i;
			}
		}
		return index;
	}
	
	public void distinct() {
		Set<DataEntry> tempSet = new HashSet<DataEntry>(this.selectColumn);
		this.selectColumn = new ArrayList<DataEntry>(tempSet);
	}
	
	public ArrayList<DataEntry> getSelectColumn() {
		return this.selectColumn;
	}
	
	public String getColumnName() {
		return this.columnName;
	}
	
	/**
	 * Takes the column and channels it to a method that performs its function
	 * @param function
	 */
	public void performFunction(SelectQuery.FunctionInstance function) {
		this.setHasFunction(true);
		if (function.function.equals(SelectQuery.FunctionName.AVG)) {
			this.average(function.isDistinct);
		}
		if (function.function.equals(SelectQuery.FunctionName.MIN)) {
			this.minimum();
		}
		if (function.function.equals(SelectQuery.FunctionName.MAX)) {
			this.maximum();
		}
		if (function.function.equals(SelectQuery.FunctionName.SUM)) {
			this.sum(function.isDistinct);
		}
		if (function.function.equals(SelectQuery.FunctionName.COUNT)) {
			this.count(function.isDistinct);
		}
	}
	
	/**
	 * takes the average of all the values in the column or only the distinct values in a column
	 * @param isDistinct
	 */
	public void average(boolean isDistinct) {
		if (this.cd.getColumnType().equals(ColumnDescription.DataType.BOOLEAN) || this.cd.getColumnType().equals(ColumnDescription.DataType.VARCHAR)) {
			throw new IllegalArgumentException("For average you must enter an int or double");
		}
		double total = 0;
		int count = 0;
		if(isDistinct) {
			ArrayList<DataEntry> tempSet = new ArrayList<DataEntry>();
			for(int j = 0; j < this.getSelectColumn().size(); j++) {
				boolean isPresent = false;
				for(int i = j + 1; i < this.getSelectColumn().size(); i++) {
					if(this.getSelectColumn().get(i).value.equals(this.getSelectColumn().get(j).value)) {
						isPresent = true;
					}
				}
				if(!isPresent) {
					tempSet.add(this.getSelectColumn().get(j));
				}
			}
			this.selectColumn = tempSet;
		}
		for(int i = this.selectColumn.size() - 1; i >= 0; i--) {
			count++;
			total = total + Double.parseDouble(this.selectColumn.get(i).value);
			this.selectColumn.remove(i);
		}
		Double average = total/count;
		average = this.round(average);
		DataEntry dEntry = new DataEntry(average.toString(), this.cd);
		this.selectColumn.add(dEntry);
	}
	
	/**
	 * rounds the average to the proper amount of decimal digits specified on the column when the table was created
	 * @param average
	 * @return the rounded average
	 */
	private double round(Double average) {
		char[] letters = average.toString().toCharArray();
		int places = this.cd.getFractionLength();
		int index = 0;
		String predecimal = "";
		for(int i = 0; i < letters.length; i++) {
			if(((Character) letters[i]).equals('.')) {
				index = i;
				break;
			}
			predecimal = predecimal + letters[i];
		}
		BigDecimal bd = new BigDecimal(average.toString().substring(index));
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    String total = predecimal + bd.toString().substring(1);
	    return Double.parseDouble(total);
	}
	
	/**
	 * takes the sum of all the values in the column or only distinct values in a column
	 * @param isDistinct
	 */
	public void sum(boolean isDistinct) {
		if (this.cd.getColumnType().equals(ColumnDescription.DataType.BOOLEAN) || this.cd.getColumnType().equals(ColumnDescription.DataType.VARCHAR)) {
			throw new IllegalArgumentException("For sum you must enter an int or double");
		}
		if(isDistinct) {
			ArrayList<DataEntry> tempSet = new ArrayList<DataEntry>();
			for(int j = 0; j < this.getSelectColumn().size(); j++) {
				boolean isPresent = false;
				for(int i = j + 1; i < this.getSelectColumn().size(); i++) {
					if(this.getSelectColumn().get(i).value.equals(this.getSelectColumn().get(j).value)) {
						isPresent = true;
					}
				}
				if(!isPresent) {
					tempSet.add(this.getSelectColumn().get(j));
				}
			}
			this.selectColumn = tempSet;
		}
		Double total = (double) 0;
		for(int i = this.selectColumn.size() - 1; i >= 0; i--) {
			total = total + Double.parseDouble(this.selectColumn.get(i).value);
			this.selectColumn.remove(i);
		}
		total = this.round(total);
		DataEntry dEntry = new DataEntry(total.toString(), this.cd);
		this.selectColumn.add(dEntry);
	}
	
	/**
	 * finds the maximum value in the column
	 */
	public void maximum() {
		String maximum = this.selectColumn.get(0).value;
		for(int i = this.selectColumn.size() - 1; i >= 0; i--) {
			if (maximum.toString().compareTo(this.selectColumn.get(i).value) < 1) {
				maximum = this.selectColumn.get(i).value;
			}
			this.selectColumn.remove(i);
		}
		DataEntry dEntry = new DataEntry(maximum, this.cd);
		this.selectColumn.add(dEntry);
	}
	
	/**
	 * finds the minimum value in the column
	 */
	public void minimum() {
		String minimum = this.selectColumn.get(0).value;
		for(int i = this.selectColumn.size() - 1; i >= 0; i--) {
			if (minimum.toString().compareTo(this.selectColumn.get(i).value) > 0) {
				minimum = this.selectColumn.get(i).value;
			}
			this.selectColumn.remove(i);
		}
		DataEntry dEntry = new DataEntry(minimum, this.cd);
		this.selectColumn.add(dEntry);
	}
	
	/**
	 * Finds the total number of entries in the column or the distinct number
	 * @param isDistinct
	 */
	public void count(boolean isDistinct) {
		Integer count = 0;
		if (isDistinct) {
			ArrayList<DataEntry> tempSet = new ArrayList<DataEntry>();
			for(int j = 0; j < this.getSelectColumn().size(); j++) {
				boolean isPresent = false;
				for(int i = j + 1; i < this.getSelectColumn().size(); i++) {
					if(this.getSelectColumn().get(i).value.equals(this.getSelectColumn().get(j).value)) {
						isPresent = true;
					}
				}
				if(!isPresent) {
					tempSet.add(this.getSelectColumn().get(j));
				}
			}
			count = tempSet.size();
		}
		else {
			count = this.getSelectColumn().size();
		}
		for (int i = this.selectColumn.size() - 1; i >= 0; i--) {
			this.selectColumn.remove(i);
		}
		DataEntry dEntry = new DataEntry(count.toString(), this.cd);
		this.selectColumn.add(dEntry);
	}


	public boolean hasOrderBy() {
		return this.hasOrderBy;
	}
	private void setHasOrderBy(boolean b) {
		this.hasOrderBy = b;
		
	}



	public OrderBy getOrderBy() {
		return orderBy;
	}



	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}
	
	public ColumnDescription getCD() {
		return this.cd;
	}




	public FunctionInstance getIndividualfunction() {
		return individualfunction;
	}



	public void setIndividualfunction(FunctionInstance individualfunction) {
		this.individualfunction = individualfunction;
	}



	public boolean hasSelect() {
		return hasSelect;
	}



	public void setHasSelect(boolean hasSelect) {
		this.hasSelect = hasSelect;
	}



	public ColumnID getColumnID() {
		return columnID;
	}

	private void setHasFunction(boolean b) {
		this.hasFunction = true;
		
	}
	
	boolean hasFunction() {
		return this.hasFunction;
		
	}



	public void setColumnID(ColumnID columnID) {
		this.columnID = columnID;
	}
}
