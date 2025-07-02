package com.zaidan.testng.model;

public class Course {
    private int idCourse; // Maps to serial4 / int4
    private int idPengajar; // Maps to int4
    private String namaCourse; // Maps to varchar(255)
    private String enrollmentKey; // Maps to varchar(255)
    private String gambarCourse; // Maps to varchar(255)
    private String deskripsi; // Maps to text

    // Constructor
    public Course(int idCourse, int idPengajar, String namaCourse, String enrollmentKey, String gambarCourse,
            String deskripsi) {
        this.idCourse = idCourse;
        this.idPengajar = idPengajar;
        this.namaCourse = namaCourse;
        this.enrollmentKey = enrollmentKey;
        this.gambarCourse = gambarCourse;
        this.deskripsi = deskripsi;
    }

    // Getters
    public int getIdCourse() {
        return idCourse;
    }

    public int getIdPenganjar() {
        return idPengajar;
    }

    public String getNamaCourse() {
        return namaCourse;
    }

    public String getEnrollmentKey() {
        return enrollmentKey;
    }

    public String getGambarCourse() {
        return gambarCourse;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    @Override
    public String toString() {
        return "Course{" +
                "idCourse=" + idCourse +
                ", idPenganjar=" + idPengajar +
                ", namaCourse='" + namaCourse + '\'' +
                ", enrollmentKey='" + enrollmentKey + '\'' +
                ", gambarCourse='" + gambarCourse + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                '}';
    }
}
