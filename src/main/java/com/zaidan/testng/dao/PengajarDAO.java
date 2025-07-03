package com.zaidan.testng.dao;

import com.zaidan.testng.model.Pengajar; // Import Pengajar model (if you created it)
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

        System.out.println("PengajarDAO: Querying DB for pengajar name with ID: " + idPengajar + ". SQL: " + sql);

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
