package com.zaidan.testng.dao;

import com.zaidan.testng.model.Pengajar;
import com.zaidan.testng.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PengajarDAO {

    /**
     * Mengambil objek Pengajar berdasarkan id_pengajar dari tabel 'pengajar'
     *
     * @param idPengajar ID dari pengajar
     * @return Objek Pengajar jika ditemukan, otherwise null
     */
    public Pengajar getPengajarById(int idPengajar) {
        String sql = "SELECT kode_dosen, nama FROM pengajar WHERE kode_dosen = ?";
        Pengajar pengajar = null;

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idPengajar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("kode_dosen");
                    String nama = rs.getString("nama");
                    pengajar = new Pengajar(id, nama); // Create Pengajar object
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving Pengajar for id " + idPengajar + ": " + e.getMessage());
            e.printStackTrace();
        }

        return pengajar;
    }
}