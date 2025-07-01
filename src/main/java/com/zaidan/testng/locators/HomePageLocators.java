package com.zaidan.testng.locators;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePageLocators {

//    @FindBy(xpath = "/html/body/div[2]/main/div/div[1]/div/div/div[1]/h1")
    @FindBy(xpath = "/html/body/div/div/div/div/div/h3")
    public WebElement homePageUserName;

//    @FindBy(xpath = "//ul[@data-sidebar='menu']/li/a/span")
    @FindBy(className = "nav-item")
    public List<WebElement> navBarItems;

    @FindBy(className = "profile-img")
    public WebElement userPhoto;

    @FindBy(xpath = "/html/body/div/div/div/nav/div/div/ul/li[4]/a")
    public WebElement username;

    @FindBy(xpath = "/html/body/div/div/div/nav/div/div/ul/li[2]")
    public WebElement kursusSayaNav;

    @FindBy(xpath = "/html/body/div/div/div/nav/div/div/ul/li[1]")
    public WebElement berandaNav;

    @FindBy(className = "courses-title")
    public WebElement courseTitle;

    @FindBy(xpath = "/html/body/div/div/div/div/div/div[2]")
    public List<WebElement> courses;
    @FindBy(xpath = "/html/body/div/div/div/nav/div/div/ul/li[4]")
    public WebElement subMenuUsername;

    @FindBy(xpath = "/html/body/div/div/div/nav/div/div/ul/li[4]/button")
    public WebElement subMenuKeluar;
}