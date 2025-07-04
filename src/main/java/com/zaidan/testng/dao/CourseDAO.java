package com.zaidan.testng.dao;

import com.zaidan.testng.model.Course;
import com.zaidan.testng.model.CourseDetails;
import com.zaidan.testng.utils.DatabaseUtil;

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

    public Course getCourseById(int idCourse) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Course course = null; // Initialize course to null

        // SQL query to select all relevant columns for a single course by its ID
        String sql =
            "SELECT " +
            "id_course, " +
            "id_pengajar, " + // Make sure this matches your DB column name exactly
            "nama_course, " +
            "enrollment_key, " +
            "gambar_course, " +
            "deskripsi " +
            "FROM course " + // Assuming your table name is 'course'
            "WHERE id_course = ?"; // Placeholder for the course ID

        System.out.println("CourseDAO: Attempting to get course with ID: " + idCourse);
        System.out.println("CourseDAO: SQL Query: " + sql);

        try {
            connection = DatabaseUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            // Set the value for the placeholder
            preparedStatement.setInt(1, idCourse);

            resultSet = preparedStatement.executeQuery();

            // Check if a row was returned (there should be at most one for a unique ID)
            if (resultSet.next()) {
                // Extract data from the ResultSet
                int retrievedIdCourse = resultSet.getInt("id_course");
                int idPengajar = resultSet.getInt("id_pengajar"); // Matches DB column name
                String namaCourse = resultSet.getString("nama_course");
                String enrollmentKey = resultSet.getString("enrollment_key");
                String gambarCourse = resultSet.getString("gambar_course");
                String deskripsi = resultSet.getString("deskripsi");

                // Create the Course object
                // For DB-fetched courses, instructorDisplayText is not directly from DB, set as null
                course = new Course(
                    retrievedIdCourse,
                    idPengajar,
                    namaCourse,
                    enrollmentKey,
                    gambarCourse,
                    deskripsi,
                    null // instructorDisplayText is for UI's text, not from DB here
                );

                System.out.println("CourseDAO: Found Course: " + course.getNamaCourse() + " (ID: " + course.getIdCourse() + ")");
            } else {
                System.out.println("CourseDAO: No Course found for ID: " + idCourse);
            }
        } catch (SQLException e) {
            System.err.println("CourseDAO: Error retrieving Course by ID: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw the exception for calling methods to handle
        } finally {
            // Close ResultSet and PreparedStatement
            DatabaseUtil.closeResources(resultSet, preparedStatement);
        }
        return course;
    }
}