package com.zaidan.testng.actions;

import org.openqa.selenium.support.PageFactory;

import com.zaidan.testng.locators.SummaryCourseLocators;
import com.zaidan.testng.utils.HelperClass;

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
}
