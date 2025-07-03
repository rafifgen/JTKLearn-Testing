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

    private static Connection jdbcConnection = null; // Renamed to clarify it's the JDBC connection
    private static Session sshSession = null;       // JSch SSH session
    private static int assignedLocalPort = -1;      // Store the actual local port used for forwarding

    // Method to initialize/get the database connection (including SSH tunnel)
    public static Connection getConnection() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                // 1. Establish SSH Tunnel if not already active
                if (sshSession == null || !sshSession.isConnected()) {
                    establishSshTunnel();
                }

                // 2. Establish JDBC Connection to the local forwarded port
                Class.forName("org.postgresql.Driver");
                // Use the assignedLocalPort for JDBC connection
                String dbUrl = String.format("jdbc:postgresql://localhost:%d/%s",
                        assignedLocalPort,
                        HelperClass.getProperty("db.url").split("/")[3]); // Extract database name part
                String dbUser = HelperClass.getProperty("db.username");
                String dbPass = HelperClass.getProperty("db.password");

                System.out.println("JDBC: Connecting to " + dbUrl + " with user " + dbUser);
                jdbcConnection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
                System.out.println("Database connection established.");
            } catch (Exception e) { // Catch JSchException as well
                System.err.println("Failed to establish database connection (or SSH tunnel): " + e.getMessage());
                e.printStackTrace();
                closeAllConnections(); // Attempt to clean up on failure
                throw new SQLException("Failed to establish database connection through SSH tunnel.", e);
            }
        }
        return jdbcConnection;
    }

    private static void establishSshTunnel() throws Exception {
        JSch jsch = new JSch();

        String sshHost = HelperClass.getProperty("ssh.host");
        System.out.println(sshHost);
        int sshPort = Integer.parseInt(HelperClass.getProperty("ssh.port"));
        System.out.println(HelperClass.getProperty("ssh.port"));
        String sshUser = HelperClass.getProperty("ssh.username");
        String sshPass = HelperClass.getProperty("ssh.password");

        sshSession = jsch.getSession(sshUser, sshHost, sshPort);
        sshSession.setPassword(sshPass); // Only if using password authentication

        // It is recommended to use an explicit host key repository.
        // For testing, you might use strictHostKeyChecking=no, but be aware of security implications.
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no"); // WARNING: Not recommended for production!
        sshSession.setConfig(config);

        System.out.println("SSH: Connecting to " + sshUser + "@" + sshHost + ":" + sshPort);
        sshSession.connect(); // SSH connection established

        int localPort = Integer.parseInt(HelperClass.getProperty("ssh.local_port"));
        String remoteHost = HelperClass.getProperty("ssh.remote_host");
        int remotePort = Integer.parseInt(HelperClass.getProperty("ssh.remote_port"));

        // Set up local port forwarding
        // L(local port to listen on):remote_host:remote_port
        assignedLocalPort = sshSession.setPortForwardingL(localPort, remoteHost, remotePort);
        System.out.println("SSH Tunnel established: localhost:" + assignedLocalPort + " -> " + remoteHost + ":" + remotePort);
    }

    // Method to explicitly close all connections (JDBC and SSH)
    public static void closeAllConnections() {
        // Close JDBC connection first
        if (jdbcConnection != null) {
            try {
                jdbcConnection.close();
                jdbcConnection = null;
                System.out.println("JDBC connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing JDBC connection: " + e.getMessage());
            }
        }
        // Then close SSH session
        if (sshSession != null) {
            sshSession.disconnect();
            sshSession = null;
            assignedLocalPort = -1; // Reset port
            System.out.println("SSH Tunnel disconnected.");
        }
    }

    // This method is now only for closing Statement and ResultSet
    public static void closeResources(ResultSet rs, Statement stmt) {
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
