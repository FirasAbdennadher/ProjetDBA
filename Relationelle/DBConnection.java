package Relationelle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @description Connects to the DB.
 *
 * @author MindFusion LLC
 * @version 1.0 $Date: 11/09/2016
 */

public class DBConnection {

    private static String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static String DB_USER = "hr";
    private static String DB_PASSWORD = "hr";

    /**
     * Establishes a connection to the database.
     *
     * @return Connection to the database.
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER,
                DB_PASSWORD);
        System.err.println("The connection is successfully obtained");
        return connection;
    }
}
