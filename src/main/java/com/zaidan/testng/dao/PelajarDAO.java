package com.zaidan.testng.dao;

import com.zaidan.testng.model.Pelajar;
import com.zaidan.testng.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PelajarDAO {
    public int getIdByName(String studentName) throws SQLException {
        int studentId = -1; // Default value if not found
        String sql = "SELECT id_pelajar FROM pelajar WHERE nama = ?";

        // try-with-resources ensures the connection and statement are closed automatically
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentName);

            try (ResultSet rs = pstmt.executeQuery()) {
                // If a record is found, get the ID
                if (rs.next()) {
                    studentId = rs.getInt("id_pelajar");
                } else {
                    System.out.println("DAO WARNING: No student found with the name: " + studentName);
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error fetching student ID by name: " + e.getMessage());
            throw e; // Re-throw exception to let the test fail
        }

        return studentId;
    }

    public Pelajar getPelajarByNama(String namaPelajar) {
        String sql = """
            SELECT id_pelajar, nama
            FROM pelajar
            WHERE nama = ?
            LIMIT 1
            """;

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, namaPelajar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idPelajar = rs.getInt("id_pelajar");
                    String nama = rs.getString("nama");
                    return new Pelajar(idPelajar, nama);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error fetching Pelajar for nama = " + namaPelajar, e);
        }
        throw new IllegalStateException(
                "No Pelajar found with nama = " + namaPelajar);
    }
}