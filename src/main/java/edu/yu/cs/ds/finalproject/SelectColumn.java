package edu.yu.cs.ds.finalproject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery.FunctionInstance;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery.OrderBy;

public class SelectColumn {
	private ArrayList<DataEntry> selectColumn;
	private String columnName;
	private ColumnDescription cd;
	private boolean hasFunction;
	private OrderBy orderBy;
	private FunctionInstance function;
	private boolean hasOrderBy;
	
	public SelectColumn(List<Row> returnedRows, ColumnDescription cd, String name, ArrayList<SelectQuery.FunctionInstance> functions, SelectQuery.OrderBy[] orderBys) {
		this.columnName = name;
		this.cd = cd;
		this.selectColumn = this.trimRows((ArrayList<Row>) returnedRows);
		this.setHasFunction(false);
		this.setHasOrderBy(false);
		for(FunctionInstance function : functions) {
			if (function.column.getColumnName().equals(this.columnName)) {
				this.setFunction(function);
				this.setHasFunction(true);
			}
		}
		if(!this.hasFunction()) {
			for(OrderBy OB : orderBys) {
				if (OB.getColumnID().getColumnName().equals(this.columnName)) {
					this.setOrderBy(OB);
					this.setHasOrderBy(true);
				}
			}
		}
	}
	
	

	public SelectColumn(ArrayList<Row> rows, ColumnDescription cd) {
		this.cd = cd;
		this.selectColumn = this.trimRows(rows);
	}



	public ArrayList<DataEntry> trimRows(ArrayList<Row> rows) {
		int index = this.findIndex(rows.get(0));
		ArrayList<DataEntry> entries = new ArrayList<DataEntry>();
		for(Row row : rows) {
			entries.add(row.rowEntries[index]);
		}
		return entries;	
	}
	
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
	
	public void performFunction(SelectQuery.FunctionInstance function) {
		this.setHasFunction(true);
		if (function.function.equals(SelectQuery.FunctionName.AVG)) {
			this.average();
		}
		if (function.function.equals(SelectQuery.FunctionName.MIN)) {
			this.minimum();
		}
		if (function.function.equals(SelectQuery.FunctionName.MAX)) {
			this.maximum();
		}
		if (function.function.equals(SelectQuery.FunctionName.SUM)) {
			this.sum();
		}
		if (function.function.equals(SelectQuery.FunctionName.COUNT)) {
			this.count(function.isDistinct);
		}
	}
	
	public void average() {
		if (this.cd.getColumnType().equals(ColumnDescription.DataType.BOOLEAN) || this.cd.getColumnType().equals(ColumnDescription.DataType.VARCHAR)) {
			throw new IllegalArgumentException("For average you must enter an int or double");
		}
		double total = 0;
		int count = 0;
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
	
	public double round(Double average) {
		char[] letters = average.toString().toCharArray();
		int places = this.cd.getFractionLength();
		int index = 0;
		String predecimal = "";
		for(int i = 0; i < letters.length; i++) {
			
			//predecimallength++;
			if(((Character) letters[i]).equals('.')) {
				index = i;
				break;
			}
			//predecimallength++;
			predecimal = predecimal + letters[i];
		}
		System.out.println(predecimal);
		
		BigDecimal bd = new BigDecimal(average.toString().substring(index));
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    String total = predecimal + bd.toString().substring(1);
	    return Double.parseDouble(total);
	}
	
	public void sum() {
		if (this.cd.getColumnType().equals(ColumnDescription.DataType.BOOLEAN) || this.cd.getColumnType().equals(ColumnDescription.DataType.VARCHAR)) {
			throw new IllegalArgumentException("For sum you must enter an int or double");
		}
		Double total = (double) 0;
		for(int i = this.selectColumn.size() - 1; i >= 0; i--) {
			total = total + Double.parseDouble(this.selectColumn.get(i).value);
			this.selectColumn.remove(i);
		}
		DataEntry dEntry = new DataEntry(total.toString(), this.cd);
		this.selectColumn.add(dEntry);
	}
	
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

	public boolean hasFunction() {
		return hasFunction;
	}

	public void setHasFunction(boolean hasFunction) {
		this.hasFunction = hasFunction;
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



	public FunctionInstance getFunction() {
		return function;
	}



	public void setFunction(FunctionInstance function) {
		this.function = function;
	}
}
