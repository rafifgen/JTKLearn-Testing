package com.zaidan.testng.actions;

import org.openqa.selenium.support.PageFactory;

import com.zaidan.testng.locators.LearnCoursePageLocators;
import com.zaidan.testng.utils.HelperClass;

public class LearnCoursePageActions {
    LearnCoursePageLocators learnCoursePageLocators = null;

    public LearnCoursePageActions() {
        this.learnCoursePageLocators = new LearnCoursePageLocators();
        PageFactory.initElements(HelperClass.getDriver(), this.learnCoursePageLocators);
    }

    public String getCourseTitle() {
        String courseTitle = null;
        try {
            courseTitle = learnCoursePageLocators.courseTitle.getText();
        } catch (Exception e) {
            System.err.println("Judul kursus tidak berhasil ditemukan");
        }
        return courseTitle;
    }

    public String getMaterialTitle() {
        String materialTitle = null;
        try {
            materialTitle = learnCoursePageLocators.materialTitle.getText();
        } catch (Exception e) {
            System.err.println("Judul materi tidak berhasil ditemukan");
        }
        return materialTitle;
    }

    public boolean verifyExampleVideoCanBePlayed() {
        String vidLink = learnCoursePageLocators.exampleVidIframe.getDomProperty("src");
        int embedWordPos = vidLink.indexOf("embed");

        if (embedWordPos != -1) {
            return true;
        } else {
            return false;
        }
    }

    public void clickExampleVidMaterial() {
        learnCoursePageLocators.exampleVidInNavBar.click();
    }

    public void playExampleVideo() {
        learnCoursePageLocators.exampleVidIframe.click();
    }

    public boolean verifyNextOrPrevButton() {
        boolean status;
        if (learnCoursePageLocators.nextButton != null || learnCoursePageLocators.prevButton != null) {
            status = true;
        } else {
            status = false;
        }
        return status;
    }
}
