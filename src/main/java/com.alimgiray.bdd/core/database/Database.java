package com.alimgiray.bdd.core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    private static Connection connection;
    private static final String URL = System.getenv("databaseUrl");
    private static final String USERNAME = System.getenv("databaseUsername");
    private static final String PASSWORD = System.getenv("databasePassword");

    public static void setupConnection() throws SQLException, ClassNotFoundException  {
        Class.forName(JdbcDrivers.ORACLE.getClassName());
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static ResultSet selectAllFromTable(String tableName) {
        try {
            return connection.createStatement().executeQuery("SELECT * FROM " + tableName);
        } catch (SQLException e) {
            return null;
        }
    }

}
