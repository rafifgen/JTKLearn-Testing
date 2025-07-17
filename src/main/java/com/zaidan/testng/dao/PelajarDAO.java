package com.zaidan.testng.dao;

import com.zaidan.testng.model.Pelajar;
import com.zaidan.testng.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Pelajar> getEnrolledStudentsByCourseId(int courseId) throws SQLException {
        List<Pelajar> enrolledStudents = new ArrayList<>();
        // This query joins the pelajar and courseParticipant tables to find the students
        String sql = "SELECT p.* FROM pelajar p " +
                     "JOIN \"courseParticipant\" cp ON p.id_pelajar = cp.id_pelajar " +
                     "WHERE cp.id_course = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    enrolledStudents.add(new Pelajar(
                        rs.getInt("id_pelajar"),
                        rs.getInt("id_user"),
                        rs.getString("nama"),
                        rs.getString("nim")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error getting enrolled students: " + e.getMessage());
            throw e;
        }
        return enrolledStudents;
    }
}