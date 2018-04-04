package edu.yu.cs.ds.finalproject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	
	
	public ResultSet select(SelectQuery SQ) {
		String[] sqTables = SQ.getFromTableNames();
		List<ColumnDescription> selectedColumns = new ArrayList<ColumnDescription>();
		for (Table table : this.allTables) {
			for (String sqTableName : sqTables) {
				if (sqTableName.equals(table.tableName)) {
					selectedColumns = table.getSelectColumns(SQ.getSelectedColumnNames());
				}
			}	
		}
		return null;
	}
	
	public ResultSet update(UpdateQuery UQ) {
		for (Table table : this.allTables) {
			if (table.tableName.equals(UQ.getTableName())) {
				try {
					Where where = new Where (table);
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
					Where where = new Where(table);
					List<Row> temp = new ArrayList<Row>(table.rows);
					List<Row> returnedRows = where.where((ArrayList<Row>) temp, DQ.getWhereCondition());
					List<Integer> indecesToBeDeleted = new ArrayList<Integer>();
					for (Row row : returnedRows) {
						indecesToBeDeleted.add(table.findIndex(row));
					}
					for(int i : indecesToBeDeleted) {
						table.rows.remove(i);
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
