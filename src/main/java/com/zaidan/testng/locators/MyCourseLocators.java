package com.zaidan.testng.locators;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class MyCourseLocators {
    // Tabs
    @FindBy(id = "inprogress-tab")
    public WebElement progressTab;

    @FindBy(id = "completed-tab")
    public WebElement completedTab;

    @FindBy(css = ".nav-link-tab[aria-selected='true']")
    public WebElement activeTab;

    // Course Card
    @FindBy(css = "#completed .custom-card")
    public List<WebElement> completedcourseCards;

    @FindBy(css = "#inprogress .custom-card")
    public List<WebElement> inProgressCourseCards;

    // Element di dalam course card - FIXED
    @FindBy(css = "img.card-img-top.custom")
    public WebElement courseImage;

    @FindBy(css = ".card-title")
    public WebElement courseName;

    @FindBy(css = ".card-text")
    public WebElement instructorName;

    @FindBy(css = ".progress-bar-fill")
    public WebElement progressBar;

    @FindBy(css = "span.progress-percentage")
    public WebElement progressPercent;

    // Empty State Messages
    @FindBy(xpath = "//div[@id='inprogress']//p[contains(text(), 'Belum ada kursus yang sedang dijalani.')]")
    public WebElement emptyStateMessageInProgress;

    @FindBy(xpath = "//div[@id='completed']//p[contains(text(), 'Belum ada kursus yang selesai.')]")
    public WebElement emptyStateMessageCompleted;

    // get logged-in username
    @FindBy(css = "li.nav-name.dropdown > a.nav-link")
    public WebElement loggedInUsername;


    // Navigation
    public static final By MY_COURSE_MENU = By.xpath("//a[contains(@class, 'nav-link') and text()='Kursus Saya']");

    // Tabs
    public static final By PROGRESS_TAB = By.id("inprogress-tab");
    public static final By COMPLETED_TAB = By.id("completed-tab");
    public static final By ACTIVE_TAB = By.cssSelector(".nav-link-tab.active");


    // Course Card
    public static final By COURSE_CARDS = By.cssSelector(".tab-pane.active .custom-card");

    // Element di dalam course card - FIXED
    public static final By COURSE_IMAGE = By.cssSelector("img.card-img-top.custom");
    public static final By COURSE_NAME = By.cssSelector(".card-title");
    public static final By INSTRUCTOR_NAME = By.cssSelector(".card-text");
    public static final By PROGRESS_BAR = By.cssSelector(".progress-bar-fill");
    public static final By PROGRESS_PERCENT = By.cssSelector("span.progress-percentage");

    // Empty State Messages
    public static final By EMPTY_STATE_MESSAGE_IN_PROGRESS = By.xpath("//div[@id='inprogress']//p[contains(text(), 'Belum ada kursus yang sedang dijalani.')]");
    public static final By EMPTY_STATE_MESSAGE_COMPLETED = By.xpath("//div[@id='completed']//p[contains(text(), 'Belum ada kursus yang selesai.')]");

    //get loggedin username
    public static final By LOGGED_IN_USERNAME = By.cssSelector("li.nav-name.dropdown > a.nav-link"); // Assuming the username is displayed in a navbar text element
}
