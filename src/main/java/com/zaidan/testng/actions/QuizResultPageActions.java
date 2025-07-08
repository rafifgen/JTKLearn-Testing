package com.zaidan.testng.actions;

import com.zaidan.testng.locators.QuizResultPageLocators;
import com.zaidan.testng.utils.HelperClass;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class QuizResultPageActions {
    QuizResultPageLocators locators = new QuizResultPageLocators();
    WebDriverWait wait = new WebDriverWait(HelperClass.getDriver(), Duration.ofSeconds(10));

    public QuizResultPageActions() {
        PageFactory.initElements(HelperClass.getDriver(), this.locators);
    }

    public void clickUlangiKuis() {
        wait.until(ExpectedConditions.visibilityOf(locators.ulangiKuisButton)).click();
    }

    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOf(locators.resultTitle)).getText();
    }

    public String getSummaryText() {
        return wait.until(ExpectedConditions.visibilityOf(locators.summaryText)).getText();
    }

    public String getPassingMessage() {
        return wait.until(ExpectedConditions.visibilityOf(locators.passingMessage)).getText();
    }
}