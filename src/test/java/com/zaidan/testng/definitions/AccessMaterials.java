package com.zaidan.testng.definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AccessMaterials {
    @And("User clicks on course {string}")
    public void userClicksOnCourse(String courseName) {
        // TODO: implement this one
    }

    @When("User clicks on one of the video in the navigation bar")
    public void userClicksOnOneVideo() {
        // TODO: implement this one
        // 1. locate the video material
        // 2. click on the video material
    }

    @Then("User should be able to see the page title that contains the material name {string}")
    public void userSeesPageTitle(String courseName) {
        // TODO: implement this one
        // 1. locate the page title
        // 2. verify the page title
    }
}
