package com.zaidan.testng.model;

public class Pengajar {
    private int idPengajar;
    private String nama;

    public Pengajar(int idPengajar, String nama) {
        this.idPengajar = idPengajar;
        this.nama = nama;
    }

    public int getIdPengajar() {
        return idPengajar;
    }

    public void setIdPengajar(int idPengajar) {
        this.idPengajar = idPengajar;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}