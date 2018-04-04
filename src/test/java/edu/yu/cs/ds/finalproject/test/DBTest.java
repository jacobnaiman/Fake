package edu.yu.cs.ds.finalproject.test;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.ds.finalproject.Database;
import edu.yu.cs.ds.finalproject.ResultSet;
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
	
	@Test(expected = IllegalArgumentException.class)
	public void createDefaultPrimaryKey() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int DEFAULT 1, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
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
	
	@Test(expected = IllegalArgumentException.class)
	public void insertDuplicateColumns() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (LastName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123)");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void wrongValueTypeInt() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 3.4, 123)");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void wrongValueTypeDecimal() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, true, true, 800123456, 123)");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void wrongValueTypeBoolean() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, yes, 800123456, 123)");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void wrongLengthVarChar() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(2), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (LastName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123)");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void wrongLengthDecimal() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9546, true, 800123456, 123)");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void notUnique() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123457, 123)");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void notUniquePrimaryKey() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 1234)");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void notNull() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, 3.9, true, 800123456, 123)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123)");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void notNullPrimaryKey() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, SSNum) VALUES (Jacob, Naiman, 3.9, true, 123)");
		db.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123)");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nonexistantColumn() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YCStudent (fakecolumn, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123)");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nonexistantTable() throws JSQLParserException {
		db.execute("CREATE TABLE YCStudent (BannerID int, SSNum int UNIQUE, FirstName varchar(255), LastName varchar(255) NOT NULL, GPA decimal(1,2) DEFAULT 0.00, CurrentStudent boolean DEFAULT true, PRIMARY KEY (BannerID))");
		db.execute("INSERT INTO YC (FirstName, LastName, GPA, CurrentStudent, BannerID, SSNum) VALUES (Jacob, Naiman, 3.9, true, 800123456, 123)");
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
		db.execute("UPDATE YCStudent SET GPA=3.0, Class='Super Senior' WHERE GPA=4.0 OR FirstName=Gilad OR SSNum=123");
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
}
