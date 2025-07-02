package com.zaidan.testng.dao;

import com.zaidan.testng.model.Course;
import com.zaidan.testng.utils.DatabaseUtil;

import java.sql.Connection; // Still needed for the type hint
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        Connection connection = null; // Local variable to hold the shared connection
        Statement statement = null;
        ResultSet resultSet = null;

        String sql = "SELECT id_course, id_pengajar, nama_course, enrollment_key, gambar_course, deskripsi FROM course";

        try {
            connection = DatabaseUtil.getConnection(); // Get the shared connection
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int idCourse = resultSet.getInt("id_course");
                int idPengajar = resultSet.getInt("id_pengajar");
                String namaCourse = resultSet.getString("nama_course");
                String enrollmentKey = resultSet.getString("enrollment_key");
                String gambarCourse = resultSet.getString("gambar_course");
                String deskripsi = resultSet.getString("deskripsi");

                Course course = new Course(idCourse, idPengajar, namaCourse, enrollmentKey, gambarCourse, deskripsi);
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all courses: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Only close ResultSet and Statement here. The main connection remains open.
            DatabaseUtil.closeResources(resultSet, statement);
        }
        return courses;
    }
}
