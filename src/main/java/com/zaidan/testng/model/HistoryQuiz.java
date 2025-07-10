package com.zaidan.testng.model;

import java.sql.Timestamp;

public class HistoryQuiz {
int idHistoryQuiz;
    int idPelajar;
    int idQuiz;
    Timestamp waktuMulai;
    Timestamp waktuSelesai;
    float nilai;

    public HistoryQuiz(int idHistoryQuiz, int idPelajar, int idQuiz, Timestamp waktuMulai, Timestamp waktuSelesai, float nilai) {
        this.idHistoryQuiz = idHistoryQuiz;
        this.idPelajar = idPelajar;
        this.idQuiz = idQuiz;
        this.waktuMulai = waktuMulai;
        this.waktuSelesai = waktuSelesai;
        this.nilai = nilai;
    }

    public float getNilai() {
        return nilai;
    }

    public Timestamp getWaktuMulai() {
        return waktuMulai;
    }
}