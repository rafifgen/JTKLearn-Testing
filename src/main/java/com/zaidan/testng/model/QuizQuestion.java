package com.zaidan.testng.model;

import java.util.List;
import java.util.Objects;

public class QuizQuestion {
    private int questionId;
    private String questionText;
    private String questionType; // "multiple_choice", "essay", etc.
    private List<String> options; // For multiple choice questions
    private String correctAnswer;
    private String studentAnswer;
    private boolean isCorrect;
    private String borderClass; // "border-success" or "border-danger"
    private String statusClass; // "text-success" or "text-unsuccess"

    // Default constructor
    public QuizQuestion() {
    }

    public QuizQuestion(int questionId, String questionText, String questionType, List<String> options, 
                       String correctAnswer, String studentAnswer, boolean isCorrect, 
                       String borderClass, String statusClass) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.studentAnswer = studentAnswer;
        this.isCorrect = isCorrect;
        this.borderClass = borderClass;
        this.statusClass = statusClass;
    }

    // Getters
    public int getQuestionId() { return questionId; }
    public String getQuestionText() { return questionText; }
    public String getQuestionType() { return questionType; }
    public List<String> getOptions() { return options; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getStudentAnswer() { return studentAnswer; }
    public boolean isCorrect() { return isCorrect; }
    public String getBorderClass() { return borderClass; }
    public String getStatusClass() { return statusClass; }

    // Setters
    public void setQuestionId(int questionId) { this.questionId = questionId; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }
    public void setOptions(List<String> options) { this.options = options; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    public void setStudentAnswer(String studentAnswer) { this.studentAnswer = studentAnswer; }
    public void setCorrect(boolean correct) { this.isCorrect = correct; }
    public void setBorderClass(String borderClass) { this.borderClass = borderClass; }
    public void setStatusClass(String statusClass) { this.statusClass = statusClass; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizQuestion that = (QuizQuestion) o;
        return questionId == that.questionId &&
                isCorrect == that.isCorrect &&
                Objects.equals(questionText, that.questionText) &&
                Objects.equals(questionType, that.questionType) &&
                Objects.equals(options, that.options) &&
                Objects.equals(correctAnswer, that.correctAnswer) &&
                Objects.equals(studentAnswer, that.studentAnswer) &&
                Objects.equals(borderClass, that.borderClass) &&
                Objects.equals(statusClass, that.statusClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, questionText, questionType, options, correctAnswer, 
                           studentAnswer, isCorrect, borderClass, statusClass);
    }

    @Override
    public String toString() {
        return "QuizQuestion{" +
                "questionId=" + questionId +
                ", questionText='" + questionText + '\'' +
                ", questionType='" + questionType + '\'' +
                ", options=" + options +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", studentAnswer='" + studentAnswer + '\'' +
                ", isCorrect=" + isCorrect +
                ", borderClass='" + borderClass + '\'' +
                ", statusClass='" + statusClass + '\'' +
                '}';
    }
} 