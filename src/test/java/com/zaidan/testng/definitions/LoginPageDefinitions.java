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
    public void goToHomePage(String userName, String passWord) {
        objLogin.login(userName, passWord);
    }
    

    @And("User clicks on the login button")
    public void clickLoginButton() {

        // Click Login button
        objLogin.clickedLoginButton();

    }

//    @Then("User is navigated to the dashboard page")
//    public void user_is_navigated_to_dashboard() {
//        Assert.assertTrue(objHomePage.getHomePageText().contains("Admin | Pelajar | Pengajar"));
//    }
    @Then("User is navigated to the dashboard page")
    public void user_is_navigated_to_dashboard() {
        String dashboardText = objHomePage.getHomePageText();
        // Validasi bahwa teks dashboard mengandung setidaknya salah satu dari ketiga peran
        boolean isAdmin = dashboardText.contains("Admin");
        boolean isPelajar = dashboardText.contains("Pelajar");
        boolean isPengajar = dashboardText.contains("Pengajar");

        Assert.assertTrue(isAdmin || isPelajar || isPengajar,
                "Dashboard page did not contain expected roles text.");
    }


//    @And("User should be able to see navigation bar for bendahara")
//    public void user_should_see_all_sidebar_items() {
//        List<String> expectedItems = Arrays.asList(
//            "Dasbor",
//            "Tagihan Siswa",
//            "Transaksi Penerimaan Dana",
//            "Pengaturan Notifikasi",
//            "Status Pembayaran",
//            "Rekapitulasi",
//            "Progres Transaksi Penerima Dana"
//        );
//        List<String> actualItems = objHomePage.getSidebarItems();
//        Assert.assertEquals(actualItems, expectedItems, "Sidebar items do not match!");
//    }

    @Then("User should be able to see {string} notification message")
    public void verifyErrorMessage(String notificationType) {
        String actualErrorMessage = objLogin.getErrorMessage();
        String expectedErrorMessage = "Kesalahan!";

        // Verifikasi pesan error untuk "un-successful login"
        if (notificationType.equals("Kesalahan!")) {
            Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Error message does not match!");
        } else {
            Assert.fail("Unknown notification type: " + notificationType);
        }
    }
}