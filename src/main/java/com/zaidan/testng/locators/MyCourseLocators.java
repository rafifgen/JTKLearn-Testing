package com.zaidan.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

public class MyCourseLocators {
    // Navigation
    public static final By MY_COURSE_MENU = By.xpath("//a[contains(@class, 'nav-link') and text()='Kursus Saya']");

    // Tabs
    public static final By PROGRESS_TAB = By.id("inprogress-tab");
    public static final By COMPLETED_TAB = By.id("completed-tab");
    public static final By ACTIVE_TAB = By.cssSelector(".nav-link-tab[aria-selected='true']");

    // Course Card
    public static final By COURSE_CARDS = By.cssSelector("#completed .custom-card");

    // Element di dalam course card - FIXED
    public static final By COURSE_IMAGE = By.cssSelector("img.card-img-top.custom");
    public static final By COURSE_NAME = By.cssSelector(".card-title");
    public static final By INSTRUCTOR_NAME = By.cssSelector(".card-text");
    public static final By PROGRESS_BAR = By.cssSelector(".progress-bar-fill");
    public static final By PROGRESS_PERCENT = By.cssSelector(".progress-percentage");

    // Empty State Messages
    public static final By EMPTY_STATE_MESSAGE_IN_PROGRESS = By.xpath("//div[@id='inprogress']//p[contains(text(), 'Belum ada kursus yang sedang dijalani.')]");
    public static final By EMPTY_STATE_MESSAGE_COMPLETED = By.xpath("//div[@id='completed']//p[contains(text(), 'Belum ada kursus yang selesai.')]");

    //get loggedin username
    public static final By LOGGED_IN_USERNAME = By.cssSelector("li.nav-name.dropdown > a.nav-link"); // Assuming the username is displayed in a navbar text element
}
