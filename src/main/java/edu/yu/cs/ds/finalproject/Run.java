package edu.yu.cs.ds.finalproject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;
import net.sf.jsqlparser.JSQLParserException;

public class Run {
	public ResultSet execute (String str) throws JSQLParserException {
		Database DB = new Database();
		SQLParser parser = new SQLParser();
		SQLQuery sqlQuery = parser.parse(str);
		ResultSet resultSet = DB.performQuery(sqlQuery);
		return resultSet;
		
	}
}
