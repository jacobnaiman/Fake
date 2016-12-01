package edu.yu.cs.dataStructures.fall2016.SimpleSQLParser;

public class ColumnValuePair
{
    private ColumnID col;
    private String value;

    ColumnValuePair(ColumnID col, String value)
    {
	this.col = col;
	this.value = value;
    }

    public ColumnID getCOlumnID()
    {
	return this.col;
    }

    public String getValue()
    {
	return this.value;
    }
}