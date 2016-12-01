package edu.yu.cs.dataStructures.fall2016.SimpleSQLParser;

public class DeleteQuery extends SQLQuery
{
    private String tableName;
    private Condition where;
    
    public DeleteQuery(String queryString)
    {
	super(queryString);
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
    public ColumnValuePair[] getColumnValuePairs()
    {
	throw new UnsupportedOperationException();
    }
    void addColumnValuePair(ColumnID col, String value)
    {
	throw new UnsupportedOperationException();	
    }    
}