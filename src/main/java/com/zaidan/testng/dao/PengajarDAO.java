package com.zaidan.testng.dao;

import com.zaidan.testng.model.Pengajar;
import com.zaidan.testng.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PengajarDAO {

    private static final Map<Integer, String> pengajarNameCache = new HashMap<>();

    /**
     * Mengambil objek Pengajar berdasarkan id_pengajar dari tabel 'pengajar'
     *
     *
     * @param idPengajar ID dari pengajar
     * @return Objek Pengajar jika ditemukan, otherwise null
     */
    public Pengajar getPengajarById(int idPengajar) {
        String sql = "SELECT kode_dosen, nama, nip FROM pengajar WHERE kode_dosen = ?";
        Pengajar pengajar = null;

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idPengajar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("kode_dosen");
                    String nama = rs.getString("nama");
                    String nip = rs.getString("nip"); // Assuming you want to fetch NIP as well
                    pengajar = new Pengajar(id, nama, nip); // Create Pengajar object
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving Pengajar for id " + idPengajar + ": " + e.getMessage());
            e.printStackTrace();
        }

        return pengajar;
    }



    /**
     * Retrieves the 'nama' (name) of the dosen based on their 'kode_dosen'.
     * This method will first check a cache. If not found, it queries the 'dosen' table directly.
     *
     * @param idPengajar The ID of the pengajar (which is kode_dosen in the DB).
     * @return The nama_pengajar as a String, or null if not found.
     */
    public String getPengajarNameById(int idPengajar) {
        // 1. Check cache first
        if (pengajarNameCache.containsKey(idPengajar)) {
            System.out.println("PengajarDAO: Found pengajar name in cache for ID: " + idPengajar);
            return pengajarNameCache.get(idPengajar);
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String pengajarName = null;

        // --- UPDATED SQL QUERY ---
        // Select 'nama' directly from the 'dosen' table using 'kode_dosen'
        String sql = "SELECT nama FROM pengajar WHERE kode_dosen = ?";

        try {
            connection = DatabaseUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idPengajar); // Set kode_dosen parameter
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                pengajarName = resultSet.getString("nama"); // Get the 'nama' from the 'dosen' table
                pengajarNameCache.put(idPengajar, pengajarName); // Cache the result
                System.out.println("PengajarDAO: Found student name: " + pengajarName + " for ID: " + idPengajar + " (and cached)");
            } else {
                System.out.println("PengajarDAO: No pengajar name found for ID: " + idPengajar);
            }
        } catch (SQLException e) {
            System.err.println("PengajarDAO: SQL Error retrieving pengajar name by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeResources(resultSet, preparedStatement);
        }
        return pengajarName;
    }

    // TODO: Soon?
    // You can add other methods to fetch Dosen details if needed, e.g., by NIP or all dosen.
    // public Pengajar getDosenByNip(String nip) { ... }
}
