package com.zaidan.testng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zaidan.testng.model.DetailHistoryQuiz;
import com.zaidan.testng.utils.DatabaseUtil;

public class DetailHistoryQuizDAO {
    public List<DetailHistoryQuiz> GetAllQuestionsByIdHistoryQuiz(int idHistoryQuiz) {
        List<DetailHistoryQuiz> result = new ArrayList<>();
        String sql = "SELECT * FROM \"detailHistoryQuiz\" WHERE id_history_quiz = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idHistoryQuiz);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    boolean status = rs.getString("status").equalsIgnoreCase("benar");
                    DetailHistoryQuiz detail = new DetailHistoryQuiz(
                        rs.getInt("id_detail_history_quiz"),
                        rs.getInt("id_history_quiz"),
                        rs.getInt("id_pertanyaan"),
                        rs.getInt("id_jawaban"),
                        rs.getString("jawaban_text"),
                        status
                    );
                    result.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<DetailHistoryQuiz> GetCorrectAnswersByIdHistoryQuiz(int idHistoryQuiz) {
        List<DetailHistoryQuiz> result = new ArrayList<>();
        String sql = "SELECT * FROM \"detailHistoryQuiz\" WHERE id_history_quiz = ? AND status = 'benar'";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idHistoryQuiz);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    boolean status = rs.getString("status").equalsIgnoreCase("benar");
                    DetailHistoryQuiz detail = new DetailHistoryQuiz(
                        rs.getInt("id_detail_history_quiz"),
                        rs.getInt("id_history_quiz"),
                        rs.getInt("id_pertanyaan"),
                        rs.getInt("id_jawaban"),
                        rs.getString("jawaban_text"),
                        status
                    );
                    result.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
