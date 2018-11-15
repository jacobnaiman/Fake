package edu.yu.cs.ds.finalproject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.DeleteQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SQLQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.UpdateQuery;

public class Where {
	private ColumnDescription[] cds;
	private ArrayList<Column> columns;
	private SQLQuery query;
	private ArrayList<BTree> indexedColumns;
	private ArrayList<String> conditionColumns;
	
	/**
	 * constructs a where object and initializes the fields so that it can be determined 
	 * which rows satisfy the necessary conditions
	 * @param cds - column descriptions
	 * @param columns - columns
	 * @param query - the query
	 * @param indexedColumns - the columns which the table has indexed
	 */
	public Where (ColumnDescription[] cds, ArrayList<Column> columns, SQLQuery query, ArrayList<BTree> indexedColumns) {
		this.cds = cds;
		this.columns = columns;
		this.query = query;
		this.indexedColumns = indexedColumns;
		this.conditionColumns = new ArrayList<String>();
	}
	
	/**
	 * Takes a list of rows and filters out the rows which don't satisfy the conditions
	 * @param rows
	 * @param condition
	 * @return rows that meet conditions
	 */
	public ArrayList<Row> where(List<Row> rows, Condition condition) {
		ArrayList<Row> returnRows = this.recurse(rows, condition);
		for(BTree bTree : this.indexedColumns) {
			if(this.query instanceof DeleteQuery) {
				bTree.delete(returnRows, this.query, this.conditionColumns);
			}
			else if(this.query instanceof UpdateQuery) {
				bTree.update(returnRows, this.conditionColumns, (UpdateQuery) this.query);
			}
		}
		return returnRows;
	}
	
	/**
	 * recursively searches through the condition and modifies the list of rows
	 * @param rows
	 * @param condition
	 * @return an arraylist of rows
	 */
	public ArrayList<Row> recurse (List<Row> rows, Condition condition) {
		//if one operand then dont need to recursively search
		if (!(condition.getLeftOperand() instanceof Condition && condition.getRightOperand() instanceof Condition)) {
			this.conditionColumns.add(condition.getLeftOperand().toString());
			return this.checkConditions(rows, condition);	
		}
		else {//recurses through the condition
			//if it is an and it recursively searches the right and left operand and removes the rows which don't satisfy either
			if (condition.getOperator().equals(Condition.Operator.AND)) {
				rows = this.recurse(rows, (Condition) condition.getLeftOperand());
				rows = this.recurse(rows, (Condition) condition.getRightOperand());
			}
			//if it is an or it filters out the ones that dont meet an individual condition then merges the two into a set so that if it met one of the two conditions it is still present
			else if (condition.getOperator().equals(Condition.Operator.OR)) {
				Set<Row> tempSet = new HashSet<Row>();
				List<Row> temp1 = this.recurse(new ArrayList<Row>(rows), (Condition) condition.getLeftOperand());
				for (Row row : temp1) {
					tempSet.add(row);
				}
				List<Row> temp2 = this.recurse(new ArrayList<Row>(rows), (Condition) condition.getRightOperand());
				for (Row row : temp2) {
					tempSet.add(row);
				}
				rows = new ArrayList<Row>(tempSet);
			}
			else {
				throw new IllegalArgumentException("You compounded two conditions without using an AND or OR");
			}
		}
		return (ArrayList<Row>) rows;
	}
	
	/**
	 * Funnels the condition to the proper method based on the operand of the condition
	 * @param rows
	 * @param condition
	 * @return the modified arraylist of rows
	 */
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
	
	/**
	 * finds the index of the inserted column from the arraylist of columns 
	 * to determine the index of the column description in the condition
	 * @param condition
	 * @return integer index of the column in the condition
	 */
	private int findColumnDescriptionIndex(Condition condition) {
		int index = -1;
		for (int i = 0; i < this.cds.length; i++) {
			if (condition.getLeftOperand().toString().equals(this.cds[i].getColumnName())) {
				index = i;
				if((condition.getRightOperand().toString().equals("null"))) {
					break;
				}
				DataEntry entry = new DataEntry(condition.getRightOperand().toString(), this.cds[i]);
				entry.performValueChecks(this.cds[i]);
			}
		}
		if (index < 0) {
			throw new IllegalArgumentException("You tried to update a column which does not exist");
		}
		return index;
	}
	
	/**
	 * finds the index of the inserted column from the arraylist of columns 
	 * to determine if the column is indexed or not
	 * @param condition
	 * @return integer index of the column in the condition
	 */
	private int findColumnIndex(Condition condition) {
		int index = -1;
		for (int i = 0; i < this.columns.size(); i++) {
			if (condition.getLeftOperand().toString().equals(this.columns.get(i).cd.getColumnName())) {
				index = i;
				return index;
			}
		}
		for (int i = 0; i < this.columns.size(); i++) {
			if (condition.getRightOperand().toString().equals(this.columns.get(i).cd.getColumnName())) {
				index = i;
				return index;
			}
		}
		if (index < 0) {
			throw new IllegalArgumentException("You tried to update a column which does not exist");
		}
		return index;
	}
	
	/**
	 * Checks if the column specified in the condition satisfies the value 
	 * in the proper position in the rows if the operand is equals. 
	 * Removes the row if it doesn't satisfy the condition
	 * @param rows
	 * @param condition
	 * @return the arraylist of rows
	 */
	private ArrayList<Row> checkEquals(List<Row> rows, Condition condition) {
		int colIndex = this.findColumnIndex(condition);
		if(this.columns.get(colIndex).hasIndex) {
			rows = new ArrayList<Row>(this.columns.get(colIndex).bTree.get(condition.getRightOperand().toString()));
		}
		else {
			int index = rows.get(0).findColumnIndex(condition.getLeftOperand().toString());
			for (int j = rows.size() - 1; j >= 0; j--) {
				if (!(rows.get(j).rowEntries[index].value.equals(condition.getRightOperand().toString()))) {
					rows.remove(j);
				}
			}
		}
		return (ArrayList<Row>) rows;
	}
	
	/**
	 * Checks if the column specified in the condition satisfies the value 
	 * in the proper position in the rows if the operand is notequals. 
	 * Removes the row if it doesn't satisfy the condition
	 * @param rows
	 * @param condition
	 * @return the arraylist of rows
	 */
	private ArrayList<Row> checkNotEquals(List<Row> rows, Condition condition) {
		if(rows.size() == 0) {
			return (ArrayList<Row>) rows;
		}
		int index = rows.get(0).findColumnIndex(condition.getLeftOperand().toString());
		for (int j = rows.size() - 1; j >= 0; j--) {
			if (rows.get(j).rowEntries[index].value.toString().equals(condition.getRightOperand().toString())) {
				rows.remove(j);
			}
		}
		return (ArrayList<Row>) rows;
	}
	
	/**
	 * Checks if the column specified in the condition satisfies the value 
	 * in the proper position in the rows if the operand is less than. 
	 * Removes the row if it doesn't satisfy the condition
	 * @param rows
	 * @param condition
	 * @return the arraylist of rows
	 */
	private ArrayList<Row> checkLessThan(List<Row> rows, Condition condition) {
		int colIndex = this.findColumnIndex(condition);
		if(this.columns.get(colIndex).hasIndex) {
			rows = this.columns.get(colIndex).bTree.getLess(condition.getRightOperand().toString());
		}
		if(rows.size() == 0) {
			return (ArrayList<Row>) rows;
		}
		int index = rows.get(0).findColumnIndex(condition.getLeftOperand().toString());
		for (int j = rows.size() - 1; j >= 0; j--) {
			if ((rows.get(j).rowEntries[index].value.toString().compareTo(condition.getRightOperand().toString()) >= 0)) {
				rows.remove(j);
			}
		}
		return (ArrayList<Row>) rows;
	}
	
	/**
	 * Checks if the column specified in the condition satisfies the value 
	 * in the proper position in the rows if the operand is less than or equals. 
	 * Removes the row if it doesn't satisfy the condition
	 * @param rows
	 * @param condition
	 * @return the arraylist of rows
	 */
	private ArrayList<Row> checkLessThanOrEquals(List<Row> rows, Condition condition) {
		int colIndex = this.findColumnIndex(condition);
		if(this.columns.get(colIndex).hasIndex) {
			rows = this.columns.get(colIndex).bTree.getLessOrEqual(condition.getRightOperand().toString());
		}
		if(rows.size() == 0) {
			return (ArrayList<Row>) rows;
		}
		int index = rows.get(0).findColumnIndex(condition.getLeftOperand().toString());
		for (int j = rows.size() - 1; j >= 0; j--) {
			if ((rows.get(j).rowEntries[index].value.toString().compareTo(condition.getRightOperand().toString()) > 0)) {
				rows.remove(j);
			}
		}
		return (ArrayList<Row>) rows;
		
	}
	
	/**
	 * Checks if the column specified in the condition satisfies the value 
	 * in the proper position in the rows if the operand is greater than. 
	 * Removes the row if it doesn't satisfy the condition
	 * @param rows
	 * @param condition
	 * @return the arraylist of rows
	 */
	private ArrayList<Row> checkGreaterThan(List<Row> rows, Condition condition) {
		int colIndex = this.findColumnIndex(condition);
		if(this.columns.get(colIndex).hasIndex) {
			rows = this.columns.get(colIndex).bTree.getGreater(condition.getRightOperand().toString());
		}
		if(rows.size() == 0) {
			return (ArrayList<Row>) rows;
		}
		int index = rows.get(0).findColumnIndex(condition.getLeftOperand().toString());
		for (int j = rows.size() - 1; j >= 0; j--) {
			if ((rows.get(j).rowEntries[index].value.toString().compareTo(condition.getRightOperand().toString()) <= 0)) {
				rows.remove(j);
			}
		}
		return (ArrayList<Row>) rows;
	}
	
	/**
	 * Checks if the column specified in the condition satisfies the value 
	 * in the proper position in the rows if the operand is greater than or equals. 
	 * Removes the row if it doesn't satisfy the condition
	 * @param rows
	 * @param condition
	 * @return the arraylist of rows
	 */
	private ArrayList<Row> checkGreaterThanOrEquals(List<Row> rows, Condition condition) {
		int colIndex = this.findColumnIndex(condition);
		if(this.columns.get(colIndex).hasIndex) {
			rows = this.columns.get(colIndex).bTree.getGreaterOrEqual(condition.getRightOperand().toString());
		}
		if(rows.size() == 0) {
			return (ArrayList<Row>) rows;
		}
		int index = rows.get(0).findColumnIndex(condition.getLeftOperand().toString());
		for (int j = rows.size() - 1; j >= 0; j--) {
			if ((rows.get(j).rowEntries[index].value.toString().compareTo(condition.getRightOperand().toString()) < 0)) {
				rows.remove(j);
			}
		}
		return (ArrayList<Row>) rows;
	}
}