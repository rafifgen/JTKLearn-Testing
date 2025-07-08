package com.zaidan.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class QuizResultPageLocators {
    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[1]/div/div/div[2]/div/button[1]")
    public WebElement ulangiKuisButton;

    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[1]/div/div/div[2]/h3")
    public WebElement resultTitle;

    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[1]/div/div/div[2]/p[1]")
    public WebElement summaryText;

    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[1]/div/div/div[2]/p[2]")
    public WebElement passingMessage;
}