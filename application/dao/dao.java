package application.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.controller.DataResult;
import application.singleton.Sconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class dao {
	public String user;
	public String password;
	private java.sql.Connection cnx;

	public dao(String user, String password) {
		cnx = (java.sql.Connection) Sconnection.getConnection(user, password);

	}
	public static String queryUser = "SELECT USERNAME ,  USER_ID ,  CREATED FROM ALL_USERS WHERE USERNAME = '";
	public static String queryIndex = "SELECT INDEX_NAME , INDEX_TYPE ,  TABLE_OWNER ,  TABLE_NAME ,  TABLE_TYPE  ,   PREFIX_LENGTH ,  TABLESPACE_NAME FROM USER_INDEXES WHERE INDEX_NAME = '";
	public static String queryTrigger = "SELECT TRIGGER_NAME ,  TRIGGER_TYPE ,  TRIGGERING_EVENT ,  TABLE_OWNER , BASE_OBJECT_TYPE ,  TABLE_NAME ,  COLUMN_NAME ,  REFERENCING_NAMES ,  WHEN_CLAUSE ,  STATUS from "
			+ "USER_TRIGGERS WHERE TRIGGER_NAME = '";

	public static String querySeq = "SELECT SEQUENCE_NAME ,  MIN_VALUE ,  MAX_VALUE ,  INCREMENT_BY  ,CACHE_SIZE ,  LAST_NUMBER FROM USER_SEQUENCES WHERE SEQUENCE_NAME = '";
	public static String queryPro="select OBJECT_NAME ,  PROCEDURE_NAME ,  OBJECT_ID ,  SUBPROGRAM_ID ,  OVERLOAD , OBJECT_TYPE ,  IMPLTYPEOWNER ,  IMPLTYPENAME ,  AUTHID from USER_PROCEDURES WHERE OBJECT_NAME = '";
//	+proc+"'";

	public static String queryView="SELECT VIEW_NAME,TEXT,TYPE_TEXT_LENGTH,TYPE_TEXT,OID_TEXT_LENGTH,OID_TEXT,VIEW_TYPE_OWNER,SUPERVIEW_NAME  FROM USER_VIEWS WHERE VIEW_NAME = '";

	public ObservableList<String> getTables() {
		ObservableList<String> tables = FXCollections.observableArrayList();

		try {

			String query = "SELECT TABLE_NAME FROM USER_TABLES";
			Statement s = cnx.createStatement();
			ResultSet result = s.executeQuery(query);

			while (result.next()) {
				String tableName = result.getString("TABLE_NAME");
				if (tableName != null)
					tables.add(tableName);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tables;
	}

	public ObservableList<String> getAllUser() {

		ObservableList<String> tables = FXCollections.observableArrayList();

		try {

			String query = "SELECT USERNAME FROM ALL_USERS";
			Statement s = cnx.createStatement();
			ResultSet result = s.executeQuery(query);

			while (result.next()) {
				String tableName = result.getString("USERNAME");
				if (tableName != null)
					tables.add(tableName);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tables;
	}

	public ObservableList<String> getIndex() {

		ObservableList<String> tables = FXCollections.observableArrayList();

		try {

			String query = "SELECT INDEX_NAME FROM USER_INDEXES";
			Statement s = cnx.createStatement();
			ResultSet result = s.executeQuery(query);

			while (result.next()) {
				String tableName = result.getString("INDEX_NAME");
				if (tableName != null)
					tables.add(tableName);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tables;
	}

	public ObservableList<String> getSequence() {

		ObservableList<String> sequences = FXCollections.observableArrayList();

		try {

			String query = "SELECT SEQUENCE_NAME FROM USER_SEQUENCES";
			Statement s = cnx.createStatement();
			ResultSet result = s.executeQuery(query);

			while (result.next()) {
				String tableName = result.getString("SEQUENCE_NAME");
				if (tableName != null)
					sequences.add(tableName);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return sequences;
	}

	public ObservableList<String> getTrigger() {

		ObservableList<String> sequences = FXCollections.observableArrayList();

		try {

			String query = "SELECT TRIGGER_NAME FROM USER_TRIGGERS";
			Statement s = cnx.createStatement();
			ResultSet result = s.executeQuery(query);

			while (result.next()) {
				String tableName = result.getString("TRIGGER_NAME");
				if (tableName != null)
					sequences.add(tableName);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return sequences;
	}

	public ObservableList<String> getProcedures() {

		ObservableList<String> sequences = FXCollections.observableArrayList();

		try {

			String query = "select OBJECT_NAME from USER_PROCEDURES";
			// String query = "select OBJECT_NAME , PROCEDURE_NAME , OBJECT_ID ,
			// SUBPROGRAM_ID , OVERLOAD , OBJECT_TYPE , IMPLTYPEOWNER , IMPLTYPENAME , PAR
			// INT DET , AUTHID from USER_PROCEDURES";

			Statement s = cnx.createStatement();
			ResultSet result = s.executeQuery(query);

			while (result.next()) {
				String tableName = result.getString("OBJECT_NAME");
				if (tableName != null)
					sequences.add(tableName);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return sequences;
	}

	public ObservableList<String> getTableColumns(String tabName) {
		ObservableList<String> tableColumns = FXCollections.observableArrayList();

		try {

			String query = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME ='" + tabName + "'";
			Statement s = cnx.createStatement();
			ResultSet result = s.executeQuery(query);

			while (result.next()) {
				String columnName = result.getString("COLUMN_NAME");
				if (columnName != null)
					tableColumns.add(columnName);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tableColumns;
	}

	public ObservableList<String> getTableContraintes(String tabName) {
		ObservableList<String> tableContraintes = FXCollections.observableArrayList();

		try {

			String query = "SELECT CONSTRAINT_NAME FROM USER_CONSTRAINTS  WHERE TABLE_NAME = '" + tabName + "'";
			Statement s = cnx.createStatement();
			ResultSet result = s.executeQuery(query);

			while (result.next()) {
				String columnName = result.getString("CONSTRAINT_NAME");
				if (columnName != null)
					tableContraintes.add(columnName);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tableContraintes;
	}

	public ObservableList<String> getTableRef(String tabName) {
		ObservableList<String> tableRef = FXCollections.observableArrayList();

		try {

			String query = "select owner,constraint_name,constraint_type,table_name,r_owner,r_constraint_name"
					+ " from all_constraints where constraint_type='R' and r_constraint_name in"
					+ " (select constraint_name from all_constraints where constraint_type in ('P','U')"
					+ " and table_name = '" + tabName + "')";
			Statement s = cnx.createStatement();
			ResultSet result = s.executeQuery(query);

			while (result.next()) {
				String columnName = result.getString("constraint_name");
				if (columnName != null)
					tableRef.add(columnName);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tableRef;
	}

	public ObservableList<String> getViews() {

		ObservableList<String> views = FXCollections.observableArrayList();

		try {

			String query = "SELECT VIEW_NAME FROM USER_VIEWS";
			Statement s = cnx.createStatement();
			ResultSet result = s.executeQuery(query);

			while (result.next()) {
				String viewName = result.getString("VIEW_NAME");
				if (viewName != null)
					views.add(viewName);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		}

		return views;
	}

	public String executeQuery(String query) {

		String column, res = "";
		int i = 1;
		try {
			Statement s = cnx.createStatement();
			ResultSet result = s.executeQuery(query);
			while (result.next()) {
				i = 1;
				column = result.getString(i);
				while (true) {
					res += column + " | ";
					i++;
					try {
						column = result.getString(i);
					} catch (SQLException e) {
						break;
					}
				}
				res += "\n";
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
		return res;

	}

	public DataResult getAllData(String sql) throws SQLException {

		List<List<Object>> data = new ArrayList<>();
		List<String> columnNames = new ArrayList<>();

		try (Statement stmt = cnx.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			int columnCount = rs.getMetaData().getColumnCount();

			for (int i = 1; i <= columnCount; i++) {
				columnNames.add(rs.getMetaData().getColumnName(i));
			}

			while (rs.next()) {
				List<Object> row = new ArrayList<>();
				for (int i = 1; i <= columnCount; i++) {
					row.add(rs.getObject(i));
				}
				data.add(row);
			}
		}

		return new DataResult(columnNames, data);
	}

}
