package edu.yu.cs.dataStructures.fall2016.SimpleSQLParser;

import java.util.HashSet;
import java.util.Set;

public class SelectQuery extends SQLQuery
{
    public enum Function
    {
	AVG("AVG"),
	COUNT("COUNT"),
	MAX("MAX"),
	MIN("MIN"),
	SUM("SUM");
	
	private String name;
	private Function(String name)
	{
	    this.name = name;
	}
	
	public String toString()
	{
	    return this.name;
	}
    };
    
    public static class OrderBy
    {
	private boolean ascending;
	private ColumnID column;
	public OrderBy(ColumnID col, boolean ascending)
	{
	    this.column = col;
	    this.ascending = ascending;
	}
	public ColumnID getColumnID()
	{
	    return this.column;
	}
	public boolean isAscending()
	{
	    return this.ascending;
	}
	public boolean isDescending()
	{
	    return !this.ascending;
	}
    };
    
    private Set<ColumnID> columnNames;
    private Set<String> tableNames;
    private boolean distinct;
    private Condition where;
    private Function func;
    private Set<OrderBy> orderBys;
    
    SelectQuery(String queryString)
    {
	super(queryString);
	this.columnNames = new HashSet<>(); 
	this.tableNames = new HashSet<>();
	this.orderBys = new HashSet<>();
    }

    /**
     * 
     * @return the column names in the order in which they were listed in the query
     */
    public OrderBy[] getOrderBys()
    {
	return this.orderBys.toArray(new OrderBy[this.orderBys.size()]);
    }
    void addOrderBy(OrderBy name)
    {
	this.orderBys.add(name);
    }
    
    /**
     * 
     * @return the column names in the order in which they were listed in the query
     */
    public ColumnID[] getSelectedColumnNames()
    {
	return this.columnNames.toArray(new ColumnID[this.columnNames.size()]);
    }
    void addSelectedColumnName(ColumnID name)
    {
	this.columnNames.add(name);
    }

    /**
     * 
     * @return the column names in the order in which they were listed in the query
     */
    public String[] getFromTableNames()
    {
	return this.tableNames.toArray(new String[this.tableNames.size()]);
    }
    void addFromTableName(String name)
    {
	this.tableNames.add(name);
    }    
    
    /**
     * @return the distinct
     */
    public boolean isDistinct()
    {
	return this.distinct;
    }
    void setDistinct(boolean distinct)
    {
	this.distinct = distinct;
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
     * @return the func
     */
    public Function getFunction()
    {
	return func;
    }
    void setFunction(Function func)
    {
	this.func = func;
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