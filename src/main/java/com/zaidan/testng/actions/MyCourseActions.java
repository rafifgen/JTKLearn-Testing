package com.zaidan.testng.actions;

import com.zaidan.testng.locators.MyCourseLocators;
import com.zaidan.testng.utils.HelperClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MyCourseActions {

    private final WebDriverWait wait = new WebDriverWait(HelperClass.getDriver(), Duration.ofSeconds(15));

    public void clickProgressTab() {
        clickTab(MyCourseLocators.PROGRESS_TAB);
    }

    public void clickCompletedTab() {
        clickTab(MyCourseLocators.COMPLETED_TAB);
    }

    private void clickTab(By locator) {
        // Scroll jika perlu
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) HelperClass.getDriver()).executeScript("arguments[0].scrollIntoView(true);", tab);

        // Klik menggunakan JavaScript untuk menghindari masalah interaksi
        ((JavascriptExecutor) HelperClass.getDriver()).executeScript("arguments[0].click();", tab);

        // Tunggu sampai tab aktif berubah
        wait.until(ExpectedConditions.attributeContains(
                locator, "class", "active"
        ));

        // Tunggu animasi selesai
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".fade:not(.show)")
        ));
    }
}