package com.zaidan.testng.dao;

import com.zaidan.testng.model.Course;
import com.zaidan.testng.utils.DatabaseUtil;
import org.checkerframework.checker.units.qual.C;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        Connection connection; // Local variable to hold the shared connection
        Statement statement = null;
        ResultSet resultSet = null;

        String sql =
                "SELECT\n" +
                        "\tid_course,\n" +
                        "\tid_pengajar,\n" +
                        "\tnama_course,\n" +
                        "\tenrollment_key,\n" +
                        "\tgambar_course,\n" +
                        "\tdeskripsi\n" +
                        "FROM course";
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

                Course course = new Course(
                        idCourse,
                        idPengajar,
                        namaCourse,
                        enrollmentKey,
                        gambarCourse,
                        deskripsi,
                        null);
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

    public List<Course> getAllJoinedCoursesByEmail(String email) throws SQLException {
        List<Course> courses = new ArrayList<Course>();
        Connection connection;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query =
                "SELECT\n" +
                "    c.id_course,\n" +
                "    c.id_pengajar,\n" +
                "    c.nama_course,\n" +
                "    c.gambar_course,\n" +
                "    c.enrollment_key,\n" +
                "    c.deskripsi\n" +
                "FROM\n" +
                "    course AS c\n" +
                "JOIN\n" +
                "    \"courseParticipant\" AS cp ON c.id_course = cp.id_course\n" +
                "JOIN\n" +
                "    pelajar AS p ON cp.id_pelajar = p.id_pelajar\n" +
                "JOIN\n" +
                "    users AS u ON p.id_user = u.id_user\n" +
                "WHERE\n" +
                "    u.email = ?";

        try {
           connection = DatabaseUtil.getConnection();
           preparedStatement = connection.prepareStatement(query);
           preparedStatement.setString(1, email);
           resultSet = preparedStatement.executeQuery();

           while(resultSet.next()) {
              int idCourse = resultSet.getInt("id_course");
              int idPengajar = resultSet.getInt("id_pengajar");
              String namaCourse = resultSet.getString("nama_course");
              String deskripsi = resultSet.getString("deskripsi");
              String namaGambar = resultSet.getString("gambar_course");
              String enrollmentKey = resultSet.getString("enrollment_key");

              Course course = new Course(
                      idCourse,
                      idPengajar,
                      namaCourse,
                      enrollmentKey,
                      namaGambar,
                      deskripsi,
                      null);
              courses.add(course);
           }
        } catch (SQLException e) {
            System.err.println("Error retrieving all courses: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeResources(resultSet, preparedStatement);
        }
        return courses;
    }
}