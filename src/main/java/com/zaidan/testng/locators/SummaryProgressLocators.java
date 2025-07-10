package com.zaidan.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SummaryProgressLocators {
    @FindBy(xpath = "//*[@id=\"root\"]/div/div/h3")
    public WebElement pageTitle;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div/div[1]/h3")
    public WebElement pageSubtitle;
}
