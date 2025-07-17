package com.zaidan.testng.utils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties; // No longer explicitly loaded by DatabaseUtil, now by HelperClass

public class DatabaseUtil {
    private static Session sshSession = null;

    /**
     * Establishes an SSH Tunnel using details from the ConfigReader.
     */
    private static void startSshTunnel() throws Exception {
        if (sshSession != null && sshSession.isConnected()) return;

        JSch jsch = new JSch();
        // Get all SSH properties from our new ConfigReader
        sshSession = jsch.getSession(
            ConfigReader.getProperty("ssh.username"),
            ConfigReader.getProperty("ssh.host"),
            Integer.parseInt(ConfigReader.getProperty("ssh.port"))
        );
        sshSession.setPassword(ConfigReader.getProperty("ssh.password"));

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        sshSession.setConfig(config);

        System.out.println("Connecting to SSH Host " + ConfigReader.getProperty("ssh.host") + "...");
        sshSession.connect();
        System.out.println("SSH Connection successful.");

        int assignedPort = sshSession.setPortForwardingL(
            Integer.parseInt(ConfigReader.getProperty("ssh.local_port")),
            ConfigReader.getProperty("ssh.remote_host"),
            Integer.parseInt(ConfigReader.getProperty("ssh.remote_port"))
        );
        System.out.println("SSH Tunnel started on port: " + assignedPort);
    }

    /**
     * Gets a NEW database connection. It no longer reuses a single static connection.
     */
    public static Connection getConnection() throws SQLException {
        try {
            startSshTunnel();
        } catch (Exception e) {
            throw new SQLException("Failed to start SSH Tunnel!", e);
        }

        try {
            // Get all DB properties from our new ConfigReader
            String url = ConfigReader.getProperty("db.url");
            String user = ConfigReader.getProperty("db.username");
            String password = ConfigReader.getProperty("db.password");
            
            Class.forName("org.postgresql.Driver");
            // Always return a new connection
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found!", e);
        }
    }

    /**
     * Closes only the SSH Tunnel. The DB connection is now managed by try-with-resources in the DAOs.
     */
    public static void closeSshTunnel() {
        if (sshSession != null && sshSession.isConnected()) {
            sshSession.disconnect();
            sshSession = null;
            System.out.println("SSH Tunnel closed.");
        }
    }

    /**
     * Method utilitas untuk menutup ResultSet dan Statement.
     * Tidak ada perubahan di sini.
     */
    public static void closeResources(ResultSet rs, Statement stmt) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("Error menutup ResultSet: " + e.getMessage());
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Error menutup Statement: " + e.getMessage());
        }
    }
}