package com.zaidan.testng.definitions;

import java.sql.Timestamp;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Instant;

import org.joda.time.DateTime;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.zaidan.testng.actions.CourseDetailsPageActions;
import com.zaidan.testng.actions.HomePageActions;
import com.zaidan.testng.actions.LearnCoursePageActions;
import com.zaidan.testng.dao.CourseDAO;
import com.zaidan.testng.dao.HistoryMateriDAO;
import com.zaidan.testng.dao.MateriDAO;
import com.zaidan.testng.model.Materi;
import com.zaidan.testng.model.Course;
import com.zaidan.testng.model.HistoryMateri;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AccessMaterials {
    HomePageActions homePageActions = new HomePageActions();
    CourseDetailsPageActions courseDetailsPageActions = new CourseDetailsPageActions();
    MateriDAO materiDAO = new MateriDAO();
    int materialLookupId = 0;

    // DAOs and Page Actions
    LearnCoursePageActions learnCoursePageActions = new LearnCoursePageActions();
    HistoryMateriDAO historyMateriDAO = new HistoryMateriDAO();
    CourseDAO courseDAO = new CourseDAO();
    
    // Variables to hold state between steps
    private double initialDbProgress;
    private double initialUiProgress;
    private Timestamp actualStartTimeFromDB;

    // Define the constant IDs for clarity
    private int currentMaterialId; 
    private final int courseLookupId = 4;
    private final int videoMaterialLookupId = 9;
    private final int pdfMaterialLookupId = 10;
    private final int studentLookupId = 1;

    @Before(value = "@ResetMaterialState", order = 1)
    public void resetMaterialStateBeforeScenario() {
        System.out.println("--- @Before Hook: Resetting material state ---");
        // We reset both video and PDF materials to be safe
        historyMateriDAO.resetFinishTimeForMaterial(this.studentLookupId, this.videoMaterialLookupId);
        historyMateriDAO.resetFinishTimeForMaterial(this.studentLookupId, this.pdfMaterialLookupId);
        System.out.println("--- @Before Hook: Reset complete ---");
    }

    @And("User clicks on course {string}")
    public void userClicksOnCourse(String courseName) {
        homePageActions.selectCourseByName(courseName);
    }

    @And("User clicks on the continue button")
    public void userClicksContinue() {
        courseDetailsPageActions.continueCourse();
    }

    @And("The initial progress is tracked")
    public void userRecordsInitialProgress() {
        System.out.println("Recording initial progress state...");
        this.initialDbProgress = courseDAO.getCourseProgressByStudentAndCourseId(studentLookupId, courseLookupId);
        this.initialUiProgress = learnCoursePageActions.getProgressBarPercentage();
        System.out.println("Initial DB Progress: " + this.initialDbProgress + "% | Initial UI Progress: " + this.initialUiProgress + "%");
    }

    @When("User clicks on one of the video in the navigation bar")
    public void userClicksOnOneVideo() {
        // Set the context to the video material
        this.currentMaterialId = this.videoMaterialLookupId;
        System.out.println("Setting current material ID to: " + this.currentMaterialId + " (Video)");
        learnCoursePageActions.clickExampleVidMaterial();
    }

    @Then("User should be able to see the page title {string}")
    public void userSeesPageTitle(String courseName) {
        // Course Title
        String uiCourseTitle = courseDetailsPageActions.getCourseTitle();
        String dbCourseTitle = courseName; 
        Assert.assertNotNull(uiCourseTitle);
        Assert.assertEquals(uiCourseTitle, dbCourseTitle);
    }

    @And("User should be able to play the video")
    public void userPlaysVideo() {
        System.out.println("Step: User should be able to play the video");
        learnCoursePageActions.playExampleVideo();
    }


    @And("User should be able to see the next or previous button")
    public void userSeesTheNextPrevButton() {
        Assert.assertTrue(learnCoursePageActions.verifyNextOrPrevButton());
    }

    @And("The course name of id {int} and material name of id {int} should be the same as in the database")
    public void uiCourseNameEqualsDBCourseName(int idCourse, int idMateri) throws SQLException {
        // Compare course title
        Course dbCourseById = courseDAO.getCourseById(idCourse);
        String dbCourseName = dbCourseById.getNamaCourse();
        String uiCourseName = learnCoursePageActions.getCourseTitle();
        Assert.assertEquals(uiCourseName, dbCourseName);

        // Compare material name
        Materi dbMaterialById = materiDAO.getMateriById(idMateri);
        String dbMaterialName = dbMaterialById.getNamaMateri();
        String uiMaterialName = learnCoursePageActions.getMaterialTitle();
        Assert.assertEquals(uiMaterialName, dbMaterialName);
    }

    @When("User clicks on the example PDF material")
    public void userClicksOnPDFMaterial() {
        // Set the context to the PDF material
        this.currentMaterialId = this.pdfMaterialLookupId;
        System.out.println("Setting current material ID to: " + this.currentMaterialId + " (PDF)");
        learnCoursePageActions.clickExamplePDFMaterial();
    }


    @And("User should be able to read the PDF file with material id {int}")
    public void userReadsPDFFile(int materialId) {
        WebElement uiPDFElement = learnCoursePageActions.getExamplePDFMaterial();
        String uiPDFKontenMaterial = uiPDFElement.getDomProperty("src");
        uiPDFKontenMaterial = uiPDFKontenMaterial.substring(uiPDFKontenMaterial.indexOf("materials/") + 10);
        Materi dbPDFMaterial = materiDAO.getMateriById(materialId);
        String dbPDFKontenMaterial = dbPDFMaterial.getKontenMateri();

        Assert.assertNotNull(uiPDFKontenMaterial);
        Assert.assertEquals(uiPDFKontenMaterial, dbPDFKontenMaterial);
    }

    @And("User moves to the next page right after {int} minutes")
    public void userMovesToNextPage(int duration) {
        // --- THIS IS THE "SKIP" ---
        historyMateriDAO.setFinishTimeAfterDuration(this.studentLookupId, this.currentMaterialId, duration);

        // Refresh the page to make the UI reflect the database change
        System.out.println("Refreshing the page to load updated progress...");
        learnCoursePageActions.refreshPage(); // You will need to add this method

        // We no longer need to click the next button because the goal
        // is to verify the progress update, which the refresh triggers.
        learnCoursePageActions.goToNextPage(); 
    }
 
    @Then("System should track the time at which the material was started")
    public void systemTrackTimeMaterialStarted() throws SQLException {
        int idPelajar = 1;
        int idMateri = 10;
        Instant nowInstant = Instant.now();
        Timestamp startingTime = Timestamp.from(nowInstant);
        Timestamp dbStartingTime = historyMateriDAO.getStartingTime(idPelajar, idMateri);

        Assert.assertNotNull(dbStartingTime, "DB Starting time wasn't successfully retrieved.");
        Assert.assertEquals(startingTime, dbStartingTime);
    }

    @And("System should track the time at which the material was finished; start time + 5 minutes")
    public void systemTrackFinishTime() throws SQLException {
        int idPelajar = 1;
        int idMateri = 10;
        Instant nowInstant = Instant.now();
        Timestamp finishTime = Timestamp.from(nowInstant);
        HistoryMateri historyMateri = historyMateriDAO.getHistoryMateri(idPelajar, idMateri);
        Timestamp dbFinishTime = historyMateri.getWaktuSelesai();

        Assert.assertNotNull(dbFinishTime);
        Assert.assertEquals(finishTime, dbFinishTime);
    }

    @And("The course percentage is set to increase to {float}")
    public void setCourseProgress(float percentage) {
        int idPelajar = 1;
        int idCourse = 4;
        courseDAO.setCourseProgressByStudentAndCourseId(idPelajar, idCourse, percentage);
    }
    // --- VIDEO-SPECIFIC VERIFICATION STEP ---
    
    @Then("The system correctly tracks video material completion for {int} minutes")
    public void theSystemCorrectlyTracksVideoCompletion(int duration) throws SQLException {
        System.out.println("Verifying VIDEO material completion...");
        
        // 1. Verify start and finish times in the database
        verifyMaterialStartTime();
        verifyMaterialFinishTime(duration, materialLookupId);

        // 2. Verify the database progress percentage was updated
        theDbPercentageIsUpdated();

        // 3. Verify the UI progress bar and text were updated
        theUiProgressBarIsUpdated();

        // 4. Verify the navigation item is now highlighted in green
        theCompletedVideoMaterialIsHighlightedInGreen();
    }


    @Then("The system correctly tracks PDF material completion for {int} minutes")
    public void theSystemCorrectlyTracksPDFCompletion(int duration) throws SQLException {
        System.out.println("Verifying PDF material completion...");
        verifyMaterialStartTime();
        verifyMaterialFinishTime(duration, this.materialLookupId);
        theDbPercentageIsUpdated();
        theUiProgressBarIsUpdated();
        theCompletedPDFMaterialIsHighlightedInGreen();
    }


    /**
     * Verifies that the completion percentage in the database has increased.
     */
    @And("The course completion percentage in the database is updated correctly")
    public void theDbPercentageIsUpdated() {
        double newDbProgress = courseDAO.getCourseProgressByStudentAndCourseId(studentLookupId, courseLookupId);
        System.out.println("Verifying DB progress. Old: " + initialDbProgress + "%, New: " + newDbProgress + "%");
        Assert.assertTrue(newDbProgress > this.initialDbProgress, "The database progress percentage did not increase.");
    }

    /**
     * Verifies that the progress bar on the UI has updated to reflect the new progress.
     */
    @And("The progress bar and percentage text on the UI are updated")
    public void theUiProgressBarIsUpdated() {
        double newUiProgress = learnCoursePageActions.getProgressBarPercentage();
        System.out.println("Verifying UI progress bar. Old: " + initialUiProgress + "%, New: " + newUiProgress + "%");
        Assert.assertTrue(newUiProgress > this.initialUiProgress, "The UI progress bar did not increase.");

        // Optional: You can also assert that the UI value matches the DB value.
        // The 'delta' of 0.1 handles any minor floating-point rounding differences.
        double newDbProgress = courseDAO.getCourseProgressByStudentAndCourseId(studentLookupId, courseLookupId);
        Assert.assertEquals(newUiProgress, newDbProgress, 0.1, "UI progress does not match DB progress.");
    }

    /**
     * Verifies that the navigation item for the completed material has turned green.
     */
    @And("The completed video material in the navigation bar is highlighted in green")
    public void theCompletedVideoMaterialIsHighlightedInGreen() {
        String navColor = learnCoursePageActions.getVidNavStatusColor();
        Assert.assertNotNull(navColor, "Could not determine the video nav item's color.");
        String expectedGreenColorRgb = "rgb(162, 245, 200)";
        Assert.assertEquals(navColor.toLowerCase().replace(" ", ""), expectedGreenColorRgb.replace(" ", ""), "The video navigation item was not highlighted in green.");
    }

    @And("The completed PDF material in the navigation bar is highlighted in green")
    public void theCompletedPDFMaterialIsHighlightedInGreen() {
        String navColor = learnCoursePageActions.getPDFNavStatusColor();
        Assert.assertNotNull(navColor, "Could not determine the PDF nav item's color.");
        String expectedGreenColorRgb = "rgb(162, 245, 200)";
        Assert.assertEquals(navColor.toLowerCase().replace(" ", ""), expectedGreenColorRgb.replace(" ", ""), "The PDF navigation item was not highlighted in green.");
    }

    @And("The initial course progress set to be 0")
    public void setInitialCourseProgressZero() {
        int idPelajar = 1;
        int idCourse = 4;
        courseDAO.setCourseProgressByStudentAndCourseId(idPelajar, idCourse, 0);
    }


    // --- PRIVATE HELPER METHODS FOR TIME VERIFICATION ---

    private void verifyMaterialStartTime() throws SQLException {
        long testStartTime = System.currentTimeMillis();
        this.actualStartTimeFromDB = historyMateriDAO.getStartingTime(studentLookupId, this.currentMaterialId);
        Assert.assertNotNull(this.actualStartTimeFromDB, "DB Starting time was not found in the database for material ID: " + this.currentMaterialId);

        long timeDifference = Math.abs(testStartTime - this.actualStartTimeFromDB.getTime());
        System.out.println("Start Time Difference (Test vs DB): " + timeDifference + " ms.");
        Assert.assertTrue(timeDifference < 15000, "The recorded start time is not close to the actual event time (within 15s).");
    }


    private void verifyMaterialFinishTime(int durationMinutes, int materialId) throws SQLException {
        Assert.assertNotNull(this.actualStartTimeFromDB, "Cannot verify finish time because start time was not retrieved.");

        long expectedFinishTimeMillis = this.actualStartTimeFromDB.getTime() + (long) durationMinutes * 60 * 1000;
        
        HistoryMateri historyMateri = historyMateriDAO.getHistoryMateri(studentLookupId, this.currentMaterialId);
        Timestamp actualFinishTimeFromDB = historyMateri.getWaktuSelesai();
        Assert.assertNotNull(actualFinishTimeFromDB, "DB Finish time was not found in the database for material ID: " + this.currentMaterialId);
        
        long timeDifference = Math.abs(expectedFinishTimeMillis - actualFinishTimeFromDB.getTime());

        System.out.println("Expected Finish Time (approx): " + new Timestamp(expectedFinishTimeMillis));
        System.out.println("Actual Finish Time (from DB):  " + actualFinishTimeFromDB);
        System.out.println("Finish Time Difference: " + timeDifference + " ms.");
        Assert.assertTrue(timeDifference < 5000, "The recorded finish time is not within the expected range (within 5s).");
    }
}