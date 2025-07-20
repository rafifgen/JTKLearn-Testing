package com.zaidan.testng.actions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.PageFactory;

import com.zaidan.testng.dao.DetailHistoryQuizDAO;
import com.zaidan.testng.locators.AccessQuizLocatorsRio;
import com.zaidan.testng.model.DetailHistoryQuiz;
import com.zaidan.testng.model.HistoryQuiz;
import com.zaidan.testng.utils.DatabaseUtil;
import com.zaidan.testng.utils.HelperClass;

public class AccessQuizActionsRio { // TODO: change this
    AccessQuizLocatorsRio accessQuizLocatorsRio = null; // TODO: change this
    private DetailHistoryQuizDAO detailHistoryQuizDAO = new DetailHistoryQuizDAO();

    public AccessQuizActionsRio() {
        this.accessQuizLocatorsRio = new AccessQuizLocatorsRio();
        PageFactory.initElements(HelperClass.getDriver(), accessQuizLocatorsRio);
    }

    public boolean IsLanjutkanKursusButtonDisplayed() {
        try {
            return accessQuizLocatorsRio.continueCourseButton.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void ClickLanjutkanKursusButton() {
        try {
            accessQuizLocatorsRio.continueCourseButton.click();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Tombol Lanjutkan Kursus tidak ditemukan");
        }
    }

    public void ClickSidebarButton(String buttonName) {
        try {
            WebElement sidebarButton = HelperClass.getDriver().findElement(
                    By.xpath(
                            "//ul[@class='learn-list mt-1']//li[contains(@class, 'learn-list-item') and normalize-space(text())='"
                                    + buttonName + "']"));

            sidebarButton.click();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Tombol " + buttonName + " tidak ditemukan di sidebar");
        }
    }

    public boolean IsQuizTitleDisplayed(String title) {
        try {
            WebElement quizTitle = HelperClass.getDriver().findElement(
                    By.xpath("//div[@class='quiz-title']//h3//b[normalize-space(text())='" + title + "']"));

            return quizTitle.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean IsQuizResultDisplayed(String score) {
        try {
            WebElement resultElement, reviewElement;
            int scoreValue = Integer.parseInt(score);
            if (scoreValue == 100){
                resultElement = accessQuizLocatorsRio.successfulResultElement;
                reviewElement = accessQuizLocatorsRio.perfectReviewElement;
            } else if (scoreValue < 100 && scoreValue >= 80) {
                resultElement = accessQuizLocatorsRio.successfulResultElement;
                reviewElement = accessQuizLocatorsRio.reviewElement;
            } else {
                resultElement = accessQuizLocatorsRio.failedResultElement;
                reviewElement = accessQuizLocatorsRio.reviewElement;
            }

            boolean validScoreElement = accessQuizLocatorsRio.scoreElement.isDisplayed()
                    && accessQuizLocatorsRio.scoreElement.getText().contains("soal dengan benar : " + score);

            return validScoreElement && resultElement.isDisplayed() &&
                    accessQuizLocatorsRio.minScoreElement.isDisplayed()
                    && reviewElement.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean IsButtonDisplayed(String buttonName) {
        try {
            WebElement button = HelperClass.getDriver().findElement(
                    By.xpath("//button[normalize-space(text())='" + buttonName + "']"));

            return button.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean IsNextOrPreviousButtonDisplayed() {
        boolean isNextButtonDisplayed = false;
        boolean isPreviousButtonDisplayed = false;

        // Next Button
        try {
            WebElement nextButton = HelperClass.getDriver().findElement(
                    By.xpath("//button[contains(@class, 'btn course-next-button')]"));
            isNextButtonDisplayed = nextButton.isDisplayed();
        } catch (NoSuchElementException e) {
            isNextButtonDisplayed = false;
        }
        
        // Previous Button
        try {
            WebElement previousButton = HelperClass.getDriver().findElement(
                    By.xpath("//button[contains(@class, 'btn course-prev-button')]"));
            isPreviousButtonDisplayed = previousButton.isDisplayed();
        } catch (NoSuchElementException e) {
            isPreviousButtonDisplayed = false;
        }

        return isNextButtonDisplayed || isPreviousButtonDisplayed;
    }

    public boolean IsQuizResultInfoTheSameAsInDatabase(String email, String quizName) {
        try {
            // Values retrieved from the database
            HistoryQuiz historyQuiz = GetHistoryQuiz(email, quizName);
            List<DetailHistoryQuiz> correctAnswers = detailHistoryQuizDAO
                    .GetCorrectAnswersByIdHistoryQuiz(historyQuiz.getIdHistoryQuiz());
            List<DetailHistoryQuiz> totalQuestions = detailHistoryQuizDAO
                    .GetAllQuestionsByIdHistoryQuiz(historyQuiz.getIdHistoryQuiz());

            // Displayed elements on the web
            WebElement displayedQuizTitle = HelperClass.getDriver().findElement(
                    By.xpath("//div[@class='quiz-title']//h3//b[normalize-space(text())='" + quizName + "']"));
            String[] displayedScore = accessQuizLocatorsRio.scoreElement.getText().split("\s"); // Kamu menjawab {m} dari {n} soal dengan benar : {skor}
            int displayedCorrectAnswersCount = Integer.parseInt(displayedScore[2]);
            int displayedTotalQuestionsCount = Integer.parseInt(displayedScore[4]);
            float displayedScoreValue = Float.parseFloat(displayedScore[9]);

            boolean isPassed = displayedQuizTitle.isDisplayed() && displayedQuizTitle.getText().equals(quizName)
                    && displayedCorrectAnswersCount == correctAnswers.size()
                    && displayedTotalQuestionsCount == totalQuestions.size()
                    && displayedScoreValue == historyQuiz.getNilai();

            return isPassed;
        } catch (Exception e) {
            throw new RuntimeException("Error while checking quiz result information: " + e.getMessage(), e);
        }
    }

    public HistoryQuiz GetHistoryQuiz(String email, String quizName) {
        // Get id_user from users table using email
        int idUser = 0;
        String userSql = "SELECT id_user FROM users WHERE email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(userSql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idUser = rs.getInt("id_user");
                } else {
                    throw new NoSuchElementException("User not found with email: " + email);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching id_user: " + e.getMessage());
            throw new RuntimeException(e);
        }

        // Get id_pelajar from pelajar table using id_user
        int idPelajar = 0;
        String pelajarSql = "SELECT id_pelajar FROM pelajar WHERE id_user = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(pelajarSql)) {
            pstmt.setInt(1, idUser);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idPelajar = rs.getInt("id_pelajar");
                } else {
                    throw new NoSuchElementException("Pelajar not found with id_user: " + idUser);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching id_pelajar: " + e.getMessage());
            throw new RuntimeException(e);
        }

        // Get id_quiz from quiz table using quiz name
        int idQuiz = 0;
        String quizSql = "SELECT id_quiz FROM quiz WHERE nama_quiz = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(quizSql)) {
            pstmt.setString(1, quizName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idQuiz = rs.getInt("id_quiz");
                } else {
                    throw new NoSuchElementException("Quiz not found with name: " + quizName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching id_quiz: " + e.getMessage());
            throw new RuntimeException(e);
        }

        String sql = "SELECT * FROM \"historyQuiz\" WHERE id_pelajar = ? AND id_quiz = ? " +
                "ORDER BY waktu_selesai DESC LIMIT 1";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPelajar);
            pstmt.setInt(2, idQuiz);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new HistoryQuiz(
                            rs.getInt("id_history_quiz"),
                            rs.getInt("id_pelajar"),
                            rs.getInt("id_quiz"),
                            rs.getTimestamp("waktu_mulai"),
                            rs.getTimestamp("waktu_selesai"),
                            rs.getFloat("nilai"));
                } else {
                    throw new NoSuchElementException(
                            "HistoryQuiz not found for id_pelajar: " + idPelajar + " and id_quiz: " + idQuiz);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching HistoryQuiz: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}