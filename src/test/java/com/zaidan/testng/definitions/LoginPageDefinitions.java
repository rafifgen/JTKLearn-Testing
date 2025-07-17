package com.zaidan.testng.definitions;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.zaidan.testng.actions.HomePageActions;
import com.zaidan.testng.actions.LoginPageActions;
import com.zaidan.testng.utils.ConfigReader;
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

    @And("The user is on the application login page")
    public void user_has_navigated_to_login_page() {
        // HelperClass.openPage(url);
        String url = ConfigReader.getProperty("app.url");
        WebDriver driver = HelperClass.getDriver();
        driver.get(url);
    }

    @When("User enters username {string} and password {string}")
    public void go_to_home_page(String userName, String passWord) {
        objLogin.login(userName, passWord);
        // Store email in shared context for other step definitions
        SharedContext.setStudentEmail(userName);
    }

    @And("User clicks on the login button")
    public void click_login_button() {

        // Click Login button
        objLogin.clickedLoginButton();

    }

    @Then("User is navigated to the dashboard page")
    public void user_is_navigated_to_dashboard() {
        try {
            // 1. Buat objek wait dengan durasi timeout (misalnya 10 detik)
            WebDriverWait wait = new WebDriverWait(HelperClass.getDriver(), Duration.ofSeconds(10));

            // 2. Perintahkan Selenium untuk menunggu sampai URL mengandung "dashboard-pelajar"
            wait.until(ExpectedConditions.urlContains("dashboard-pelajar"));

            // 3. (Opsional) Lakukan assertion tambahan jika diperlukan
            // Jika baris di atas berhasil, berarti URL sudah benar.
            String currentUrl = HelperClass.getDriver().getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("dashboard-pelajar"), "URL tidak mengandung 'dashboard-pelajar' setelah menunggu.");

        } catch (Exception e) {
            // Jika setelah 10 detik URL tidak juga berubah, tes akan gagal di sini.
            // Cetak URL terakhir untuk debugging.
            String finalUrl = HelperClass.getDriver().getCurrentUrl();
            Assert.fail("User tidak diarahkan ke halaman dashboard. URL terakhir: " + finalUrl, e);
        }
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
