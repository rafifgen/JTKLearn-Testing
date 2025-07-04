package com.zaidan.testng.dao;

import com.zaidan.testng.model.Pelajar;
import com.zaidan.testng.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PelajarDAO {

    public Pelajar getPelajarByNama(String namaPelajar) {
        String sql = """
            SELECT id_pelajar, nama
            FROM pelajar
            WHERE nama = ?
            LIMIT 1
            """;

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, namaPelajar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idPelajar = rs.getInt("id_pelajar");
                    String nama = rs.getString("nama");
                    return new Pelajar(idPelajar, nama);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error fetching Pelajar for nama = " + namaPelajar, e);
        }
        throw new IllegalStateException(
                "No Pelajar found with nama = " + namaPelajar);
    }
}