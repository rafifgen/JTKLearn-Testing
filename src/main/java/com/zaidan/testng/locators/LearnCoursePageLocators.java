package com.zaidan.testng.locators;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LearnCoursePageLocators {

    // More robust: Finds the h4 by its specific class.
    @FindBy(className = "course-title")
    public WebElement courseTitle;

    // More robust: Finds the h3 by its specific class.
    @FindBy(className = "material-title")
    public WebElement materialTitle;

    // More robust: Finds the list item that contains the unique text "Test Video Komgraf".
    // This is much better than relying on the order (li[1]).
    @FindBy(xpath = "//ul[contains(@class, 'learn-list')]//li[contains(., 'Test Video Komgraf')]")
    public WebElement exampleVidInNavBar;
    
    // More robust: Finds the list item containing the unique text "Test PDF Komgraf".
    @FindBy(xpath = "//*[@id=\"sidebarMenu\"]/div/ul/li[2]")
    public WebElement examplePDFInNavBar;

    public By contentIframe = By.cssSelector("div.content-box iframe");

    public By exampleVidIframeLocator = By.cssSelector("div.content-box iframe");
    
    // --- REVISED VIDEO PLAYER LOCATOR ---
    // This locator finds the big play button inside a YouTube embed.
    public By youTubePlayButton = By.cssSelector("button.ytp-large-play-button");
    
    // Robust selector for the "Selanjutnya" (Next) button.
    @FindBy(xpath = "//button[contains(@class, 'course-next-button')]")
    public WebElement nextButton;
    
    // Example for a "Previous" button if it exists
    @FindBy(xpath = "//button[contains(text(), 'Sebelumnya')]")
    public WebElement prevButton;

    // --- NEW LOCATORS FOR PROGRESS TRACKING ---

    // Locator for the progress bar element. We can get its value from 'aria-valuenow'.
    @FindBy(css = "div.progress-bar") 
    public WebElement progressBar;

    // Locator for the text displaying the percentage, e.g., "66.666664%"
    // This finds the span that is a sibling to the div with class 'progress'.
    @FindBy(xpath = "//div[@class='progress']/following-sibling::span")
    public WebElement percentageText;
}