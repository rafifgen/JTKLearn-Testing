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

    public int getIdPengajar() {
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
                ", idPengajar=" + idPengajar +
                ", namaCourse='" + namaCourse + '\'' +
                ", enrollmentKey='" + enrollmentKey + '\'' +
                ", gambarCourse='" + gambarCourse + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                '}';
    }

    // IMPORTANT: Override equals and hashCode for proper comparison of lists/sets
    // of Course objects
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Course course = (Course) o;
        // Compare relevant fields for equality.
        // For UI vs DB, 'namaCourse' and 'gambarCourse' (image URL) are key.
        // 'idCourse' might be 0 for UI objects if not extractable.
        // So, compare only the fields you can reliably get from both.
        return idCourse == course.idCourse &&
                idPengajar == course.idPengajar && // Only if you can get instructor ID reliably from UI
                namaCourse.equals(course.namaCourse) &&
                gambarCourse.equals(course.gambarCourse) && // Compare image URLs
                enrollmentKey.equals(course.enrollmentKey) && // If you can extract it
                deskripsi.equals(course.deskripsi); // If you can extract it
    }

    @Override
    public int hashCode() {
        // Generate hash code based on the fields used in equals
        return java.util.Objects.hash(idCourse, idPengajar, namaCourse, enrollmentKey, gambarCourse, deskripsi);
    }
}
