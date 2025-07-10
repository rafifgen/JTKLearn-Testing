package com.zaidan.testng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; // Import ResultSet
import java.sql.SQLException;

import com.zaidan.testng.model.Materi;
import com.zaidan.testng.enums.JenisMateri; // Import your enum
import com.zaidan.testng.utils.DatabaseUtil; // Import DatabaseUtil

public class MateriDAO {

    public Materi getMateriById(int idMateri) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null; // Declare ResultSet
        Materi materi = null; // Initialize materi to null

        // --- SQL Query to select all relevant columns for a Materi by id_materi ---
        String query =
            "SELECT " +
            "id_materi, " +
            "id_course, " +
            "nama_materi, " +
            "konten_materi, " +
            "jenis_materi " + // This is your ENUM column
            "FROM materi " + // Assuming your table name is 'materi'
            "WHERE id_materi = ?"; // Placeholder for the ID

        System.out.println("MateriDAO: Attempting to get materi with ID: " + idMateri);
        System.out.println("MateriDAO: SQL Query: " + query);

        try {
            connection = DatabaseUtil.getConnection(); // Get the shared connection
            preparedStatement = connection.prepareStatement(query); // Prepare the statement

            preparedStatement.setInt(1, idMateri); // Set the id_materi parameter

            resultSet = preparedStatement.executeQuery(); // Execute the query

            // Check if a row was returned
            if (resultSet.next()) {
                // Extract data from the ResultSet
                int retrievedIdMateri = resultSet.getInt("id_materi");
                int idCourse = resultSet.getInt("id_course");
                String namaMateri = resultSet.getString("nama_materi");
                String kontenMateri = resultSet.getString("konten_materi");

                // --- CRITICAL: Convert database string to Java Enum ---
                String jenisMateriDbString = resultSet.getString("jenis_materi");
                JenisMateri jenisMateri = JenisMateri.fromDbValue(jenisMateriDbString); // Use your enum's static method

                // Create the Materi object
                materi = new Materi(retrievedIdMateri, idCourse, namaMateri, kontenMateri, jenisMateri);

                System.out.println("MateriDAO: Found Materi: " + materi.getNamaMateri() + " (ID: " + materi.getIdMateri() + ")");
            } else {
                System.out.println("MateriDAO: No Materi found for ID: " + idMateri);
            }
        } catch (SQLException e) {
            System.err.println("MateriDAO: SQL Error retrieving Materi by ID: " + e.getMessage());
            e.printStackTrace();
            // You might want to throw a custom exception or re-throw SQLException here
        } catch (IllegalArgumentException e) {
            // This catches errors from MateriJenisMateri.fromDbValue() if an unknown enum string is found
            System.err.println("MateriDAO: Data integrity error for Materi ID " + idMateri + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Always close resources
            DatabaseUtil.closeResources(resultSet, preparedStatement);
        }
        return materi;
    }
}