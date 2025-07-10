package com.zaidan.testng.model;

import java.util.Objects;

public class QuizAttemptDetails {
    private String quizName;
    private String quizDescription;
    private double highestScore;
    private int totalAttempts;
    private String startTime;
    private String endTime;

    public QuizAttemptDetails(String quizName, String quizDescription, double highestScore, int totalAttempts, String startTime, String endTime) {
        this.quizName = quizName;
        this.quizDescription = quizDescription;
        this.highestScore = highestScore;
        this.totalAttempts = totalAttempts;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters
    public String getQuizName() { return quizName; }
    public String getQuizDescription() { return quizDescription; }
    public double getHighestScore() { return highestScore; }
    public int getTotalAttempts() { return totalAttempts; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizAttemptDetails that = (QuizAttemptDetails) o;
        return Double.compare(that.highestScore, highestScore) == 0 &&
                totalAttempts == that.totalAttempts &&
                Objects.equals(quizName, that.quizName) &&
                Objects.equals(quizDescription, that.quizDescription) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quizName, quizDescription, highestScore, totalAttempts, startTime, endTime);
    }

    @Override
    public String toString() {
        return "QuizAttemptDetails{" +
                "quizName='" + quizName + '\'' +
                ", quizDescription='" + quizDescription + '\'' +
                ", highestScore=" + highestScore +
                ", totalAttempts=" + totalAttempts +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}