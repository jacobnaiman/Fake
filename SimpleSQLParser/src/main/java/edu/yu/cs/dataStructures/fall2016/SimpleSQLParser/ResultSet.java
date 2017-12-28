package edu.yu.cs.dataStructures.fall2016.SimpleSQLParser;

import java.util.ArrayList;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription.DataType;

public class ResultSet{
	ArrayList<Row> results;
	String[] columnNames;
	DataType[] columnTypes;

	public ResultSet(int i) { //the amount of columns it is
		results = new ArrayList<Row>();
		columnNames = new String[i];
		columnTypes = new DataType[i];
	}
	
	public void addToResult(Row row) {
		results.add(row);
	}
	
	public void addColumnName(String col, int i) {
		columnNames[i] = col;
	}
	public void addColumnType(DataType col, int i) {
		columnTypes[i] = col;
	}

	public void printFunction() {
		System.out.println("RESULT SET (Function)");
		System.out.println(results.get(0).get(0));
	}
	
	public void printBoolean() {
		System.out.println("RESULT SET");
		System.out.println(results.get(0).get(0));
		System.out.println();
	}
	
	public void printResults() { 
		System.out.println("RESULT SET (SELECT)");
		for(int i =0; i < columnNames.length; i++) {
			System.out.print("ColumnName" + (i + 1) + ": " + columnNames[i] + ". ");
		}
		System.out.println();
		for(int i =0; i < columnTypes.length; i++) {
			System.out.print("Column Type" + (i + 1) + ": " + columnTypes[i] + ". ");
		}
		System.out.println();
 		for(int i = 0; i < results.size(); i++) { //how many rows there are
			System.out.print("Row" + (i + 1) + ": ");
 			for(int j = 0; j < results.get(i).rowSize(); j++) {
 				System.out.print(results.get(i).get(j) + " "); //how many values in each row
 				}
 			System.out.println();

 			}
 		System.out.println();
		}
}
