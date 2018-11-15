package edu.yu.cs.ds.finalproject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;

public class Column {
	public ColumnDescription cd;
	public boolean hasIndex = false;
	public BTree bTree;
	public Table table;
	public Column (ColumnDescription col, Table table) {
		this.cd = col;
		this.table = table;
	}
	
	public void createIndex() {
		this.hasIndex = true;
		this.bTree = new BTree(this);
	}
}
