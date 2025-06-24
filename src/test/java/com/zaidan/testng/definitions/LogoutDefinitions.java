package com.zaidan.testng.definitions;

import com.zaidan.testng.actions.LoginPageActions;
import com.zaidan.testng.actions.LogoutActions;
import com.zaidan.testng.actions.HomePageActions;
import com.zaidan.testng.utils.HelperClass;
import io.cucumber.java.en.*;
import org.testng.Assert;

public class LogoutDefinitions {

    LoginPageActions objLogin = new LoginPageActions();
    HomePageActions objHomePage = new HomePageActions();
    LogoutActions objLogout = new LogoutActions();

    @Given("User has successfully logged in as {string} with password {string}")
    public void user_has_successfully_logged_in(String username, String password) {
        HelperClass.setUpDriver();
        HelperClass.openPage("http://ptbsp.ddns.net:6882");
        objLogin.login(username, password);
        objLogin.clickedLoginButton();
        Assert.assertTrue(objHomePage.getHomePageText().contains("Dasbor - Bendahara"));
    }

    @And("User is on the Dasbor page Education Fund Payment Management System for Zaidan Educare School app {string}")
    public void user_is_on_dasbor_page(String url) {
        Assert.assertTrue(HelperClass.getDriver().getCurrentUrl().contains(url));
        Assert.assertTrue(objHomePage.getHomePageText().contains("Dasbor - Bendahara"));
    }

    @When("User clicks on logout button")
    public void user_clicks_on_logout_button() {
        objLogout.clickLogoutButton();
    }

    @And("User clicks yes button on logout confirmation pop up")
    public void user_clicks_yes_on_logout_confirmation_pop_up() {
        objLogout.clickYesOnConfirmation();
    }

    @Then("User should be redirected to the login page")
    public void user_should_be_redirected_to_the_login_page() {
        Assert.assertTrue(HelperClass.getDriver().getCurrentUrl().contains("/login"));
    }
}