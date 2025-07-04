package com.zaidan.testng.definitions;

import java.sql.SQLException;

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

    @And("User should be able to see the material name with material id {int}")
    public void userSeesMaterialName(int idMateri) {
        Materi dbMateri = materiDAO.getMateriById(idMateri);
        String uiMaterialName = learnCoursePageActions.getMaterialTitle();
        String dbMaterialName = dbMateri.getNamaMateri();

        Assert.assertNotNull(uiMaterialName);
        Assert.assertEquals(uiMaterialName, dbMaterialName);
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

    @And("The course name of id {int} should be the same as in the database")
    public void uiCourseNameEqualsDBCourseName(int idCourse) throws SQLException {
        Course dbCourseById = courseDAO.getCourseById(idCourse);
        String dbCourseName = dbCourseById.getNamaCourse();
        String uiCourseName = learnCoursePageActions.getCourseTitle();
        Assert.assertEquals(uiCourseName, dbCourseName);
    }
}