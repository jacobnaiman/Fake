package edu.yu.cs.ds.finalproject;

import java.util.ArrayList;
import java.util.List;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;
import net.sf.jsqlparser.JSQLParserException;

public class Database {
	public List<Table> allTables;
	
	/**
	 * Constructs a database with a list of tables
	 */
	public Database() {
		this.allTables = new ArrayList<Table>();
	}
	
	/**
	 * This method takes an SQL string parses it and returns a resultset
	 * @param str
	 * @return resultSet
	 * @throws JSQLParserException
	 */
	public ResultSet execute (String str) throws JSQLParserException {
		SQLParser parser = new SQLParser();
		SQLQuery sqlQuery = parser.parse(str);
		try {
			ResultSet resultSet = this.performQuery(sqlQuery);
			return resultSet;
		}
		catch (RuntimeException e) {
			return new ResultSet(e);
		}
	}
	
	/**
	 * This method takes the SQLQuery and funnels it the method for the specific type of query it is
	 * @param sqlQuery
	 * @return resultSet
	 * @throws JSQLParserException
	 */
	public ResultSet performQuery(SQLQuery sqlQuery) throws JSQLParserException {
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
		if (sqlQuery instanceof CreateIndexQuery) {
			CreateIndexQuery CIQ = (CreateIndexQuery) sqlQuery;
			resultSet = this.createIndex(CIQ);
		}
		return resultSet;
	}
	
	/**
	 * creates an index on a given column
	 * @param CIQ
	 * @return a result set with a boolean value if it is successful or not
	 */
	private ResultSet createIndex(CreateIndexQuery CIQ) {
		boolean foundTable = false;
		for(Table table : this.allTables) {
			if(table.tableName.equals(CIQ.getTableName())) {
				foundTable = true;
				boolean foundColumn = false;
				for(Column col : table.columns) {
					if(col.cd.getColumnName().equals(CIQ.getColumnName())) {
						foundColumn = true;
						col.createIndex();
						table.indexedColumns.add(col.bTree);
						if(table.rows.size() == 0) {
							continue;
						}
						int index = table.rows.get(0).findColumnIndex(col.cd.getColumnName());
						for(Row row : table.rows) {
							col.bTree.put(row.rowEntries[index].value, row, false);
						}
					}
				}
				if(!foundColumn) {
					throw new IllegalArgumentException("You tried to index a column which does not exist");
				}
			}
		}
		if(!foundTable) {
			throw new IllegalArgumentException("You tried to index a column in a table which does not exist");
		}
		return new ResultSet();
	}

	/**
	 * This method creates a table
	 * @param CTQ - createtablequery
	 * @return a result set containing the column names
	 * @throws JSQLParserException
	 */
	public ResultSet createTable(CreateTableQuery CTQ) throws JSQLParserException {
		Table table = new Table(CTQ.getTableName(), CTQ.getColumnDescriptions(), CTQ.getPrimaryKeyColumn());
		table.primaryKeyChecks();
		this.allTables.add(table);
		this.execute("CREATE INDEX "+table.primaryKey.getColumnName()+"_Index ON "+table.tableName+" ("+table.primaryKey.getColumnName()+")");
		return new ResultSet(CTQ);
	}
	
	/**
	 * Inserts a row into a table
	 * @param IQ - insert query
	 * @return resultSet with a boolean value true or false
	 */
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
				for(BTree bTree : table.indexedColumns) {
					int index = row.findColumnIndex(bTree.column.cd.getColumnName());
					bTree.put((Comparable) row.rowEntries[index].value, row, false);
				}
			}
		}
		if (counter != 1) {
			throw new IllegalArgumentException("You tried to insert into a table which does not exist");
		}
		return new ResultSet();
	}
	
	/**
	 * Select either columns or functions from a table
	 * @param SQ
	 * @return a resultset containing the column names, data types and columns
	 */
	public ResultSet select(SelectQuery SQ) {
		boolean foundTable = false;
		ResultSet resultSet = null;
		for (Table table : this.allTables) {
			if (SQ.getFromTableNames()[0].equals(table.tableName)) {
				foundTable = true;
				ColumnID[] columnIDs = SQ.getSelectedColumnNames();
				ArrayList<ColumnDescription> selectedColumns = table.addSelectedColumns(SQ);
				List<Row> returnedRows = table.addSelectRows(SQ, selectedColumns);
				List<SelectColumn> allSelectColumns = table.gatherSelectColumns(SQ, returnedRows, selectedColumns, columnIDs);
				allSelectColumns = table.performFunctions(SQ, allSelectColumns);
				if(SQ.getOrderBys().length > 0) {
					OrderByTable newTable = new OrderByTable((ArrayList<Row>)table.rows, (ArrayList<SelectColumn>)allSelectColumns, SQ.getOrderBys());
					newTable.doOrderBys();
					returnedRows = newTable.putInOrder(returnedRows);
					allSelectColumns = newTable.getSelectColumns();
				}
			resultSet = new ResultSet(allSelectColumns, SQ);
			}	
		}
		if (!foundTable) {
			throw new IllegalArgumentException("You tried to select from a table which does not exist");
		}
		return resultSet;
	}	
	
	/**
	 * updates values within a table
	 * @param UQ - an update query
	 * @return returns a resultset with a boolean value
	 */
	public ResultSet update(UpdateQuery UQ) {
		boolean foundTable = false;
		for (Table table : this.allTables) {
			if (table.tableName.equals(UQ.getTableName())) {
				foundTable = true;
				try {//if there is a where condition it filters the rows
					Where where = new Where (table.cds, table.columns, UQ, table.indexedColumns);
					List<Row> temp = new ArrayList<Row>(table.rows);
					List<Row> returnedRows = where.where((ArrayList<Row>) temp, UQ.getWhereCondition());
					for (Row row : returnedRows) {//uses the same method as insert so it does all the proper checks on the data
						table.putInValues(UQ.getColumnValuePairs(), row);
					}
				}
				catch (NullPointerException e) {//if the where is null it updates all the rows
					for(BTree btree: table.indexedColumns) {
						btree.updateAll(UQ);
					}
					for (Row row : table.rows) {//uses the same method as insert so it does all the proper checks on the data
						table.putInValues(UQ.getColumnValuePairs(), row);
					}
				}
			}
		}
		if (!foundTable) {
			throw new IllegalArgumentException("You tried to update a table which does not exist");
		}
		return new ResultSet();
	}
	
	/**
	 * Deletes either all rows from a table or deletes the rows that meet a condition from a table
	 * @param DQ
	 * @return a result set with a boolean value
	 */
	public ResultSet delete(DeleteQuery DQ) {
		boolean foundTable = false;
		for (Table table : this.allTables) {
			if (table.tableName.equals(DQ.getTableName())) {
				foundTable = true;
				try {//if the where is not null it will return the rows that meet the condition
					Where where = new Where(table.cds, table.columns, DQ, table.indexedColumns);
					List<Row> temp = new ArrayList<Row>(table.rows);
					List<Row> returnedRows = where.where((ArrayList<Row>) temp, DQ.getWhereCondition());
					table.deleteRows(returnedRows);
				}
				catch (NullPointerException e) {//if there is no where delete all rows
					for(BTree bTree : table.indexedColumns) {
						bTree.clear((ArrayList) table.rows);
					}
					int size = table.rows.size();
					for (int i = size - 1; i >= 0; i--) {
						table.rows.remove(i);
					}
				}
			}
		}
		if (!foundTable) {
			throw new IllegalArgumentException("You tried to delete from a table which does not exist");
		}
		return new ResultSet();
	}
}
