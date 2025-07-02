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

    // Method to get a database connection
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver"); // Load PostgreSQL driver
            return DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.username"),
                    props.getProperty("db.password"));
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found!", e);
        }
    }

    // Method to close connection, statement, and resultSet
    public static void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            System.err.println("Error closing ResultSet: " + e.getMessage());
        }
        try {
            if (stmt != null)
                stmt.close();
        } catch (SQLException e) {
            System.err.println("Error closing Statement: " + e.getMessage());
        }
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing Connection: " + e.getMessage());
        }
    }

    // You can add more utility methods here for common operations like:
    // - executeQuery(String sql) returning ResultSet
    // - executeUpdate(String sql) returning int
    // - etc. (but DAOs will wrap this for specific entities)
}
