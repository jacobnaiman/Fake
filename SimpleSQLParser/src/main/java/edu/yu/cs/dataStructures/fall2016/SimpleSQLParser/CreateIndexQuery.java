package edu.yu.cs.dataStructures.fall2016.SimpleSQLParser;

public class CreateIndexQuery extends SQLQuery
{
    private String tableName;
    private String columnName;
    private String indexName;  
    
    CreateIndexQuery(String queryString)
    {
	super(queryString);
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
     * @return the columnName
     */
    public String getColumnName()
    {
	return this.columnName;
    }
    void setColumnName(String columnName)
    {
	this.columnName = columnName;
    }

    /**
     * @return the indexName
     */
    public String getIndexName()
    {
	return this.indexName;
    }
    void setIndexName(String indexName)
    {
	this.indexName = indexName;
    }
    public ColumnValuePair[] getColumnValuePairs()
    {
	throw new UnsupportedOperationException();
    }
    void addColumnValuePair(ColumnID col, String value)
    {
	throw new UnsupportedOperationException();	
    }    
}