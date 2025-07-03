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

    private static Properties props = new Properties();
    private static Connection connection = null;
    private static Session sshSession = null;

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

    /**
     * Memulai SSH Tunnel.
     * Method ini akan dipanggil secara otomatis oleh getConnection().
     */
    private static void startSshTunnel() throws Exception {
        if (sshSession != null && sshSession.isConnected()) {
            return; // Tunnel sudah aktif
        }

        JSch jsch = new JSch();
        sshSession = jsch.getSession(
                props.getProperty("ssh.username"),
                props.getProperty("ssh.host"),
                Integer.parseInt(props.getProperty("ssh.port"))
        );
        sshSession.setPassword(props.getProperty("ssh.password"));

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        sshSession.setConfig(config);

        System.out.println("Menghubungkan ke SSH Host " + props.getProperty("ssh.host") + "...");
        sshSession.connect(); // Mulai koneksi SSH
        System.out.println("Koneksi SSH berhasil dibuat.");

        // Atur Port Forwarding
        // Lport, Rhost, Rport
        int assignedPort = sshSession.setPortForwardingL(
                Integer.parseInt(props.getProperty("ssh.local_port")),
                props.getProperty("ssh.remote_host"),
                Integer.parseInt(props.getProperty("ssh.remote_port"))
        );
        System.out.println("SSH Tunnel dimulai: localhost:" + assignedPort + " -> " + props.getProperty("ssh.remote_host") + ":" + props.getProperty("ssh.remote_port"));
    }

    /**
     * Mendapatkan koneksi database, memastikan SSH Tunnel sudah aktif sebelumnya.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Pastikan SSH Tunnel sudah berjalan sebelum membuat koneksi database
            startSshTunnel();
        } catch (Exception e) {
            throw new SQLException("Gagal memulai SSH Tunnel!", e);
        }

        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(
                        props.getProperty("db.url"), // URL ini sudah benar menunjuk ke localhost
                        props.getProperty("db.username"),
                        props.getProperty("db.password")
                );
                System.out.println("Koneksi database melalui tunnel berhasil dibuka.");
            } catch (ClassNotFoundException e) {
                throw new SQLException("PostgreSQL JDBC Driver tidak ditemukan!", e);
            }
        }
        return connection;
    }

    /**
     * Menutup koneksi database dan juga koneksi SSH Tunnel.
     */
    public static void closeConnection() {
        // Tutup koneksi database terlebih dahulu
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Koneksi database ditutup.");
            } catch (SQLException e) {
                System.err.println("Error menutup koneksi database: " + e.getMessage());
            }
        }

        // Tutup sesi SSH
        if (sshSession != null && sshSession.isConnected()) {
            sshSession.disconnect();
            sshSession = null;
            System.out.println("SSH Tunnel ditutup.");
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