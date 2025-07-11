package com.zaidan.testng.model;

import java.sql.Timestamp;

public class CourseProgress extends Course {
    private int idPelajar; // Maps to int4, representing the student ID
    private int persentase; // Maps to int4, representing the percentage of course completion
    private String status; // Maps to varchar(255), representing the course status
    private Timestamp createdAt; // Maps to timestamp, representing creation time
    private Timestamp updatedAt; // Maps to timestamp, representing last update time

    // Constructor
    public CourseProgress(int idCourse, int idPengajar, String namaCourse, String enrollmentKey, String gambarCourse,
                          String deskripsi, int idPelajar, int persentase, String status, Timestamp createdAt,
                          Timestamp updatedAt, String instructorDisplayText) {
        super(idCourse, idPengajar, namaCourse, enrollmentKey, gambarCourse, deskripsi, instructorDisplayText);
        this.idPelajar = idPelajar;
        this.persentase = persentase;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public int getIdPelajar() {
        return idPelajar;
    }

    public int getPersentase() {
        return persentase;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "CourseProgress{" +
                "idPelajar=" + idPelajar +
                ", persentase=" + persentase +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", idCourse=" + getIdCourse() +
                ", idPengajar=" + getIdPengajar() +
                ", namaCourse='" + getNamaCourse() + '\'' +
                ", enrollmentKey='" + getEnrollmentKey() + '\'' +
                ", gambarCourse='" + getGambarCourse() + '\'' +
                ", deskripsi='" + getDeskripsi() + '\'' +
                '}';
    }
}