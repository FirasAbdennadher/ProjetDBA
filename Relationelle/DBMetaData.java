package Relationelle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import application.singleton.Sconnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

/**
 * @description Provides methods for extracting data from the DB.
 *
 * @author MindFusion LLC
 * @version 1.0 $Date: 11/09/2016
 */
public class DBMetaData {

    //static variables
   /* static Connection connection = null;*/
    static DatabaseMetaData metadata = null;

    static // Static block for initialization
	Connection cnx;
    static {
        Preferences userPreferences = Preferences.userRoot();
        String log = userPreferences.get("login","");
        String password = userPreferences.get("password","");

     
        cnx = Sconnection.getConnection(log,password);
        //System.out.println("OOOOOOOOOOOOOO"+info);
        try {
            metadata = cnx.getMetaData();
        } catch (SQLException e) {
            System.err.println("There was an error getting the metadata: "
                    + e.getMessage());
        }
    }


    /**
     *
     * @return Arraylist with the names of the DB tables
     * * @throws SQLException
     */
    public static ArrayList<String> getTablesMetadata() throws SQLException {
    	Statement stmt = cnx.createStatement();
		ResultSet rs = null;
		ArrayList<String> tables = new ArrayList<String>();
		rs = stmt.executeQuery("SELECT TABLE_NAME FROM USER_TABLES");
		while (rs.next()) {
			tables.add(rs.getString(1));
		}
		return tables;
    }


    /**
     * Gets a list with all relationships in the DB.
     * @param tableNames A list with the tables in the DB.
     * @return Arraylist
     * * @throws SQLException
     */
    public static ArrayList<DBRelation> getRelationsMetadata(ArrayList<String> tableNames)
            throws SQLException {

        ArrayList<DBRelation> result = new ArrayList<DBRelation>();

        for (String tableName : tableNames) {
            //get the foreign keys
            ResultSet foreignKeys = metadata.getImportedKeys(cnx.getCatalog(), null, tableName);
            while (foreignKeys.next()) {
                String fkTableName = foreignKeys.getString("FKTABLE_NAME");
                String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
                String pkTableName = foreignKeys.getString("PKTABLE_NAME");
                String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");

                DBRelation relation = new DBRelation(pkColumnName, pkTableName, fkColumnName, fkTableName);

                result.add(relation);
            }
        }

        return result;

    }

    /**
     * Returns a list with DBColumn objects, one for each column
     * in the table provided as a parameter.
     *
     * @param tableName
     * @return List with the columns in the table.
     * @throws SQLException
     */
    public static ArrayList<DBColumn> getColumnsMetadata(String tableName,String log,String pw)
            throws SQLException {
        ResultSet rs = null;

        rs = metadata.getPrimaryKeys(null, null, tableName);

        //get primary key
        ArrayList<String> primaryKeys = new ArrayList<String>();
        while (rs.next()) {
            primaryKeys.add(rs.getString("COLUMN_NAME"));
        }

        // gets the columns
        ArrayList<DBColumn> result = new ArrayList<DBColumn>();
        rs = metadata.getColumns(null, null, tableName, null);

        //records the data for each column
        while (rs.next()) {
            DBColumn column = new DBColumn(rs.getString("COLUMN_NAME"), rs.getString("TYPE_NAME"),
                    rs.getString("COLUMN_SIZE"));

            //checks if this column is the primary key
            for(String primaryKey : primaryKeys) {
                if (primaryKey.equals(column.name))
                    column.primaryKey = true;
            }

            result.add(column);
        }

        return result;
    }

}
