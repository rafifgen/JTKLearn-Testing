package com.zaidan.testng.dao;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void setFinishTimeAfterDuration(int studentId, int materialId, int minutesLater) {
        System.out.println("DAO: Simulating " + minutesLater + " minute wait by updating the database directly.");
        
        Timestamp startTime = null;
        
        // Step 1: Read the current start time from the database
        String selectSql = "SELECT \"waktu_akses\" FROM \"historyMateri\" WHERE \"id_pelajar\" = ? AND \"id_materi\" = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            
            selectStmt.setInt(1, studentId);
            selectStmt.setInt(2, materialId);
            
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    startTime = rs.getTimestamp("waktu_akses");
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error reading start time: " + e.getMessage());
            e.printStackTrace();
            return; // Exit if we can't read the start time
        }

        if (startTime == null) {
            System.out.println("DAO WARNING: Could not find a start time for the given student/material. Cannot set finish time.");
            return;
        }

        // Step 2: Calculate the new finish time in Java
        Instant startInstant = startTime.toInstant();
        Instant finishInstant = startInstant.plus(minutesLater, ChronoUnit.MINUTES);
        Timestamp newFinishTime = Timestamp.from(finishInstant);

        // Step 3: Update the record with the new, final timestamp
        String updateSql = "UPDATE \"historyMateri\" SET \"waktu_selesai\" = ? WHERE \"id_pelajar\" = ? AND \"id_materi\" = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            
            updateStmt.setTimestamp(1, newFinishTime);
            updateStmt.setInt(2, studentId);
            updateStmt.setInt(3, materialId);
            
            int rowsAffected = updateStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("DAO: Successfully set finish time to: " + newFinishTime);
            } else {
                System.out.println("DAO WARNING: Update did not affect any rows, though a start time was found.");
            }

        } catch (SQLException e) {
            System.err.println("DAO: Error writing new finish time: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
