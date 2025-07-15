package com.zaidan.testng.model;

public class Pelajar {
    private int idPelajar;
    private int idUser;
    private String nama;
    private String nim;

    // Constructor(s)
    public Pelajar(int idPelajar, int idUser, String nama, String nim) {
        this.idPelajar = idPelajar;
        this.idUser = idUser;
        this.nama = nama;
        this.nim = nim;
    }

    public Pelajar(int idPelajar, String nama) {
        this.idPelajar = idPelajar;
        this.nama = nama;
    }

    public int getIdPelajar() {
        return idPelajar;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdPelajar(int idPelajar) {
        this.idPelajar = idPelajar;
    }

    public String getNama() {
        return nama;
    }

    public String getNim() {
        return nim;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}