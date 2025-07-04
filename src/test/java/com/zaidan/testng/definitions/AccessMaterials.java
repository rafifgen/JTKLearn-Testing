package com.zaidan.testng.definitions;

import java.sql.SQLException;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.zaidan.testng.actions.CourseDetailsPageActions;
import com.zaidan.testng.actions.HomePageActions;
import com.zaidan.testng.actions.LearnCoursePageActions;
import com.zaidan.testng.dao.CourseDAO;
import com.zaidan.testng.dao.MateriDAO;
import com.zaidan.testng.model.Materi;
import com.zaidan.testng.model.Course;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AccessMaterials {
    HomePageActions homePageActions = new HomePageActions();
    CourseDetailsPageActions courseDetailsPageActions = new CourseDetailsPageActions();
    LearnCoursePageActions learnCoursePageActions = new LearnCoursePageActions();
    MateriDAO materiDAO = new MateriDAO();
    CourseDAO courseDAO = new CourseDAO();
    int courseLookupId = 0;
    int materialLookupId = 0;

    @And("User clicks on course {string}")
    public void userClicksOnCourse(String courseName) {
        homePageActions.selectCourseByName(courseName);
    }

    @And("User clicks on the continue button")
    public void userClicksContinue() {
        courseDetailsPageActions.continueCourse();
    }

    @When("User clicks on one of the video in the navigation bar")
    public void userClicksOnOneVideo() {
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
        boolean vidPlayableStatus = learnCoursePageActions.verifyExampleVideoCanBePlayed();
        Assert.assertTrue(vidPlayableStatus, "The video cannot be played because it doesn't contain the embed link");
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

    @And("User clicks on the example PDF material")
    public void userClicksOnPDFMaterial() {
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
}