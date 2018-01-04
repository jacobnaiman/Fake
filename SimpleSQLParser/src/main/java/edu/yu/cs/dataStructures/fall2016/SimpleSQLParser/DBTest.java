package edu.yu.cs.dataStructures.fall2016.SimpleSQLParser;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SQLParser;
import net.sf.jsqlparser.JSQLParserException;
//SHOW EVERYTHING IN MY CODE THAT WORKS!!!! all of the exceptions show that also
//PRINT OUT ALL OF THE STRING QUERIES AS WELL, add a ptinln method in DataBase to print out the SQL Query
public class DBTest {
	private static long startTime = System.currentTimeMillis();
	static SQLParser parser = new SQLParser();

	public static void main(String[] args) throws JSQLParserException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException {
		DataBase DB = new DataBase();
		/*
		 * **************************************************
		 * CREATE TABLE FUNCTIONS
		 * **************************************************
		 */
		
		DB.execute("CREATE TABLE YCStudent"
				+ "("
				+ " BannerID int,"
				+ " SSNum int,"
				+ " FirstName varchar(255) NOT NULL,"
				+ " LastName varchar(255),"
				+ " GPA decimal(1,2) UNIQUE," //if i put UNIQUE here test later
				+ " CurrentStudent boolean,"
				+ " PRIMARY KEY (BannerID)"
				+ ")");
		
		/***************************************************
		 * CREATE INDEX FUNCTIONS
		 * ***************************************************/
		DB.execute("CREATE INDEX GPA_Index on YCStudent (GPA)"); 
		DB.execute("CREATE INDEX GPA_Index on YCStudent (GPA)");  //show everything!!!!!! all exceptional cases as well!!
		/**
		 * **************************************************
		 * INSERT FUNCTIONS
		 * **************************************************
		 */
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES ('alex', 'hey',9.76, 'True', 1, 2)"); 
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA , CurrentStudent, BannerID) VALUES ('alex', 'jones', 3.75,'True', 3)"); 
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID) VALUES ('Jon','hod',1.10, 'True',200)"); 
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA , CurrentStudent, BannerID) VALUES ('evan','jon',3.40, 'False',25)"); 
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA , CurrentStudent, BannerID) VALUES ('alex','johnson',3.80, 'True',2)"); 
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA , CurrentStudent, BannerID) VALUES ('tony','j1n',2.10, 'True',143)"); 
		DB.execute("INSERT INTO YCStudent (FirstName, GPA , CurrentStudent, BannerID) VALUES ('jon',3.70, 'False',1477)"); 
		System.out.println();
		
		/***************************************************
		 * SELECT FUNCTIONS 
		 ***************************************************
		 * 
		 */
		DB.execute("select * from YCStudent"); 
		DB.execute("select FirstName, BannerID FROM YCStudent WHERE GPA>2.01"); //work this out 
		DB.execute("select distinct GPA, CurrentStudent, FirstName from YCStudent WHERE GPA>2.00"); 
		DB.execute("select BannerID from YCStudent WHERE (FirstName>'jon' OR BannerID<100) AND (LastName<>'bird' AND CurrentStudent='True') OR GPA>1.09");  //if any value is nullor ssnum = 1, deal with null, and if referenced lastname=, the column 
		DB.execute("select FirstName from YCStudent WHERE CurrentStudent='TRUE'"); 
		DB.execute("select LastName from YCStudent");  //make it that if i reference wrong columns, i throw an exception
		DB.execute("select MIN(GPA), MIN(BannerID) from YCStudent"); 

		/***************************************************
		 * UPDATE FUNCTIONS
		 * ***************************************************/
		DB.execute("UPDATE YCStudent SET GPA=1.10 WHERE BannerID=25"); //can i have a * here? check when where conditions are all false, if result is 0 then print a message nothing satisfies the where condition, do this for other qeuries as well

		/***************************************************
		 * DELETE FUNCTIONS
		 ****************************************************/
	    
		DB.execute("DELETE FROM YCStudent WHERE BannerID=143"); //where condition isnt satisfied
		//finish
		long endTime = System.currentTimeMillis();
		System.out.println();
		System.out.println();
	    System.out.println("It took " + (endTime - startTime) + " milliseconds");
	   
	}
}