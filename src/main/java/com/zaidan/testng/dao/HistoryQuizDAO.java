package com.zaidan.testng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zaidan.testng.enums.TaskStatus;
import com.zaidan.testng.model.HistoryQuiz;
import com.zaidan.testng.utils.DatabaseUtil;

public class HistoryQuizDAO {
    public boolean isQuizScoreBelowThreshold(int idPelajar, int idQuiz) throws SQLException {
        // Assume the condition is met unless we find a record with a score of 80 or higher.
        boolean isBelowThreshold = true;
        
        String sql = "SELECT nilai FROM \"historyQuiz\" WHERE id_pelajar = ? AND id_quiz = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setInt(1, idPelajar);
            pStatement.setInt(2, idQuiz);

            try (ResultSet rs = pStatement.executeQuery()) {
                // Check if a row was found
                if (rs.next()) {
                    // A record exists, so now we check the score.
                    int score = rs.getInt("nilai");
                    System.out.println("DAO: Found existing quiz record with score: " + score);

                    // If the score is 80 or greater, the condition is NOT met.
                    if (score >= 80) {
                        isBelowThreshold = false;
                    }
                } else {
                    // No record exists, so the condition is met.
                    // 'isBelowThreshold' remains true.
                    System.out.println("DAO: No quiz record found for this student/quiz.");
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error checking quiz score: " + e.getMessage());
            throw e;
        }

        return isBelowThreshold;
    }

    public TaskStatus getDBQuizStatus(int studentId, int quizId, int passingScore) throws SQLException {
        // First, try to get the history record for this quiz
        HistoryQuiz history = getHistoryQuiz(studentId, quizId);

        // Rule 1: Not Taken (no record exists)
        if (history == null) {
            System.out.println("DAO: No history found for quiz " + quizId + ". Status: NOT_TAKEN");
            return TaskStatus.NOT_TAKEN;
        }

        // Rule 2: Check if the score meets the passing threshold
        if (history.getNilai() >= passingScore) {
            System.out.println("DAO: Score for quiz " + quizId + " is " + history.getNilai() + ". Status: FINISHED");
            return TaskStatus.FINISHED;
        } else {
            System.out.println("DAO: Score for quiz " + quizId + " is " + history.getNilai() + ". Status: NOT_TAKEN");
            return TaskStatus.NOT_TAKEN;
        }
    }

    private HistoryQuiz getHistoryQuiz(int studentId, int quizId) throws SQLException {
        // Use quotes to preserve case for table and column names
        String sql = "SELECT * FROM \"historyQuiz\" WHERE \"id_pelajar\" = ? AND \"id_quiz\" = ?";
        HistoryQuiz historyQuiz = null;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, quizId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    historyQuiz = new HistoryQuiz(
                        rs.getInt("id_history_quiz"),
                        rs.getInt("id_pelajar"),
                        rs.getInt("id_quiz"),
                        rs.getTimestamp("waktu_mulai"),
                        rs.getTimestamp("waktu_selesai"),
                        rs.getFloat("nilai")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error fetching historyQuiz record: " + e.getMessage());
            throw e;
        }
        return historyQuiz;
    }

    public void setQuizFinished(int studentId, int quizId, float score) {
        String deleteSql = "DELETE FROM \"historyQuiz\" WHERE \"id_pelajar\" = ? AND \"id_quiz\" = ?";
        String insertSql = "INSERT INTO \"historyQuiz\" (id_pelajar, id_quiz, nilai, waktu_mulai, waktu_selesai, \"createdAt\", \"updatedAt\") " +
        "VALUES (?, ?, ?, NOW(), NOW() + INTERVAL '15 seconds', NOW(), NOW())";

        // A single try-with-resources block for the connection is efficient
        try (Connection conn = DatabaseUtil.getConnection()) {
            
            // Step 1: Delete any old record to ensure a clean slate.
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, studentId);
                deleteStmt.setInt(2, quizId);
                deleteStmt.executeUpdate();
            }

            // Step 2: Insert the new, finished record with the desired score.
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, studentId);
                insertStmt.setInt(2, quizId);
                insertStmt.setFloat(3, score);
                insertStmt.executeUpdate();
            }
            
            System.out.println("DAO: Set quiz " + quizId + " to FINISHED for student " + studentId + " with score " + score);

        } catch (SQLException e) {
            System.err.println("DAO: Error in delete-then-insert for quiz history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void resetQuizProgressByStudentAndQuizId(int idPelajar, int idKuis) throws SQLException {
        String deleteSQL = "DELETE FROM \"historyQuiz\" WHERE \"id_pelajar\" = ? AND \"id_quiz\" = ?";
        
        // This 'try-with-resources' block automatically closes the connection and statement.
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement pStatement = conn.prepareStatement(deleteSQL)) {
            
            // Set the parameters for the WHERE clause
            pStatement.setInt(1, idPelajar);
            pStatement.setInt(2, idKuis);
            
            // Execute the DELETE command
            pStatement.executeUpdate();
            
            System.out.println("DAO: Successfully deleted history for student " + idPelajar + " and quiz " + idKuis);

        } catch (SQLException e) {
            System.err.println("DAO: Error deleting history record: " + e.getMessage());
            // Re-throw the exception so the calling method knows something went wrong.
            throw e;
        }
    }
}