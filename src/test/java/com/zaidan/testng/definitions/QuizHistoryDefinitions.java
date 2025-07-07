package com.zaidan.testng.definitions;

import com.zaidan.testng.actions.QuizHistoryPageActions;
import com.zaidan.testng.dao.QuizHistoryDAO;
import com.zaidan.testng.model.QuizAttemptDetails;
import io.cucumber.java.en.*;
import org.testng.Assert;

import java.util.List;


public class QuizHistoryDefinitions {
    QuizHistoryPageActions quizHistoryActions = new QuizHistoryPageActions();
    private QuizHistoryDAO quizHistoryDAO = new QuizHistoryDAO();

    @When("User navigates to the Quiz History page")
    public void user_navigates_to_the_quiz_history_page() {
        quizHistoryActions.navigateToQuizHistory();
    }

    @Then("The Quiz History page should display the title {string}")
    public void the_quiz_history_page_should_display_the_title(String expectedTitle) {
        String actualTitle = quizHistoryActions.getPageTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Judul halaman Riwayat Kuis tidak sesuai.");
    }

    @Then("The Quiz History page should display a message {string}")
    public void the_quiz_history_page_should_display_a_message(String expectedMessage) {
        String actualMessage = quizHistoryActions.getEmptyHistoryMessage();
        Assert.assertEquals(actualMessage, expectedMessage, "Pesan untuk riwayat kuis kosong tidak sesuai.");
    }

    @And("User is on the Quiz History page")
    public void user_is_on_the_quiz_history_page() {
        quizHistoryActions.navigateToQuizHistory();
    }

    @And("User clicks the detail button for the course {string}")
    public void user_clicks_the_detail_button_for_the_course(String courseName) {
        quizHistoryActions.clickDetailButtonForCourse(courseName);
    }

    @Then("The Sub Quiz History page should display the title {string}")
    public void the_sub_quiz_history_page_should_display_the_title(String expectedTitle) {
        String actualTitle = quizHistoryActions.getSubPageTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Judul sub-halaman Riwayat Kuis tidak sesuai.");
    }

    @Then("The subtitle for the course {string} should be visible")
    public void the_subtitle_for_the_course_should_be_visible(String courseName) {
        Assert.assertTrue(quizHistoryActions.isCourseSubtitleVisible(courseName),
                "Sub-judul untuk kursus '" + courseName + "' tidak terlihat.");
    }

    @Then("The history for {string} should show a message {string}")
    public void the_history_for_should_show_a_message(String courseName, String expectedMessage) {
        String actualMessage = quizHistoryActions.getNoAttemptsMessageForCourse(courseName);
        Assert.assertEquals(actualMessage, expectedMessage,
                "Pesan untuk kursus '" + courseName + "' tidak sesuai.");
    }

    @Then("The system should display a list of quizzes for {string}")
    public void the_system_should_display_a_list_of_quizzes(String courseName) {
        // Verifikasi sederhana bahwa setidaknya satu card riwayat kuis muncul.
        // Anda perlu membuat method isQuizHistoryVisible() di Actions Anda.
        // Contoh: return !quizHistoryLocators.quizHistoryCards.isEmpty();
        Assert.assertTrue(quizHistoryActions.isQuizHistoryVisible(), "Daftar riwayat kuis tidak ditampilkan untuk kursus " + courseName);
    }

    @Then("The quiz details displayed on the UI for student {string} and course {string} should match the database records")
    public void the_quiz_details_should_match_database(String studentEmail, String courseName) {
        // 1. Ambil data dari UI menggunakan Actions
        List<QuizAttemptDetails> uiHistory = quizHistoryActions.getDisplayedQuizHistory();
        Assert.assertFalse(uiHistory.isEmpty(), "Gagal mengambil data riwayat dari UI.");

        // 2. Ambil data dari Database menggunakan DAO
        List<QuizAttemptDetails> dbHistory = quizHistoryDAO.getQuizHistoryForStudent(studentEmail, courseName);
        Assert.assertFalse(dbHistory.isEmpty(), "Gagal mengambil data riwayat dari Database.");

        // 3. Bandingkan kedua list tersebut.
        // Perbandingan ini bisa berhasil karena kita sudah mendefinisikan
        // method equals() dan hashCode() di dalam model QuizAttemptDetails.
        Assert.assertEquals(uiHistory, dbHistory, "Data riwayat kuis antara UI dan database tidak cocok.");
    }

    @And("User searches for the quiz {string}")
    public void user_searches_for_the_quiz(String searchTerm) {
         // Simpan untuk verifikasi akhir
        quizHistoryActions.searchForQuiz(searchTerm);
    }

    @Then("The system should display a filtered list containing {string}")
    public void the_system_should_display_a_filtered_list_containing(String searchTerm) {
        List<QuizAttemptDetails> uiHistory = quizHistoryActions.getDisplayedQuizHistory();
        Assert.assertFalse(uiHistory.isEmpty(), "Hasil pencarian di UI kosong.");
        // Verifikasi bahwa hasil pertama mengandung kata kunci pencarian
        Assert.assertTrue(uiHistory.get(0).getQuizName().contains(searchTerm),
                "Hasil pertama pencarian tidak mengandung '" + searchTerm + "'.");
    }

    @And("The filtered quiz details for student {string} should match the database records for course {string} and search term {string}")
    public void the_filtered_quiz_details_should_match_database(String studentEmail, String courseName, String searchTerm) {
        // 1. Ambil data dari UI setelah difilter
        List<QuizAttemptDetails> uiHistory = quizHistoryActions.getDisplayedQuizHistory();

        // 2. Ambil data dari DB menggunakan method DAO yang baru (dengan filter)
        List<QuizAttemptDetails> dbHistory = quizHistoryDAO.getQuizHistoryForStudent(studentEmail, courseName, searchTerm);

        // 3. Bandingkan kedua list
        Assert.assertEquals(uiHistory, dbHistory, "Data hasil pencarian antara UI dan database tidak cocok.");
    }
}
