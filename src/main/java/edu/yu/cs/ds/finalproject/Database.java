package edu.yu.cs.ds.finalproject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;
import net.sf.jsqlparser.JSQLParserException;

public class Database {
	public List<Table> allTables;
	
	public Database() {
		this.allTables = new ArrayList<Table>();
	}
	
	public ResultSet execute (String str) throws JSQLParserException {
		SQLParser parser = new SQLParser();
		SQLQuery sqlQuery = parser.parse(str);
		ResultSet resultSet = this.performQuery(sqlQuery);
		return resultSet;
	}
	
	public ResultSet performQuery(SQLQuery sqlQuery) {
		ResultSet resultSet = null;
		if (sqlQuery instanceof CreateTableQuery) {
			CreateTableQuery CTQ = (CreateTableQuery) sqlQuery;
			resultSet = this.createTable(CTQ);
		}
		
		if (sqlQuery instanceof InsertQuery) {
			InsertQuery IQ = (InsertQuery) sqlQuery;
			resultSet = this.insert(IQ);
		}
		
		if (sqlQuery instanceof SelectQuery) {
			SelectQuery SQ = (SelectQuery) sqlQuery;
			resultSet = this.select(SQ);
		}
		
		if (sqlQuery instanceof UpdateQuery) {
			UpdateQuery UQ = (UpdateQuery) sqlQuery;
			resultSet = this.update(UQ);
		}
		
		if (sqlQuery instanceof DeleteQuery) {
			DeleteQuery DQ = (DeleteQuery) sqlQuery;
			resultSet = this.delete(DQ);
		}
		
		return resultSet;
	}
	
	public ResultSet createTable(CreateTableQuery CTQ) {
		Table table = new Table(CTQ.getTableName(), CTQ.getColumnDescriptions(), CTQ.getPrimaryKeyColumn());
		table.primaryKeyChecks();
		this.allTables.add(table);
		return null;
	}
	
	public ResultSet insert(InsertQuery IQ) {
		//find table that query being run on
		int counter = 0;
		for (Table table : this.allTables) {
			if (table.tableName.equals(IQ.getTableName())) {
				counter++;
				Row row = table.addRow();//adds the new row
				//setting the default columns
				for (int j = 0; j < table.cds.length; j++) {
					if (table.cds[j].getHasDefault()) {
						DataEntry entry = new DataEntry(table.cds[j].getDefaultValue(), table.cds[j]);
						row.rowEntries[j] = entry;
					}
				}
				row = table.putInValues(IQ.getColumnValuePairs(), row);
			}
		}
		if (counter != 1) {
			throw new IllegalArgumentException("You tried to insert into a table which does not exist");
		}
		return null;
	}
	
	//2.rounding of sum/ figure out the whole number lengths/fractional
	public ResultSet select(SelectQuery SQ) {
		ResultSet resultSet = null;
		for (Table table : this.allTables) {
			if (SQ.getFromTableNames()[0].equals(table.tableName)) {
				ArrayList<ColumnDescription> selectedColumns = table.addSelectedColumns(SQ);
				List<Row> returnedRows = table.addSelectRows(SQ, selectedColumns);
				if(SQ.isDistinct()) {
					returnedRows = table.distinct(selectedColumns, returnedRows);
				}
				List<SelectColumn> allSelectColumns = new ArrayList<SelectColumn>();
				for(ColumnDescription cd : selectedColumns) {
					SelectColumn selectColumn = new SelectColumn(returnedRows, cd, cd.getColumnName(), SQ.getFunctions(), SQ.getOrderBys());
					allSelectColumns.add(selectColumn);
				}
				for(int i = 0; i < allSelectColumns.size(); i++) {
					if (allSelectColumns.get(i).hasFunction()){
						allSelectColumns.get(i).performFunction(allSelectColumns.get(i).getFunction());
					}
				}
				table.extendFunctionColumns(allSelectColumns);
				if(SQ.getOrderBys().length > 0) {
					OrderByTable newTable = new OrderByTable((ArrayList<Row>)returnedRows, (ArrayList<SelectColumn>)allSelectColumns, SQ.getOrderBys());
					newTable.doOrderBys();
					allSelectColumns = newTable.getSelectColumns();
				}
			resultSet = new ResultSet(allSelectColumns, SQ);
			}	
		}
		return resultSet;
	}	
	
	public ResultSet update(UpdateQuery UQ) {
		for (Table table : this.allTables) {
			if (table.tableName.equals(UQ.getTableName())) {
				try {
					Where where = new Where (table.cds);
					List<Row> temp = new ArrayList<Row>(table.rows);
					List<Row> returnedRows = where.where((ArrayList<Row>) temp, UQ.getWhereCondition());
					for (Row row : returnedRows) {
						table.putInValues(UQ.getColumnValuePairs(), row);
					}
				}
				catch (NullPointerException e) {
					for (Row row : table.rows) {
						table.putInValues(UQ.getColumnValuePairs(), row);
					}
					return null;
				}
			}
		}
		return null;
	}
	
	public ResultSet delete(DeleteQuery DQ) {
		for (Table table : this.allTables) {
			if (table.tableName.equals(DQ.getTableName())) {
				try {
					Where where = new Where(table.cds);
					List<Row> temp = new ArrayList<Row>(table.rows);
					List<Row> returnedRows = where.where((ArrayList<Row>) temp, DQ.getWhereCondition());
					List<Integer> indecesToBeDeleted = new ArrayList<Integer>();
					for (Row row : returnedRows) {
						indecesToBeDeleted.add(table.findIndex(row));
					}
					indecesToBeDeleted = table.sortIntegers((ArrayList<Integer>) indecesToBeDeleted);
					int size = indecesToBeDeleted.size() - 1;
					for (int j = size; j >= 0; j--) {
						int index = indecesToBeDeleted.get(j);
						table.rows.remove(index);
					}
				}
				catch (NullPointerException e) {
					int size = table.rows.size();
					for (int i = size - 1; i >= 0; i--) {
						table.rows.remove(i);
					}
					return null;
				}
			}
		}
		return null;
	}
}
