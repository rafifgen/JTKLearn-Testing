package com.zaidan.testng.actions;

import com.zaidan.testng.locators.LogoutLocators;
import com.zaidan.testng.utils.HelperClass;
import org.openqa.selenium.support.PageFactory;

public class LogoutActions {

    LogoutLocators logoutLocators = null;

    public LogoutActions() {
        this.logoutLocators = new LogoutLocators();
        PageFactory.initElements(HelperClass.getDriver(), logoutLocators);
    }

    public void clickLogoutButton() {
        logoutLocators.logoutButton.click();
    }

    public void clickYesOnConfirmation() {
        logoutLocators.confirmYesButton.click();
    }
}