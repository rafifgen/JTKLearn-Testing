package com.zaidan.testng.actions;

import org.openqa.selenium.support.PageFactory;
import com.zaidan.testng.locators.LoginPageLocators;
import com.zaidan.testng.utils.HelperClass;

public class LoginPageActions {

    LoginPageLocators loginPageLocators = null;

    public LoginPageActions() {

        this.loginPageLocators = new LoginPageLocators();

        PageFactory.initElements(HelperClass.getDriver(), loginPageLocators);
    }

    public void login(String strUserName, String strPassword) {

        // Fill username
        loginPageLocators.email.sendKeys(strUserName);

        // Fill password
        loginPageLocators.password.sendKeys(strPassword);

    }

    public void clickedLoginButton() {
        // Click Login button
        loginPageLocators.login.click();
    }


    // Get the error message of Login Page
    public String getErrorMessage() {
        return loginPageLocators.errorMessage.getText();
    }

}