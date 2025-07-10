package com.zaidan.testng.dao;

import com.zaidan.testng.model.QuizAttemptDetails;
import com.zaidan.testng.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizHistoryDAO {

    public List<QuizAttemptDetails> getQuizHistoryForStudent(String studentEmail, String courseName) {
        List<QuizAttemptDetails> historyList = new ArrayList<>();
        String sql = "WITH Aggregates AS ( " +
                "    SELECT id_quiz, id_pelajar, MAX(nilai) AS highest_score, COUNT(id_quiz) AS total_attempts " +
                "    FROM \"historyQuiz\" " +
                "    GROUP BY id_quiz, id_pelajar " +
                "), " +
                "RankedAttempts AS ( " +
                "    SELECT id_quiz, id_pelajar, waktu_mulai, waktu_selesai, " +
                "           ROW_NUMBER() OVER(PARTITION BY id_quiz, id_pelajar ORDER BY nilai DESC, waktu_selesai DESC) as rn " +
                "    FROM \"historyQuiz\" " +
                ") " +
                "SELECT " +
                "    q.nama_quiz, " +
                "    q.deskripsi_quiz, " +
                "    agg.highest_score, " +
                "    agg.total_attempts, " +
                "    TO_CHAR(ra.waktu_mulai, 'DD-MM-YYYY HH24:MI:SS') as start_time, " +
                "    TO_CHAR(ra.waktu_selesai, 'DD-MM-YYYY HH24:MI:SS') as end_time " +
                "FROM quiz q " +
                "JOIN Aggregates agg ON q.id_quiz = agg.id_quiz " +
                "JOIN RankedAttempts ra ON q.id_quiz = ra.id_quiz AND agg.id_pelajar = ra.id_pelajar " +
                "WHERE ra.rn = 1 " +
                "  AND agg.id_pelajar = (SELECT p.id_pelajar FROM pelajar p JOIN users u ON p.id_user = u.id_user WHERE u.email = ?) " +
                "  AND q.id_course = (SELECT c.id_course FROM course c WHERE c.nama_course = ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentEmail);
            pstmt.setString(2, courseName);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String quizName = rs.getString("nama_quiz");
                    String quizDescription = rs.getString("deskripsi_quiz");
                    double highestScore = rs.getDouble("highest_score");
                    int totalAttempts = rs.getInt("total_attempts");
                    String startTime = rs.getString("start_time");
                    String endTime = rs.getString("end_time");

                    historyList.add(new QuizAttemptDetails(quizName, quizDescription, highestScore, totalAttempts, startTime, endTime));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    public List<QuizAttemptDetails> getQuizHistoryForStudent(String studentEmail, String courseName, String searchTerm) {
        List<QuizAttemptDetails> historyList = new ArrayList<>();
        // Query ini sama dengan sebelumnya, namun dengan tambahan kondisi AND q.nama_quiz ILIKE ?
        String sql = "WITH Aggregates AS ( " +
                "    SELECT id_quiz, id_pelajar, MAX(nilai) AS highest_score, COUNT(id_quiz) AS total_attempts " +
                "    FROM \"historyQuiz\" " +
                "    GROUP BY id_quiz, id_pelajar " +
                "), " +
                "RankedAttempts AS ( " +
                "    SELECT id_quiz, id_pelajar, waktu_mulai, waktu_selesai, " +
                "           ROW_NUMBER() OVER(PARTITION BY id_quiz, id_pelajar ORDER BY nilai DESC, waktu_selesai DESC) as rn " +
                "    FROM \"historyQuiz\" " +
                ") " +
                "SELECT " +
                "    q.nama_quiz, q.deskripsi_quiz, agg.highest_score, agg.total_attempts, " +
                "    TO_CHAR(ra.waktu_mulai, 'DD-MM-YYYY HH24:MI:SS') as start_time, " +
                "    TO_CHAR(ra.waktu_selesai, 'DD-MM-YYYY HH24:MI:SS') as end_time " +
                "FROM quiz q " +
                "JOIN Aggregates agg ON q.id_quiz = agg.id_quiz " +
                "JOIN RankedAttempts ra ON q.id_quiz = ra.id_quiz AND agg.id_pelajar = ra.id_pelajar " +
                "WHERE ra.rn = 1 " +
                "  AND agg.id_pelajar = (SELECT p.id_pelajar FROM pelajar p JOIN users u ON p.id_user = u.id_user WHERE u.email = ?) " +
                "  AND q.id_course = (SELECT c.id_course FROM course c WHERE c.nama_course = ?) " +
                "  AND q.nama_quiz ILIKE ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentEmail);
            pstmt.setString(2, courseName);
            pstmt.setString(3, "%" + searchTerm + "%"); // Menggunakan wildcard % untuk mencari sebagian teks

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String quizName = rs.getString("nama_quiz");
                    String quizDescription = rs.getString("deskripsi_quiz");
                    double highestScore = rs.getDouble("highest_score");
                    int totalAttempts = rs.getInt("total_attempts");
                    String startTime = rs.getString("start_time");
                    String endTime = rs.getString("end_time");

                    historyList.add(new QuizAttemptDetails(quizName, quizDescription, highestScore, totalAttempts, startTime, endTime));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyList;
    }
}