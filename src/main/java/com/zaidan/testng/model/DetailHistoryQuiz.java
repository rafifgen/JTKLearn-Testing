package com.zaidan.testng.model;

public class DetailHistoryQuiz {
    int idDetailHistoryQuiz;
    int idHistoryQuiz;
    int idPertanyaan;
    int idJawaban;
    String jawabanText;
    boolean status;

    public DetailHistoryQuiz(int idDetailHistoryQuiz, int idHistoryQuiz, int idPertanyaan, int idJawaban, String jawabanText, boolean status) {
        this.idDetailHistoryQuiz = idDetailHistoryQuiz;
        this.idHistoryQuiz = idHistoryQuiz;
        this.idPertanyaan = idPertanyaan;
        this.idJawaban = idJawaban;
        this.jawabanText = jawabanText;
        this.status = status;
    }

    public int getIdDetailHistoryQuiz() {
        return idDetailHistoryQuiz;
    }

    public void setIdDetailHistoryQuiz(int idDetailHistoryQuiz) {
        this.idDetailHistoryQuiz = idDetailHistoryQuiz;
    }

    public int getIdHistoryQuiz() {
        return idHistoryQuiz;
    }

    public void setIdHistoryQuiz(int idHistoryQuiz) {
        this.idHistoryQuiz = idHistoryQuiz;
    }

    public int getIdPertanyaan() {
        return idPertanyaan;
    }

    public void setIdPertanyaan(int idPertanyaan) {
        this.idPertanyaan = idPertanyaan;
    }

    public int getIdJawaban() {
        return idJawaban;
    }

    public void setIdJawaban(int idJawaban) {
        this.idJawaban = idJawaban;
    }

    public String getJawabanText() {
        return jawabanText;
    }

    public void setJawabanText(String jawabanText) {
        this.jawabanText = jawabanText;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
