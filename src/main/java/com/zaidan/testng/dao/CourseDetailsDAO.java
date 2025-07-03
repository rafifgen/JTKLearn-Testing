package com.zaidan.testng.dao;

import com.zaidan.testng.model.CourseDetails;
import com.zaidan.testng.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseDetailsDAO {

    public CourseDetails getCourseDetailsForStudent(String studentEmail, String courseName) {
        CourseDetails courseDetails = null;

        String sql = "SELECT " +
                "    c.nama_course, " +
                "    c.gambar_course, " +
                "    p.nama AS instructor_name, " +
                "    cp.persentase_course " +
                "FROM " +
                "    \"courseParticipant\" cp " +
                "JOIN " +
                "    users s ON cp.id_pelajar = s.id_user " +
                "JOIN " +
                "    course c ON cp.id_course = c.id_course " +
                "JOIN " +
                "    pengajar p ON c.id_pengajar = p.kode_dosen " +
                "WHERE " +
                "    s.email = ? AND c.nama_course = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentEmail);
            pstmt.setString(2, courseName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String dbCourseName = rs.getString("nama_course");
                    String dbInstructorName = rs.getString("instructor_name");
                    String dbCourseImage = rs.getString("gambar_course");
                    double dbProgress = rs.getDouble("persentase_course");

                    courseDetails = new CourseDetails(dbCourseName, dbInstructorName, dbCourseImage, dbProgress);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseDetails;
    }
}