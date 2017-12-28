package edu.yu.cs.dataStructures.fall2016.SimpleSQLParser;

import java.util.ArrayList;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Table;
import net.sf.jsqlparser.JSQLParserException;

public class DataBase {
	static SQLParser parser = new SQLParser();
	private ArrayList<Table> dataBase;

	public DataBase() {
		dataBase = new ArrayList<Table>();
	}
	/*
	 * MOST IMPORTANT METHOD FOR THE PROJECT
	 */
	public ResultSet execute(String SQL) throws JSQLParserException {
		System.out.println();
		System.out.println(SQL);
		if(SQL.contains("CREATE TABLE")) {
			createTable((CreateTableQuery) parser.parse(SQL));	
		}
		else if(SQL.contains("INSERT INTO")) {
			return insert((InsertQuery) parser.parse(SQL));	
		}
		else if(SQL.contains("CREATE INDEX")) {
			return index((CreateIndexQuery) parser.parse(SQL));	
		}
		else if(SQL.contains("select")) {
			return select((SelectQuery) parser.parse(SQL));	
		}
		else if(SQL.contains("DELETE")) {
			return delete((DeleteQuery) parser.parse(SQL));	
		}
		else if(SQL.contains("UPDATE")) {
			return update((UpdateQuery) parser.parse(SQL));	
		}
		return Table.trueResult();
	}
	
	
	private void createTable(CreateTableQuery createTable) {
		try {
		Table newT = new Table(createTable);
		dataBase.add(newT); //stores the table in my arraylist of tables in my dataBase class
		}
		catch(Exception e) {
			e.printStackTrace();
			Table.falseResult();
		}
	}
	
	private ResultSet select(SelectQuery select) {
		try {
			for(int i = 0; i < dataBase.size(); i++) {
				if(dataBase.get(i).getTableName().equals(select.getFromTableNames()[0])) { //check if table name matches 
					dataBase.get(i).startSelect(select); //always only going to be one table getting from for this project
				}
				else {
					Table.falseResult();
					throw new IllegalArgumentException("You referenced a table not in the database!");
				}
			}	
		}
		catch(Exception e) {
			e.printStackTrace();
			return Table.falseResult();
		}
		return null;
	}
	
	private ResultSet insert(InsertQuery insert) {
		try {
			for(int i = 0; i < dataBase.size(); i++) {
				if(dataBase.get(i).getTableName().equals(insert.getTableName())) { //check if table name matches 
					dataBase.get(i).startInsert(insert); //always only going to be one table getting from for this project	
					return Table.trueResult();
				}
				else {
					Table.falseResult();
					throw new IllegalArgumentException("You referenced a table not in the database!");
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return Table.falseResult();
		}
		return null;
	}
	
	private ResultSet index(CreateIndexQuery create) {
		try {
			for(int i = 0; i < dataBase.size(); i++) {
				if(dataBase.get(i).getTableName().equals(create.getTableName())) { //check if table name matches 
					dataBase.get(i).startIndex(create); //always only going to be one table getting from for this project		
					return Table.trueResult();
				}	
				else {
					Table.falseResult();
					throw new IllegalArgumentException("You referenced a table not in the database!");
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return Table.falseResult();
		}
		return null;
	}
	
	private ResultSet update(UpdateQuery update) {
		try {
			for(int i = 0; i < dataBase.size(); i++) {
				if(dataBase.get(i).getTableName().equals(update.getTableName())) { //check if table name matches 
					dataBase.get(i).startUpdate(update); //always only going to be one table getting from for this project
					return Table.trueResult();
				}
				else {
					Table.falseResult();
					throw new IllegalArgumentException("You referenced a table not in the database!");
				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
			return Table.falseResult();
			
		}
		return null;
	}
	
	private ResultSet delete(DeleteQuery delete) {
		try {
			for(int i = 0; i < dataBase.size(); i++) {
				if(dataBase.get(i).getTableName().equals(delete.getTableName())) { //check if table name matches 
					dataBase.get(i).startDelete(delete); //always only going to be one table getting from for this project	
					return Table.trueResult();
				}
				else {
					Table.falseResult();
					throw new IllegalArgumentException("You referenced a table not in the database!");
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return Table.falseResult();
		}
		return null;
	}
}
