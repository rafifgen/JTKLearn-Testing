package com.zaidan.testng.definitions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.zaidan.testng.actions.AccessQuizActionsRio;
import com.zaidan.testng.utils.HelperClass;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AccessQuizDefinitionsRio { // TODO: change this
    private AccessQuizActionsRio accessQuizActions = new AccessQuizActionsRio(); // TODO: change this
    private WebDriver driver;

    @And("User waits for {int} seconds")
    public void UserWaitsForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @And("User scrolls to course {string}")
    public void UserScrollsToCourse(String courseName) {
        driver = HelperClass.getDriver();
        WebElement courseElement = driver.findElement(
            By.xpath("//h6[normalize-space()='" + courseName + "']/ancestor::div[@class='card custom-card']")
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", courseElement);
        
        // Small delay after scroll
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @And("User is enrolled on the selected course")
    public void UserIsEnrolledOnCourse() {
        Assert.assertTrue(accessQuizActions.IsLanjutkanKursusButtonDisplayed(), "User belum terdaftar pada kursus");
    }

    @And("User clicks the lanjutkan kursus button")
    public void UserClicksTheLanjutkanKursusButton() {
        accessQuizActions.ClickLanjutkanKursusButton();
    }

    @When("User scrolls to the {string} button on the sidebar")
    public void UserScrollsToTheButtonOnTheSidebar(String buttonName) {
        driver = HelperClass.getDriver();
        WebElement sidebarButton = driver.findElement(
            By.xpath("//li[contains(@class, 'learn-list-item') and contains(., '" + buttonName + "')]")
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", sidebarButton);

        // Small delay after scroll
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @And("User clicks the {string} button on the sidebar")
    public void UserClicksTheButtonOnTheSidebar(String buttonName) {
        accessQuizActions.ClickSidebarButton(buttonName);
    }

    @Then("User should see the quiz title {string}")
    public void UserShouldSeeTheQuizTitle(String title) {
        Assert.assertTrue(accessQuizActions.IsQuizTitleDisplayed(title), "Judul kuis tidak tampil: " + title);
    }

    @And("User should see quiz result information with score {string}")
    public void UserShouldSeeQuizResultInformation(String score) {
        Assert.assertTrue(accessQuizActions.IsQuizResultDisplayed(score), "Hasil kuis tidak tampil dengan benar");
    }

    @And("User should see {string} button")
    public void UserShouldSeeButton(String buttonName) {
        Assert.assertTrue(accessQuizActions.IsButtonDisplayed(buttonName), "Tombol " + buttonName + " tidak ditemukan");
    }

    @And("User should see next or previous button")
    public void UserShouldSeeNextOrPreviousButton() {
        Assert.assertTrue(accessQuizActions.IsNextOrPreviousButtonDisplayed(), "Tombol berikutnya atau sebelumnya tidak ditemukan");
    }

    @And("User with email {string} should see the quiz {string} result information is the same as in the database")
    public void UserShouldSeeQuizResultInformationIsTheSameAsInDatabase(String email, String quizName) {
        Assert.assertTrue(accessQuizActions.IsQuizResultInfoTheSameAsInDatabase(email, quizName), "Informasi hasil kuis tidak sesuai dengan yang ada di database");
    }
}
