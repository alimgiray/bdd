package com.alimgiray.bdd;

import com.alimgiray.bdd.core.database.Database;
import com.alimgiray.bdd.core.database.Query;

import java.sql.SQLException;

public class Application {

    public static void main(String[] args) {
        setupDatabase();
        String s = new Query().select("NAME").from("PARAMETER").where("ID").is("41").getResult();
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