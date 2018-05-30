package com.alimgiray.bdd.example;

import com.alimgiray.bdd.core.database.Database;
import com.alimgiray.bdd.core.subject.Product;
import com.alimgiray.bdd.example.xporter.WsEndpoint;
import com.alimgiray.bdd.example.xporter.XPorter;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author aaytar
 * @since 30.05.2018 11:59
 */
public class Runner {

    private static Product product;
    private static Properties properties;

    public static void main(String[] args) {
        loadProperties();
        setupDatabase();
        setupProduct();

    }

    private static void loadProperties() {
        properties = new Properties();
        String filename = "config.properties";
        InputStream inputStream = Runner.class.getClassLoader().getResourceAsStream(filename);
        if (inputStream != null) {
            try {
                properties.load(inputStream);
                inputStream.close();
            } catch (IOException e) {
                System.err.println("Could not find properties file.");
            }
        }
    }

    private static void setupProduct() {
        product = new XPorter(properties.getProperty("product.name"));
        product.addEndpoint(new WsEndpoint(
                properties.getProperty("product.endpoint.name"), properties.getProperty("product.endpoint.url")));
    }

    private static void setupDatabase() {
        try {
            Database.setupConnection(
                    properties.getProperty("database.url"),
                    properties.getProperty("database.username"),
                    properties.getProperty("database.password"));
        } catch (SQLException e) {
            System.err.println("Could not connect to the database. Reason: " + e);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver could not found.");
        }
    }
}
