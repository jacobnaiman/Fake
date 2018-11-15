package edu.yu.cs.ds.finalproject;

import java.util.ArrayList;
import java.util.List;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnID;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.CreateTableQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SQLQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery;

public class ResultSet {
	private ArrayList<SelectColumn> columns;
	public boolean successful;
	private ColumnID[] columnNames;
	private ArrayList<ColumnDescription.DataType> columnDataTypes;
	private String message;
	
	/**
	 * creates a resultset from a select query and initializes the columns, column names and column data types
	 * @param allSelectColumns
	 * @param query
	 */
	public ResultSet(List<SelectColumn> allSelectColumns, SQLQuery query) {
		if(query instanceof SelectQuery) {
			this.columns = (ArrayList<SelectColumn>) allSelectColumns;
			this.setColumnNames(((SelectQuery) query).getSelectedColumnNames());
			this.columnDataTypes = new ArrayList<ColumnDescription.DataType>();
			for(SelectColumn SC : this.columns) {
				this.columnDataTypes.add(SC.getCD().getColumnType());
			}
			this.successful = true;
		}
	}
	
	/**
	 * creates a result set with a boolean value for queries of type createIndex, Delete, Update, and Insert
	 */
	public ResultSet() {
		this.successful = true;
		this.columns = new ArrayList<SelectColumn>();
		this.columns.add(new SelectColumn(this.successful));
	}
	
	public ResultSet(RuntimeException e) {
		this.successful = false;
		this.message = e.getMessage();
	}

	/**
	 * creates a resultset of column names for a create table query
	 * @param CTQ
	 */
	public ResultSet(CreateTableQuery CTQ) {
		this.successful = true;
		ColumnDescription[] cols = CTQ.getColumnDescriptions();
		this.columnNames = new ColumnID[cols.length];
		for(int i = 0; i < cols.length; i++) {
			this.columnNames[i] = new ColumnID(cols[i].getColumnName(), CTQ.getTableName());
		}
	}
	
	/**
	 * Prints out the result set
	 */
	public void print() {
		if(this.columnDataTypes == null) {
			System.out.println(this.successful);
		}
		else {
			String title = "";
			for(SelectColumn col : this.columns) {
				if(col.hasFunction()) {
					title = col.getIndividualfunction().function.toString() + "("+col.getColumnName()+")";
					System.out.printf("%25s", title);
				}
				else {
					System.out.printf("%25s", col.getCD().getColumnName());
				}
			}
			System.out.println();
			for(ColumnDescription.DataType dataType : this.columnDataTypes) {
				System.out.printf("%25s", dataType);
			}
			System.out.println();
			for(int i = 0; i < this.columns.get(0).getSelectColumn().size(); i++) {
				for(SelectColumn SC : this.columns) {
					System.out.printf("%25s", SC.getSelectColumn().get(i).value);
				}
				System.out.println();
			}
		}
	}


	public ColumnID[] getColumnNames() {
		return columnNames;
	}
	
	public ArrayList<ColumnDescription.DataType> getColumnDataTypes() {
		return this.columnDataTypes;
	}
	
	public ArrayList<SelectColumn> getColumns() {
		return this.columns;
	}

	public void setColumnNames(ColumnID[] columnNames) {
		this.columnNames = columnNames;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public boolean isSuccessful() {
		return this.successful;
	}

}
