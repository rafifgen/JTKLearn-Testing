package com.zaidan.testng.model;

public class Pelajar {
    private int idPelajar;
    private int idUser;
    private String nama;
    private String nim;
    private String kelas;

    // Constructor(s)
    public Pelajar(int idPelajar, int idUser, String nama, String nim, String kelas) {
        this.idPelajar = idPelajar;
        this.idUser = idUser;
        this.nama = nama;
        this.nim = nim;
        this.kelas = kelas;
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

    public String getKelas() {
        return kelas;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}