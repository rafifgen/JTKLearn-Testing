package com.zaidan.testng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zaidan.testng.utils.DatabaseUtil;

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
}
