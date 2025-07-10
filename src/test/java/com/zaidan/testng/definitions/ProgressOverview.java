package com.zaidan.testng.definitions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.Assert;

import com.zaidan.testng.actions.HomePageActions;
import com.zaidan.testng.actions.SummaryCourseActions;
import com.zaidan.testng.actions.SummaryProgressActions;
import com.zaidan.testng.actions.SummaryQuizActions;
import com.zaidan.testng.actions.SummaryQuizDetailActions;
import com.zaidan.testng.dao.CourseDAO;
import com.zaidan.testng.dao.HistoryMateriDAO;
import com.zaidan.testng.dao.HistoryQuizDAO;
import com.zaidan.testng.dao.PelajarDAO;
import com.zaidan.testng.enums.TaskStatus;
import com.zaidan.testng.model.StudentProgressUI;
import com.zaidan.testng.utils.HelperClass;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ProgressOverview {
    HomePageActions homePageActions = new HomePageActions();
    SummaryCourseActions summaryCourseActions = new SummaryCourseActions();
    SummaryProgressActions summaryProgressActions = new SummaryProgressActions();
    SummaryQuizActions summaryQuizActions = new SummaryQuizActions();
    SummaryQuizDetailActions summaryQuizDetailActions = new SummaryQuizDetailActions();
    HistoryMateriDAO historyMateriDAO = new HistoryMateriDAO();
    CourseDAO courseDAO = new CourseDAO();
    HistoryQuizDAO historyQuizDAO = new HistoryQuizDAO();
    PelajarDAO pelajarDAO = new PelajarDAO();
    int idMateri9 = 9;
    int idMateri10 = 10;

    @And("User clicks on Pemantauan")
    public void userClicksOnProgressPage() {
        homePageActions.clickOnPemantauan();
    }

    @And("User clicks on Progres button of Komputer Grafik course")
    public void userClicksOnProgresBtn() {
        summaryCourseActions.clickOnKomputerGrafikProgresBtn();
    }


    @Then("The page title should be {string}")
    public void verifyPageTitle(String expectedPageSubtitle) {
        String actualPageSubtitle = summaryProgressActions.getPageTitle().getText();
        Assert.assertEquals(actualPageSubtitle, expectedPageSubtitle);
    }

    @And("The page subtitle should be the same with course with id {int}")
    public void verifyPageTitle(int idCourse) throws SQLException {
        String expectedPageTitle = courseDAO.getCourseById(idCourse).getNamaCourse();
        String actualPageTitle = summaryProgressActions.getPageSubtitle().getText();
        Assert.assertEquals(actualPageTitle, expectedPageTitle);
    }

    @And("Student of id {int} has not started yet material of id {int}")
    public void studentHasNotStartedMaterial(int idPelajar, int idMateri) throws SQLException {
        historyMateriDAO.resetMateriProgressByStudentAndMateriId(idPelajar, idMateri);
    }

    @And("Student of id {int} has not started yet quiz of id {int}")
    public void studentHasNotStartedQuiz(int idPelajar, int idQuiz) throws SQLException {
        historyQuizDAO.resetQuizProgressByStudentAndQuizId(idPelajar, idQuiz);
    }

    @And("The study progress of every student should be displayed")
    public void verifyDisplayedStudentProgressMatchesDatabase() throws SQLException {
        // Step 1: Get all the data from the UI table
        List<StudentProgressUI> uiProgressList = summaryProgressActions.getAllStudentProgressFromUI();
        Assert.assertFalse(uiProgressList.isEmpty(), "No student progress data was found on the page.");

        // Step 2: Loop through each student found on the UI
        for (StudentProgressUI uiProgress : uiProgressList) {
            String studentName = uiProgress.getStudentName();
            System.out.println("--- Verifying data for student: " + studentName + " ---");

            // Get the student's ID from the database to use for DB checks
            int studentId = pelajarDAO.getIdByName(studentName);
            Assert.assertNotEquals(studentId, -1, "Could not find ID for student: " + studentName);

            // --- Verify Overall Progress Percentage ---
            float dbProgress = courseDAO.getCourseProgressByStudentAndCourseId(studentId, 4); // Assuming course ID 4
            Assert.assertEquals(uiProgress.getProgressPercentage(), dbProgress, 0.1,
                "Overall progress mismatch for student: " + studentName);

            // Step 3: Loop through each task found for that student
            for (Map.Entry<String, TaskStatus> uiTaskEntry : uiProgress.getTaskStatuses().entrySet()) {
                String taskName = uiTaskEntry.getKey(); // e.g., "M10", "M9", or "Q7"
                TaskStatus uiStatus = uiTaskEntry.getValue();
                TaskStatus dbStatus = null;

                // Step 4: Use the task name to decide which DB check to run
                switch (taskName) {
                    case "M10":
                        dbStatus = historyMateriDAO.getDBMaterialStatus(studentId, 10, 2); // ID 10, requires 2 mins
                        break;
                    case "M9":
                        dbStatus = historyMateriDAO.getDBMaterialStatus(studentId, 9, 5); // ID 9, requires 5 mins
                        break;
                    case "Q7":
                        dbStatus = historyQuizDAO.getDBQuizStatus(studentId, 7, 80); // ID 7, requires 80 to pass
                        break;
                    default:
                        System.out.println("WARNING: Unknown task header '" + taskName + "' found on UI. Skipping verification.");
                        continue; // Skip to the next task
                }

                // Step 5: Assert that the UI status matches the DB status for that specific task
                Assert.assertEquals(uiStatus, dbStatus,
                    "Task '" + taskName + "' status mismatch for student: " + studentName);
            }
        }
    }

    @And("Student of id {int} finished material of id {int}")
    public void studentFinishedMaterial(int studentId, int materialId) {
        // We assume text material (ID 10) requires 2 minutes to be "finished".
        int requiredDuration = 5; 
        historyMateriDAO.setFinishTimeAfterDuration(studentId, materialId, requiredDuration);
    }

    /**
     * Sets a material to IN_PROGRESS using the new DAO method.
     */
    @And("Student of id {int} is still on progress in material of id {int}")
    public void studentIsOnProgressInMaterial(int studentId, int materialId) {
        historyMateriDAO.setMaterialInProgress(studentId, materialId);
    }

    /**
     * Sets a quiz to FINISHED with a passing score using the new DAO method.
     */
    @And("Student of id {int} finished quiz of id {int} with score {float}")
    public void studentFinishedQuiz(int studentId, int quizId, float passingScore) {
        // Set a high score to ensure it's considered "passed"
        historyQuizDAO.setQuizFinished(studentId, quizId, passingScore);
    }

    @And("The page should be reloaded first")
    public void reloadPage() {
        HelperClass.getDriver().navigate().refresh();
    }

    @And("The progress of student with id {int} is set to {float}")
    public void studentProgressSetTo(int idPelajar, float percentage) {
        courseDAO.setCourseProgressByStudentAndCourseId(idPelajar, 4, percentage);
    }

    @When("User clicks on Detail Kuis button of Komputer Grafik course")
    public void userClicksOnDetailKuis() {
        summaryCourseActions.clickOnKomputerGrafikDetailKuisBtn();
    }

    @And("User clicks on Lihat Hasil button of Tes Progres quiz")
    public void userClicksOnLihatHasil() {
        summaryQuizActions.clickOnLihatHasilBtn();
    }

    @And("User clicks on {string} sorting button")
    public void userClicksOnSortingButton(String sorting) {
        if (sorting.equals("ascending")) {
            summaryQuizDetailActions.selectSortOptionByVisibleText("A-Z");
        } else {
            summaryQuizDetailActions.selectSortOptionByVisibleText("Z-A");
        }
    }

    @Then("The displayed names should be sorted in {string} order")
    public void verifyDisplayedNames(String sorting) {
        if (sorting.equals("ascending")) {
            verifyAscendingSorting();
        } else {
            verifyDescendingSorting();
        }
    }

    @Then("The displayed names on Pemantau Progres Belajar page should be sorted in descending order")
    public void verifyPemantauProgresDescendingSorting() {
        // 1. Get the list of names from the UI
        List<String> uiNames = summaryProgressActions.getDisplayedStudentNames();
        Assert.assertFalse(uiNames.isEmpty(), "No student names were found on the page.");

        // 2. Create a copy and sort it in reverse alphabetical order (Z-A)
        List<String> sortedNames = new ArrayList<>(uiNames);
        sortedNames.sort(String.CASE_INSENSITIVE_ORDER.reversed());

        System.out.println("Original UI Order: " + uiNames);
        System.out.println("Expected Sorted Order (Desc): " + sortedNames);

        // 3. Assert that the original list matches the reverse-sorted copy
        Assert.assertEquals(uiNames, sortedNames, "The student names are not sorted in descending order.");
    }

    public void verifyAscendingSorting() {
        // 1. Get the list of names from the UI
        List<String> uiNames = summaryQuizDetailActions.getDisplayedStudentNames();
        Assert.assertFalse(uiNames.isEmpty(), "No student names were found on the page.");

        // 2. Create a copy and sort it alphabetically, ignoring case
        List<String> sortedNames = new ArrayList<>(uiNames);
        // --- THIS IS THE FIX ---
        sortedNames.sort(String.CASE_INSENSITIVE_ORDER);

        System.out.println("Original UI Order: " + uiNames);
        System.out.println("Expected Sorted Order: " + sortedNames);

        // 3. Assert that the lists are identical
        Assert.assertEquals(uiNames, sortedNames, "The student names are not sorted case-insensitively (A-Z).");
    }


    public void verifyDescendingSorting() {
        // 1. Get the list of names from the UI
        List<String> uiNames = summaryQuizDetailActions.getDisplayedStudentNames();
        Assert.assertFalse(uiNames.isEmpty(), "No student names were found on the page.");

        // 2. Create a copy and sort it in reverse alphabetical order, ignoring case
        List<String> sortedNames = new ArrayList<>(uiNames);
        // --- THIS IS THE FIX ---
        sortedNames.sort(String.CASE_INSENSITIVE_ORDER.reversed());

        System.out.println("Original UI Order: " + uiNames);
        System.out.println("Expected Sorted Order (Desc): " + sortedNames);

        // 3. Assert that the lists are identical
        Assert.assertEquals(uiNames, sortedNames, "The student names are not sorted case-insensitively (Z-A).");
    }
}