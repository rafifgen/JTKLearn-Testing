package com.zaidan.testng.model;

import java.sql.Time;
import java.sql.Timestamp;

public class HistoryMateri {
    int idPelajar;
    int idMateri;
    Timestamp waktuAkses;
    Timestamp waktuSelesai;

    public HistoryMateri(int idPelajar, int idMateri, Timestamp waktuAkses, Timestamp waktuSelesai) {
        this.idPelajar = idPelajar;
        this.idMateri = idMateri;
        this.waktuAkses = waktuAkses;
        this.waktuSelesai = waktuSelesai;
    }
    
    public int getIdPelajar() {
        return idPelajar;
    }
    public int getIdMateri() {
        return idMateri;
    }
    public Timestamp getWaktuAkses() {
        return waktuAkses;
    }
    public Timestamp getWaktuSelesai() {
        return waktuSelesai;
    }
}
