package com.zaidan.testng.model;

import java.util.List;
import java.util.Objects;

public class QuizReview {
    private int quizId;
    private String quizName;
    private String quizDescription;
    private int studentId;
    private double score;
    private List<QuizQuestion> questions;
    private String reviewUrl;

    // Default constructor
    public QuizReview() {
    }

    public QuizReview(int quizId, String quizName, String quizDescription, int studentId, double score, List<QuizQuestion> questions, String reviewUrl) {
        this.quizId = quizId;
        this.quizName = quizName;
        this.quizDescription = quizDescription;
        this.studentId = studentId;
        this.score = score;
        this.questions = questions;
        this.reviewUrl = reviewUrl;
    }

    // Getters
    public int getQuizId() { return quizId; }
    public String getQuizName() { return quizName; }
    public String getQuizDescription() { return quizDescription; }
    public int getStudentId() { return studentId; }
    public double getScore() { return score; }
    public List<QuizQuestion> getQuestions() { return questions; }
    public String getReviewUrl() { return reviewUrl; }

    // Setters
    public void setQuizId(int quizId) { this.quizId = quizId; }
    public void setQuizName(String quizName) { this.quizName = quizName; }
    public void setQuizDescription(String quizDescription) { this.quizDescription = quizDescription; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public void setScore(double score) { this.score = score; }
    public void setQuestions(List<QuizQuestion> questions) { this.questions = questions; }
    public void setReviewUrl(String reviewUrl) { this.reviewUrl = reviewUrl; }
    
    // Additional setter for quiz title (alternative to quiz name)
    public void setQuizTitle(String quizTitle) { this.quizName = quizTitle; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizReview that = (QuizReview) o;
        return quizId == that.quizId &&
                studentId == that.studentId &&
                Double.compare(that.score, score) == 0 &&
                Objects.equals(quizName, that.quizName) &&
                Objects.equals(quizDescription, that.quizDescription) &&
                Objects.equals(questions, that.questions) &&
                Objects.equals(reviewUrl, that.reviewUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quizId, quizName, quizDescription, studentId, score, questions, reviewUrl);
    }

    @Override
    public String toString() {
        return "QuizReview{" +
                "quizId=" + quizId +
                ", quizName='" + quizName + '\'' +
                ", quizDescription='" + quizDescription + '\'' +
                ", studentId=" + studentId +
                ", score=" + score +
                ", questions=" + questions +
                ", reviewUrl='" + reviewUrl + '\'' +
                '}';
    }
} 