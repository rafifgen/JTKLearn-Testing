package com.zaidan.testng.actions;

import org.openqa.selenium.support.PageFactory;

import com.zaidan.testng.locators.SummaryCourseLocators;
import com.zaidan.testng.utils.HelperClass;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class SummaryCourseActions {
    SummaryCourseLocators summaryCourseLocators = null;

    public SummaryCourseActions() {
        this.summaryCourseLocators = new SummaryCourseLocators();
        PageFactory.initElements(HelperClass.getDriver(), summaryCourseLocators);
    }

    public void clickOnKomputerGrafikProgresBtn() {
        summaryCourseLocators.komputerGrafikProgresBtn.click();
    }

    public void clickOnKomputerGrafikDetailKuisBtn() {
        summaryCourseLocators.komputerGrafikDetailKuisBtn.click();
    }

    public void clickCourseActionButton(String courseName, String buttonText) {
        // This dynamic XPath finds the correct button based on the course and button names
        String dynamicXpath = String.format(
            "//tr[td[text()='%s']]/td[last()]/button[contains(text(),'%s')]",
            courseName, buttonText
        );
        
        try {
            System.out.println("Finding button '" + buttonText + "' for course '" + courseName + "'");
            WebElement actionButton = HelperClass.getDriver().findElement(By.xpath(dynamicXpath));
            Actions actions = new Actions(HelperClass.getDriver());
            actions.moveToElement(actionButton).click().perform();
        } catch (NoSuchElementException e) {
            System.err.println("Could not find button '" + buttonText + "' for course '" + courseName + "'");
            throw new AssertionError("Failed to find action button for course: " + courseName, e);
        }
    }
}
