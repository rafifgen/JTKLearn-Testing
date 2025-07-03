package com.zaidan.testng.dao;

import com.zaidan.testng.model.Course; // Keep if needed for other methods like getJoinedCourses
import com.zaidan.testng.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; // Keep if needed for other methods like getJoinedCourses
import java.util.List; // Keep if needed for other methods like getJoinedCourses

public class UserDAO {

    /**
     * Retrieves the 'nama' (name) of the 'pelajar' (student)
     * from the database based on the user's email.
     * This involves joining the 'users' and 'pelajar' tables.
     * 
     * @param email The email of the user (from the 'users' table).
     * @return The 'nama' (name) as a String, or null if not found.
     */
    public String getPelajarNameByEmail(String email) {
        Connection connection;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String pelajarName = null;

        // SQL query to join 'users' and 'pelajar' tables
        // Assuming 'users.id_user' is a foreign key in 'pelajar.id_user'
        String sql = "SELECT p.nama " +
                "FROM users u " +
                "JOIN pelajar p ON u.id_user = p.id_user " +
                "WHERE u.email = ?";

        try {
            connection = DatabaseUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                pelajarName = resultSet.getString("nama"); // Get the 'nama' from the 'pelajar' table
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving student name by email: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeResources(resultSet, preparedStatement);
        }
        return pelajarName;
    }

    /**
     * Placeholder for retrieving joined courses for a user.
     * This will require joining 'users', 'pelajar', and likely a 'user_courses' or
     * 'enrollments' table
     * with the 'course' table.
     * YOU WILL NEED TO IMPLEMENT THIS BASED ON YOUR 'JOINED COURSE' TABLE
     * STRUCTURE.
     *
     * @param email The email of the user.
     * @return A list of Course objects the user has joined.
     */
    public List<Course> getJoinedCoursesByEmail(String email) {
        List<Course> joinedCourses = new ArrayList<>();
        Connection connection;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // --- TODO: IMPLEMENT THIS SQL QUERY ---
        // This query should select course details for courses joined by the user with
        // the given email.
        // It will likely involve joining:
        // users (u) -> pelajar (p) -> some_enrollment_table (e) -> course (c)
        String sql = "SELECT c.id_course, c.id_pengajar, c.nama_course, c.enrollment_key, c.gambar_course, c.deskripsi "
                +
                "FROM users u " +
                "JOIN pelajar p ON u.id_user = p.id_user " +
                "JOIN YOUR_ENROLLMENT_TABLE_NAME et ON p.id_pelajar = et.id_pelajar " + // Replace
                                                                                        // YOUR_ENROLLMENT_TABLE_NAME
                                                                                        // and et.id_pelajar
                "JOIN course c ON et.id_course = c.id_course " + // Replace et.id_course
                "WHERE u.email = ?";

        try {
            connection = DatabaseUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Populate Course object from ResultSet, similar to CourseDAO.getAllCourses()
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
                joinedCourses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving joined courses by email: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeResources(resultSet, preparedStatement);
        }
        return joinedCourses;
    }
}
