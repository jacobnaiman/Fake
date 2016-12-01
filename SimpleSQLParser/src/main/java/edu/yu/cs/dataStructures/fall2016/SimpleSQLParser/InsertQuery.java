package edu.yu.cs.dataStructures.fall2016.SimpleSQLParser;

import java.util.HashSet;
import java.util.Set;

/**
 * @author diament@yu.edu
 *
 */
public class InsertQuery extends SQLQuery
{
    private String tableName;
    private String indexName;
    private Set<ColumnValuePair> colValPairs;

    /**
     * 
     */
    InsertQuery(String queryString)
    {
	super(queryString);
	this.colValPairs = new HashSet<>();
    }

    /**
     * @return the tableName
     */
    public String getTableName()
    {
	return this.tableName;
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

    /**
     * @return the indexName
     */
    public String getIndexName()
    {
	return indexName;
    }

    void setIndexName(String indexName)
    {
	this.indexName = indexName;
    }
}