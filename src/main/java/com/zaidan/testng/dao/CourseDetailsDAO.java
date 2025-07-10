package com.zaidan.testng.dao;

import com.zaidan.testng.model.ContentItem;
import com.zaidan.testng.model.ContentType;
import com.zaidan.testng.model.CourseDetails;
import com.zaidan.testng.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<ContentItem> getCourseContentItems(String courseName) {
        String sql =
                "SELECT id_materi    AS sort_key, nama_materi AS name, 'MATERIAL' AS type\n" +
                        "  FROM materi m\n" +
                        "  JOIN course c ON m.id_course = c.id_course\n" +
                        " WHERE c.nama_course = ?\n" +
                        "UNION ALL\n" +
                        "SELECT id_quiz      AS sort_key, nama_quiz   AS name, 'QUIZ'     AS type\n" +
                        "  FROM quiz q\n" +
                        "  JOIN course c ON q.id_course = c.id_course\n" +
                        " WHERE c.nama_course = ?\n" +
                        "ORDER BY sort_key";
        List<ContentItem> items = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseName);
            ps.setString(2, courseName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    ContentType type = ContentType.valueOf(rs.getString("type"));
                    items.add(new ContentItem(name, type));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return items;
    }

    public List<String> getCompletedItemNames(int idPelajar, String courseName) {
        List<String> completedNames = new ArrayList<>();

        String sql = """
        SELECT m.nama_materi AS item_name
        FROM materi m
        JOIN "historyMateri" hm ON m.id_materi = hm.id_materi
        JOIN course c ON m.id_course = c.id_course
        WHERE hm.id_pelajar = ? AND c.nama_course = ? AND hm.waktu_selesai IS NOT NULL

        UNION ALL

        SELECT q.nama_quiz AS item_name
        FROM quiz q
        JOIN "historyQuiz" hq ON q.id_quiz = hq.id_quiz
        JOIN course c ON q.id_course = c.id_course
        WHERE hq.id_pelajar = ? AND c.nama_course = ? AND hq.waktu_selesai IS NOT NULL
    """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPelajar);
            ps.setString(2, courseName);
            ps.setInt(3, idPelajar);
            ps.setString(4, courseName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    completedNames.add(rs.getString("item_name").trim());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return completedNames;
    }

    public List<ContentItem> getCompletedContentItemsByPelajar(int idPelajar, String courseName) {
        List<ContentItem> completedItems = new ArrayList<>();

        String sql = """
        SELECT m.nama_materi AS item_name, 'MATERIAL' AS type
                               FROM materi m
                               JOIN (
                                   SELECT h1.id_materi
                                   FROM "historyMateri" h1
                                   JOIN (
                                       SELECT id_materi, MAX(waktu_akses) AS latest_start
                                       FROM "historyMateri"
                                       WHERE id_pelajar = ?
                                       GROUP BY id_materi
                                   ) h2 ON h1.id_materi = h2.id_materi AND h1.waktu_akses = h2.latest_start
                                   INNER JOIN materi m2 ON h1.id_materi = m2.id_materi
                                   WHERE h1.id_pelajar = ?\s
                                     AND (
                                         (m2.jenis_materi = 'video' AND h1.waktu_selesai >= h1.waktu_akses + INTERVAL '5 minutes') OR
                                         (m2.jenis_materi = 'teks' AND h1.waktu_selesai >= h1.waktu_akses + INTERVAL '2 minutes')
                                     )
                               ) hm ON m.id_materi = hm.id_materi
                               JOIN course c ON m.id_course = c.id_course
                               WHERE c.nama_course = ?

        UNION ALL

        SELECT q.nama_quiz AS item_name, 'QUIZ' AS type
                               FROM quiz q
                               JOIN (
                                   SELECT h1.id_quiz
                                   FROM "historyQuiz" h1
                                   JOIN (
                                       SELECT id_quiz, MAX(waktu_mulai) AS latest_start
                                       FROM "historyQuiz"
                                       WHERE id_pelajar = ?
                                       GROUP BY id_quiz
                                   ) h2 ON h1.id_quiz = h2.id_quiz AND h1.waktu_mulai = h2.latest_start
                                   WHERE h1.id_pelajar = ? AND h1.nilai >= 80
                               ) hq ON q.id_quiz = hq.id_quiz
                               JOIN course c ON q.id_course = c.id_course
                               WHERE c.nama_course = ?
    """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPelajar);
            ps.setInt(2, idPelajar);
            ps.setString(3, courseName);
            ps.setInt(4, idPelajar);
            ps.setInt(5, idPelajar);
            ps.setString(6, courseName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("item_name").trim();
                    ContentType type = ContentType.valueOf(rs.getString("type"));
                    completedItems.add(new ContentItem(name, type));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return completedItems;
    }




}