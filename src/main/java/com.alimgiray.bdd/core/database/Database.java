package com.alimgiray.bdd.core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    private static Connection connection;

    public static void setupConnection(String url, String username, String password)
            throws SQLException, ClassNotFoundException  {
        Class.forName(JdbcDrivers.ORACLE.getClassName());
        connection = DriverManager.getConnection(url, username, password);
    }

    public static String selectQuery(String sql, String columnName) {
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getObject(columnName).toString();
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
    }
}
