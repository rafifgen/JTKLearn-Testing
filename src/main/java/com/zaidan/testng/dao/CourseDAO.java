package com.zaidan.testng.dao;

import com.zaidan.testng.model.Course;
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
