package com.zaidan.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CourseDetailsPageLocators {

    @FindBy(className = "course-title")
    public WebElement courseTitle;

    @FindBy(className = "image-preview")
    public WebElement courseImage;

    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[2]/div[3]")
    public WebElement instructorName;

    @FindBy(className = "progress-bar-fill")
    public WebElement progressBar;

    @FindBy(className = "progress-percentage-text")
    public WebElement progressPercentageText;
}