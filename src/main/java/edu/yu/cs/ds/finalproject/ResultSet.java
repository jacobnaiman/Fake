package edu.yu.cs.ds.finalproject;

import java.util.ArrayList;
import java.util.List;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnID;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SQLQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery;

public class ResultSet {
	public ArrayList<SelectColumn> columns;
	public boolean successful;
	public ColumnID[] columnNames;
	
	public ResultSet(List<SelectColumn> allSelectColumns, SQLQuery query) {
		if(query instanceof SelectQuery) {
			this.columns = (ArrayList<SelectColumn>) allSelectColumns;
			this.extendFunctionColumns();
			//SelectQuery select = (SelectQuery) SQLQuery;
			this.columnNames = ((SelectQuery) query).getSelectedColumnNames();
		}
		else {
			this.successful = true;
		}
	}
	
	private void extendFunctionColumns() {
		int maxLength = 0;
		for(SelectColumn column : columns) {
			if(column.getSelectColumn().size() > maxLength) {
				maxLength = column.getSelectColumn().size();
			}
		}
		if(maxLength > 1) {
			for(SelectColumn column : columns) {
				if(column.hasFunction()) {
					DataEntry entry = column.getSelectColumn().get(0);
					for(int i = 1; i < maxLength; i++) {
						column.getSelectColumn().add(entry);
					}
				}
			}
		}
	}
	
	public void print() {
		if(this.columns.equals(null)) {
			System.out.println(this.successful);
		}
		else {
			//System.out.printf(format, args)
			String title = "";
			for(SelectColumn col : this.columns) {
				if(col.hasFunction()) {
					title = col.getFunction().function.toString() + "("+col.getColumnName()+")";
					System.out.printf("%25s", title);
				}
				else {
					System.out.printf("%25s", col.getCD().getColumnName());
				}
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
}
