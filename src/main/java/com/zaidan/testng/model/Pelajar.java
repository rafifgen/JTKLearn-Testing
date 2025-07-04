package com.zaidan.testng.model;

public class Pelajar {
    private int idPelajar;
    private String nama;

    public Pelajar(int idPelajar, String nama) {
        this.idPelajar = idPelajar;
        this.nama = nama;
    }

    public int getIdPelajar() {
        return idPelajar;
    }

    public void setIdPelajar(int idPelajar) {
        this.idPelajar = idPelajar;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}