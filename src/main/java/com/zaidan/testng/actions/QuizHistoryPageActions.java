package com.zaidan.testng.actions;

import com.zaidan.testng.locators.QuizHistoryPageLocators;
import com.zaidan.testng.utils.HelperClass;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class QuizHistoryPageActions {
    QuizHistoryPageLocators quizHistoryLocators = new QuizHistoryPageLocators();

    public QuizHistoryPageActions() {
        PageFactory.initElements(HelperClass.getDriver(), this.quizHistoryLocators);
    }

    public void navigateToQuizHistory() {
        quizHistoryLocators.quizHistoryMenuLink.click();
    }

    public String getPageTitle() {
        try {
            return quizHistoryLocators.pageTitle.getText();
        } catch (NoSuchElementException e) {
            return "Judul Halaman Tidak Ditemukan";
        }
    }

    public boolean isCourseSubtitleVisible(String courseName) {
        try {
            String xpath = String.format("//h3[text()='%s']", courseName); // Asumsi sub-judul ada di tag <h3>
            WebElement subtitle = HelperClass.getDriver().findElement(By.xpath(xpath));
            return subtitle.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void clickDetailButtonForCourse(String courseName) {
        try {
            // XPath ini melakukan hal berikut:
            // 1. Cari sel tabel <td> yang berisi teks nama kursus.
            // 2. Naik ke induknya, yaitu baris <tr>.
            // 3. Dari baris itu, cari tombol <button> yang berisi teks 'Lihat Detail'.
            String xpath = String.format(
                    "//td[normalize-space()='%s']/ancestor::tr//button[text()='Lihat Detail']",
                    courseName
            );
            WebElement detailButton = HelperClass.getDriver().findElement(By.xpath(xpath));
            detailButton.click();
        } catch (NoSuchElementException e) {
            throw new AssertionError("Tombol 'Lihat Detail' untuk kursus '" + courseName + "' tidak ditemukan.", e);
        }
    }

    public String getEmptyHistoryMessage() {
        try {
            return quizHistoryLocators.emptyHistoryMessage.getText();
        } catch (NoSuchElementException e) {
            return "Pesan Tidak Ditemukan";
        }
    }
    public String getNoAttemptsMessageForCourse(String courseName) {
        try {
            return quizHistoryLocators.emptyHistoryMessage.getText();
        } catch (NoSuchElementException e) {
            return "Pesan untuk kursus '" + courseName + "' tidak ditemukan.";
        }
    }

    public String getSubPageTitle() {
        try {
            return quizHistoryLocators.subPageTitle.getText();
        } catch (Exception e) {
            return "Judul Sub-Halaman Tidak Ditemukan";
        }
    }
}
