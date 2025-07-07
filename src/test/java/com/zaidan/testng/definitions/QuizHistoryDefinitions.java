package com.zaidan.testng.definitions;

import com.zaidan.testng.actions.QuizHistoryPageActions;
import io.cucumber.java.en.*;
import org.testng.Assert;


public class QuizHistoryDefinitions {
    QuizHistoryPageActions quizHistoryActions = new QuizHistoryPageActions();

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
}
