package com.zaidan.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LogoutLocators {

    @FindBy(xpath = "/html/body/div[2]/main/div/div[1]/div/div/div[2]/button") 
    public WebElement logoutButton;

//    @FindBy(xpath = "/html/body/div[4]/div[2]/button[2]")
//    @FindBy(xpath = "/html/body/div/main/div/div[1]/div/div/div[2]/button")
//    @FindBy(xpath = "/html/body/div/main/div/div[1]/div/div/div[2]/button")
    @FindBy(xpath = "/html/body/div[3]/div[2]/button[2]")
    public WebElement confirmYesButton;
}