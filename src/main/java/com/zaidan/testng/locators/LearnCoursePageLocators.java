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

    // Finds the first item in the list that has a "Materi" icon
    public By firstVideoInSidebar = By.xpath("(//ul[contains(@class, 'learn-list')]//li[.//img[@alt='Materi']])[1]");

    // Finds the second item in the list that has a "Materi" icon
    public By secondPdfInSidebar = By.xpath("(//ul[contains(@class, 'learn-list')]//li[.//img[@alt='Materi']])[2]");

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