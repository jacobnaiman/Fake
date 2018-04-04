package edu.yu.cs.ds.finalproject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;

public class Where {
	private ColumnDescription[] cds;
	public Where (Table table) {
		this.cds = table.cds;
	}

	public ArrayList<Row> where (List<Row> rows, Condition condition) {
		//if one thing and dont need to recursively search
		if (!(condition.getLeftOperand() instanceof Condition && condition.getRightOperand() instanceof Condition)) {
			return this.checkConditions(rows, condition);
		}
		else {
			if (condition.getOperator().equals(Condition.Operator.AND)) {
				rows = this.where(rows, (Condition) condition.getLeftOperand());
				rows = this.where(rows, (Condition) condition.getRightOperand());
				return (ArrayList<Row>) rows;
			}
			else {
				Set<Row> tempSet = new HashSet<Row>();
				List<Row> temp1 = this.where(new ArrayList<Row>(rows), (Condition) condition.getLeftOperand());
				for (Row row : temp1) {
					tempSet.add(row);
				}
				List<Row> temp2 = this.where(new ArrayList<Row>(rows), (Condition) condition.getRightOperand());
				for (Row row : temp2) {
					tempSet.add(row);
				}
				return new ArrayList<Row>(tempSet);
			}
		}
	}
	
	private ArrayList<Row> checkConditions(List<Row> rows, Condition condition) {
		if (condition.getOperator().equals(Condition.Operator.EQUALS)) {
			return this.checkEquals(rows, condition);
		}
		if (condition.getOperator().equals(Condition.Operator.NOT_EQUALS)) {
			return this.checkNotEquals(rows, condition);
		}
		if (condition.getOperator().equals(Condition.Operator.LESS_THAN)) {
			return this.checkLessThan(rows, condition);
		}
		if (condition.getOperator().equals(Condition.Operator.LESS_THAN_OR_EQUALS)) {
			return this.checkLessThanOrEquals(rows, condition);
		}
		if (condition.getOperator().equals(Condition.Operator.GREATER_THAN)) {
			return this.checkGreaterThan(rows, condition);
		}
		if (condition.getOperator().equals(Condition.Operator.GREATER_THAN_OR_EQUALS)) {
			return this.checkGreaterThanOrEquals(rows, condition);
		}
		return null;
	}
	
	private int findColumnIndex(Condition condition) {
		int index = -1;
		for (int i = 0; i < this.cds.length; i++) {
			if (condition.getLeftOperand().toString().equals(this.cds[i].getColumnName())) {
				index = i;
			}
		}
		if (index < 0) {
			throw new IllegalArgumentException("You tried to update a column which does not exist");
		}
		return index;
	}
	
	private ArrayList<Row> checkEquals(List<Row> rows, Condition condition) {
		int index = this.findColumnIndex(condition);
		for (int j = rows.size() - 1; j >= 0; j--) {
			if (!(rows.get(j).rowEntries[index].value.toString().equals(condition.getRightOperand().toString()))) {
				rows.remove(j);
			}
		}
		return (ArrayList<Row>) rows;
	}
	
	private ArrayList<Row> checkNotEquals(List<Row> rows, Condition condition) {
		int index = this.findColumnIndex(condition);
		for (int j = rows.size() - 1; j >= 0; j--) {
			if (rows.get(j).rowEntries[index].value.toString().equals(condition.getRightOperand().toString())) {
				rows.remove(j);
			}
		}
		return (ArrayList<Row>) rows;
	}
	
	private ArrayList<Row> checkLessThan(List<Row> rows, Condition condition) {
		int index = this.findColumnIndex(condition);
		for (int j = rows.size() - 1; j >= 0; j--) {
			if ((rows.get(j).rowEntries[index].value.toString().compareTo(condition.getRightOperand().toString()) >= 0)) {
				rows.remove(j);
			}
		}
		return (ArrayList<Row>) rows;
	}
	
	private ArrayList<Row> checkLessThanOrEquals(List<Row> rows, Condition condition) {
		int index = this.findColumnIndex(condition);
		for (int j = rows.size() - 1; j >= 0; j--) {
			if ((rows.get(j).rowEntries[index].value.toString().compareTo(condition.getRightOperand().toString()) > 0)) {
				rows.remove(j);
			}
		}
		return (ArrayList<Row>) rows;
		
	}
	
	private ArrayList<Row> checkGreaterThan(List<Row> rows, Condition condition) {
		int index = this.findColumnIndex(condition);
		for (int j = rows.size() - 1; j >= 0; j--) {
			if ((rows.get(j).rowEntries[index].value.toString().compareTo(condition.getRightOperand().toString()) <= 0)) {
				rows.remove(j);
			}
		}
		return (ArrayList<Row>) rows;
	}
	
	private ArrayList<Row> checkGreaterThanOrEquals(List<Row> rows, Condition condition) {
		int index = this.findColumnIndex(condition);
		for (int j = rows.size() - 1; j >= 0; j--) {
			if ((rows.get(j).rowEntries[index].value.toString().compareTo(condition.getRightOperand().toString()) < 0)) {
				rows.remove(j);
			}
		}
		return (ArrayList<Row>) rows;
	}
}