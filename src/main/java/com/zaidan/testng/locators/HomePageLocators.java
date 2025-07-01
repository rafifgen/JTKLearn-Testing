package com.zaidan.testng.locators;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePageLocators {

    // @FindBy(xpath = "/html/body/div[2]/main/div/div[1]/div/div/div[1]/h1")
    @FindBy(xpath = "/html/body/div/div/div/div/div/h3")
    public WebElement homePageUserName;

    // @FindBy(xpath = "//ul[@data-sidebar='menu']/li/a/span")
    @FindBy(className = "nav-item")
    public List<WebElement> navBarItems;

    @FindBy(className = "profile-img")
    public WebElement userPhoto;

    @FindBy(xpath = "/html/body/div/div/div/nav/div/div/ul/li[4]/a")
    public WebElement username;

    @FindBy(xpath = "/html/body/div/div/div/div/div/div[1]/h3")
    public WebElement homePageTitle;

    @FindBy(css = ".row-custom-gap .custom-card")
    public List<WebElement> courseCards; // A list of all individual course cards

    // Locators *relative* to a course card (used within a found courseCard
    // WebElement)
    public By courseImage = By.cssSelector(".custom-card-img-top");
    public By courseName = By.cssSelector(".custom-card-title");
    public By instructorName = By.cssSelector(".custom-card-text");
}
