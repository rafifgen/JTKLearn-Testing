package com.zaidan.testng.actions;

import com.zaidan.testng.locators.MyCourseLocators;
import com.zaidan.testng.utils.HelperClass;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;



public class MyCourseActions {

    private final WebDriverWait wait;
    private final MyCourseLocators locators;
    private final JavascriptExecutor jsExecutor;

    public MyCourseActions() {
        this.wait = new WebDriverWait(HelperClass.getDriver(), Duration.ofSeconds(15));
        this.locators = new MyCourseLocators();
        this.jsExecutor = (JavascriptExecutor) HelperClass.getDriver();
        PageFactory.initElements(HelperClass.getDriver(), locators);
    }

    public void clickProgressTab() {
        clickTab(locators.progressTab);
    }

    public void clickCompletedTab() {
        clickTab(locators.completedTab);
    }

    private void clickTab(WebElement tab) {
        // Scroll ke tab jika perlu
        wait.until(ExpectedConditions.elementToBeClickable(tab));
        jsExecutor.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", tab);

        // Klik menggunakan JavaScript untuk menghindari masalah interaksi
        jsExecutor.executeScript("arguments[0].click();", tab);

        // Tunggu sampai tab aktif berubah
        wait.until(ExpectedConditions.attributeContains(
                tab, "class", "active"
        ));

    }

    public WebElement findCourseCardByProgress(String progressText) {
        // 1. Tunggu semua card di tab aktif muncul
        By cardsLocator = MyCourseLocators.COURSE_CARDS;
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cardsLocator));

        List<WebElement> cards = HelperClass.getDriver().findElements(cardsLocator);
        for (WebElement card : cards) {
            try {
                // 2. Tunggu nested element .progress-percentage dalam card
                WebElement progressEl = new WebDriverWait(HelperClass.getDriver(), Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(
                                card, MyCourseLocators.PROGRESS_PERCENT))
                        .get(0);

                // 3. Baca teks, hilangkan whitespace
                String uiProgress = progressEl.getText().trim();

                // 4. Cocokkan (bisa dengan or tanpa “%”)
                if (uiProgress.equals(progressText) ||
                        uiProgress.equals(progressText.replace("%",""))) {
                    return card;
                }
            } catch (Exception ignored) {
                // skip jika card ini tidak punya .progress-percentage
            }
        }
        return null;
    }


    public WebElement findCourseCardByProgressBetween(String min, String max) {
        // 1. Tunggu semua card di tab aktif muncul
        By cardsLocator = MyCourseLocators.COURSE_CARDS;
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cardsLocator));

        List<WebElement> cards = HelperClass.getDriver().findElements(cardsLocator);
        for (WebElement card : cards) {
            try {
                // 2. Tunggu nested element .progress-percentage dalam card
                WebElement progressEl = new WebDriverWait(HelperClass.getDriver(), Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(
                                card, MyCourseLocators.PROGRESS_PERCENT))
                        .get(0);

                // 3. Baca teks, hilangkan whitespace
                String uiProgress = progressEl.getText().trim();

                // 4. Ambil angka dari string
                String percentageString = uiProgress.replace("%", "").trim();
                double percentage = Double.parseDouble(percentageString);

                // 5. Cocokkan dengan rentang
                double minVal = Double.parseDouble(min.replace("%", ""));
                double maxVal = Double.parseDouble(max.replace("%", ""));
                if (percentage >= minVal && percentage <= maxVal) {
                    return card;
                }
            } catch (Exception ignored) {
                // skip jika card ini tidak punya .progress-percentage
            }
        }
        return null;
    }
}