package com.zaidan.testng.dao;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zaidan.testng.enums.TaskStatus;
import com.zaidan.testng.model.HistoryMateri;
import com.zaidan.testng.utils.DatabaseUtil;

public class HistoryMateriDAO {
    public Timestamp getStartingTime(int idPelajar, int idMateri) throws SQLException {
        Connection connection = null; // Renamed 'conn' to 'connection' for consistency
        PreparedStatement preparedStatement = null; // Renamed 'pStatement' to 'preparedStatement'
        ResultSet resultSet = null;
        Timestamp startingTime = null;

        // SQL query to select 'waktu_akses' from 'historyMateri'
        // based on 'id_pelajar' and 'id_materi'
        String query = "SELECT waktu_akses FROM \"historyMateri\" WHERE id_pelajar = ? AND id_materi = ?";

        System.out.println("HistoryMateriDAO: Attempting to get starting time for Pelajar ID: " + idPelajar + ", Materi ID: " + idMateri);
        System.out.println("HistoryMateriDAO: SQL Query: " + query);

        try {
            connection = DatabaseUtil.getConnection(); // Get the shared database connection

            preparedStatement = connection.prepareStatement(query); // Prepare the SQL statement
            preparedStatement.setInt(1, idPelajar); // Set the first placeholder ('id_pelajar')
            preparedStatement.setInt(2, idMateri);  // Set the second placeholder ('id_materi')

            resultSet = preparedStatement.executeQuery(); // Execute the query

            if (resultSet.next()) { // If a row is found
                startingTime = resultSet.getTimestamp("waktu_akses"); // Retrieve the timestamp
                System.out.println("HistoryMateriDAO: Found starting time: " + startingTime);
            } else {
                System.out.println("HistoryMateriDAO: No history entry found for Pelajar ID: " + idPelajar + ", Materi ID: " + idMateri);
            }
        } catch (SQLException e) {
            System.err.println("HistoryMateriDAO: SQL Error retrieving starting time: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw the exception for proper error handling upstream
        } finally {
            // Close ResultSet and PreparedStatement
            DatabaseUtil.closeResources(resultSet, preparedStatement);
        }
        return startingTime;
    }

    public HistoryMateri getHistoryMateri(int idPelajar, int idMateri) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        HistoryMateri history = null;

        String query = "SELECT id_pelajar, id_materi, waktu_akses, waktu_selesai FROM \"historyMateri\" WHERE id_pelajar = ? AND id_materi = ?";

        try {
            connection = DatabaseUtil.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idPelajar);
            preparedStatement.setInt(2, idMateri);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Timestamp waktuAkses = resultSet.getTimestamp("waktu_akses");
                Timestamp waktuSelesai = resultSet.getTimestamp("waktu_selesai");
                // Note: createdAt and updatedAt are not selected as per your model
                history = new HistoryMateri(idPelajar, idMateri, waktuAkses, waktuSelesai);
            }
        } catch (SQLException e) {
            System.err.println("HistoryMateriDAO: SQL Error retrieving history materi: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            DatabaseUtil.closeResources(resultSet, preparedStatement);
        }
        return history;
    }

    public void resetFinishTimeForMaterial(int studentId, int materialId) {
        // SQL command to update the specific record
        String sql = "UPDATE \"historyMateri\" SET \"waktu_selesai\" = NULL WHERE \"id_pelajar\" = ? AND \"id_materi\" = ?";

        System.out.println("DAO: Resetting finish time to NULL for student " + studentId + " and material " + materialId);

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, materialId);

            // Use executeUpdate() for INSERT, UPDATE, or DELETE statements
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("DAO: Reset successful.");
            } else {
                System.out.println("DAO: No existing record found to reset.");
            }

        } catch (SQLException e) {
            System.err.println("DAO: Error resetting finish time: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setFinishTimeAfterDuration(int studentId, int materialId, int durationMinutes) {
        String deleteSql = "DELETE FROM \"historyMateri\" WHERE \"id_pelajar\" = ? AND \"id_materi\" = ?";
        
        // This SQL inserts a start time of NOW() and calculates the finish time based on the duration
        String insertSql = "INSERT INTO \"historyMateri\" (id_pelajar, id_materi, waktu_akses, waktu_selesai) " +
                        "VALUES (?, ?, NOW(), NOW() + INTERVAL '" + durationMinutes + " minutes')";

        System.out.println("DAO: Setting material " + materialId + " to FINISHED for student " + studentId);

        try (Connection conn = DatabaseUtil.getConnection()) {
            // Step 1: Delete any old record to ensure a clean state
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, studentId);
                deleteStmt.setInt(2, materialId);
                deleteStmt.executeUpdate();
                System.out.println("HistoryMateriDAO: Delete successful");
            }

            // Step 2: Insert the new, finished record
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, studentId);
                insertStmt.setInt(2, materialId);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error in delete-then-insert for material history: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void resetMateriProgressByStudentAndMateriId(int idPelajar, int idMateri) throws SQLException {
        String deleteSQL = "DELETE FROM \"historyMateri\" WHERE \"id_pelajar\" = ? AND \"id_materi\" = ?";
        
        // This 'try-with-resources' block automatically closes the connection and statement.
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement pStatement = conn.prepareStatement(deleteSQL)) {
            
            // Set the parameters for the WHERE clause
            pStatement.setInt(1, idPelajar);
            pStatement.setInt(2, idMateri);
            
            // Execute the DELETE command
            pStatement.executeUpdate();
            
            System.out.println("DAO: Successfully deleted history for student " + idPelajar + " and material " + idMateri);

        } catch (SQLException e) {
            System.err.println("DAO: Error deleting history record: " + e.getMessage());
            // Re-throw the exception so the calling method knows something went wrong.
            throw e;
        }
    }

    public boolean isHistoryRecordUnavailable(int idPelajar, int idMateri) throws SQLException {
        String sql = "SELECT COUNT(*) FROM \"historyMateri\" WHERE \"id_pelajar\" = ? AND \"id_materi\" = ?";
        int rowCount = 0;

        // try-with-resources ensures the connection and statement are always closed safely
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setInt(1, idPelajar);
            pStatement.setInt(2, idMateri);

            try (ResultSet rs = pStatement.executeQuery()) {
                // There will always be one row in the result set for a COUNT(*) query
                if (rs.next()) {
                    rowCount = rs.getInt(1); // Get the count from the first column
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error counting historyMateri records: " + e.getMessage());
            throw e; // Re-throw the exception to let the test know something failed
        }
        
        // If the count is 0, the row is unavailable, so we return true.
        return rowCount == 0;
    }

    public TaskStatus getDBMaterialStatus(int studentId, int materialId, int requiredMinutes) throws SQLException {
        HistoryMateri history = getHistoryMateri(studentId, materialId); // Assuming you have this method

        // Rule 1: Not Started (no record exists)
        if (history == null) {
            return TaskStatus.NOT_TAKEN;
        }
        
        // Rule 2: In Progress (record exists but has no finish time yet)
        if (history.getWaktuSelesai() == null) {
            return TaskStatus.IN_PROGRESS;
        }

        // Rule 3: Record exists and has a finish time. Check if the duration is long enough.
        long diffInMillis = history.getWaktuSelesai().getTime() - history.getWaktuAkses().getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);

        if (diffInMinutes >= requiredMinutes) {
            // The time spent was long enough to be considered finished.
            return TaskStatus.PASSED;
        } else {
            // The time spent was too short, so it's still considered in progress.
            return TaskStatus.IN_PROGRESS;
        }
    }

    public void setMaterialInProgress(int studentId, int materialId) {
        // First, delete any existing record to ensure a clean slate
        String deleteSql = "DELETE FROM \"historyMateri\" WHERE \"id_pelajar\" = ? AND \"id_materi\" = ?";
        // Then, insert a new record with the current time as the start time
        String insertSql = "INSERT INTO \"historyMateri\"(\"id_pelajar\", \"id_materi\", \"waktu_akses\", \"waktu_selesai\") " +
        "VALUES (?, ?, NOW(), NOW() + INTERVAL '2 seconds')";

        try (Connection conn = DatabaseUtil.getConnection()) {
            // Run DELETE
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, studentId);
                deleteStmt.setInt(2, materialId);
                deleteStmt.executeUpdate();
            }
            // Run INSERT
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, studentId);
                insertStmt.setInt(2, materialId);
                insertStmt.executeUpdate();
            }
            System.out.println("DAO: Set material " + materialId + " to IN_PROGRESS for student " + studentId);
        } catch (SQLException e) {
            System.err.println("DAO: Error setting material to in-progress state: " + e.getMessage());
            e.printStackTrace();
        }
    }
}