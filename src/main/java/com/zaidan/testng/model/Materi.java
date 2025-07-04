package com.zaidan.testng.model;

import com.zaidan.testng.enums.JenisMateri;

public class Materi {
    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
    }

    public int getIdMateri() {
        return idMateri;
    }

    public void setIdMateri(int idMateri) {
        this.idMateri = idMateri;
    }

    public String getNamaMateri() {
        return namaMateri;
    }

    public void setNamaMateri(String namaMateri) {
        this.namaMateri = namaMateri;
    }

    public String getKontenMateri() {
        return kontenMateri;
    }

    public void setKontenMateri(String kontenMateri) {
        this.kontenMateri = kontenMateri;
    }

    public JenisMateri getJenisMateri() {
        return jenisMateri;
    }

    public void setJenisMateri(JenisMateri jenisMateri) {
        this.jenisMateri = jenisMateri;
    }

    int idCourse;
    int idMateri;
    String namaMateri;
    String kontenMateri;
    JenisMateri jenisMateri;

    public Materi(int idCourse, int idMateri, String namaMateri, String kontenMateri, JenisMateri jenisMateri) {
        this.idCourse = idCourse;
        this.idMateri = idMateri;
        this.namaMateri = namaMateri;
        this.kontenMateri = kontenMateri;
        this.jenisMateri = jenisMateri;
    }
}
