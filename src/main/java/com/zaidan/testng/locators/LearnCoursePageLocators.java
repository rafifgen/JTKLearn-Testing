package com.zaidan.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LearnCoursePageLocators {
    @FindBy(xpath = "/html/body/div/div/div/div/div[1]/nav/div/div/div/h4")
    public WebElement courseTitle;

    @FindBy(xpath = "/html/body/div/div/div/div/div[1]/nav/div/div/div/ul/li[1]")
    public WebElement exampleVidInNavBar;
    
    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[1]/div/iframe")
    public WebElement exampleVidIframe;

    @FindBy(xpath = "/html/body/div/div/div/div/div[1]/nav/div/div/div/ul/li[2]")
    public WebElement examplePDFInNavBar;

    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[1]/div/iframe")
    public WebElement examplePDF;

    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[1]/h3")
    public WebElement materialTitle;

    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[2]/button")
    public WebElement nextButton;

    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[2]/button[1]")
    public WebElement prevButton;
}
