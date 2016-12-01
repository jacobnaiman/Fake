package edu.yu.cs.dataStructures.fall2016.SimpleSQLParser;

public class ColumnID
{
    private String columnName;
    private String tableName;
    
    public ColumnID(String columnName, String tableName)
    {
	this.columnName = columnName;
	this.tableName = tableName;
    }   
    public String getColumnName()
    {
	return this.columnName;
    }
    public String getTableName()
    {
	return this.tableName;
    }
    
    public String toString()
    {
	if(this.tableName != null)
	{
	    return this.tableName + "." + this.columnName;
	}
	return this.columnName;
    }
}