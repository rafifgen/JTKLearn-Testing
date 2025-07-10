package com.zaidan.testng.actions;

import org.openqa.selenium.support.PageFactory;

import com.zaidan.testng.locators.SummaryQuizLocators;
import com.zaidan.testng.utils.HelperClass;

public class SummaryQuizActions {
    SummaryQuizLocators summaryQuizLocators;

    public SummaryQuizActions() {
        this.summaryQuizLocators = new SummaryQuizLocators();
        PageFactory.initElements(HelperClass.getDriver(), summaryQuizLocators);
    }

    public void clickOnLihatHasilBtn() {
        summaryQuizLocators.lihatHasilBtn.click();
    }
}
