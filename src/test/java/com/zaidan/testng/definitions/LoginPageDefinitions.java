package com.zaidan.testng.definitions;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import com.zaidan.testng.actions.HomePageActions;
import com.zaidan.testng.actions.LoginPageActions;
import com.zaidan.testng.utils.HelperClass;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginPageDefinitions {

    LoginPageActions objLogin = new LoginPageActions();
    HomePageActions objHomePage = new HomePageActions();

    @Given("User has opened the browser")
    public void user_has_opened_the_browser() {
        HelperClass.setUpDriver();
    }

    @And("User has navigated to the login page of JTK Learn app {string}")
    public void user_has_navigated_to_login_page(String url) {
        HelperClass.openPage(url);
    }

    @When("User enters username {string} and password {string}")
    public void go_to_home_page(String userName, String passWord) {
        objLogin.login(userName, passWord);
    }

    @And("User clicks on the login button")
    public void click_login_button() {

        // Click Login button
        objLogin.clickedLoginButton();

    }

    @Then("User is navigated to the dashboard page")
    public void user_is_navigated_to_dashboard() {
        String dashboardText = objHomePage.getHomePageText();
        // Validasi bahwa teks dashboard mengandung setidaknya salah satu dari ketiga
        // peran
        boolean isAdmin = dashboardText.contains("Admin");
        boolean isPelajar = dashboardText.contains("Pelajar");
        boolean isPengajar = dashboardText.contains("Pengajar");

        Assert.assertTrue(isAdmin || isPelajar || isPengajar,
                "Dashboard page did not contain expected roles text.");
    }

    @And("User should be able to see navigation bar for pelajar")
    public void user_should_see_all_sidebar_items() {
        List<String> expectedItems = Arrays.asList(
                "Beranda",
                "Kursus Saya",
                "Riwayat Kuis");
        List<String> actualItems = objHomePage.getSidebarItems();
        Assert.assertEquals(actualItems, expectedItems, "Sidebar items do not match!");
    }

    @And("User should be able to see photo and username")
    public void userShouldSeePhotoAndUsername() {
        // Cek foto user
        Assert.assertTrue(objHomePage.isUserPhotoDisplayed(), "User photo is not displayed");

        // Cek username
        Assert.assertTrue(objHomePage.isUsernameDisplayed(), "Username is not displayed");
    }

    // @Then("User should be able to see {string} notification message")
    // public void verifyErrorMessage(String notificationType) {
    // String actualErrorMessage = objLogin.getErrorMessage();
    // String expectedErrorMessage = "Kesalahan!";
    //
    // // Verifikasi pesan error untuk "un-successful login"
    // if (notificationType.equals("Kesalahan!")) {
    // Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Error message
    // does not match!");
    // } else {
    // Assert.fail("Unknown notification type: " + notificationType);
    // }
    // }

    @And("User clicks on Kursus Saya navigation")
    public void click_kursus_saya_navigation() {
        objHomePage.clickedKursusSayaNav();
    }

    @And("User clicks on Beranda navigation")
    public void click_beranda_navigation() {
        objHomePage.clickedBerandaNav();
    }

    @Then("Page title should be displayed")
    public void page_title_displayed() {
        String title = objHomePage.getCourseTitle();
        Assert.assertEquals(title, "Kursus", "The title is not match!");
    }

    @And("Course list created by the instructor should be visible")
    public void course_visible() {
        // Assert.assertTrue(objHomePage.isCourseListVisible(), "There's no course
        // displayed");

        List<String> listCourses = objHomePage.getCourses();
        Assert.assertFalse(listCourses.isEmpty(), "There's no course displayed");
    }

    @And("User clicks on username")
    public void click_on_username() {
        objHomePage.clickOnSubMenuUsername();
    }

    @And("Sub menu keluar is displayed")
    public void keluar_displayed() {
        Assert.assertTrue(objHomePage.isKeluarDisplayed(), "Sub menu Keluar is not displayed");
    }
}
