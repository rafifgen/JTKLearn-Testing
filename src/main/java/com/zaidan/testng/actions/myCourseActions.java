package com.zaidan.testng.actions;

import com.zaidan.testng.locators.myCourseLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class myCourseActions {

    myCourseLocators locators;

    public myCourseActions(WebDriver driver) {
        this.locators = new myCourseLocators();
        PageFactory.initElements(driver, this.locators);
    }

    public boolean isTabDalamProgresActive() {
        return locators.tabDalamProgres.getAttribute("class").contains("active");
    }

    public void clickTabSelesai() {
        locators.tabSelesai.click();
    }

    public boolean isAnyCourseDisplayed() {
        return !locators.courseCards.isEmpty();
    }

    public String getCourseTitle() {
        return locators.courseTitle.getText();
    }

    public String getInstructorName() {
        return locators.instructorName.getText();
    }

    public String getProgressValue() {
        return locators.progressPercentage.getText();
    }
}