package com.zaidan.testng.dao;

import com.zaidan.testng.model.Course;
import com.zaidan.testng.model.CourseDetails;
import com.zaidan.testng.model.CourseProgress;
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

    public float getCourseProgressByStudentAndCourseId(int idPelajar, int idCourse) {
        float progress = 0.0f; // Default value if no record is found
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // The table name "courseParticipant" is quoted because it likely contains uppercase
        // letters in your PostgreSQL database, making it case-sensitive.
        String sql = "SELECT persentase_course FROM \"courseParticipant\" WHERE id_pelajar = ? AND id_course = ?";

        System.out.println("CourseDAO: Getting progress for student ID: " + idPelajar + ", course ID: " + idCourse);

        try {
            connection = DatabaseUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            // Set the integer parameters for the WHERE clause placeholders
            preparedStatement.setInt(1, idPelajar);
            preparedStatement.setInt(2, idCourse);

            resultSet = preparedStatement.executeQuery();

            // If a record is found, update the progress value from the result
            if (resultSet.next()) {
                progress = resultSet.getFloat("persentase_course");
                System.out.println("CourseDAO: Progress found: " + progress + "%");
            } else {
                System.out.println("CourseDAO: No progress record found for this student and course.");
            }
        } catch (SQLException e) {
            System.err.println("CourseDAO: Error retrieving course progress: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure database resources are always closed to prevent leaks
            DatabaseUtil.closeResources(resultSet, preparedStatement);
        }

        return progress;
    }

    public void setCourseProgressByStudentAndCourseId(int idPelajar, int idCourse, float courseProgress) {
        String deleteSql = "DELETE FROM \"courseParticipant\" WHERE id_pelajar = ? AND id_course = ?";
        
        // This query inserts the new progress and sets createdAt/updatedAt to the current time
        String insertSql = "INSERT INTO \"courseParticipant\" " +
                         "(id_pelajar, id_course, persentase_course, \"createdAt\", \"updatedAt\") " +
                         "VALUES (?, ?, ?, NOW(), NOW())";

        System.out.println("DAO: Setting progress for student " + idPelajar + " in course " + idCourse + " to " + courseProgress + "%");

        try (Connection conn = DatabaseUtil.getConnection()) {
            
            // Step 1: Delete any old progress record to ensure a clean state.
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, idPelajar);
                deleteStmt.setInt(2, idCourse);
                deleteStmt.executeUpdate();
            }

            // Step 2: Insert the new progress record.
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, idPelajar);
                insertStmt.setInt(2, idCourse);
                insertStmt.setFloat(3, courseProgress);
                insertStmt.executeUpdate();
            }
            
        } catch (SQLException e) {
            System.err.println("DAO: Error setting course progress: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Ambil list kursus yang completed, beserta persentase dan statusnya.
     */
    public List<CourseProgress> getCompletedCourses() {
        return getCoursesByStatus("Completed");
    }

    /**
     * Ambil list kursus yang sedang berjalan (in progress).
     */
    public List<CourseProgress> getInProgressCourses() {
        return getCoursesByStatus("In Progress");
    }

    /**
     * General method untuk ambil courses by status.
     */
    private List<CourseProgress> getCoursesByStatus(String status) {
        List<CourseProgress> list = new ArrayList<>();

        String sql = """
            SELECT 
                c.id_course,
                c.id_pengajar,
                c.nama_course,
                c.enrollment_key,
                c.gambar_course,
                c.deskripsi,
                cp.id_pelajar,
                cp.persentase_course,
                cp.status_penyelesaian,
                cp."createdAt",
                cp."updatedAt"
            FROM "courseParticipant" cp
            JOIN course c ON cp.id_course = c.id_course
            WHERE cp.status_penyelesaian::text = ?
            ORDER BY cp."updatedAt" DESC
            """;

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CourseProgress cp = new CourseProgress(
                            rs.getInt("id_course"),
                            rs.getInt("id_pengajar"),
                            rs.getString("nama_course"),
                            rs.getString("enrollment_key"),
                            rs.getString("gambar_course"),
                            rs.getString("deskripsi"),
                            rs.getInt("id_pelajar"),
                            rs.getInt("persentase_course"),
                            rs.getString("status_penyelesaian"),
                            rs.getTimestamp("createdAt"),
                            rs.getTimestamp("updatedAt"),
                            null
                    );
                    list.add(cp);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving courses by status [" + status + "]: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Ambil list kursus yang completed untuk satu pelajar.
     */
    public List<CourseProgress> getCompletedCoursesByPelajar(int idPelajar) {
        return getCoursesByStatusAndPelajar("Completed", idPelajar);
    }

    private List<CourseProgress> getCoursesByStatusAndPelajar(String status, int idPelajar) {
        List<CourseProgress> list = new ArrayList<>();
        String sql = """
        SELECT 
          c.id_course,
          c.id_pengajar,
          c.nama_course,
          c.enrollment_key,
          c.gambar_course,
          c.deskripsi,
          cp.id_pelajar,
          cp.persentase_course,
          cp.status_penyelesaian,
          cp."createdAt",
          cp."updatedAt"
        FROM "courseParticipant" cp
        JOIN course c ON cp.id_course = c.id_course
        WHERE cp.status_penyelesaian::text = ?
          AND cp.id_pelajar = ?
        ORDER BY cp."updatedAt" DESC
        """;

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, status);
            ps.setInt   (2, idPelajar);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new CourseProgress(
                            rs.getInt("id_course"),
                            rs.getInt("id_pengajar"),
                            rs.getString("nama_course"),
                            rs.getString("enrollment_key"),
                            rs.getString("gambar_course"),
                            rs.getString("deskripsi"),
                            rs.getInt("id_pelajar"),
                            rs.getInt("persentase_course"),
                            rs.getString("status_penyelesaian"),
                            rs.getTimestamp("createdAt"),
                            rs.getTimestamp("updatedAt"),
                            null
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving courses by status ["
                    + status + "] for pelajar " + idPelajar + ": "
                    + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // get course by name
    public Course getCourseByName(String courseName) {
        String sql = "SELECT id_course, id_pengajar, nama_course, enrollment_key, gambar_course, deskripsi " +
                     "FROM course WHERE nama_course = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Course(
                            rs.getInt("id_course"),
                            rs.getInt("id_pengajar"),
                            rs.getString("nama_course"),
                            rs.getString("enrollment_key"),
                            rs.getString("gambar_course"),
                            rs.getString("deskripsi"),
                            null
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving course by name [" + courseName + "]: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Return null if no course found
    }
}