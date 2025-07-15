package com.zaidan.testng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zaidan.testng.utils.DatabaseUtil;

public class QuizDAO {
    public int getIdByName(String quizName) throws SQLException {
        int quizId = -1; // Default value if not found
        // This query assumes your quiz table is named 'quiz' and the name column is 'nama_quiz'
        String sql = "SELECT id_quiz FROM quiz WHERE nama_quiz = ?";

        // try-with-resources automatically closes the connection and statement
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, quizName);

            try (ResultSet rs = pstmt.executeQuery()) {
                // If a record is found, get the ID from the result
                if (rs.next()) {
                    quizId = rs.getInt("id_quiz");
                } else {
                    System.out.println("DAO WARNING: No quiz found with the name: " + quizName);
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error fetching quiz ID by name: " + e.getMessage());
            throw e; // Re-throw the exception to let the test fail
        }
        return quizId;
    }
}
