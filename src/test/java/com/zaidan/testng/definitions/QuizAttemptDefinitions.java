package com.zaidan.testng.definitions;

import com.zaidan.testng.actions.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class QuizAttemptDefinitions {
    HomePageActions homePageActions = new HomePageActions();
    CourseDetailsPageActions courseDetailsActions = new CourseDetailsPageActions();
    QuizResultPageActions resultActions = new QuizResultPageActions();
    QuizAttemptPageActions attemptActions = new QuizAttemptPageActions();

    @When("User selects the course {string} from the dashboard")
    public void user_selects_course_from_dashboard(String courseName) {
        homePageActions.selectCourseByName(courseName);
    }

    @And("User clicks the {string} button")
    public void user_clicks_the_button(String buttonText) {
        switch (buttonText.toLowerCase()) {
            case "lanjutkan kursus":
                courseDetailsActions.clickLanjutkanKursus();
                break;
            case "ulangi kuis":
                resultActions.clickUlangiKuis();
                break;
            default:
                Assert.fail("Button text '" + buttonText + "' is not recognized.");
        }
    }

    @And("User starts the quiz named {string}")
    public void user_starts_the_quiz_named(String quizName) {
        // Memanggil action baru untuk memulai kuis
        attemptActions.startQuiz(quizName);
    }


    @And("User answers the questions to get a score of 80")
    public void user_answers_questions() {
        attemptActions.answerQuestionsForScore80();
    }

    @And("User submits the quiz")
    public void user_submits_quiz() {
        attemptActions.clickSubmitButton();
    }

    @Then("The result page should show the title {string}")
    public void the_result_page_should_show_title(String expectedTitle) {
        Assert.assertEquals(resultActions.getPageTitle(), expectedTitle, "Judul halaman hasil kuis tidak sesuai.");
    }

    @And("The result page should confirm {string} and a score of {string}")
    public void the_result_page_should_confirm_score(String expectedSummary, String expectedScore) {
        String summaryText = resultActions.getSummaryText();
        Assert.assertTrue(summaryText.contains(expectedSummary), "Ringkasan jumlah soal tidak sesuai.");
        Assert.assertTrue(summaryText.contains(expectedScore), "Skor yang ditampilkan tidak sesuai.");
    }

    @And("The result page should show the passing message {string}")
    public void the_result_page_should_show_passing_message(String expectedMessage) {
        Assert.assertEquals(resultActions.getPassingMessage(), expectedMessage, "Pesan kelulusan tidak sesuai.");
    }
}