package edu.yu.cs.dataStructures.fall2016.SimpleSQLParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription.DataType;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition.Operator;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery.FunctionInstance;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery.FunctionName;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery.OrderBy;

public class Table {
	ArrayList<Row> table = new ArrayList<Row>();
	private ColumnDescription[] columnDescriptions;
    private ColumnDescription primaryKeyColumn; 
    private String tableName;
    private int numbOfCol;
    private HashMap<String, BTree> bTrees; //name of column mapped to index of that column, gonna need this for update and delete
    
    		public Table(CreateTableQuery table) { 
    			columnDescriptions = table.getColumnDescriptions(); 
    			tableName = table.getTableName();
    			numbOfCol = table.getColumnDescriptions().length;
    			bTrees = new HashMap<String, BTree>();
    			setPK(table.getPrimaryKeyColumn());
    	}
         /*
        	 * @param the primary key column of the table
        	 * if it has a default value throw an exception when constructing the table uaing a map
        	 */
        	private void setPK(ColumnDescription cd) {
        		if(cd.getHasDefault()) {
   				throw new IllegalArgumentException("The primary key column " + cd.getColumnName() +  " cannot have a default value because it must be unique!");			
        		}
        		else {
        			//create an index automatically on it
        			//figure out way to map all the indices to their columns in a specific table-using a map
        			primaryKeyColumn = cd;
        			String name = primaryKeyColumn.getColumnName();
        			if(primaryKeyColumn.getColumnType() == DataType.INT) {
        				BTree<Integer, ArrayList<Row>> newBT = new  BTree<Integer, ArrayList<Row>>(name); //initialize with name
            			bTrees.put(name, newBT); 
        			}
        			else if(primaryKeyColumn.getColumnType() == DataType.DECIMAL) {
        				BTree<Double, ArrayList<Row>> newBT = new BTree<Double, ArrayList<Row>>(name); //initialize with name, 3.75 holds an arraylist of rows now
        				//either put it in a map to start, or when insert create the index of the primary key column
            			bTrees.put(name, newBT); //can't add an empty btree to my btree arraylist maybe use a map, key is the name, value is the btree
        			}
        			else if(primaryKeyColumn.getColumnType() == DataType.VARCHAR) {
        				BTree<String, ArrayList<Row>> newBT = new BTree<String, ArrayList<Row>>(name); //initialize with name
            			bTrees.put(name, newBT);
        			}
        			else if(primaryKeyColumn.getColumnType() == DataType.BOOLEAN) {
        				BTree<Boolean, ArrayList<Row>> newBT = new BTree<Boolean, ArrayList<Row>>(name); //initialize with name
            			bTrees.put(name, newBT);
        			}
        		}
        	}
        		
        	
    		/*
    		 * @param takes an insertquery object
    		 * Goes through each column value pair and matches it's columnID name to the table's column names
    		 * then checks to make sure the data type is equal and converts the column value pair's value from string a certain data type
    		 * then checks other conditions that were set at create table
    		 * calls the insert row method which puts the entry object made here into the table
    		 */
    		public void startInsert(InsertQuery insertQuery) {
    			if(checkColumnNamesInsertUpdate(insertQuery.getColumnValuePairs())) {	//first check to make sure there were no columns put in that don't matcht he tables columns
    				Row row =	new Row(numbOfCol);
    				ColumnValuePair colValPairs[] = insertQuery.getColumnValuePairs();
    				for(ColumnValuePair cvp : colValPairs) {
    	    				for(int i = 0; i < numbOfCol; i++) {
    	    					if(cvp.getColumnID().getColumnName().equals(columnDescriptions[i].getColumnName())) {
    	    						continueInsert(cvp, i, row );
    	    					}
    	    				}
    				}
    				checkForNull(row); //account for null spots in the entry object
    				checkForUnique(row); //make sure all unique restrictions are followed
    				insertRow(row);
    				updateIndices(row); //update the indices if a column has one
    				}
    		}
    		
    		/*
    		 * @param, the number column of table, the row im creating, and the cvp im inserting
    		 */
    		public void continueInsert(ColumnValuePair cvp, int i, Row row) {
			//make another method here-refactor
			if(isInteger(cvp.getValue())) {
				if(columnDescriptions[i].getColumnType() == DataType.INT) {
					Integer num = (Integer)Integer.valueOf(cvp.getValue());
					row.addToRow(num, i);
				}
			}	   	    				
			else if(isDouble(cvp.getValue())) {
				if(columnDescriptions[i].getColumnType() == DataType.DECIMAL) { //check that the data types align
					checkDouble(cvp.getValue(), i); //						BE ABLE TO CATCH THESE 
					Double dbl = Double.valueOf(cvp.getValue());
					row.addToRow(dbl, i);
				}
			}
			else if(isBoolean(cvp.getValue())) {
				if(columnDescriptions[i].getColumnType() == DataType.BOOLEAN) { //check that the data types align
					Boolean bool = (Boolean)Boolean.valueOf(cvp.getValue().substring(1, cvp.getValue().length() - 1));
					row.addToRow(bool, i);
				}
			}   	   
			else { //idk if there is a way to check that its a string/it should be able to take anything
				if(columnDescriptions[i].getColumnType() == DataType.VARCHAR) { //if it isn't double, boolean or int then it's a string, and if the type is varchar, put it in here
					String str = String.valueOf(cvp.getValue());
					checkVarChar(str, i); 
					row.addToRow(str.substring(1, str.length() - 1), i); //stores the varchar without the single quotation marks
				}
			}
    		}
		
    		/*
    		 * @return a false resultSet
    		 */
    		static ResultSet falseResult() {
			ResultSet nope = new ResultSet(0);
			Row no = new Row(1);
			no.addToRow(false, 0);
			nope.addToResult(no);
			nope.printBoolean();
				return nope;
    		}
    		
    		/*
    		 * @return true resultSet
    		 */
    		static ResultSet trueResult() {
    			ResultSet YES = new ResultSet(0);
    			Row ya = new Row(1);
    			ya.addToRow(true, 0);
    			YES.addToResult(ya);
    			YES.printBoolean();
    				return YES;
        		}
    		/*
    		 * after I insert/update/delete a row, i need to check if i need to update the indices
    		 */
    		public void updateIndices(Row row) {
    			for(int i = 0; i < numbOfCol; i++) {
    				if(bTrees.containsKey(columnDescriptions[i].getColumnName())) { //check if there exists a bTree on a column
    					BTree BT = bTrees.get(columnDescriptions[i].getColumnName());
    						if(BT.get((Comparable) row.get(i)) == null) { //that key does not exist yet in the btree
    							ArrayList<Row> newVal = new ArrayList<Row>();
    							newVal.add(row);
    							BT.put((Comparable) row.get(i), newVal);
    					    	}
    						else { //there already exists this key in the btree, so then get the arraylist and add it to that arraylist of rows
    							ArrayList<Row> newVal = (ArrayList<Row>) BT.get((Comparable) row.get(i)); //maybe delete this now
    							BT.delete((Comparable) row.get(i)); //this is the key, i'm deleting its value, now delete the previous arraylist<Row> from btree!!!!!
    							newVal.add(row); //add the row to the arraylsit<Row> MAKE SURE IM NOT USING THE NEW VALUE AS A KEY TO DELETE THE OLD ARRAYLIST
    							BT.put((Comparable) row.get(i), newVal); //then add it back
    						}
    				}
    			}
    		}	
    			
    		
		/*
		 *@param array of column value pairs to be inserted 
		 * if the amount of column value pair names that are equal to table names is not the total amount of column 
		 * value pairs entered--> exception thrown, try to cach it and rpint out an error 
		 * colvalpairs, for insert and update
		 */
    		private boolean checkColumnNamesInsertUpdate(ColumnValuePair[] colValPairs) {
    			int counter = 0;
    			for(int i = 0; i < colValPairs.length; i++) {
    				for(int j = 0; j < numbOfCol; j++) {
    					if(colValPairs[i].getColumnID().getColumnName().equals(columnDescriptions[j].getColumnName())) {
    						counter++;
    	    				}
    	    			}    	    		
    	    		
    			}
    			if(counter != colValPairs.length) {
    				throw new IllegalArgumentException("Those column names look a bit off!");
    			}
    				return true;
    		}
    		
    		
    		/*
    		 * Takes the string and the table's column number and checks that columns VarChar Max
    		 */
    	    	private boolean checkVarChar(String s, int i) {
    	    		if((s.length() - 2) > columnDescriptions[i].getVarCharLength()) { //takes care of the single quotes outside of the string
    				throw new IllegalArgumentException("your " + columnDescriptions[i].getColumnName() + " VarChar is too long!");
    	    		}
    	    			return true;
    	    	}
    	    	
    	    	/*
    	    	 * @param the Double and the column's number
    	    	 * @returns true if it matches preset format
    	    	 */
    	    	private boolean checkDouble(String dbl, int i) {
    	    		String[] splitter = dbl.split("\\.");
    	    		if(splitter[0].length() == columnDescriptions[i].getWholeNumberLength() 
    	    		&& splitter[1].length() == columnDescriptions[i].getFractionLength()) {   // Before Decimal Count
    	    			return true;  
    	    		}
    	    		throw new IllegalArgumentException("Your decimal is not formatted correctly silly!");
    	    	}

    		
    	    	/*
    	    	 * @param Entry object 
    	    	 * checks if any spot in the entry array object is null
    	    	 * calls the dealWithNnull method for each position
    	    	 */
    		private void checkForNull(Row row) {
    			for(int i = 0; i < numbOfCol; i++) {
	    			if(row.get(i) == null) {
	    				row.addToRow(dealWithNull(i), i);
	    			}
	    		}
    		}
    		
    		/*
    		 * @param the number of the column that has the null value
    		 * checks if the column has a default value and sets it to it
    		 * if it doesn't have a default value and is not null then throws exception
    		 * otherwise leaves it as null
    		 */
    		private Object dealWithNull(int j) {
    				if(columnDescriptions[j].getHasDefault()) {
    					if(columnDescriptions[j].getColumnType() == DataType.INT) {
	    					Integer num = Integer.valueOf(columnDescriptions[j].getDefaultValue());
	    					return num;
    					}
    					else if(columnDescriptions[j].getColumnType() == DataType.DECIMAL) {
	    					Double dbl = Double.valueOf(columnDescriptions[j].getDefaultValue());
	    					return dbl;

    					}
    					else if(columnDescriptions[j].getColumnType() == DataType.BOOLEAN) {
	    					Boolean bool = Boolean.valueOf(columnDescriptions[j].getDefaultValue());
	    					return bool;

    					}
    					else if(columnDescriptions[j].getColumnType() == DataType.VARCHAR) {
	    					String str = String.valueOf(columnDescriptions[j].getDefaultValue());
	    					return str;

    					}
    				}	
    				else if(columnDescriptions[j].isNotNull()) { //if I get here, it means the value is null, and I have no default value, return a false resultset
    					throw new IllegalArgumentException("The column " + columnDescriptions[j].getColumnName() +  " can't be null, set a default value!");
    				}
    				else if(columnDescriptions[j].getColumnName().equals(primaryKeyColumn.getColumnName())) { //makes sure PrimaryKC is not null if it has no default value
    					throw new IllegalArgumentException("The primary key column " + columnDescriptions[j].getColumnName() +  " cannot be null!");
    				}
				return null; //no problem with adding a null value to the row
    		}
    		
    		/*
    		 * goes through each column and check if it is unique , only if the table size is greater than one
    		 * if a column is supposed to only contain unique values, then deal with that column 
    		 */
    		private void checkForUnique(Row row) {
    			if(table.size() >= 1) {
    				for(int i = 0; i < numbOfCol; i++) {
    					if((columnDescriptions[i].isUnique()) || (columnDescriptions[i].getColumnName().equals(primaryKeyColumn.getColumnName()))) {
	    				row.addToRow(dealWithUnique((row.get(i)), i), i);
    					}
    				}
    			}
	    	}
			
		/*
		 * if not unique throw an invalid argument exception
		 * @return object j is column number that isUnique
		 */
		private Object dealWithUnique(Object obj, int j) {
				for(int i = 0; i < table.size(); i++) { //how many rows there are
					if(!isDiff(obj, j, i)) {   //how many values in each entry row
						throw new IllegalArgumentException("The column " + columnDescriptions[j].getColumnName() 
															+  " has to be given only unique values! (check if " 
															+ columnDescriptions[j].getColumnName() +  " is the primary key Column)");
					}
				}
			
			return obj;
		}

		/*
		 * @param two objects to test for equality
		 * if one is null, they are not equal
		 * use this for testing if values are distinct 
		 * @return true if they are equal 
		 */
    		private static final boolean equalsWithNulls(Object a, Object b) {
    			if (a == b) return true;
    			    if ((a == null)||(b == null)) return false;
    			    return a.equals(b);
    			  }

    		/*
    		 * check if string is of integer type
    		 */
    		static boolean isInteger(String s) {
    			try { 
    				Integer.parseInt(s); 
    			} catch(NumberFormatException e) { 
    				return false; 
    			} catch(NullPointerException e) {
    				return false;
    			}
    			// only got here if we didn't return false
    			return true;
    		}
    		
    		/*
    		 * check if string is of double type
    		 */
    		 static boolean isDouble(String s) {
    			try
    			{
    				Double.parseDouble(s);
    			}
    			catch(NumberFormatException e)
    			{
    				return false;
    			}
    			return true;
    		}
    		
    		/*
    		 * check if a string is of boolean type
    		 */
     	 static boolean isBoolean(String s) {
     		s = s.toLowerCase();
     	    if(s.substring(1, s.length() - 1).equals("true") || s.substring(1, s.length() - 1).equals("false")) {
     	    		return true;
     	    }
     	    return false;
     	} 
     	
     	/*
     	 * @return number of columns in the table
     	 */
     	public int getNumbOfCol() {
     		return numbOfCol;
     	}
     	
     	/*
     	 * @return the table name
     	 */
     	public String getTableName() {
     		return tableName;
     	}
     	
     	/*
     	 * @param a Row object
     	 * adds the Entry object to the table's arrayList of Entries
     	 */
     	private void insertRow(Row row) {
     		table.add(row);
     		//if does this then go to result class, and call a printInsert method
     		//returns a resultSet (a miniature version of a table) find out how to see if the queries put in dont match the table's column names?
     		//if doesn't refer to any tables also print a result class with false
     	}
     	
/******************************************
 * CREATE INDEX CODE STARTS HERE work on it, bc my btree actually works now, dont forget to put all rows with the same keys in the same key
******************************************/
     	//FIGURE OUT HOW TO STORE ALL ROWS WITH THE SAME VALUE IN THE ARRALIST OF ROWS, THEN PUT THAT IN FOR THT KEY
    	
     	/*
     	 * @param a create index query object
     	 * creating an index
     	 * cant have a key that is null, that row does not get indexed
     	 * any repeated key is condensed into one
     	 */
     	public void startIndex(CreateIndexQuery create) {  //need to make sure to add all of the rows with the same value in that column to the same arraylist, then put that arraylist in the value for that key
     		checkIndexColumnName(create);
     		//creating an index on the jth column
     		for(int j = 0; j < numbOfCol; j++) {
     			if(create.getColumnName().equals(columnDescriptions[j].getColumnName())) {
     	     		alreadyIndex(columnDescriptions[j].getColumnName());
     	     		String name = columnDescriptions[j].getColumnName();
     				 if(columnDescriptions[j].getColumnType() == DataType.INT) { 
     					createIntegerIndex(name, j);
         				}
     				else if(columnDescriptions[j].getColumnType() == DataType.BOOLEAN) { 
     					createBooleanIndex(name, j);
     					}
     				else if(columnDescriptions[j].getColumnType() == DataType.DECIMAL) { 
     					createDoubleIndex(name, j);
     					}
     				else if(columnDescriptions[j].getColumnType() == DataType.VARCHAR) { 
     					createStringIndex(name, j);
     				}
     			}
     		}
     	}
     	/*
     	 * throws exception if the column is already indexed
     	 */
     	public boolean alreadyIndex(String colName) {
     		if(bTrees.containsKey(colName)) {
     			throw new IllegalArgumentException("This column is already indexed!");
     		}
     		else {
     			return true;
     		}
     		
     	}
     	public boolean checkIndexColumnName(CreateIndexQuery create) {
     		for(int i = 0; i < numbOfCol; i++) {
     			if(create.getColumnName().equals(columnDescriptions[i].getColumnName())) {
     				return true;
     			}
     		}
     		throw new IllegalArgumentException("Awkward... " + create.getColumnName() + " is definitely not a column name, so you can't create an index on it!");
     	}
     	
     	/*
     	 * creates a Boolean index on the jth column of the table
     	 * @param columnName
     	 *
     	 */
     	private void createBooleanIndex(String name, int j) {
     		BTree<Boolean, ArrayList<Row>> newBT = new BTree<Boolean, ArrayList<Row>>(name); //create a new bTree/index for this column of type Integer
			for(int k = 0; k < table.size(); k++) { //for each row in the table
				if(isDiff(table.get(k).get(j), j, k)) { //if this is a distinct key, then put an arraylist of all other ones into the Btree as a keyvalue pair
					ArrayList<Row> rows = new ArrayList<Row>(); //create the new ArrayList<Row>
					rows.add(table.get(k));
						for(int i = (k + 1); i < table.size(); i++) { //can start number i at k, because the number wont appear before that row, since it is unique
							if(table.get(k).get(j).equals(table.get(i).get(j))) { //if the distinct value appears any more in that table's column in future rows
							//then add that row to this arrayList of rows as well! bc they share same bannerID
							rows.add(table.get(i));
							}
						}							//now i need to add that arrayList for the distinct number as a key and the arraylist as the value
					newBT.put((Boolean)table.get(k).get(j), rows);
				}
				}
			bTrees.put(name, newBT); //save the BTree with this table in a map
     	}
     	
     	/*
     	 * creates a String index on the jth column of the table
     	 */
     	private void createStringIndex(String name, int j) {
     		BTree<String, ArrayList<Row>> newBT = new BTree<String, ArrayList<Row>>(name); //create a new bTree/index for this column of type Integer
			for(int k = 0; k < table.size(); k++) { //for each row in the table
				if(isDiff(table.get(k).get(j), j, k)) { //if this is a distinct key, then put an arraylist of all other ones into the Btree as a keyvalue pair
					ArrayList<Row> rows = new ArrayList<Row>(); //create the new ArrayList<Row>
					rows.add(table.get(k));
						for(int i = (k + 1); i < table.size(); i++) { //can start number i at k, because the number wont appear before that row, since it is unique
							if(table.get(k).get(j).equals(table.get(i).get(j))) { //if the distinct value appears any more in that table's column in future rows
							//then add that row to this arrayList of rows as well! bc they share same bannerID
							rows.add(table.get(i));
							}
						}							//now i need to add that arrayList for the distinct number as a key and the arraylist as the value
					newBT.put((String)table.get(k).get(j), rows);
				}
				}
			bTrees.put(name, newBT); //save the BTree with this table in a map
     	}
     	
     	/*
     	 * creates a Double index on the jth column of the table
     	 */
     	private void createDoubleIndex(String name, int j) {
     		BTree<Double, ArrayList<Row>> newBT = new BTree<Double, ArrayList<Row>>(name); //create a new bTree/index for this column of type Integer
			for(int k = 0; k < table.size(); k++) { //for each row in the table
				if(isDiff(table.get(k).get(j), j, k)) { //if this is a distinct key, then put an arraylist of all other ones into the Btree as a keyvalue pair
					ArrayList<Row> rows = new ArrayList<Row>(); //create the new ArrayList<Row>
					rows.add(table.get(k));
						for(int i = (k + 1); i < table.size(); i++) { //can start number i at k, because the number wont appear before that row, since it is unique
							if(table.get(k).get(j).equals(table.get(i).get(j))) { //if the distinct value appears any more in that table's column in future rows
							//then add that row to this arrayList of rows as well! bc they share same bannerID
							rows.add(table.get(i));
							}
						}							//now i need to add that arrayList for the distinct number as a key and the arraylist as the value
					newBT.put((Double)table.get(k).get(j), rows);
				}
				}
			bTrees.put(name, newBT); //save the BTree with this table in a map
     	}
     	
     	/*
     	 * creates an Integer index on the jth column of the table
     	 *  	private boolean isDiff(Object obj, int columnNum, int currentRow) 
     	 */
     	private void createIntegerIndex(String name, int j) {
				BTree<Integer, ArrayList<Row>> newBT = new BTree<Integer, ArrayList<Row>>(name); //create a new bTree/index for this column of type Integer
				for(int k = 0; k < table.size(); k++) { //for each row in the table
					if(isDiff(table.get(k).get(j), j, k)) { //if this is a distinct key, then put an arraylist of all other ones into the Btree as a keyvalue pair
						ArrayList<Row> rows = new ArrayList<Row>(); //create the new ArrayList<Row>
						rows.add(table.get(k));
							for(int i = (k + 1); i < table.size(); i++) { //can start number i at k, because the number wont appear before that row, since it is unique
								if(table.get(k).get(j).equals(table.get(i).get(j))) { //if the distinct value appears any more in that table's column in future rows
								//then add that row to this arrayList of rows as well! bc they share same bannerID
								rows.add(table.get(i));
								}
							}							//now i need to add that arrayList for the distinct number as a key and the arraylist as the value
						newBT.put((Integer)table.get(k).get(j), rows);
					}
 				}
				bTrees.put(name, newBT); //save the BTree with this table in a map				
     	}
     	
     	/*
     	 * @return primaryKeyColumn
     	 */
     	public ColumnDescription getPrimaryKeyColumn() {
     		return primaryKeyColumn;
     	}
/******************************************
 * SELECT CODE STARTS HERE  incorporate into WHERE the btree IFF there is an index (if map with the column name doesn't return null)
 ******************************************
 */
     	
		/*
		 * For my OR statements
		 */
		public ArrayList<Row> union(ArrayList<Row> list1, ArrayList<Row> list2) {
	        Set<Row> set = new HashSet<Row>();
	        //for each row in each 
	        if(list1 != null) {
	        		for(int i = 0; i < list1.size(); i++) {
	        			set.add(list1.get(i));
	        		}
	        }
	        if(list2 != null) {
	        		for(int i = 0; i < list2.size(); i++) {
	        			set.add(list2.get(i));
	        		}
	        }
	        ArrayList<Row> result = new ArrayList<Row>(set); //puts all the elements in the set into the arrayList
	        return  result;
	    }
		/*
		 * for my AND operators
		 */
	    public  ArrayList<Row> intersection(ArrayList<Row> list1, ArrayList<Row> list2) { 
	    	//if neither are empty
	        ArrayList<Row> result = new ArrayList<Row>();
	        if(list1 != null && list2 != null) { //if they both have stuff in them...
	        		for (Row row : list1) {
	        			for(int i = 0; i < list2.size(); i++) {
	        				if(list2.get(i).equals(row)) {
	        					result.add(row);
	        				}
	        			}
	        		}
	        return result;
	    		}
	        else { //if either of them has no rows in it, then they share 0 values
	        		return result;
	        }
	    }


	    public ArrayList<Row> andOr(Condition cond, ArrayList<Row> list1, ArrayList<Row> list2) {
	   	 	if(cond.getOperator().equals(Operator.AND)) {
	   	 		return intersection(list1, list2);
	   	 	}
	   	 	if(cond.getOperator().equals(Operator.OR)) {
	   	 		return union(list1, list2);
	   	 	}
				return null;
	    }
	    
	    /*
	     * @param the where condition of the selectQuery
	     * @param the row we are evaluating currently
	     * works recursively search through binary boolean expression tree
	     * @return true if the row satsfies the WHERE clause
	     */
	    public ArrayList<Row> getWhereRows(Condition cond) {
	   	 	if (!(cond.getLeftOperand() instanceof Condition) && !(cond.getRightOperand() instanceof Condition)) { //means i went all the way down a certain side, right or left, since its a COMPELTE BST
	   	 			checkWhereCondColNames(cond.getLeftOperand().toString());
	   	 			return equationUp(cond); //if this is true, then add the row to the list of rows
	   	 	}
	   	 	//turns the leaves into truth values, True or False
	   	 	 ArrayList<Row> lVal = getWhereRows((Condition) cond.getLeftOperand()); //keep traversing the tree until get left-most left and right child
	   	 	 ArrayList<Row> rVal = getWhereRows((Condition) cond.getRightOperand());        	 
	   	 	 	return andOr(cond, lVal, rVal);
		}
	    
	    public void checkWhereCondColNames(String colName) {
	      	int counter = 0;
	      	for(int j = 0; j < numbOfCol; j++) {
	      		if(!colName.equalsIgnoreCase(columnDescriptions[j].getColumnName())) { //if they dont equal
	      				counter++;
	     		}
	    		}
	    		if(counter == numbOfCol) {
	      		throw new IllegalArgumentException("Those WHERE condition column names look a bit off!");
	      		}
	     }
	    
	    /*
	     * @param condition that has an equation, like GPA=3.75
	     * @param right is the right side of the equation,i.e. 3.45 in "GPA>3.45"
	     * in this method i should go through the rows and return an arrayList of rows
	     */
	    public ArrayList<Row> equationUp(Condition cond) {
	    	ArrayList<Row> result = new ArrayList<Row>();
	    	for(int j = 0; j < table.size(); j++) { //row j
	   	 	for(int i = 0; i < numbOfCol; i++) { //if there is an index on a column, then call a diff method that repeats all of the equals code, like if the operator is equals, 
	   	 		//then call the btree equals method and return a result rows to here
	   	 		//check if the column in the where condition actually exists
	   	 		if(columnDescriptions[i].getColumnName().equals(cond.getLeftOperand().toString())) { //check for column name
	   	 			if(!(cond.getOperator().equals(Operator.EQUALS) || cond.getOperator().equals(Operator.NOT_EQUALS))) { //for these always table search
	   	 			//its at this point that i need to check if there is an index on the column
	   	 				//now i need to search for the rows of the column
	   	 			if(cond.getOperator().equals(Operator.EQUALS)) {
	   	 				if(doesEqual(i, j, cond.getRightOperand())) {
	   	 					result.add(table.get(j));
	   	 					//add the row j of the table, into the arraylist
	   	 				}
	   	 			}
	   	 			else if(cond.getOperator().equals(Operator.NOT_EQUALS)) {
	   	 				if(doesNotEqual(i, j, cond.getRightOperand())) {
	   	 					result.add(table.get(j));
	   	 				}
	   	 			}
	   	 			else if(cond.getOperator().equals(Operator.GREATER_THAN)) {
	   	 				if(greaterThan(i, j, cond.getRightOperand())) {
	   	 					result.add(table.get(j));
	   	 				}
	   	 			}
	   	 			else if(cond.getOperator().equals(Operator.GREATER_THAN_OR_EQUALS)) {
	   	 				if(greaterThanOrEquals(i, j, cond.getRightOperand())) {
	   	 					result.add(table.get(j));
	   	 				}
	   	 			}
	   	 			else if(cond.getOperator().equals(Operator.LESS_THAN)) {
	   	 				if(lessThan(i, j, cond.getRightOperand())) {
	   	 					result.add(table.get(j));
	   	 				}
	   	 			}
	   	 			else if(cond.getOperator().equals(Operator.LESS_THAN_OR_EQUALS)) {
	   	 				if(lessThanOrEquals(i, j, cond.getRightOperand())) {
	   	 					result.add(table.get(j));
	   	 				}
	   	 			}

	   	 		}
	   	 			else { //THERE IS AN INDEX ON THIS COLUMN, NOW CHECK WHICH OPERATOR IT IS, AND THEN CALL THE BTREE METHOD
	   	 				//get the btree and put it in the method 
	   	 				if(bTrees.get(columnDescriptions[i].getColumnName()) == null) { //if there is no btree
	   	 					//then do equals or not equals on reg table
	   	 					if(cond.getOperator().equals(Operator.EQUALS)) {
		   	 				if(doesEqual(i, j, cond.getRightOperand())) {
		   	 					result.add(table.get(j));
		   	 					//add the row j of the table, into the arraylist
		   	 				}
	   	 					}
	   	 					else if(cond.getOperator().equals(Operator.NOT_EQUALS)) {
		   	 				if(doesNotEqual(i, j, cond.getRightOperand())) {
		   	 					result.add(table.get(j));
		   	 				}
	   	 					}
	   	 					}
	   	 				else {
	   	 					BTree myBT = bTrees.get(columnDescriptions[i].getColumnName());
	   	 					return bTreeEquationUp(cond, myBT, i); //put in the condition and the column that is indexed
	   	 			}
	   	 		}		
	   	 }
	   }
	    	}
			return result;
	   }
	    /*
	     * how I search through the bTree for a where condition, j is the column number
	     */
	    public ArrayList<Row> bTreeEquationUp(Condition cond, BTree BT, int j) {
	    		if(cond.getOperator().equals(Operator.EQUALS)) {  //check data type
	 				return BT.getEquals(cond.getRightOperand()); //searches the btree and returns any values that 
	 			}
	    		else if(cond.getOperator().equals(Operator.NOT_EQUALS)) {
	    			ArrayList<Row> result = new ArrayList<Row>();
		    		for(int i = 0; i < table.size(); i++) {
		    			result.add(table.get(i));
		    		}
		    			if(BT.getEquals(cond.getRightOperand()) != null) {
	 				result.removeAll(BT.getEquals(cond.getRightOperand())); //searches the btree and returns any values that 
	 					//whatever the btree returns, i just remove all from all of my table's rows
		    			}
	 				return result;
	 			}  	
			return null;
	    }
     /*
      * @param i is the column number in the table
      * @param j is the row number of the table
      * @param right is the value on the right side of the WHERE clause
      * @return does this row satisfy the WHERE clause?
      */
     public Boolean doesEqual(int i, int j, Object right) { //check for int, varchar, double, and boolean
    	 	//if both null, then they are equal
    	 	if(table.get(j).get(i) == null && right.toString().equalsIgnoreCase("null")) {
    	 		return true;
    	 	}
    	 	else if(table.get(j).get(i) != null && right.toString().equalsIgnoreCase("null")) {
    	 		return false;
    	 	}
    	 	else if(table.get(j).get(i) == null && !right.toString().equalsIgnoreCase("null")) {
    	 		return false;
    	 	}

    	 	else if(columnDescriptions[i].getColumnType() == DataType.VARCHAR) {
    	 		String str = (String) right;
    	 		if(table.get(j).get(i).toString().equals(str.substring(1, str.length() - 1))) {
    	 			return true;
    	 		}	
    	 		}
    	 		else if(columnDescriptions[i].getColumnType() == DataType.BOOLEAN) {
    	 		   String str = (String) right;
    	 		   Boolean bool = Boolean.valueOf(str.substring(1, str.length() - 1));
    	 		   if(table.get(j).get(i).equals(bool)) {
    				   return true;
    	 		   }
    	 		}
    	 		else if(columnDescriptions[i].getColumnType() == DataType.DECIMAL) {
     	 		   String str = (String) right;
     	 		   Double dbl = Double.valueOf(str);
     	 		   if(table.get(j).get(i).equals(dbl)) {
     	 			   return true;
     	 		   }
     	 		}
    	 		else if(columnDescriptions[i].getColumnType() == DataType.INT) {
     	 		   String str = (String) right;
     	 		   Integer integer = Integer.valueOf(str);
     	 		   
     	 		   if(table.get(j).get(i).equals(integer)) {
     	 			   return true;
     	 		   }
     	 		}
		return false;
    	 }

	 /*
	  * @param the row number, j
	  * @param the column number, i
	  * @param right is the right side of the equation,i.e. 3.45 in "GPA>3.45"
	  * @return boolean for truth value of the condition
	  */
     public Boolean doesNotEqual(int i, int j, Object right) { //check for int, varchar, double, and boolean
    	 	if(table.get(j).get(i) == null && right.toString().equalsIgnoreCase("null")) {
 	 		return false;
 	 	}
 	 	else if(table.get(j).get(i) != null && right.toString().equalsIgnoreCase("null")) {
 	 		return true;
 	 	}
 	 	else if(table.get(j).get(i) == null && !right.toString().equalsIgnoreCase("null")) {
 	 		return true;
 	 	}

 	 	else if(columnDescriptions[i].getColumnType() == DataType.VARCHAR) {
 			   String str = (String) right;
 			   if(!table.get(j).get(i).toString().equals(str.substring(1, str.length() - 1))) {
 				   return true;
 			   }	
 	 		}
 	 		else if(columnDescriptions[i].getColumnType() == DataType.BOOLEAN) {
 	 		   String str = (String) right;
 	 		   Boolean bool = Boolean.valueOf(str.substring(1, str.length() - 1));
 	 		   if(!table.get(j).get(i).equals(bool)) {
 	 			 return true;
 	 		   }
 	 		}
 	 		else if(columnDescriptions[i].getColumnType() == DataType.DECIMAL) {
  	 		   String str = (String) right;
  	 		   Double dbl = Double.valueOf(str);
  	 		   if(!table.get(j).get(i).equals(dbl)) {
  	 			return true;
  	 		   }
  	 		}
 	 		else if(columnDescriptions[i].getColumnType() == DataType.INT) {
  	 		   String str = (String) right;
  	 		   Integer integer = Integer.valueOf(str);
  	 		   if(!table.get(j).get(i).equals(integer)) {
  	 			return true;
  	 		   }
  	 		}
 	 	return false;
 	 }

	 /*
	  * @param the row number, j
	  * @param the column number, i
	  * @param right is the right side of the equation,i.e. 3.45 in "GPA>3.45"
	  * @return boolean for truth value of the condition
	  */
    	 public Boolean greaterThan(int i, int j, Object right) {
    		 if(table.get(j).get(i) == null || right.toString().equalsIgnoreCase("null")) {
    	 	 		return false;
    	 	 	}
    		 else if(columnDescriptions[i].getColumnType() == DataType.VARCHAR) {
    				 String str = (String) right;
     			 if(table.get(j).get(i).toString().compareTo((str.substring(1, str.length() - 1))) > 0) { //if greater than 0, then add that string
     				return true;
     			 }	
     		 }
    			 // not supporting greaterthan for boolean
     	 		else if(columnDescriptions[i].getColumnType() == DataType.DECIMAL) {
      	 		   String str = (String) right;
      	 		   Double dbl = Double.valueOf(str);
      	 		   if((Double)table.get(j).get(i) > dbl) {
      	 			return true;
      	 		   }
      	 		}
     	 		else if(columnDescriptions[i].getColumnType() == DataType.INT) {
      	 		   String str = (String) right;
      	 		   Integer integer = Integer.valueOf(str);
      	 		   if((Integer)table.get(j).get(i) > integer) {
      	 			return true;
      	 		   }
      	 		}
     	 	
    		 return false;
        	 }
    	 
    	 /*
    	  * @param the row number, j
    	  * @param the column number, i
    	  * @param right is the right side of the equation,i.e. 3.45 in "GPA>3.45"
    	  * @return boolean for truth value of the condition
    	  */
    	 public Boolean greaterThanOrEquals(int i, int j, Object right) {
    		 if(table.get(j).get(i) == null || right.toString().equalsIgnoreCase("null")) {
 	 	 		return false;
 	 	 	}
    			 if(columnDescriptions[i].getColumnType() == DataType.VARCHAR) {
    				 String str = (String) right;
     			 if(table.get(j).get(i).toString().compareTo((str.substring(1, str.length() - 1))) >= 0) { //if greater than 0, then add that string
     				return true;
     			 }	
     		 }
    			 // not supporting greaterthanOrEquals for boolean
     	 		else if(columnDescriptions[i].getColumnType() == DataType.DECIMAL) {
      	 		   String str = (String) right;
      	 		   Double dbl = Double.valueOf(str);
      	 		   if((Double)table.get(j).get(i) >= dbl) {
      	 			return true;
      	 		   }
      	 		}
     	 		else if(columnDescriptions[i].getColumnType() == DataType.INT) {
      	 		   String str = (String) right;
      	 		   Integer integer = Integer.valueOf(str);
      	 		   if((Integer)table.get(j).get(i) >= integer) {
      	 			return true;
      	 		   }
      	 		}
    		 return false;
    	 }
    	 
    	 /*
    	  * @param the row number, j
    	  * @param the column number, i
    	  * @param right is the right side of the equation,i.e. 3.45 in "GPA>3.45"
    	  * @return boolean for truth value of the condition
    	  */
    	 public Boolean lessThan(int i, int j, Object right) {
    		 if(table.get(j).get(i) == null || right.toString().equalsIgnoreCase("null")) {
 	 	 		return false;
 	 	 	}
    			 if(columnDescriptions[i].getColumnType() == DataType.VARCHAR) {
    				 String str = (String) right;
     			 if(table.get(j).get(i).toString().compareTo((str.substring(1, str.length() - 1))) < 0) { //if greater than 0, then add that string
     				return true;
     			 }	
     		 }
    			 // not supporting lessThan for boolean
     	 		else if(columnDescriptions[i].getColumnType() == DataType.DECIMAL) {
      	 		   String str = (String) right;
      	 		   Double dbl = Double.valueOf(str);
      	 		   if((Double)table.get(j).get(i) < dbl) {
      	 			return true;
      	 		   }
      	 		}
     	 		else if(columnDescriptions[i].getColumnType() == DataType.INT) {
      	 		   String str = (String) right;
      	 		   Integer integer = Integer.valueOf(str);
      	 		   if((Integer)table.get(j).get(i) < integer) {
      	 			return true;
      	 		   }
      	 		}
    		 return false;

    	 }
    	 
    	 public Boolean lessThanOrEquals(int i, int j, Object right) {
    		 if(table.get(j).get(i) == null || right.toString().equalsIgnoreCase("null")) {
 	 	 		return false;
 	 	 	}
    		 if(columnDescriptions[i].getColumnType() == DataType.VARCHAR) {
    				 String str = (String) right;
     			 if(table.get(j).get(i).toString().compareTo((str.substring(1, str.length() - 1))) <= 0) { //if greater than 0, then add that string
     				return true;
     			 }	
     		 }
    			 // not supporting lessThanOrEquals for boolean
     	 		else if(columnDescriptions[i].getColumnType() == DataType.DECIMAL) {
      	 		   String str = (String) right;
      	 		   Double dbl = Double.valueOf(str);
      	 		   if((Double)table.get(j).get(i) <= dbl) {
      	 			   return true;
      	 		   }
      	 		}
     	 		else if(columnDescriptions[i].getColumnType() == DataType.INT) {
      	 		   String str = (String) right;
      	 		   Integer integer = Integer.valueOf(str);
      	 		   if((Integer)table.get(j).get(i) <= integer) {
      	 			   return true;
      	 		   }
      	 		}
    		 return false;
    	 }

     /*
      * have to deal with:
      * where conditions alone, with functions, and with orderBys
      * orderbys alone
      * functions alone
      * MAKE SURE LOGIC HERE IS GOOD
      */
     public void startSelect(SelectQuery select) { //surround with try catch
    	 Condition where = select.getWhereCondition();
    	 	if(select.getWhereCondition() != null) { //if there is a condition, get my rows needed for it
    	 		ArrayList<Row> result = getWhereRows(select.getWhereCondition());
    	 		if(result != null) { //need to make the btree spit out an empty result as opposed to a null
    	 			if(!result.isEmpty()) { //do the same thing for update 
    	 			if(!select.getFunctions().isEmpty()) { //if there are also functions, then put the result into the functionUp
    	 				functionUp(select.getFunctions(), result); //will not be going to select columns after, just stopping after this
    	 			}
    	 			else if(select.getOrderBys().length != 0){ //if there is a where and OrderBys
    	 				fromColumns(orderUp(select.getOrderBys(), result), select); //SHOULD GO TO SELECT COLUMNS AFTER THIS
    	 			}
    	 			else { //JUST A WHERE CLAUSE, put in the resultRows and the selectObject
    	 			fromColumns(result, select);
    	 			//this distinct is checked on a whole statment basis, so I can check for this at the end when i get from certain selected columns
    	 			
    	 			}
    	 		}
    	 			else {
    	 				System.out.println("No rows matched your WHERE condition: " + where);
    	 			}
    	 		}
    	 		else {
    	 			System.out.println("No rows matched your WHERE condition: " + where);
    	 			//print out that there were no rows that satisfied the condition
    	 		}
    	 	}
    	 	else if(!select.getFunctions().isEmpty()) { //if there are functions but no where functionUp table here
    	 		functionUp(select.getFunctions(), table); //will not be going to select columns after, just stopping after this
    	 	}
    	 	else if(select.getOrderBys().length != 0) { //deal with orderBys array, if its not empty
    	 		fromColumns(orderUp(select.getOrderBys(), table), select); //need fromColumns() here
    	 	}
    	 	else { //if there is nothing then just go straight to dealing with how many columns i am selecting
    	 	fromColumns(table, select);
    	 	}
     }
     
  	/*
  	 * THIS DOES NOT APPLY TO FUNCTIONS, only selecting data from the table w where and OrderBy
  	 * @param an arrayList of rows
  	 * @param selectquery object
  	 */
  	public void fromColumns(ArrayList<Row> result, SelectQuery select) { //determine isDistinct after
  		ColumnID[] cd = select.getSelectedColumnNames(); //gets an array of the columns to select from the table
  		ArrayList<Row> finalResult = new ArrayList<Row>(); //
  			if(cd[0].getColumnName().equals("*")) { //deal with this, it is not an array of column data
  				for(int i = 0; i < result.size(); i++) {
  					finalResult.add(result.get(i)); //add all the rows into the finalResult
  				}
  			}
  			else { //want specific columns only from result 
  				//check the columns to make sure they exist in the table, loop thru, and if the name doesnt exist the amount of columns in the table, then throw an excpetion
  				checkSelectCol(cd);
  				for(int i = 0; i < result.size(); i++) { //for each row in the result, put the specific columns into the new finalResult, but keep track of column types and names
  					Row newRow = new Row(cd.length); //how many columns this new table will have
  					for(int k = 0; k < cd.length; k++) {  //for each column that i want
  						for(int j = 0; j < numbOfCol; j++) { 	
  							if(cd[k].getColumnName().equals(columnDescriptions[j].getColumnName())) { //table columns line up with result columns
  								newRow.addToRow(result.get(i).get(j), k);  //add this value to a row at position k						
  							}
  						}	
  					}
  					finalResult.add(newRow);
  				}
  			}
  		
  		createSelectRS(checkDistinct(select, finalResult), cd); //create my select result set
  	}
  	
  	
  	
  	/*
  	 * for the selected columns before the where condition
  	 */
  	public void checkSelectCol(ColumnID[] cd) {
  		for(int i =0; i < cd.length; i++) {
  			int counter = 0;
  			for(int j = 0; j < numbOfCol; j++) {
  				if(!cd[i].getColumnName().equalsIgnoreCase(columnDescriptions[j].getColumnName())) { //if they dont equal
  					counter++;
  				}
  			}
  			if(counter == numbOfCol) {
  				throw new IllegalArgumentException("The columns you asked for DO NOT EXIST BROTHA!");
  			}
  		}
  		//for each cd, check if it doesnt equal every column in table, if so, throw an exception
  	}
  	/*
  	 * used for the final stage of select, to check for all of the repeated rows
  	 * only if it is distinct, if not, then nothing changes
  	 */
	public ArrayList<Row> checkDistinct(SelectQuery select, ArrayList<Row> result) {
		if(select.isDistinct()) { //have to compare every value, if a value does not equal the other values, then it is distinct
			ArrayList<Row> finalResult = new ArrayList<Row>();
			for(int i = 0; i < result.size(); i++) { //for each row, if every value !(equals the array below at every value), then it is distinct
				if(rowIsDiff(result.get(i), result, i)) {
					finalResult.add(result.get(i));
				}
			}
			return finalResult;
		}
		else {
			return result;
		}
	}
	
	/*
	 * compares any two objects no matter their dataType, true if equal
	 */
	private boolean genericCompare(Object a, Object b) {
		String str1 = a.toString();
		String str2 = b.toString();
			return a.equals(b);
	}
	
  	private boolean rowIsDiff(Row row, ArrayList<Row> result, int currentRow) { //check if there is an equal value before it in the table, if there is do not add it
		Boolean flag = true;	
  		for(int i = currentRow - 1; i >= 0; i--) { //how many rows there are, from the current row and upward until first row
			int k = row.rowSize();//amount of columns i need to check
			int counter = 0; //this will count how many values are similar, if the counter is less than j, then i know it is a distinct row
			for(int j = 0; j < row.rowSize(); j++) { //make a equals method for any data type, isinteger, etc, else .equals(string)
 				if(genericCompare(result.get(i).get(j), row.get(j))) { //row.get(j) is the row im checking is distinct
 					counter++; //similar value
 				}
 			}
 				if(counter == k) { //means that not ALL values are similar, and therefore it is distinct
 					flag = false;
 				}
 				
		}
			return flag;
 	}
  	
  	//first order by the first thing, then if two columns have the same value, then order those by the second thing etc.
  	//use hash code and a map for something, prob distinct values
    /*
     * makes the result set for a create query
     */
  	public ResultSet createSelectRS(ArrayList<Row> finalResult, ColumnID[] selCol) {
  		//if the name matches the one in the table, then put in the column type and the column name in their respective 
  		if(!selCol[0].getColumnName().equals("*")) {
  			ResultSet ftw = new ResultSet(selCol.length);
  			for(int i = 0; i < finalResult.size(); i++) {
  				ftw.addToResult(finalResult.get(i));
  			}
  			for(int i = 0; i < selCol.length; i++) { //have to account for the *
  				for(int j = 0; j < numbOfCol; j++) {
  					if(selCol[i].getColumnName().equals(columnDescriptions[j].getColumnName())) {
  						ftw.addColumnName(columnDescriptions[j].getColumnName(), i);//then put in the name 
  						ftw.addColumnType(columnDescriptions[j].getColumnType(), i);//put in the data type
  					}
  				}
  			}
  			ftw.printResults();
  	  		return ftw;
  		}
  			else { //if there is a "*"
  				ResultSet ftw = new ResultSet(numbOfCol);
  	  			for(int i = 0; i < finalResult.size(); i++) {
  	  				ftw.addToResult(finalResult.get(i));
  	  			}
  	  			for(int j = 0; j < numbOfCol; j++) {
  	  				ftw.addColumnName(columnDescriptions[j].getColumnName(), j);//then put in the name 
  	  				ftw.addColumnType(columnDescriptions[j].getColumnType(), j);//put in the data type
  	  					}
  	  		ftw.printResults();
  	  		return ftw;
  	  		}
  	}
  		

  		
  	
     /*
      * @param teh arraylist of orderBys
      * @param the list of rows im going to be ordering and returning back to the user
      *have the column name, if its asc or not
      *have a primary column, order the first one, then if the primary column has any same values, order the secondary column, then move the labels to the right, and if the primary column has any equivalent values, then order the secondary column
      *keep being able to decrease the amount of rows that i need to order as the loop goes on, 
      *the number rows that i need to order can keep decreasing 
      */
     public ArrayList<Row> orderUp(OrderBy[] orders, ArrayList<Row> result) {
    	 for(int i = 0; i < orders.length; i++) { //for the amount of orderBys for a column
    		 String primaryCol = orders[i].getColumnID().getColumnName();
    		 String secondaryCol = orders[i + 1].getColumnID().getColumnName();
    		 //try recursion
    		 //if the primary column has any repeated values, for those repeated values, order the secondary column appropriately(asc/desc)
    		 
    	 }
		return result;
    	 	
     }
     
     /*
      * @param Boolean if true, i need to take info from my resultRows, if false, take it from the table bc there is no where clause
      * has these fields in each function instance object--FunctionName function;, ACCOUNT FOR THIS boolean isDistinct;, ColumnID column; 
      * MAYBE SPLIT THIS AND INSERT UP A LITTLE BIT AT THE END    
      */
     public void functionUp(ArrayList<FunctionInstance> functions, ArrayList<Row> result) {
    	 	for(int i = 0; i < functions.size(); i++) { //loop through all of the functions, for each function, check if the column name matches one of the tables columns
    	 		for(int j = 0; j < numbOfCol; j++) {
    	 			if(functions.get(i).column.getColumnName().equals(columnDescriptions[j].getColumnName())) {
    	 				//now that i have the column, and the function i, go to that functions action method
    	 				//if isDistinct, that means that I 
    	 				if(functions.get(i).function.equals(FunctionName.AVG)) {
    	 					if(functions.get(i).isDistinct) {
    	 						averageUp(j, result, true); //column j, 
    	 					}
    	 					else {
        	 					averageUp(j, result, false); //column j, 
    	 					}
    	 				}
    	 				if(functions.get(i).function.equals(FunctionName.SUM)) {
    	 					if(functions.get(i).isDistinct) {
    	 						sumUp(j, result, true); //pass in the column number
    	 					}
    	 					else {
    	 						sumUp(j, result, false); //not distinct here
    	 					}
    	 				}
    	 				if(functions.get(i).function.equals(FunctionName.MAX)) {
    	 					maxUp(j, result);
    	 				}
    	 				if(functions.get(i).function.equals(FunctionName.MIN)) {
    	 					minUp(j, result);
    	 				}
    	 				if(functions.get(i).function.equals(FunctionName.COUNT)) {
    	 					if(functions.get(i).isDistinct) {
    	 						countUp(j, result, true);
    	 					}
    	 					else {
        	 					countUp(j, result, false);
    	 					}
    	 				}
    	 			}
    	 		}
    	 	}
    	 }
     
     /*
      * @param boolean true if counts only distinct values
      * @param resultRows the table that im counting from
      * @param column number j
      */
     public ResultSet countUp(int j, ArrayList<Row> resultRows, Boolean isDistinct) { //start to also take the functioninstance as a param, and check if it is isdistinct
    	 	int counter = 0;
    	 	if(isDistinct) {
    	 		for(int i = 0; i < resultRows.size(); i++) {
    	 			if(isDiff(resultRows.get(i).get(j), j, i) && resultRows.get(i).get(j) != null) {
    	 				counter++;
    	 			}
    	 		}
    	 	}
    	 	else {
    	 		for(int i = 0; i < resultRows.size(); i++) {
    	 			if(resultRows.get(i).get(j) != null) {
    	 				counter++;
    	 			}
    	 		}
    	 	}
    	 		ResultSet count = new ResultSet(0);
			Row row = new Row(1);
			row.addToRow(counter, 0);
			count.addToResult(row);
			count.printFunction();    
			return count;
     }
     /*
      * very important method for checking repeating values
      * checks if the value i'm about to insert exists already in the table
      * @param a certain data object
      * @param a column number
      * @param a certain row get rid of -1
      */ 
 	private boolean isDiff(Object obj, int columnNum, int currentRow) { //check if there is an equal value before it in the table, if there is do not add it
 		for(int i = currentRow - 1; i >= 0; i--) { //how many rows there are
			if(equalsWithNulls(table.get(i).get(columnNum), obj)) { //this isnt the actual value's row) 
				return false;
			}
		}
 		return true;     		
 	}
     /*
      * @param column number j of table
      * @param resultRows is either from the where condition, or the entire table if no where condition
      */
     public ResultSet minUp(int j, ArrayList<Row> resultRows) {
    	 	if(columnDescriptions[j].getColumnType().equals(DataType.INT)) {
 		 	Integer[] integers = new Integer[resultRows.size()];
 		 		for(int i = 0; i < resultRows.size(); i++ ) {
 		 		integers[i] = (Integer) resultRows.get(i).get(j); //switch it all to this array of integers 
 		 		}
 		 		GenericQuicksort.<Integer>qsort(integers, 0, integers.length - 1);
		 		ResultSet min = new ResultSet(0);
				Row row = new Row(1);
				row.addToRow(integers[0], 0);
				min.addToResult(row);
				min.printFunction();    
				return min;
 		 	}
 	 	else if(columnDescriptions[j].getColumnType().equals(DataType.DECIMAL)) {
		 	Double[] doubles = new Double[resultRows.size()];
		 		for(int i = 0; i < resultRows.size(); i++ ) {
		 			doubles[i] = (Double) resultRows.get(i).get(j); //switch it all to this array of integers 
		 		}
		 		GenericQuicksort.<Double>qsort(doubles, 0, doubles.length - 1);
		 		ResultSet min = new ResultSet(0);
				Row row = new Row(1);
				row.addToRow(doubles[0], 0);
				min.addToResult(row);
				min.printFunction();    
				return min;
 	 	}
 	 	else if(columnDescriptions[j].getColumnType().equals(DataType.VARCHAR)) {
		 	String[] strings = new String[resultRows.size()];
		 		for(int i = 0; i < resultRows.size(); i++ ) {
		 			strings[i] = (String) resultRows.get(i).get(j); //switch it all to this array of integers 
		 		}
		 		GenericQuicksort.<String>qsort(strings, 0, strings.length - 1);
		 		ResultSet min = new ResultSet(0);
				Row row = new Row(1);
				row.addToRow(strings, 0);
				min.addToResult(row);
				min.printFunction();    
				return min;
		 	}	 	
 	 	else {
 	 		return null;
 	 	}
     }
     /*
      * @param column number j of table
      * dont care about distinct values here
      */
     public ResultSet maxUp(int j, ArrayList<Row> resultRows) {
 	 	if(columnDescriptions[j].getColumnType().equals(DataType.INT)) {
		 	Integer[] integers = new Integer[resultRows.size()];
		 		for(int i = 0; i < resultRows.size(); i++ ) {
		 		integers[i] = (Integer) resultRows.get(i).get(j); //switch it all to this array of integers 
		 		}
		 		GenericQuicksort.<Integer>qsort(integers, 0, integers.length - 1);
		 		ResultSet max = new ResultSet(0);
				Row row = new Row(1);
				row.addToRow(integers[resultRows.size() - 1], 0);
				max.addToResult(row);
				max.printFunction();   
				return max;
		 	}
	 	else if(columnDescriptions[j].getColumnType().equals(DataType.DECIMAL)) {
		 	Double[] doubles = new Double[resultRows.size()];
		 		for(int i = 0; i < resultRows.size(); i++ ) {
		 			doubles[i] = (Double) resultRows.get(i).get(j); //switch it all to this array of integers 
		 		}
		 		GenericQuicksort.<Double>qsort(doubles, 0, doubles.length - 1);
		 		ResultSet max = new ResultSet(0);
				Row row = new Row(1);
				row.addToRow(doubles[resultRows.size() - 1], 0);
				max.addToResult(row);
				max.printFunction(); 
				return max;
	 	}
	 	else if(columnDescriptions[j].getColumnType().equals(DataType.VARCHAR)) {
		 	String[] strings = new String[resultRows.size()];
		 		for(int i = 0; i < resultRows.size(); i++ ) {
		 			strings[i] = (String) resultRows.get(i).get(j); //switch it all to this array of integers 
		 		}
		 		GenericQuicksort.<String>qsort(strings, 0, strings.length - 1);
		 		ResultSet max = new ResultSet(0);
				Row row = new Row(1);
				row.addToRow(strings[resultRows.size() - 1], 0);
				max.addToResult(row);
				max.printFunction();  
				return max;
		 	}	
	 	else {
 	 	return null;
	 	}
  }
     /*
      * @param column number j
      * need distinct for sum, avg, and count
      */
     public ResultSet sumUp(int j, ArrayList<Row> resultRows, Boolean isDistinct) {
    	 	if(columnDescriptions[j].getColumnType().equals(DataType.INT)) {
    	 		Integer result = 0;
    	 		for(int i = 0; i < resultRows.size(); i++ ) {
    	 			if(isDistinct) {
    	 				if(isDiff(resultRows.get(i).get(j), j, i)) {
    	 					Integer addOn = (Integer) resultRows.get(i).get(j);
    	 					result += addOn;    	 			
    	 				}
    	 			}
    	 			else {
    	 				Integer addOn = (Integer) resultRows.get(i).get(j);
    	 				result += addOn;
    	 			}
    	 		}
    	 		ResultSet sum = new ResultSet(0);
			Row row = new Row(1);
			row.addToRow(result, 0);
			sum.addToResult(row);
			sum.printFunction();   
			return sum;
	}
    	 else if(columnDescriptions[j].getColumnType().equals(DataType.DECIMAL)) {
    		 	Double result = 0.0;
    		 	for(int i = 0; i < resultRows.size(); i++ ) {
    		 		if(isDistinct) {
    	    	 			if(isDiff(resultRows.get(i).get(j), j, i)) {
    	    	 				Double addOn = (Double) resultRows.get(i).get(j);
    	    	 				result += addOn;    	 			
    	    	 			}
    	    	 		}
    		 		else {
    		 			Double addOn = (Double) resultRows.get(i).get(j);
    		 			result += addOn;
    		 		}
    	    	 	}
    	 		ResultSet sum = new ResultSet(0);
			Row row = new Row(1);
			row.addToRow(result, 0);
			sum.addToResult(row);
			sum.printFunction();  
			return sum;
    	    	 }
    	 else {
    		 return null;
    	 }
    	 }
    	 /*
    	  * @param column number j of the table
    	  * @return the average of the columns
    	  * still have to factor in the distinct stuff
    	  * if it specifies distinct, i have to go through all the values, and start with the second one, if it equals anything before it, then dont add it to teh total calculation
    	  */
    	 	private ResultSet averageUp(int j, ArrayList<Row> resultRows, Boolean isDistinct) {
    	 		int counter = 0;
	 		Double result = 0.0; //average might have decimal points
    	 		if(columnDescriptions[j].getColumnType().equals(DataType.INT)) {
    	 			for(int i = 0; i < resultRows.size(); i++ ) {
    	 				if(isDistinct) {
    	 					if(isDiff(resultRows.get(i).get(j), j, i) && resultRows.get(i).get(j) != null) {
    	    	 				Integer addOn = (Integer) resultRows.get(i).get(j);
    	    	 				result += addOn;  
    	    	 				counter++;
    	    	 			}
    	 				}
    	 				else {
    	 				Integer addOn = (Integer) resultRows.get(i).get(j);
    	 				result += addOn;
    	 				counter++;
    	 				}
    	 			}
    	 		}
    	 		if(columnDescriptions[j].getColumnType().equals(DataType.DECIMAL)) {
    	 			for(int i = 0; i < resultRows.size(); i++ ) {
    	 				if(isDistinct) {
    	 					if(isDiff(resultRows.get(i).get(j), j, i)) {
    	    	 				Double addOn = (Double) resultRows.get(i).get(j);
    	    	 				result += addOn;  
    	    	 				counter++; //amount of values im dividing by
    	    	 			}
    	 				}
    	 				else {
    	 				Double addOn = (Double) resultRows.get(i).get(j);
    	 				result += addOn;
    	 				counter++;
    	 				}
    	 			}
    	 		}
    	 			Double avg = (Double) (result/counter); 
	 			ResultSet average = new ResultSet(0);
	 			Row row = new Row(1);
	 			row.addToRow(avg, 0);
	 			average.addToResult(row);
	 			average.printFunction();
	 			return average;
    	 	}

     	/************************************
		* UPDATE QUERY CODE STARTS HERE
     	**************************************/

     	public void startUpdate(UpdateQuery update) {
     		ColumnValuePair[] CV = update.getColumnValuePairs();
 			Condition where = update.getWhereCondition();   	
     		ArrayList<Row> result = new ArrayList<Row>();
     		if(checkColumnNamesInsertUpdate(update.getColumnValuePairs())) { //first check columns that i want to update
     			if(where != null) { //if there is a where condition do this stuff
     				result = getWhereRows(update.getWhereCondition()); //these are the rows of the table i need to change
     				if(result != null) {
     					if(!result.isEmpty()) { //there were some satisfied rows and there was a where condition
     						for(int i = 0; i < table.size(); i++) {
     							if(result.contains(table.get(i))) { //that means that row needs to be changed immediately! lol
     								updateTable(CV, i);
     							}
     						}
     					}
     				else {
     					System.out.println("No rows matched your WHERE condition: " + where.toString());
     				}
     				}
     				else { //result is null (from bTree returning null)
     					System.out.println("No rows matched your WHERE condition: " + where.toString());
     				}
     			}
     			else { //no where statement, update all rows
     				for(int i = 0; i < table.size(); i++) {
     					updateTable(CV, i);
     			}
     		}
     		}
     		else { //some random columns were referenced, DO NOT EXECUTE THE UPDATE
     			throw new IllegalArgumentException();
     		}
     		printTable();
     	}
     		
     	/*
     	 * part II to the update sequence, just to split up my logic easier
     	 * @param the cvps to change to
     	 * @param the number row im updating, i
     	 */
     	public void updateTable(ColumnValuePair[] CV, int i) {
     		for(ColumnValuePair cvp : CV) { //for each cvp go through each column of the table, and if the names match, then put it in this specific row
     			for(int j = 0; j < numbOfCol; j++) { //go through the tables' columns
     				if(cvp.getColumnID().getColumnName().equals(columnDescriptions[j].getColumnName())) {
     					DataType type = columnDescriptions[j].getColumnType();
     					if(type.equals(DataType.INT)) {
     						Integer num = (Integer)Integer.valueOf(cvp.getValue());
         					table.get(i).addToRow(num, j); //turn this string into 
         					updateIndices(table.get(i));
     					}
     					else if(type.equals(DataType.DECIMAL)) {
     						//check for null here
         					checkDouble(cvp.getValue(), j);								
     						Double dbl = (Double)Double.valueOf(cvp.getValue());
         					table.get(i).addToRow(dbl, j); //turn this string into 
         					updateIndices(table.get(i)); //CHECK IF THIS WORKS
     					}
     					else if(type.equals(DataType.BOOLEAN)) {
     						String str = cvp.getValue().toString();
     						
     						Boolean bool = (Boolean)Boolean.valueOf(str.substring(1, str.length() - 1));
         					table.get(i).addToRow(bool, j); //turn this string into 
         					updateIndices(table.get(i));
     					}
     					else if(type.equals(DataType.VARCHAR)) {
     						String str = cvp.getValue().toString();
     						checkVarChar(str, j);
     						String val = (String)String.valueOf(str.substring(1, str.length() - 1));
         					table.get(i).addToRow(val, j); 
         					updateIndices(table.get(i)); //CHECK IF THIS WORKS maybe update before updating the table
     					}
     				}
     			}
     		}
     		updateUnique(table.get(i), i); //make sure columns stay unique if their description is so
     		//dont need to check for null, bc only if you leave out during insert does notnull matter
     	}
     	/*
     	 * check to make sure no values are equal in the same column are above or beneath it
     	 * @param, the row, and the row number
     	 */
     	public void updateUnique(Row row, int i) {
     		//make sure its not equal to anything below it
     		//make sure its not equal to anything above it
     		if(table.size() >= 1) {
				for(int j = 0; j < numbOfCol; j++) { //columns j
					if((columnDescriptions[j].isUnique()) || (columnDescriptions[j].getColumnName().equals(primaryKeyColumn.getColumnName()))) {
    				row.addToRow(dealWithUniqueUpdate((row.get(j)), j, i), j); //obj, column numb, and row number, then columnnumber again to add to row
					}
				}
			}
    	}
     	/*
     	 * object, column num, and row number
     	 */
     	private Object dealWithUniqueUpdate(Object obj, int j, int i) {
			for(int k = 0; k < i; k++) { //until this updated row
				if(equalsWithNulls(table.get(i).get(j), table.get(k).get(j))) {   //how many values in each entry row
					throw new IllegalArgumentException("The column " + columnDescriptions[j].getColumnName() 
														+  " has to be given only unique values when updating too! (check if " 
														+ columnDescriptions[j].getColumnName() +  " is the primary key Column)");
				}
			}
			for(int k = (i + 1); k < table.size(); k++) { //skip the updated row, and go until end of table
				if(equalsWithNulls(table.get(i).get(j), table.get(k).get(j))) {   //original value , or all others skipped over myRowhow many values in each entry row
					throw new IllegalArgumentException("The column " + columnDescriptions[j].getColumnName() 
														+  " has to be given only unique values when updating too! (check if " 
														+ columnDescriptions[j].getColumnName() +  " is the primary key Column)");
				}
			}
		return obj;
	}
     	
     	/************************************
		* DELETE QUERY CODE STARTS HERE
     	**************************************/
     	/*
     	 * @param DeleteQeury object
     	 */
     	public void startDelete(DeleteQuery delete) {
     		//if there is a where condition then delete all the rows that is contained in resultRows, if no where condition, then delete the entire table
     		Condition where =delete.getWhereCondition(); //CHECK WHERE CONDITION, MIGHT NOT HAVE TO AGAIN
     		if(where != null) { //then only delete certain rows
     				ArrayList<Row> result = getWhereRows(where);
         			for(int i = 0; i < table.size(); i++) {
         				for(int j = 0; j < result.size(); j++) {
         					if(result.get(j).equals(table.get(i))) { //UPDATE INDICES RIGHT BEFORE REMOVING IT FROM TABLE
         					table.remove(i);
         					}
         				}
         			}
     				//if a table contains a row that exists in this arraylist, then remove its row
     		}
     		else { //delete all the rows, first update indices!!!!   			
     			//make every value in btree null for every btree
     			for(int j = 0; j < numbOfCol; j++) { //j is column
     	     		if(bTrees.get(columnDescriptions[j].getColumnName()) != null) { //if there is a btree
     	     			BTree BT = bTrees.get(columnDescriptions[j].getColumnName()); //my btree for this column
     	     			//go through every row for value in column j as the key
     	     			for(int i = 0; i < table.size(); i++) {
     	     				if(columnDescriptions[j].getColumnType().equals(DataType.DECIMAL)) { //now check data types
     	     					if(table.get(i).get(j) != null) {
     	     						Double dbl = (Double) table.get(i).get(j);
     	     						BT.delete(dbl);
     	     					}
     	     				}
     	     				if(columnDescriptions[j].getColumnType().equals(DataType.BOOLEAN)) { //now check data types
     	     					if(table.get(i).get(j) != null) {
     	     					Boolean bool = (Boolean) table.get(i).get(j);
     							BT.delete(bool);
     	     					}
     	     				}
     	     				if(columnDescriptions[j].getColumnType().equals(DataType.INT)) { //now check data types
     	     					if(table.get(i).get(j) != null) {
     	     					Integer integer = (Integer) table.get(i).get(j);
     							BT.delete(integer);
     	     					}
     	     				}
     	     				if(columnDescriptions[j].getColumnType().equals(DataType.VARCHAR)) { //now check data types
     	     					if(table.get(i).get(j) != null) {
     	     					String str = (String) table.get(i).get(j);
     							BT.delete(str);
     	     					}
     	     				}
     	     			}
     			
     				}
     			}
     			int size = table.size();
     			for(int i = size - 1; i >= 0; i--) {
     				table.remove(i); //gets rid of every row in the table
     			}
     		}
 			printTable();
     	}
     	
     	/*
     	 * prints the table
     	 */
     	public void printTable() {
     		System.out.print("MY TABLE: ");
     		for(int i = 0; i < table.size(); i++) { //how many rows there are
     			System.out.println();
     			for(int j = 0; j < table.get(i).rowSize(); j++) {
     				System.out.print(table.get(i).get(j)); //how many values in each row
     				}
     			}
     		System.out.println();
    		}
     	
     	public ColumnDescription[] getColumnDescriptions() {
     		return columnDescriptions;
     	}
     	
		public int size() {
			return table.size();
		}
		public Row get(int i) {
			return table.get(i);
		}  
}