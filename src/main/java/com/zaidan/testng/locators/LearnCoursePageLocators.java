package com.zaidan.testng.locators;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LearnCoursePageLocators {

    @FindBy(xpath = "/html/body/div/div/div/div/div[1]/nav/div/div/div/h4")
    public WebElement courseTitle;

    @FindBy(xpath = "/html/body/div/div/div/div/div[1]/nav/div/div/div/ul/li[1]")
    public WebElement exampleVidInNavBar;

    // The @FindBy WebElement for the iframe itself (if needed elsewhere)
    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[1]/div/iframe")
    public WebElement exampleVidIframeWebElement;

    // The By locator for the iframe (recommended for ExpectedConditions.visibilityOfElementLocated)
    public By exampleVidIframeLocator = By.xpath("/html/body/div/div/div/div/div[2]/div[1]/div/iframe");

    // This targets the <video> element with its specific classes.
    public By html5VideoPlayer = By.cssSelector("video.video-stream.html5-main-video");

    @FindBy(xpath = "/html/body/div/div/div/div/div[1]/nav/div/div/div/ul/li[2]")
    public WebElement examplePDFInNavBar;

    // Verify if this XPath for PDF iframe is truly unique from video iframe
    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[1]/div/iframe")
    public WebElement examplePDF;

    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[1]/h3")
    public WebElement materialTitle;

    // Assuming these XPaths for navigation buttons are correct
    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[2]/button[2]") // Assuming Next is the second button
    public WebElement nextButton;

    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[2]/button[1]") // Assuming Prev is the first button
    public WebElement prevButton;
}