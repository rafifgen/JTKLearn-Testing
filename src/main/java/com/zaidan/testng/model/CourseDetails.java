package com.zaidan.testng.model;

// Model untuk menampung detail lengkap kursus dari hasil query JOIN
public class CourseDetails {
    private String courseName;
    private String instructorName;
    private String courseImage;
    private double progressPercentage; // Progres dalam bentuk persentase

    public CourseDetails(String courseName, String instructorName, String courseImage, double progressPercentage) {
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.courseImage = courseImage;
        this.progressPercentage = progressPercentage;
    }

    // Getters
    public String getCourseName() { return courseName; }
    public String getInstructorName() { return instructorName; }
    public String getCourseImage() { return courseImage; }
    public double getProgressPercentage() { return progressPercentage; }

//    @Override
//    public String toString() {
//        return "CourseDetails{" +
//                "courseName='" + courseName + '\'' +
//                ", instructorName='" + instructorName + '\'' +
//                ", courseImage='" + courseImage + '\'' +
//                ", progressPercentage=" + progressPercentage +
//                '}';
//    }
}