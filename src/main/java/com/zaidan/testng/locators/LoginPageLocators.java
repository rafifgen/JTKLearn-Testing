package com.zaidan.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPageLocators {

    @FindBy(xpath = "/html/body/div/div/div/main/div/section/div/div/div/div[1]/div[5]/form/div[1]/div/input")
    public WebElement email;

    @FindBy(xpath = "/html/body/div/div/div/main/div/section/div/div/div/div[1]/div[5]/form/div[2]/input")
    public WebElement password;

//    @FindBy(xpath = "/html/body/div[2]/div/div[2]/div/div/form/button")
    @FindBy(xpath = "/html/body/div/div/div/main/div/section/div/div/div/div[1]/div[5]/form/div[3]/button")

    public WebElement login;

//    @FindBy(xpath = "/html/body/div[2]/div/div[2]/div/div/div[2]/div/p")
    @FindBy(xpath = "/html/body/div[2]/div/h2")
    public WebElement errorMessage;
}