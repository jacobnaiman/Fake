package edu.yu.cs.ds.finalproject.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition.Operator;
import edu.yu.cs.ds.finalproject.Database;
import edu.yu.cs.ds.finalproject.ResultSet;
import edu.yu.cs.ds.finalproject.Row;
import net.sf.jsqlparser.JSQLParserException;

public class DBTest {
	private Database db = new Database();
	private ResultSet resultSet;
	
	@Test
	public void testCreateTable() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		for (int i = 0; i < 6; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}
		assertEquals(1, db.allTables.size());
		assertEquals("YCStudent", db.allTables.get(0).tableName);
		assertEquals("BannerID", db.allTables.get(0).primaryKey.getColumnName());
		assertEquals("BannerID", db.allTables.get(0).cds[ban].getColumnName());
		assertEquals(ColumnDescription.DataType.INT, db.allTables.get(0).cds[ban].getColumnType());
		assertEquals("SSNum", db.allTables.get(0).cds[ss].getColumnName());
		assertEquals(ColumnDescription.DataType.INT, db.allTables.get(0).cds[ss].getColumnType());
		assertEquals(true, db.allTables.get(0).cds[ss].isUnique());
		assertEquals("FirstName", db.allTables.get(0).cds[first].getColumnName());
		assertEquals(ColumnDescription.DataType.VARCHAR, db.allTables.get(0).cds[first].getColumnType());
		assertEquals(255, db.allTables.get(0).cds[first].getVarCharLength());
		assertEquals("LastName", db.allTables.get(0).cds[last].getColumnName());
		assertEquals(ColumnDescription.DataType.VARCHAR, db.allTables.get(0).cds[last].getColumnType());
		assertEquals(255, db.allTables.get(0).cds[last].getVarCharLength());
		assertEquals(true, db.allTables.get(0).cds[last].isNotNull());
		assertEquals("GPA", db.allTables.get(0).cds[gpa].getColumnName());
		assertEquals(ColumnDescription.DataType.DECIMAL, db.allTables.get(0).cds[gpa].getColumnType());
		assertEquals(true, db.allTables.get(0).cds[gpa].getHasDefault());
		assertEquals("0.00", db.allTables.get(0).cds[gpa].getDefaultValue());
		assertEquals(2, db.allTables.get(0).cds[gpa].getFractionLength());
		assertEquals("CurrentStudent", db.allTables.get(0).cds[cur].getColumnName());
		assertEquals(ColumnDescription.DataType.BOOLEAN, db.allTables.get(0).cds[cur].getColumnType());
		assertEquals(true, db.allTables.get(0).cds[cur].getHasDefault());
		assertEquals("true", db.allTables.get(0).cds[cur].getDefaultValue());
	}
	
	@Test
	public void createDefaultPrimaryKey() throws JSQLParserException {
		ResultSet RS = db.execute("CREATE TABLE YCStudent (BannerID int DEFAULT 1, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		assertEquals(false, RS.successful);
		assertNotEquals(null, RS.getMessage());
	}
	
	@Test
	public void insertHappyPath() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123)");
		assertEquals(1, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		for (int i = 0; i < 6; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.9", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
	}
	
	@Test
	public void insertDuplicateColumns() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		ResultSet RS = db.execute("INSERT INTO YCStudent (LastName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123)");
		assertEquals(RS.successful, false);
		assertNotEquals(RS.getMessage(), null);
	}
	
	@Test
	public void wrongValueTypeInt() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		ResultSet RS = db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 3.4, 123)");
		assertEquals(RS.successful, false);
		assertNotEquals(RS.getMessage(), null);
	}
	
	@Test
	public void wrongValueTypeDecimal() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		ResultSet RS = db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, true, true, 800123456, 123)");
		assertEquals(RS.successful, false);
		assertNotEquals(RS.getMessage(), null);
	}
	
	@Test
	public void wrongValueTypeBoolean() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		ResultSet RS = db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, yes, 800123456, 123)");
		assertEquals(RS.successful, false);
		assertNotEquals(RS.getMessage(), null);
	}
	
	@Test
	public void wrongLengthVarChar() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(2), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		ResultSet RS = db.execute("INSERT INTO YCStudent (LastName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123)");
		assertEquals(RS.successful, false);
		assertNotEquals(RS.getMessage(), null);
	}
	
	@Test
	public void wrongLengthDecimal() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		ResultSet RS = db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9546, true, 800123456, 123)");
		assertEquals(RS.successful, false);
		assertNotEquals(RS.getMessage(), null);
	}
	
	@Test
	public void notUnique() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123)");
		ResultSet RS = db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123457, 123)");
		assertEquals(RS.successful, false);
		assertNotEquals(RS.getMessage(), null);
	}
	
	@Test
	public void notUniquePrimaryKey() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123)");
		ResultSet RS = db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 1234)");
		assertEquals(RS.successful, false);
		assertNotEquals(RS.getMessage(), null);
	}
	
	@Test
	public void notNull() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, 3.9, true, 800123456, 123)");
		ResultSet RS = db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123)");
		assertEquals(RS.successful, false);
		assertNotEquals(RS.getMessage(), null);	
	}
	
	@Test
	public void notNullPrimaryKey() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		ResultSet RS = db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, SSNum) VALUES (Jacob, Naiman, 3.9, true, 123)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123)");
		assertEquals(RS.successful, false);
		assertNotEquals(RS.getMessage(), null);
	}
	
	@Test
	public void nonexistantColumn() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		ResultSet RS = db.execute("INSERT INTO YCStudent (fakecolumn, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123)");
		assertEquals(RS.successful, false);
		assertNotEquals(RS.getMessage(), null);
	}
	
	@Test
	public void nonexistantTable() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		ResultSet RS = db.execute("INSERT INTO YC (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123)");
		assertEquals(RS.successful, false);
		assertNotEquals(RS.getMessage(), null);
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
	
	@Test
	public void updateNoWhere() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("UPDATE YCStudent SET GPA=3.0, Class='Super Senior'");
		assertEquals(3, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
		assertEquals("800123457", db.allTables.get(0).rows.get(1).rowEntries[ban].value);
		assertEquals("Eli", db.allTables.get(0).rows.get(1).rowEntries[first].value);
		assertEquals("Goldberg", db.allTables.get(0).rows.get(1).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(1).rowEntries[gpa].value);
		assertEquals("124", db.allTables.get(0).rows.get(1).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(1).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(1).rowEntries[cla].value);
		assertEquals("800123458", db.allTables.get(0).rows.get(2).rowEntries[ban].value);
		assertEquals("Gilad", db.allTables.get(0).rows.get(2).rowEntries[first].value);
		assertEquals("Felson", db.allTables.get(0).rows.get(2).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(2).rowEntries[gpa].value);
		assertEquals("125", db.allTables.get(0).rows.get(2).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(2).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(2).rowEntries[cla].value);
	}
	
	@Test
	public void updateWithWhereOneCondition() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("UPDATE YCStudent SET GPA=3.0, Class='Super Senior' WHERE FirstName=Jacob");
		assertEquals(3, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
		assertEquals("800123457", db.allTables.get(0).rows.get(1).rowEntries[ban].value);
		assertEquals("Eli", db.allTables.get(0).rows.get(1).rowEntries[first].value);
		assertEquals("Goldberg", db.allTables.get(0).rows.get(1).rowEntries[last].value);
		assertEquals("4.0", db.allTables.get(0).rows.get(1).rowEntries[gpa].value);
		assertEquals("124", db.allTables.get(0).rows.get(1).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(1).rowEntries[cur].value);
		assertEquals("freshman", db.allTables.get(0).rows.get(1).rowEntries[cla].value);
		assertEquals("800123458", db.allTables.get(0).rows.get(2).rowEntries[ban].value);
		assertEquals("Gilad", db.allTables.get(0).rows.get(2).rowEntries[first].value);
		assertEquals("Felson", db.allTables.get(0).rows.get(2).rowEntries[last].value);
		assertEquals("3.7", db.allTables.get(0).rows.get(2).rowEntries[gpa].value);
		assertEquals("125", db.allTables.get(0).rows.get(2).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(2).rowEntries[cur].value);
		assertEquals("sophomore", db.allTables.get(0).rows.get(2).rowEntries[cla].value);
	}
	
	@Test
	public void updateWithWhereANDCondition() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 4.0, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("UPDATE YCStudent SET GPA=3.0, Class='Super Senior' WHERE GPA=4.0 AND Class=sophomore");
		assertEquals(3, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
		assertEquals("800123457", db.allTables.get(0).rows.get(1).rowEntries[ban].value);
		assertEquals("Eli", db.allTables.get(0).rows.get(1).rowEntries[first].value);
		assertEquals("Goldberg", db.allTables.get(0).rows.get(1).rowEntries[last].value);
		assertEquals("4.0", db.allTables.get(0).rows.get(1).rowEntries[gpa].value);
		assertEquals("124", db.allTables.get(0).rows.get(1).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(1).rowEntries[cur].value);
		assertEquals("freshman", db.allTables.get(0).rows.get(1).rowEntries[cla].value);
		assertEquals("800123458", db.allTables.get(0).rows.get(2).rowEntries[ban].value);
		assertEquals("Gilad", db.allTables.get(0).rows.get(2).rowEntries[first].value);
		assertEquals("Felson", db.allTables.get(0).rows.get(2).rowEntries[last].value);
		assertEquals("3.7", db.allTables.get(0).rows.get(2).rowEntries[gpa].value);
		assertEquals("125", db.allTables.get(0).rows.get(2).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(2).rowEntries[cur].value);
		assertEquals("sophomore", db.allTables.get(0).rows.get(2).rowEntries[cla].value);
	}
	
	@Test
	public void updateWithWhereANDCondition2() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 4.0, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("UPDATE YCStudent SET GPA=3.0, Class='Super Senior' WHERE GPA=4.0 AND Class=sophomore AND FirstName=Eli");
		assertEquals(3, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("4.0", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("sophomore", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
		assertEquals("800123457", db.allTables.get(0).rows.get(1).rowEntries[ban].value);
		assertEquals("Eli", db.allTables.get(0).rows.get(1).rowEntries[first].value);
		assertEquals("Goldberg", db.allTables.get(0).rows.get(1).rowEntries[last].value);
		assertEquals("4.0", db.allTables.get(0).rows.get(1).rowEntries[gpa].value);
		assertEquals("124", db.allTables.get(0).rows.get(1).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(1).rowEntries[cur].value);
		assertEquals("freshman", db.allTables.get(0).rows.get(1).rowEntries[cla].value);
		assertEquals("800123458", db.allTables.get(0).rows.get(2).rowEntries[ban].value);
		assertEquals("Gilad", db.allTables.get(0).rows.get(2).rowEntries[first].value);
		assertEquals("Felson", db.allTables.get(0).rows.get(2).rowEntries[last].value);
		assertEquals("3.7", db.allTables.get(0).rows.get(2).rowEntries[gpa].value);
		assertEquals("125", db.allTables.get(0).rows.get(2).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(2).rowEntries[cur].value);
		assertEquals("sophomore", db.allTables.get(0).rows.get(2).rowEntries[cla].value);
	}
	
	@Test
	public void updateWithWhereOneORCondition() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("UPDATE YCStudent SET GPA=3.0, Class='Super Senior' WHERE GPA=4.0 OR FirstName=Gilad");
		assertEquals(3, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.9", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("sophomore", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
		assertEquals("800123457", db.allTables.get(0).rows.get(1).rowEntries[ban].value);
		assertEquals("Eli", db.allTables.get(0).rows.get(1).rowEntries[first].value);
		assertEquals("Goldberg", db.allTables.get(0).rows.get(1).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(1).rowEntries[gpa].value);
		assertEquals("124", db.allTables.get(0).rows.get(1).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(1).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(1).rowEntries[cla].value);
		assertEquals("800123458", db.allTables.get(0).rows.get(2).rowEntries[ban].value);
		assertEquals("Gilad", db.allTables.get(0).rows.get(2).rowEntries[first].value);
		assertEquals("Felson", db.allTables.get(0).rows.get(2).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(2).rowEntries[gpa].value);
		assertEquals("125", db.allTables.get(0).rows.get(2).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(2).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(2).rowEntries[cla].value);
	}
	
	@Test
	public void updateWithWhereTwoORCondition() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		ResultSet RS = db.execute("UPDATE YCStudent SET GPA=3.0, Class='Super Senior' WHERE GPA=4.0 OR FirstName=Gilad OR SSNum=123");
		assertEquals(3, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
		assertEquals("800123457", db.allTables.get(0).rows.get(1).rowEntries[ban].value);
		assertEquals("Eli", db.allTables.get(0).rows.get(1).rowEntries[first].value);
		assertEquals("Goldberg", db.allTables.get(0).rows.get(1).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(1).rowEntries[gpa].value);
		assertEquals("124", db.allTables.get(0).rows.get(1).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(1).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(1).rowEntries[cla].value);
		assertEquals("800123458", db.allTables.get(0).rows.get(2).rowEntries[ban].value);
		assertEquals("Gilad", db.allTables.get(0).rows.get(2).rowEntries[first].value);
		assertEquals("Felson", db.allTables.get(0).rows.get(2).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(2).rowEntries[gpa].value);
		assertEquals("125", db.allTables.get(0).rows.get(2).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(2).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(2).rowEntries[cla].value);
		assertEquals(true, RS.successful);
		assertEquals(RS.getMessage(), null);
		assertEquals(RS.getColumns().size(), 1);
	}
	
	@Test
	public void updateWithWhereThreeORCondition() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("UPDATE YCStudent SET GPA=3.0, Class='Super Senior' WHERE GPA=4.0 OR FirstName=Gilad OR SSNum=124 OR LastName=g");
		assertEquals(3, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.9", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("sophomore", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
		assertEquals("800123457", db.allTables.get(0).rows.get(1).rowEntries[ban].value);
		assertEquals("Eli", db.allTables.get(0).rows.get(1).rowEntries[first].value);
		assertEquals("Goldberg", db.allTables.get(0).rows.get(1).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(1).rowEntries[gpa].value);
		assertEquals("124", db.allTables.get(0).rows.get(1).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(1).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(1).rowEntries[cla].value);
		assertEquals("800123458", db.allTables.get(0).rows.get(2).rowEntries[ban].value);
		assertEquals("Gilad", db.allTables.get(0).rows.get(2).rowEntries[first].value);
		assertEquals("Felson", db.allTables.get(0).rows.get(2).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(2).rowEntries[gpa].value);
		assertEquals("125", db.allTables.get(0).rows.get(2).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(2).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(2).rowEntries[cla].value);
	}
	
	@Test
	public void updateWithWhereANDandORCondition() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("UPDATE YCStudent SET GPA=3.0, Class='Super Senior' WHERE GPA=4.0 AND FirstName=Gilad OR SSNum=124 OR LastName=g");
		assertEquals(3, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}//just eli
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.9", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("sophomore", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
		assertEquals("800123457", db.allTables.get(0).rows.get(1).rowEntries[ban].value);
		assertEquals("Eli", db.allTables.get(0).rows.get(1).rowEntries[first].value);
		assertEquals("Goldberg", db.allTables.get(0).rows.get(1).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(1).rowEntries[gpa].value);
		assertEquals("124", db.allTables.get(0).rows.get(1).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(1).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(1).rowEntries[cla].value);
		assertEquals("800123458", db.allTables.get(0).rows.get(2).rowEntries[ban].value);
		assertEquals("Gilad", db.allTables.get(0).rows.get(2).rowEntries[first].value);
		assertEquals("Felson", db.allTables.get(0).rows.get(2).rowEntries[last].value);
		assertEquals("3.7", db.allTables.get(0).rows.get(2).rowEntries[gpa].value);
		assertEquals("125", db.allTables.get(0).rows.get(2).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(2).rowEntries[cur].value);
		assertEquals("sophomore", db.allTables.get(0).rows.get(2).rowEntries[cla].value);
	}
	
	@Test
	public void updateWithWhereANDandORParentasesCondition() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("UPDATE YCStudent SET GPA=3.0, Class='Super Senior' WHERE (GPA=4.0 OR GPA=3.7) AND (FirstName=Gilad OR SSNum=124) OR LastName=g");
		assertEquals(3, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}//just eli and gilad
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.9", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("sophomore", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
		assertEquals("800123457", db.allTables.get(0).rows.get(1).rowEntries[ban].value);
		assertEquals("Eli", db.allTables.get(0).rows.get(1).rowEntries[first].value);
		assertEquals("Goldberg", db.allTables.get(0).rows.get(1).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(1).rowEntries[gpa].value);
		assertEquals("124", db.allTables.get(0).rows.get(1).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(1).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(1).rowEntries[cla].value);
		assertEquals("800123458", db.allTables.get(0).rows.get(2).rowEntries[ban].value);
		assertEquals("Gilad", db.allTables.get(0).rows.get(2).rowEntries[first].value);
		assertEquals("Felson", db.allTables.get(0).rows.get(2).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(2).rowEntries[gpa].value);
		assertEquals("125", db.allTables.get(0).rows.get(2).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(2).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(2).rowEntries[cla].value);
	}
	
	@Test
	public void updateWithWherenotequals() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("UPDATE YCStudent SET GPA=3.0, Class='Super Senior' WHERE Class<>freshman OR LastName=g");
		assertEquals(3, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}//just me and gilad
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
		assertEquals("800123457", db.allTables.get(0).rows.get(1).rowEntries[ban].value);
		assertEquals("Eli", db.allTables.get(0).rows.get(1).rowEntries[first].value);
		assertEquals("Goldberg", db.allTables.get(0).rows.get(1).rowEntries[last].value);
		assertEquals("4.0", db.allTables.get(0).rows.get(1).rowEntries[gpa].value);
		assertEquals("124", db.allTables.get(0).rows.get(1).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(1).rowEntries[cur].value);
		assertEquals("freshman", db.allTables.get(0).rows.get(1).rowEntries[cla].value);
		assertEquals("800123458", db.allTables.get(0).rows.get(2).rowEntries[ban].value);
		assertEquals("Gilad", db.allTables.get(0).rows.get(2).rowEntries[first].value);
		assertEquals("Felson", db.allTables.get(0).rows.get(2).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(2).rowEntries[gpa].value);
		assertEquals("125", db.allTables.get(0).rows.get(2).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(2).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(2).rowEntries[cla].value);
	}
	
	@Test
	public void updateWithWherelessthan() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("UPDATE YCStudent SET GPA=3.0, Class='Super Senior' WHERE GPA<4.0");
		assertEquals(3, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}//just me and gilad
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
		assertEquals("800123457", db.allTables.get(0).rows.get(1).rowEntries[ban].value);
		assertEquals("Eli", db.allTables.get(0).rows.get(1).rowEntries[first].value);
		assertEquals("Goldberg", db.allTables.get(0).rows.get(1).rowEntries[last].value);
		assertEquals("4.0", db.allTables.get(0).rows.get(1).rowEntries[gpa].value);
		assertEquals("124", db.allTables.get(0).rows.get(1).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(1).rowEntries[cur].value);
		assertEquals("freshman", db.allTables.get(0).rows.get(1).rowEntries[cla].value);
		assertEquals("800123458", db.allTables.get(0).rows.get(2).rowEntries[ban].value);
		assertEquals("Gilad", db.allTables.get(0).rows.get(2).rowEntries[first].value);
		assertEquals("Felson", db.allTables.get(0).rows.get(2).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(2).rowEntries[gpa].value);
		assertEquals("125", db.allTables.get(0).rows.get(2).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(2).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(2).rowEntries[cla].value);
	}
	
	@Test
	public void updateWithWherelessthanorequals() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("UPDATE YCStudent SET GPA=3.0, Class='Super Senior' WHERE GPA<=3.9");
		assertEquals(3, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}//just me and gilad
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
		assertEquals("800123457", db.allTables.get(0).rows.get(1).rowEntries[ban].value);
		assertEquals("Eli", db.allTables.get(0).rows.get(1).rowEntries[first].value);
		assertEquals("Goldberg", db.allTables.get(0).rows.get(1).rowEntries[last].value);
		assertEquals("4.0", db.allTables.get(0).rows.get(1).rowEntries[gpa].value);
		assertEquals("124", db.allTables.get(0).rows.get(1).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(1).rowEntries[cur].value);
		assertEquals("freshman", db.allTables.get(0).rows.get(1).rowEntries[cla].value);
		assertEquals("800123458", db.allTables.get(0).rows.get(2).rowEntries[ban].value);
		assertEquals("Gilad", db.allTables.get(0).rows.get(2).rowEntries[first].value);
		assertEquals("Felson", db.allTables.get(0).rows.get(2).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(2).rowEntries[gpa].value);
		assertEquals("125", db.allTables.get(0).rows.get(2).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(2).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(2).rowEntries[cla].value);
	}
	
	@Test
	public void updateWithWheregreaterthan() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("UPDATE YCStudent SET GPA=3.0, Class='Super Senior' WHERE GPA>3.7");
		assertEquals(3, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}//just me and eli
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
		assertEquals("800123457", db.allTables.get(0).rows.get(1).rowEntries[ban].value);
		assertEquals("Eli", db.allTables.get(0).rows.get(1).rowEntries[first].value);
		assertEquals("Goldberg", db.allTables.get(0).rows.get(1).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(1).rowEntries[gpa].value);
		assertEquals("124", db.allTables.get(0).rows.get(1).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(1).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(1).rowEntries[cla].value);
		assertEquals("800123458", db.allTables.get(0).rows.get(2).rowEntries[ban].value);
		assertEquals("Gilad", db.allTables.get(0).rows.get(2).rowEntries[first].value);
		assertEquals("Felson", db.allTables.get(0).rows.get(2).rowEntries[last].value);
		assertEquals("3.7", db.allTables.get(0).rows.get(2).rowEntries[gpa].value);
		assertEquals("125", db.allTables.get(0).rows.get(2).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(2).rowEntries[cur].value);
		assertEquals("sophomore", db.allTables.get(0).rows.get(2).rowEntries[cla].value);
	}
	
	@Test
	public void updateWithWheregreaterthanorequals() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("UPDATE YCStudent SET GPA=3.0, Class='Super Senior' WHERE GPA>=3.7");
		assertEquals(3, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}//all 3
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
		assertEquals("800123457", db.allTables.get(0).rows.get(1).rowEntries[ban].value);
		assertEquals("Eli", db.allTables.get(0).rows.get(1).rowEntries[first].value);
		assertEquals("Goldberg", db.allTables.get(0).rows.get(1).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(1).rowEntries[gpa].value);
		assertEquals("124", db.allTables.get(0).rows.get(1).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(1).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(1).rowEntries[cla].value);
		assertEquals("800123458", db.allTables.get(0).rows.get(2).rowEntries[ban].value);
		assertEquals("Gilad", db.allTables.get(0).rows.get(2).rowEntries[first].value);
		assertEquals("Felson", db.allTables.get(0).rows.get(2).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(2).rowEntries[gpa].value);
		assertEquals("125", db.allTables.get(0).rows.get(2).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(2).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(2).rowEntries[cla].value);
	}
	
	@Test
	public void updateWithWheregreaterthanstring() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("UPDATE YCStudent SET GPA=3.0, Class='Super Senior' WHERE FirstName>=Gilad");
		assertEquals(3, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}//me and gilad
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
		assertEquals("800123457", db.allTables.get(0).rows.get(1).rowEntries[ban].value);
		assertEquals("Eli", db.allTables.get(0).rows.get(1).rowEntries[first].value);
		assertEquals("Goldberg", db.allTables.get(0).rows.get(1).rowEntries[last].value);
		assertEquals("4.0", db.allTables.get(0).rows.get(1).rowEntries[gpa].value);
		assertEquals("124", db.allTables.get(0).rows.get(1).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(1).rowEntries[cur].value);
		assertEquals("freshman", db.allTables.get(0).rows.get(1).rowEntries[cla].value);
		assertEquals("800123458", db.allTables.get(0).rows.get(2).rowEntries[ban].value);
		assertEquals("Gilad", db.allTables.get(0).rows.get(2).rowEntries[first].value);
		assertEquals("Felson", db.allTables.get(0).rows.get(2).rowEntries[last].value);
		assertEquals("3.0", db.allTables.get(0).rows.get(2).rowEntries[gpa].value);
		assertEquals("125", db.allTables.get(0).rows.get(2).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(2).rowEntries[cur].value);
		assertEquals("'Super Senior'", db.allTables.get(0).rows.get(2).rowEntries[cla].value);
	}
	
	@Test
	public void deleteNoWhere() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		assertEquals(3, db.allTables.get(0).rows.size());
		db.execute("DELETE FROM YCStudent;");
		assertEquals(0, db.allTables.get(0).rows.size());
	}
	
	@Test
	public void deleteWithWhereHappyPath() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("DELETE FROM YCStudent WHERE FirstName=Eli");
		assertEquals(2, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}//delete eli
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.9", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("sophomore", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
		assertEquals("800123458", db.allTables.get(0).rows.get(1).rowEntries[ban].value);
		assertEquals("Gilad", db.allTables.get(0).rows.get(1).rowEntries[first].value);
		assertEquals("Felson", db.allTables.get(0).rows.get(1).rowEntries[last].value);
		assertEquals("3.7", db.allTables.get(0).rows.get(1).rowEntries[gpa].value);
		assertEquals("125", db.allTables.get(0).rows.get(1).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(1).rowEntries[cur].value);
		assertEquals("sophomore", db.allTables.get(0).rows.get(1).rowEntries[cla].value);
	}
	
	@Test
	public void deleteWithWhereHappyPath2() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("DELETE FROM YCStudent WHERE GPA<3.9 AND Class=sophomore OR SSNum=124");
		assertEquals(1, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}//delete eli and gilad
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.9", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("sophomore", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
	}
	
	@Test
	public void deleteWithWhereHappyPath3() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, junior)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("DELETE FROM YCStudent WHERE GPA<3.9 AND Class=sophomore OR SSNum=124");
		assertEquals(2, db.allTables.get(0).rows.size());
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			if (db.allTables.get(0).cds[i].getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (db.allTables.get(0).cds[i].getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}//delete eli and gilad, and judah
		assertEquals("800123456", db.allTables.get(0).rows.get(0).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(0).rowEntries[first].value);
		assertEquals("Naiman", db.allTables.get(0).rows.get(0).rowEntries[last].value);
		assertEquals("3.9", db.allTables.get(0).rows.get(0).rowEntries[gpa].value);
		assertEquals("123", db.allTables.get(0).rows.get(0).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(0).rowEntries[cur].value);
		assertEquals("sophomore", db.allTables.get(0).rows.get(0).rowEntries[cla].value);
		assertEquals("800123459", db.allTables.get(0).rows.get(1).rowEntries[ban].value);
		assertEquals("Jacob", db.allTables.get(0).rows.get(1).rowEntries[first].value);
		assertEquals("Mendleson", db.allTables.get(0).rows.get(1).rowEntries[last].value);
		assertEquals("4.0", db.allTables.get(0).rows.get(1).rowEntries[gpa].value);
		assertEquals("126", db.allTables.get(0).rows.get(1).rowEntries[ss].value);
		assertEquals("true", db.allTables.get(0).rows.get(1).rowEntries[cur].value);
		assertEquals("junior", db.allTables.get(0).rows.get(1).rowEntries[cla].value);
	}
	
	@Test
	public void selectAllColumns() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, junior)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		assertEquals(5, db.allTables.get(0).rows.size());
		ResultSet RS = db.execute("SELECT * FROM YCStudent");
		//System.out.println(RS.getColumns().get(0));
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 7; i++) {
			//System.out.println("count");
			if (RS.getColumns().get(i).getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (RS.getColumns().get(i).getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (RS.getColumns().get(i).getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (RS.getColumns().get(i).getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (RS.getColumns().get(i).getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (RS.getColumns().get(i).getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (RS.getColumns().get(i).getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}//delete eli and gilad, and judah
		assertEquals("800123456", RS.getColumns().get(ban).getSelectColumn().get(0).value);
		assertEquals("800123457", RS.getColumns().get(ban).getSelectColumn().get(1).value);
		assertEquals("800123458", RS.getColumns().get(ban).getSelectColumn().get(2).value);
		assertEquals("800123459", RS.getColumns().get(ban).getSelectColumn().get(3).value);
		assertEquals("800123454", RS.getColumns().get(ban).getSelectColumn().get(4).value);
		assertEquals("true", RS.getColumns().get(cur).getSelectColumn().get(0).value);
		assertEquals("true", RS.getColumns().get(cur).getSelectColumn().get(1).value);
		assertEquals("true", RS.getColumns().get(cur).getSelectColumn().get(2).value);
		assertEquals("true", RS.getColumns().get(cur).getSelectColumn().get(3).value);
		assertEquals("true", RS.getColumns().get(cur).getSelectColumn().get(4).value);
		assertEquals("Jacob", RS.getColumns().get(first).getSelectColumn().get(0).value);
		assertEquals("Eli", RS.getColumns().get(first).getSelectColumn().get(1).value);
		assertEquals("Gilad", RS.getColumns().get(first).getSelectColumn().get(2).value);
		assertEquals("Jacob", RS.getColumns().get(first).getSelectColumn().get(3).value);
		assertEquals("Judah", RS.getColumns().get(first).getSelectColumn().get(4).value);
		assertEquals("Naiman", RS.getColumns().get(last).getSelectColumn().get(0).value);
		assertEquals("Goldberg", RS.getColumns().get(last).getSelectColumn().get(1).value);
		assertEquals("Felson", RS.getColumns().get(last).getSelectColumn().get(2).value);
		assertEquals("Mendleson", RS.getColumns().get(last).getSelectColumn().get(3).value);
		assertEquals("Goldfeder", RS.getColumns().get(last).getSelectColumn().get(4).value);
		assertEquals("3.9", RS.getColumns().get(gpa).getSelectColumn().get(0).value);
		assertEquals("4.0", RS.getColumns().get(gpa).getSelectColumn().get(1).value);
		assertEquals("3.7", RS.getColumns().get(gpa).getSelectColumn().get(2).value);
		assertEquals("4.0", RS.getColumns().get(gpa).getSelectColumn().get(3).value);
		assertEquals("3.2", RS.getColumns().get(gpa).getSelectColumn().get(4).value);
		assertEquals("sophomore", RS.getColumns().get(cla).getSelectColumn().get(0).value);
		assertEquals("freshman", RS.getColumns().get(cla).getSelectColumn().get(1).value);
		assertEquals("sophomore", RS.getColumns().get(cla).getSelectColumn().get(2).value);
		assertEquals("junior", RS.getColumns().get(cla).getSelectColumn().get(3).value);
		assertEquals("sophomore", RS.getColumns().get(cla).getSelectColumn().get(4).value);
		assertEquals("123", RS.getColumns().get(ss).getSelectColumn().get(0).value);
		assertEquals("124", RS.getColumns().get(ss).getSelectColumn().get(1).value);
		assertEquals("125", RS.getColumns().get(ss).getSelectColumn().get(2).value);
		assertEquals("126", RS.getColumns().get(ss).getSelectColumn().get(3).value);
		assertEquals("127", RS.getColumns().get(ss).getSelectColumn().get(4).value);
		//RS.print();
	}
	
	@Test
	public void selectAllColumnsWithWhere() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, junior)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		assertEquals(5, db.allTables.get(0).rows.size());
		ResultSet RS = db.execute("SELECT * FROM YCStudent WHERE (GPA > 3.6 AND FirstName<>Jacob) OR GPA <= 3.2");
		assertEquals(7, RS.getColumns().size());
		assertEquals(3, RS.getColumns().get(0).getSelectColumn().size());
		assertEquals("true", RS.getColumns().get(0).getSelectColumn().get(0).value);
		assertEquals("true", RS.getColumns().get(0).getSelectColumn().get(1).value);
		assertEquals("sophomore", RS.getColumns().get(1).getSelectColumn().get(0).value);
		//delete eli and gilad, and judah
		assertEquals(3, RS.getColumns().get(0).getSelectColumn().size());
		//RS.print();
	}
	
	@Test
	public void selectSomeColumns() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, junior)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		assertEquals(5, db.allTables.get(0).rows.size());
		ResultSet RS = db.execute("SELECT FirstName, LastName, GPA, SSNum FROM YCStudent");
		int ban = 0;
		int ss = 0;
		int first = 0;
		int last = 0;
		int gpa = 0;
		int cur = 0;
		int cla = 0;
		for (int i = 0; i < 4; i++) {
			if (RS.getColumns().get(i).getColumnName().equals("BannerID")) {
				ban = i;
				continue;
			}
			if (RS.getColumns().get(i).getColumnName().equals("Class")) {
				cla = i;
				continue;
			}
			if (RS.getColumns().get(i).getColumnName().equals("SSNum")) {
				ss = i;
				continue;
			}
			if (RS.getColumns().get(i).getColumnName().equals("FirstName")) {
				first = i;
				continue;
			}
			if (RS.getColumns().get(i).getColumnName().equals("LastName")) {
				last = i;
				continue;
			}
			if (RS.getColumns().get(i).getColumnName().equals("GPA")) {
				gpa = i;
				continue;
			}
			if (RS.getColumns().get(i).getColumnName().equals("CurrentStudent")) {
				cur = i;
				continue;
			}
		}
		assertEquals("Jacob", RS.getColumns().get(first).getSelectColumn().get(0).value);
		assertEquals("Eli", RS.getColumns().get(first).getSelectColumn().get(1).value);
		assertEquals("Gilad", RS.getColumns().get(first).getSelectColumn().get(2).value);
		assertEquals("Jacob", RS.getColumns().get(first).getSelectColumn().get(3).value);
		assertEquals("Judah", RS.getColumns().get(first).getSelectColumn().get(4).value);
		assertEquals("Naiman", RS.getColumns().get(last).getSelectColumn().get(0).value);
		assertEquals("Goldberg", RS.getColumns().get(last).getSelectColumn().get(1).value);
		assertEquals("Felson", RS.getColumns().get(last).getSelectColumn().get(2).value);
		assertEquals("Mendleson", RS.getColumns().get(last).getSelectColumn().get(3).value);
		assertEquals("Goldfeder", RS.getColumns().get(last).getSelectColumn().get(4).value);
		assertEquals("3.9", RS.getColumns().get(gpa).getSelectColumn().get(0).value);
		assertEquals("4.0", RS.getColumns().get(gpa).getSelectColumn().get(1).value);
		assertEquals("3.7", RS.getColumns().get(gpa).getSelectColumn().get(2).value);
		assertEquals("4.0", RS.getColumns().get(gpa).getSelectColumn().get(3).value);
		assertEquals("3.2", RS.getColumns().get(gpa).getSelectColumn().get(4).value);
		assertEquals("123", RS.getColumns().get(ss).getSelectColumn().get(0).value);
		assertEquals("124", RS.getColumns().get(ss).getSelectColumn().get(1).value);
		assertEquals("125", RS.getColumns().get(ss).getSelectColumn().get(2).value);
		assertEquals("126", RS.getColumns().get(ss).getSelectColumn().get(3).value);
		assertEquals("127", RS.getColumns().get(ss).getSelectColumn().get(4).value);
		//RS.print();
	}
	
	@Test
	public void selectDistinctSomeColumns() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, junior)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		assertEquals(5, db.allTables.get(0).rows.size());
		ResultSet RS = db.execute("SELECT DISTINCT Class FROM YCStudent");
		assertEquals("sophomore", RS.getColumns().get(0).getSelectColumn().get(0).value);
		assertEquals("freshman", RS.getColumns().get(0).getSelectColumn().get(1).value);
		assertEquals("junior", RS.getColumns().get(0).getSelectColumn().get(2).value);
		assertEquals(3, RS.getColumns().get(0).getSelectColumn().size());
		//RS.print();
	}
	
	@Test
	public void selectDistinctMoreColumns() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		assertEquals(5, db.allTables.get(0).rows.size());
		ResultSet RS = db.execute("SELECT DISTINCT Class, FirstName FROM YCStudent");
		assertEquals(2, RS.getColumns().size());
		assertEquals("sophomore", RS.getColumns().get(0).getSelectColumn().get(0).value);
		assertEquals("Jacob", RS.getColumns().get(1).getSelectColumn().get(0).value);
		assertEquals("freshman", RS.getColumns().get(0).getSelectColumn().get(1).value);
		assertEquals("sophomore", RS.getColumns().get(0).getSelectColumn().get(2).value);
		assertEquals("sophomore", RS.getColumns().get(0).getSelectColumn().get(3).value);
		assertEquals("Judah", RS.getColumns().get(1).getSelectColumn().get(3).value);
		assertEquals(4, RS.getColumns().get(0).getSelectColumn().size());
		//RS.print();
	}
	
	@Test
	public void selectSomeColumnsOrderBy() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, junior)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		assertEquals(5, db.allTables.get(0).rows.size());
		ResultSet RS = db.execute("SELECT FirstName, LastName, GPA, SSNum FROM YCStudent ORDER BY LastName ASC");
		//RS.print();
		assertEquals(4, RS.getColumns().size());
		assertEquals("Felson", RS.getColumns().get(1).getSelectColumn().get(0).value);
		assertEquals("Goldberg", RS.getColumns().get(1).getSelectColumn().get(1).value);
		assertEquals("Goldfeder", RS.getColumns().get(1).getSelectColumn().get(2).value);
		assertEquals("Mendleson", RS.getColumns().get(1).getSelectColumn().get(3).value);
		assertEquals("Naiman", RS.getColumns().get(1).getSelectColumn().get(4).value);
	}
	
	@Test
	public void selectSomeColumnsOrderByMultiple() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, junior)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		ResultSet RS = db.execute("SELECT FirstName, LastName, GPA, SSNum FROM YCStudent ORDER BY GPA ASC, FirstName DESC");
		assertEquals(5, db.allTables.get(0).rows.size());
		assertEquals(4, RS.getColumns().size());
		assertEquals("Felson", RS.getColumns().get(1).getSelectColumn().get(1).value);
		assertEquals("Goldberg", RS.getColumns().get(1).getSelectColumn().get(4).value);
		assertEquals("Goldfeder", RS.getColumns().get(1).getSelectColumn().get(0).value);
		assertEquals("Mendleson", RS.getColumns().get(1).getSelectColumn().get(3).value);
		assertEquals("Naiman", RS.getColumns().get(1).getSelectColumn().get(2).value);
		//RS.print();
	}
	
	@Test
	public void selectSomeColumnsOrderByMultiple3() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		assertEquals(5, db.allTables.get(0).rows.size());
		ResultSet RS = db.execute("SELECT FirstName, LastName, GPA, SSNum, Class FROM YCStudent ORDER BY Class ASC, FirstName DESC, GPA DESC");
		assertEquals(5, db.allTables.get(0).rows.size());
		assertEquals(5, RS.getColumns().size());
		assertEquals("Felson", RS.getColumns().get(1).getSelectColumn().get(4).value);
		assertEquals("Goldberg", RS.getColumns().get(1).getSelectColumn().get(0).value);
		assertEquals("Goldfeder", RS.getColumns().get(1).getSelectColumn().get(1).value);
		assertEquals("Mendleson", RS.getColumns().get(1).getSelectColumn().get(2).value);
		assertEquals("Naiman", RS.getColumns().get(1).getSelectColumn().get(3).value);
		//RS.print();
	}
	
	@Test
	public void selectSomeColumnsOrderByMultiple5() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123451, 122, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123452, 121, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123453, 120, sophomore)");
		assertEquals(8, db.allTables.get(0).rows.size());
		ResultSet RS = db.execute("SELECT FirstName, LastName, GPA, SSNum, Class FROM YCStudent ORDER BY Class ASC, FirstName DESC, GPA DESC, SSNum ASC");
		assertEquals(8, db.allTables.get(0).rows.size());
		assertEquals("121", RS.getColumns().get(3).getSelectColumn().get(0).value);
		assertEquals("124", RS.getColumns().get(3).getSelectColumn().get(1).value);
		assertEquals("127", RS.getColumns().get(3).getSelectColumn().get(2).value);
		assertEquals("126", RS.getColumns().get(3).getSelectColumn().get(3).value);
		assertEquals("122", RS.getColumns().get(3).getSelectColumn().get(4).value);
		assertEquals("123", RS.getColumns().get(3).getSelectColumn().get(5).value);
		assertEquals("120", RS.getColumns().get(3).getSelectColumn().get(6).value);
		assertEquals("125", RS.getColumns().get(3).getSelectColumn().get(7).value);
		//RS.print();
	}
	
	@Test
	public void selectAverageAndSum() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		assertEquals(5, db.allTables.get(0).rows.size());
		ResultSet RS = db.execute("SELECT AVG (GPA), SUM(GPA) FROM YCStudent");
		assertEquals(2, RS.getColumns().size());
		assertEquals(1, RS.getColumns().get(0).getSelectColumn().size());
		assertEquals("3.76", RS.getColumns().get(0).getSelectColumn().get(0).value);
		assertEquals("18.8", RS.getColumns().get(1).getSelectColumn().get(0).value);
		//assertEquals("sophomore", RS.getColumns().get(0).getSelectColumn().get(1).value);
		//RS.print();
	}
	
	@Test
	public void selectMinAndMaxandCount() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		assertEquals(5, db.allTables.get(0).rows.size());
		ResultSet RS = db.execute("SELECT MIN (GPA), COUNT(SSNum), MAX(LastName) FROM YCStudent");
		assertEquals(3, RS.getColumns().size());
		assertEquals(1, RS.getColumns().get(0).getSelectColumn().size());
		assertEquals("3.2", RS.getColumns().get(0).getSelectColumn().get(0).value);
		assertEquals("5", RS.getColumns().get(1).getSelectColumn().get(0).value);
		assertEquals("Naiman", RS.getColumns().get(2).getSelectColumn().get(0).value);
		//RS.print();
	}
	
	@Test
	public void selectCountDistinct() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		assertEquals(5, db.allTables.get(0).rows.size());
		ResultSet RS = db.execute("SELECT MIN (FirstName), COUNT (DISTINCT GPA), MAX(LastName) FROM YCStudent");
		assertEquals(3, RS.getColumns().size());
		assertEquals(1, RS.getColumns().get(0).getSelectColumn().size());
		assertEquals("Eli", RS.getColumns().get(0).getSelectColumn().get(0).value);
		assertEquals("4", RS.getColumns().get(1).getSelectColumn().get(0).value);
		assertEquals("Naiman", RS.getColumns().get(2).getSelectColumn().get(0).value);
		//RS.print();
	}
	
	@Test
	public void selectColumnsandFunction() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		assertEquals(5, db.allTables.get(0).rows.size());
		ResultSet RS = db.execute("SELECT FirstName, LastName, MIN (GPA), AVG(SSNum), CurrentStudent FROM YCStudent");
		assertEquals(5, RS.getColumns().size());
		assertEquals(5, RS.getColumns().get(0).getSelectColumn().size());
		assertEquals("Jacob", RS.getColumns().get(0).getSelectColumn().get(0).value);
		assertEquals("Naiman", RS.getColumns().get(1).getSelectColumn().get(0).value);
		assertEquals("3.2", RS.getColumns().get(2).getSelectColumn().get(0).value);
		assertEquals("125.0", RS.getColumns().get(3).getSelectColumn().get(0).value);
		assertEquals("Eli", RS.getColumns().get(0).getSelectColumn().get(1).value);
		assertEquals("Goldberg", RS.getColumns().get(1).getSelectColumn().get(1).value);
		assertEquals("3.2", RS.getColumns().get(2).getSelectColumn().get(1).value);
		assertEquals("125.0", RS.getColumns().get(3).getSelectColumn().get(1).value);
		//RS.print();
	}
	
	@Test
	public void selectColumnsandFunctionSameColumn() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		assertEquals(5, db.allTables.get(0).rows.size());
		ResultSet RS = db.execute("SELECT GPA, MIN (GPA), AVG(GPA) FROM YCStudent");
		assertEquals(3, RS.getColumns().size());
		assertEquals(5, RS.getColumns().get(0).getSelectColumn().size());
		assertEquals("3.9", RS.getColumns().get(0).getSelectColumn().get(0).value);
		assertEquals("3.2", RS.getColumns().get(1).getSelectColumn().get(0).value);
		assertEquals("3.76", RS.getColumns().get(2).getSelectColumn().get(0).value);
		assertEquals("4.0", RS.getColumns().get(0).getSelectColumn().get(1).value);
		assertEquals("3.2", RS.getColumns().get(1).getSelectColumn().get(1).value);
		//RS.print();
	}
	
	
	@Test
	public void createIndex() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123451, 122, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123452, 121, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123453, 120, sophomore)");
		db.execute("CREATE INDEX FirstName_Index ON YCStudent (FirstName)");
		assertEquals(2, db.allTables.get(0).indexedColumns.size());
		assertEquals(8, db.allTables.get(0).indexedColumns.get(1).n);
		assertEquals(1, db.allTables.get(0).indexedColumns.get(1).height);
		assertEquals(4 , db.allTables.get(0).indexedColumns.get(1).root.entries.length);
		assertEquals("Eli", db.allTables.get(0).indexedColumns.get(1).root.entries[0].key);
		assertEquals("Jacob", db.allTables.get(0).indexedColumns.get(1).root.entries[1].key);
		assertEquals("Jacob", db.allTables.get(0).indexedColumns.get(1).root.entries[1].child.entries[0].key);
		assertEquals(3, db.allTables.get(0).indexedColumns.get(1).root.entries[1].child.entries[0].val.size());
		assertEquals("Judah", db.allTables.get(0).indexedColumns.get(1).root.entries[1].child.entries[1].key);
		assertEquals(1, db.allTables.get(0).indexedColumns.get(1).root.entries[1].child.entries[1].val.size());
		assertEquals("Gilad", db.allTables.get(0).indexedColumns.get(1).root.entries[0].child.entries[1].key);
		assertEquals(2, db.allTables.get(0).indexedColumns.get(1).root.entries[0].child.entries[1].val.size());
		assertEquals("Eli", db.allTables.get(0).indexedColumns.get(1).root.entries[0].child.entries[0].key);
		assertEquals(2, db.allTables.get(0).indexedColumns.get(1).root.entries[0].child.entries[0].val.size());
		
	}
	
	@Test
	public void testingGet() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123451, 122, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123452, 121, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123453, 120, sophomore)");
		db.execute("CREATE INDEX FirstName_Index ON YCStudent (FirstName)");
		assertEquals(2, db.allTables.get(0).indexedColumns.size());
		assertEquals(8, db.allTables.get(0).indexedColumns.get(1).n);
		assertEquals("Eli", db.allTables.get(0).indexedColumns.get(1).root.entries[0].key);
		assertEquals("Jacob", db.allTables.get(0).indexedColumns.get(1).root.entries[1].key);
		assertEquals("Jacob", db.allTables.get(0).indexedColumns.get(1).root.entries[1].child.entries[0].key);
		assertEquals("Judah", db.allTables.get(0).indexedColumns.get(1).root.entries[1].child.entries[1].key);
		assertEquals(1, db.allTables.get(0).indexedColumns.get(1).root.entries[1].child.entries[1].val.size());
		assertEquals(3, db.allTables.get(0).indexedColumns.get(1).get("Jacob").size());
		assertEquals(2, db.allTables.get(0).indexedColumns.get(1).get("Eli").size());
		assertEquals(2, db.allTables.get(0).indexedColumns.get(1).get("Gilad").size());
		assertEquals(1, db.allTables.get(0).indexedColumns.get(1).get("Judah").size());
	}
	
	@Test
	public void testingGetLess() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123451, 122, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123452, 121, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123453, 120, sophomore)");
		db.execute("CREATE INDEX FirstName_Index ON YCStudent (FirstName)");
		assertEquals(7, db.allTables.get(0).indexedColumns.get(1).getLess("Judah").size());
		assertEquals(2, db.allTables.get(0).indexedColumns.get(1).getLess("Gilad").size());
	}
	
	@Test
	public void testingGetLessOrEqual() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123451, 122, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123452, 121, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123453, 120, sophomore)");
		db.execute("CREATE INDEX FirstName_Index ON YCStudent (FirstName)");
		assertEquals(8, db.allTables.get(0).indexedColumns.get(1).getLessOrEqual("Judah").size());
		assertEquals(7, db.allTables.get(0).indexedColumns.get(1).getLessOrEqual("Jacob").size());
		assertEquals(2, db.allTables.get(0).indexedColumns.get(1).getLessOrEqual("Eli").size());
	}
	
	@Test
	public void testingGetGreaterOrEqual() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123451, 122, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123452, 121, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123453, 120, sophomore)");
		db.execute("CREATE INDEX FirstName_Index ON YCStudent (FirstName)");
		assertEquals(1, db.allTables.get(0).indexedColumns.get(1).getGreaterOrEqual("Judah").size());
		assertEquals(4, db.allTables.get(0).indexedColumns.get(1).getGreaterOrEqual("Jacob").size());
		assertEquals(8, db.allTables.get(0).indexedColumns.get(1).getGreaterOrEqual("Eli").size());
		assertEquals(6, db.allTables.get(0).indexedColumns.get(1).getGreaterOrEqual("Gilad").size());
	}
	
	@Test
	public void testingGetGreater() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123451, 122, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123452, 121, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123453, 120, sophomore)");
		db.execute("CREATE INDEX FirstName_Index ON YCStudent (FirstName)");
		assertEquals(0, db.allTables.get(0).indexedColumns.get(1).getGreater("Judah").size());
		assertEquals(1, db.allTables.get(0).indexedColumns.get(1).getGreater("Jacob").size());
		assertEquals(6, db.allTables.get(0).indexedColumns.get(1).getGreater("Eli").size());
		assertEquals(4, db.allTables.get(0).indexedColumns.get(1).getGreater("Gilad").size());
	}
	
	@Test
	public void testDelete() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123451, 122, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123452, 121, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123453, 120, sophomore)");
		db.execute("CREATE INDEX FirstName_Index ON YCStudent (FirstName)");
		db.allTables.get(0).indexedColumns.get(1).delete("Jacob");
		assertEquals(0, db.allTables.get(0).indexedColumns.get(1).getGreater("Judah").size());
		assertEquals(1, db.allTables.get(0).indexedColumns.get(1).getGreater("Jacob").size());
		assertEquals(3, db.allTables.get(0).indexedColumns.get(1).getGreater("Eli").size());
		assertEquals(1, db.allTables.get(0).indexedColumns.get(1).getGreater("Gilad").size());
	}
	
	@Test
	public void btreeWithWhere() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123451, 122, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123452, 121, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123453, 120, sophomore)");
		db.execute("CREATE INDEX FirstName_Index ON YCStudent (FirstName)");
		db.execute("CREATE INDEX GPA_Index ON YCStudent (GPA)");
		ResultSet RS = db.execute("Select * FROM YCStudent WHERE FirstName = Jacob OR GPA > 3.8");
		//RS.print();
		assertEquals(7, RS.getColumns().size());
		assertEquals(5, RS.getColumns().get(0).getSelectColumn().size());
		assertEquals("Eli", RS.getColumns().get(3).getSelectColumn().get(0).value);
		assertEquals("Jacob", RS.getColumns().get(3).getSelectColumn().get(1).value);
		assertEquals("Jacob", RS.getColumns().get(3).getSelectColumn().get(2).value);
		assertEquals("Eli", RS.getColumns().get(3).getSelectColumn().get(3).value);
		assertEquals("Jacob", RS.getColumns().get(3).getSelectColumn().get(4).value);
	}
	
	@Test
	public void autoIndexofPK() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123451, 122, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123452, 121, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123453, 120, sophomore)");
		assertEquals(1, db.allTables.get(0).indexedColumns.size());
		assertEquals("BannerID", db.allTables.get(0).indexedColumns.get(0).column.cd.getColumnName());
	}
	
	@Test
	public void testBTreeDelete() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123451, 122, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123452, 121, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123453, 120, sophomore)");
		db.execute("CREATE INDEX GPA_Index ON YCStudent (GPA)");
		db.execute("CREATE INDEX FirstName_Index ON YCStudent (FirstName)");
		db.execute("DELETE FROM YCStudent WHERE FirstName > Jacob");
		db.execute("DELETE FROM YCStudent WHERE BannerID = 800123458");
		ResultSet RS = db.execute("SELECT * FROM YCStudent WHERE FirstName > Eli");
		//RS.print();
		assertEquals(7, RS.getColumns().size());
		assertEquals(4, RS.getColumns().get(0).getSelectColumn().size());
		assertEquals("Jacob", RS.getColumns().get(3).getSelectColumn().get(0).value);
		assertEquals("Jacob", RS.getColumns().get(3).getSelectColumn().get(1).value);
		assertEquals("Jacob", RS.getColumns().get(3).getSelectColumn().get(2).value);
		assertEquals("Gilad", RS.getColumns().get(3).getSelectColumn().get(3).value);
	}
	
	@Test
	public void testBTreeUpdate() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123451, 122, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123452, 121, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123453, 120, sophomore)");
		db.execute("CREATE INDEX GPA_Index ON YCStudent (GPA)");
		db.execute("CREATE INDEX FirstName_Index ON YCStudent (FirstName)");
		db.execute("UPDATE YCStudent SET GPA = 3.0 WHERE FirstName >= Jacob");
		db.execute("UPDATE YCStudent SET Class = 'Super Senior' WHERE BannerID = 800123458");
		ResultSet RS = db.execute("SELECT * FROM YCStudent WHERE FirstName > Eli");
		//RS.print();
		assertEquals(7, RS.getColumns().size());
		assertEquals(6, RS.getColumns().get(0).getSelectColumn().size());
		assertEquals("Judah", RS.getColumns().get(3).getSelectColumn().get(0).value);
		assertEquals("Jacob", RS.getColumns().get(3).getSelectColumn().get(1).value);
		assertEquals("Jacob", RS.getColumns().get(3).getSelectColumn().get(2).value);
		assertEquals("Jacob", RS.getColumns().get(3).getSelectColumn().get(3).value);
		assertEquals("Gilad", RS.getColumns().get(3).getSelectColumn().get(4).value);
		assertEquals("Gilad", RS.getColumns().get(3).getSelectColumn().get(5).value);
		assertEquals("3.0", RS.getColumns().get(2).getSelectColumn().get(0).value);
		assertEquals("3.0", RS.getColumns().get(2).getSelectColumn().get(1).value);
		assertEquals("3.0", RS.getColumns().get(2).getSelectColumn().get(2).value);
		assertEquals("3.0", RS.getColumns().get(2).getSelectColumn().get(3).value);
		assertEquals("3.7", RS.getColumns().get(2).getSelectColumn().get(4).value);
		assertEquals("3.7", RS.getColumns().get(2).getSelectColumn().get(5).value);
		assertEquals("'Super Senior'", RS.getColumns().get(1).getSelectColumn().get(4).value);
		assertEquals("sophomore", RS.getColumns().get(1).getSelectColumn().get(5).value);
	}
	
	@Test
	public void testBTreeUpdateAll() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123451, 122, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123452, 121, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123453, 120, sophomore)");
		db.execute("CREATE INDEX GPA_Index ON YCStudent (GPA)");
		db.execute("CREATE INDEX FirstName_Index ON YCStudent (FirstName)");
		db.execute("UPDATE YCStudent SET GPA = 3.0");
		db.execute("UPDATE YCStudent SET Class = 'Super Senior' WHERE BannerID = 800123458");
		ResultSet RS = db.execute("SELECT * FROM YCStudent WHERE FirstName > Eli");
		//RS.print();
		assertEquals(7, RS.getColumns().size());
		assertEquals(6, RS.getColumns().get(0).getSelectColumn().size());
		assertEquals("Judah", RS.getColumns().get(3).getSelectColumn().get(0).value);
		assertEquals("Jacob", RS.getColumns().get(3).getSelectColumn().get(1).value);
		assertEquals("Jacob", RS.getColumns().get(3).getSelectColumn().get(2).value);
		assertEquals("Jacob", RS.getColumns().get(3).getSelectColumn().get(3).value);
		assertEquals("Gilad", RS.getColumns().get(3).getSelectColumn().get(4).value);
		assertEquals("Gilad", RS.getColumns().get(3).getSelectColumn().get(5).value);
		assertEquals("3.0", RS.getColumns().get(2).getSelectColumn().get(0).value);
		assertEquals("3.0", RS.getColumns().get(2).getSelectColumn().get(1).value);
		assertEquals("3.0", RS.getColumns().get(2).getSelectColumn().get(2).value);
		assertEquals("3.0", RS.getColumns().get(2).getSelectColumn().get(3).value);
		assertEquals("3.0", RS.getColumns().get(2).getSelectColumn().get(4).value);
		assertEquals("3.0", RS.getColumns().get(2).getSelectColumn().get(5).value);
		assertEquals("'Super Senior'", RS.getColumns().get(1).getSelectColumn().get(4).value);
		assertEquals("sophomore", RS.getColumns().get(1).getSelectColumn().get(5).value);
	}
	
	@Test
	public void testBTreeDeleteAll() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123451, 122, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123452, 121, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123453, 120, sophomore)");
		db.execute("CREATE INDEX GPA_Index ON YCStudent (GPA)");
		db.execute("CREATE INDEX FirstName_Index ON YCStudent (FirstName)");
		db.execute("UPDATE YCStudent SET GPA = 3.0");
		db.execute("DELETE FROM YCStudent");
		ResultSet RS = db.execute("SELECT * FROM YCStudent WHERE FirstName > Eli");
		assertEquals(7, RS.getColumns().size());
		assertEquals(0, RS.getColumns().get(0).getSelectColumn().size());
		//RS.print();
	}
	
	@Test
	public void testOrderByNonSelectColumn() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123451, 122, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123452, 121, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123453, 120, sophomore)");
		ResultSet RS = db.execute("SELECT FirstName, LastName, BannerID FROM YCStudent ORDER BY GPA DESC");
		//RS.print();
		assertEquals(3, RS.getColumns().size());
		assertEquals(8, RS.getColumns().get(0).getSelectColumn().size());
		assertEquals("Eli", RS.getColumns().get(0).getSelectColumn().get(0).value);
		assertEquals("Jacob", RS.getColumns().get(0).getSelectColumn().get(1).value);
		assertEquals("Eli", RS.getColumns().get(0).getSelectColumn().get(2).value);
		assertEquals("Jacob", RS.getColumns().get(0).getSelectColumn().get(3).value);
		assertEquals("Jacob", RS.getColumns().get(0).getSelectColumn().get(4).value);
		assertEquals("Gilad", RS.getColumns().get(0).getSelectColumn().get(5).value);
		assertEquals("Gilad", RS.getColumns().get(0).getSelectColumn().get(6).value);
		assertEquals("Judah", RS.getColumns().get(0).getSelectColumn().get(7).value);
	}
	
	@Test
	public void testBTreeDeleteWithValuesInsertedInTheMiddle() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("CREATE INDEX GPA_Index ON YCStudent (GPA)");
		db.execute("CREATE INDEX FirstName_Index ON YCStudent (FirstName)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123451, 122, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123452, 121, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123453, 120, sophomore)");
		db.execute("DELETE FROM YCStudent WHERE FirstName > Jacob");
		db.execute("DELETE FROM YCStudent WHERE BannerID = 800123458");
		ResultSet RS = db.execute("SELECT * FROM YCStudent WHERE FirstName > Eli");
		//RS.print();
		assertEquals(7, RS.getColumns().size());
		assertEquals(4, RS.getColumns().get(0).getSelectColumn().size());
		assertEquals("Jacob", RS.getColumns().get(3).getSelectColumn().get(0).value);
		assertEquals("Jacob", RS.getColumns().get(3).getSelectColumn().get(1).value);
		assertEquals("Jacob", RS.getColumns().get(3).getSelectColumn().get(2).value);
		assertEquals("Gilad", RS.getColumns().get(3).getSelectColumn().get(3).value);
	}
	
	@Test
	public void testWhere() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123457, 124, freshman)");
		db.execute("CREATE INDEX GPA_Index ON YCStudent (GPA)");
		db.execute("CREATE INDEX FirstName_Index ON YCStudent (FirstName)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123451, 122, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Eli, Goldberg, 4.0, true, 800123452, 121, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Gilad, Felson, 3.7, true, 800123453, 120, sophomore)");
		ResultSet RS1 = db.execute("DELETE FROM YCStudent WHERE GPA = first");
		ResultSet RS = db.execute("SELECT * FROM YCStudent");
		//RS1.print();
		assertEquals(RS.successful, true);
	}
	
	@Test
	public void testWhereWithNull() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, Class varchar(100), PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (null, Goldberg, null, true, 800123457, 124, freshman)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (null, Felson, null, true, 800123458, 125, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Jacob, Mendleson, 4.0, true, 800123459, 126, sophomore)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum, Class) VALUES (Judah, Goldfeder, 3.2, true, 800123454, 127, sophomore)");
		ResultSet RS= db.execute("SELECT * FROM YCStudent WHERE GPA = null");
		assertEquals(7, RS.getColumns().size());
		assertEquals(0, RS.getColumns().get(0).getSelectColumn().size());
		
	}	
}
