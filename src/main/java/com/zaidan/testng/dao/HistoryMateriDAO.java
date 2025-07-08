package com.zaidan.testng.dao;

import java.sql.Timestamp;
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

        // SQL query to select 'waktu_akses' from 'history_materi'
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

        String query = "SELECT id_pelajar, id_materi, waktu_akses, waktu_selesai FROM history_materi WHERE id_pelajar = ? AND id_materi = ?";

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

}
