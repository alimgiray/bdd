package com.alimgiray.bdd;

import com.alimgiray.bdd.core.database.Database;

import java.sql.SQLException;

public class DbTest {

    public static void main(String[] args) {
        setupDatabase();
        String s = Database.selectFieldFromTableWithWhere("PARAMETER", "NAME", "ID", "41");
        System.out.println(s);
    }

    private static void setupDatabase() {
        try {
            Database.setupConnection();
        } catch (SQLException e) {
            System.err.println("Could not connect to the database. Reason: " + e);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver could not found.");
        }
    }
}