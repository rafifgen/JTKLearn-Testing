package com.zaidan.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPageLocators {

    @FindBy(name = "username")
    public WebElement userName;

    @FindBy(name = "password")
    public WebElement password;

//    @FindBy(xpath = "/html/body/div[2]/div/div[2]/div/div/form/button")
    @FindBy(xpath = "/html/body/div/div/div[2]/div/div/form/button")

    public WebElement login;

//    @FindBy(xpath = "/html/body/div[2]/div/div[2]/div/div/div[2]/div/p")
    @FindBy(xpath = "/html/body/div/div/div[2]/div/div/div[2]/div/p")
    public WebElement errorMessage;
}