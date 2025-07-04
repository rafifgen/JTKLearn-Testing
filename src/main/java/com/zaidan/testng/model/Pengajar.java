package com.zaidan.testng.model;

public class Pengajar {
    private int idPengajar;    // Corresponds to kode_dosen
    private String namaPengajar; // Corresponds to nama in dosen table
    private String nip;         // New: Corresponds to nip in dosen table
    // private int idUser; // Optional: If you need the linked user ID
    // private String alamat; // Optional
    // private String jenisKelamin; // Optional

    public Pengajar(int idPengajar, String namaPengajar, String nip) {
        this.idPengajar = idPengajar;
        this.namaPengajar = namaPengajar;
        this.nip = nip;
    }

    public int getIdPengajar() { return idPengajar; }
    public String getNamaPengajar() { return namaPengajar; }
    public String getNip() { return nip; }

    @Override
    public String toString() {
        return "Pengajar{" +
                "idPengajar=" + idPengajar +
                ", namaPengajar='" + namaPengajar + '\'' +
                ", nip='" + nip + '\'' +
                '}';
    }


    public void setNama(String nama) {
        this.namaPengajar = nama;
    }
}