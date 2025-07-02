package com.zaidan.testng.definitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;

import org.testng.Assert;

import com.zaidan.testng.locators.HomePageLocators;

public class DashboardDefinitions {
    HomePageLocators homePage = new HomePageLocators();

    @Then("User should see the welcome message for email {string}")
    public void userShouldSeeTheWelcomeMessage(String email) {
        // TODO: Implement this logic
        // 1. Query database about user's name by email
        // 2. Compare with string "Halo, {User's name}!"
    }

    @And("User should see the page title {string}")
    public void userShouldSeeThePageTitle(String expectedPageTitle) {
        if (homePage.homePageTitle != null) {
            String actualPageTitle = homePage.homePageTitle.getText();
            Assert.assertEquals(actualPageTitle, expectedPageTitle);
        }
    }

    @And("User should see all courses matching with the database records")
    public void userShouldSeeAllCourses() {
        // TODO: Implement this logic
        // 1. Query all courses from database
        // 2. Compare each attribute of each course in the user interface with the
        // actual
        // data from the database
    }

    @And("User should see the joined courses matching with the database records")
    public void userShouldSeeJoinedCourses() {
        // TODO: Implement this logic
        // 1. Query all courses from database
        // 2. Compare each attribute of each course in the user interface with the
        // actual
        // data from the database
    }
}
