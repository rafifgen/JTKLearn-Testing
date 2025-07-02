package com.zaidan.testng.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseUtil {

    private static Properties props = new Properties();
    private static Connection connection = null; // Declare a static connection

    static {
        try (InputStream input = HelperClass.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
            }
            props.load(input);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Method to initialize/get the database connection
    // This method will now ensure only one connection is active.
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver"); // Load PostgreSQL driver
                connection = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.username"),
                    props.getProperty("db.password")
                );
                System.out.println("Database connection opened.");
            } catch (ClassNotFoundException e) {
                throw new SQLException("PostgreSQL JDBC Driver not found!", e);
            }
        }
        return connection;
    }

    // Method to explicitly close the main database connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; // Set to null after closing
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    // This method is now only for closing Statement and ResultSet,
    // as the main connection is managed globally by getConnection/closeConnection.
    public static void closeResources(ResultSet rs, Statement stmt) { // Removed Connection conn from params
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("Error closing ResultSet: " + e.getMessage());
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Error closing Statement: " + e.getMessage());
        }
    }
}
