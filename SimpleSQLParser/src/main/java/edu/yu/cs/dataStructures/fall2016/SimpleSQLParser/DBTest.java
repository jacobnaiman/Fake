package edu.yu.cs.dataStructures.fall2016.SimpleSQLParser;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SQLParser;
import net.sf.jsqlparser.JSQLParserException;

public class DBTest {
	private static long startTime = System.currentTimeMillis();
	static SQLParser parser = new SQLParser();

	public static void main(String[] args) throws JSQLParserException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException {
		DataBase DB = new DataBase();
		/*
		 * **************************************************
		 * CREATE TABLE TESTS
		 * **************************************************
		 */
		//good 
		DB.execute("CREATE TABLE YCStudent"
				+ "("
				+ " BannerID int,"
				+ " SSNum int DEFAULT 100,"
				+ " FirstName varchar(10) NOT NULL,"
				+ " LastName varchar(12),"
				+ " GPA decimal(1,2) UNIQUE," 
				+ " CurrentStudent boolean,"
				+ " PRIMARY KEY (BannerID)"
				+ ")");
		DB.execute("CREATE TABLE RABBIS"
				+ "("
				+ " SmichaYear int,"
				+ " ShulMembers int,"
				+ " FirstName varchar(10) UNIQUE,"
				+ " LastName varchar(12) DEFAULT 'Greenberg'," //can't be
				+ " DafADay decimal(2,4),"
				+ " CurrentRabbi boolean,"
				+ " PRIMARY KEY (DafADay)"
				+ ")");
		
		//bad 
		DB.execute("CREATE TABLE BASEBALLPLAYER"
				+ "("
				+ " UniformNumber int,"
				+ " PhoneNumber int,"
				+ " FirstName varchar(10) UNIQUE,"
				+ " LastName varchar(12) DEFAULT 'Greenberg'," //can't be
				+ " BATTINGAVG decimal(0,3),"
				+ " CurrentPlayer boolean,"
				+ " PRIMARY KEY (LastName)"
				+ ")");

		/***************************************************
		 * CREATE INDEX FUNCTIONS
		 * ***************************************************/
		//good 
		DB.execute("CREATE INDEX GPA_Index on YCStudent (GPA)"); 
		//bad 
		DB.execute("CREATE INDEX GPA_Index on YCStudent (GPA)"); //already indexed
		DB.execute("CREATE INDEX GPA_Index on YCStudent (LUNCH)"); //column does not exist
		/**
		 * **************************************************
		 * INSERT FUNCTIONS
		 * **************************************************
		 */
		
		//good 
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES ('alex', 'hey',9.76, 'True', 1, 2)"); 
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA , CurrentStudent, BannerID) VALUES ('alex', 'jones', 3.75,'True', 3)"); 
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID) VALUES ('Jon','hod',1.15, 'True',200)"); 
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA , CurrentStudent, BannerID) VALUES ('evan','jon',3.40, 'False',25)"); 
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA , CurrentStudent, BannerID) VALUES ('alex','johnson',3.80, 'True',2)"); 
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA , CurrentStudent, BannerID) VALUES ('tony','j1n',2.10, 'True',143)");
		DB.execute("INSERT INTO YCStudent (FirstName, GPA , CurrentStudent, BannerID) VALUES ('evan',3.56, 'False',245)"); 
		DB.execute("INSERT INTO YCStudent (FirstName, GPA , CurrentStudent, BannerID) VALUES ('jon',3.70, 'False',1477)"); 
		//bad  
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA , CurrentStudent, BannerID) VALUES ('alex','johnson',3.80657473, 'True',217)"); //wrong decimal format
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA , CurrentStudent, BannerID) VALUES ('alex','johnson',32.8, 'True',217)"); //wrong decimal format
		DB.execute("INSERT INTO YCStudent (MaidenName, SirName, Grades, AmIAStudent, MyNumba, SS) VALUES ('alex', 'hey',9.76, 'True', 1, 2)"); //wrong columns to insert
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES ('Googenheimerstein', 'hey',9.76, 'True', 1, 132)"); //Long FirstName 
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES ('alex', 'Williamsonbergfeld',9.76, 'True', 1, 254)");
		DB.execute("INSERT INTO YCStudent (LastName, GPA, CurrentStudent, BannerID) VALUES ('hod',1.10, 'True',200)"); //firstname not null
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID) VALUES ('Jon','hod',1.15,'True',210)"); //GPA unique
		DB.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, SSNum) VALUES ('bill', 'Bob',3.52, 'False', 4)"); //PK cant be null
		System.out.println();
		
		/***************************************************
		 * SELECT FUNCTIONS 
		 ***************************************************
		 * 
		 */
		//good
		DB.execute("select * from YCStudent"); 
		DB.execute("select FirstName, CurrentStudent, GPA from YCStudent"); 
		DB.execute("select GPA, CurrentStudent, FirstName from YCStudent WHERE GPA>2.00 OR BannerID<200"); 
		DB.execute("select distinct GPA, CurrentStudent, FirstName from YCStudent WHERE GPA>2.00"); 
		DB.execute("select BannerID from YCStudent WHERE (FirstName>'jon' OR BannerID<100) AND (LastName<>'bird' AND CurrentStudent='True') OR GPA>1.09");  //if any value is nullor ssnum = 1, deal with null, and if referenced lastname=, the column 
		DB.execute("select distinct FirstName from YCStudent WHERE CurrentStudent='TRUE' OR (GPA>4.76 AND FirstName='alex')"); 
		DB.execute("select LastName from YCStudent");  
		DB.execute("select MIN(GPA), MAX(BannerID), SUM(GPA) from YCStudent WHERE BannerID>=10"); 
		DB.execute("select COUNT(Distinct FirstName) from YCStudent WHERE BannerID>10 AND LastName<>null"); 
		DB.execute("select MIN(GPA), MIN(BannerID), AVG(BannerID), SUM(GPA) from YCStudent WHERE FirstName<>'alex' AND LastName<>null"); 
		DB.execute("select COUNT(BannerID) from YCStudent WHERE CurrentStudent='False' AND (GPA<=9.64 OR CurrentStudent='True')"); 

		//bad
		DB.execute("select FirstName, BannerID FROM YCStudent WHERE Bad>2.01 AND Names='judah'"); //columns in where condition don't exist in  
		DB.execute("select CrazyName, WhyNot, GPA from YCStudent");  //columns to view dont exist in table
		DB.execute("select FirstName, CurrentStudent, GPA from YCStudent WHERE Haha=90 AND Wutttt='jon'"); //columns in where condition don't exist in  
		DB.execute("select LastName from PlzGiveMeAnA");  //wrong table referenced

		/***************************************************
		 * UPDATE FUNCTIONS
		 * ***************************************************/
		//good
		DB.execute("UPDATE YCStudent SET CurrentStudent='True' WHERE BannerID<100"); 
		DB.execute("UPDATE YCStudent SET FirstName='Jonathan' WHERE BannerID<>10"); 
		DB.execute("UPDATE YCStudent SET CurrentStudent='True', LastName='Friday' WHERE BannerID<100"); 

		//bad
		DB.execute("UPDATE NotATable SET BannerID=231 WHERE BannerID<100");  //wrong table
		DB.execute("UPDATE YCStudent SET GPA=3.14 WHERE BannerID<100");  //GPA is Unique

		/***************************************************
		 * DELETE FUNCTIONS
		 ****************************************************/
	    //good
		DB.execute("DELETE FROM YCStudent WHERE BannerID<>24 AND FirstName='alex'"); 
		
		//bad
		DB.execute("DELETE FROM YCStudent WHERE Heyoo<>24 AND Letsgoo='alex'");  //where condition columns dont exist
		DB.execute("DELETE FROM WHATUP WHERE BannerID<>24 AND FirstName='alex'");  //wrong table

		//finish
		long endTime = System.currentTimeMillis();
		System.out.println();
		System.out.println();
	    System.out.println("It took " + (endTime - startTime) + " milliseconds");
	   
	}
}