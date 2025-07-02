package com.zaidan.testng.dao;

import com.zaidan.testng.model.Course;
import com.zaidan.testng.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<Course>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        String sql = "SELECT id_course, id_pengajar, nama_course, enrollment_key, gambar_course, deskripsi FROM course";

        try {
            connection = DatabaseUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                // Correctly mapping SQL data types to Java types
                int idCourse = resultSet.getInt("id_course"); // serial4 -> int
                int idPenganjar = resultSet.getInt("id_penganjar"); // int4 -> int
                String namaCourse = resultSet.getString("nama_course"); // varchar(255) -> String
                String enrollmentKey = resultSet.getString("enrollment_key"); // varchar(255) -> String
                String gambarCourse = resultSet.getString("gambar_course"); // varchar(255) -> String
                String deskripsi = resultSet.getString("deskripsi"); // text -> String

                Course course = new Course(idCourse, idPenganjar, namaCourse, enrollmentKey, gambarCourse, deskripsi);
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all courses: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeResources(resultSet, statement, connection);
        }
        return courses;
    }
}
