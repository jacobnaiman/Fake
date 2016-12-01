package edu.yu.cs.dataStructures.fall2016.SimpleSQLParser;

import java.util.HashSet;
import java.util.Set;

public class UpdateQuery extends SQLQuery
{
    private Set<ColumnValuePair> colValPairs;
    private String tableName;
    private Condition where;

    UpdateQuery(String queryString)
    {
	super(queryString);
	this.colValPairs = new HashSet<>();
    }

    /**
     * @return the where
     */
    public Condition getWhereCondition()
    {
	return this.where;
    }

    void setWhereCondition(Condition where)
    {
	this.where = where;
    }

    /**
     * @return the tableName
     */
    public String getTableName()
    {
	return tableName;
    }
    void setTableName(String tableName)
    {
	this.tableName = tableName;
    }

    /**
     * 
     * @return the column-value pairs in the order in which they were listed in the
     *         query
     */
    public ColumnValuePair[] getColumnValuePairs()
    {
	return this.colValPairs.toArray(new ColumnValuePair[this.colValPairs.size()]);
    }

    void addColumnValuePair(ColumnID col, String value)
    {
	this.colValPairs.add(new ColumnValuePair(col, value));
    }
}