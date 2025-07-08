package com.zaidan.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class QuizAttemptPageLocators {
    // Locator untuk semua kotak soal di halaman
    // Sesuaikan selector ini dengan aplikasi Anda
    @FindBy(xpath = "//div[contains(@class, 'input-quiz-text')]")
    public List<WebElement> questionContainers;

    @FindBy(xpath = "//button[text()='KIRIM']")
    public WebElement submitButton;

    @FindBy(xpath = "//div[contains(@class, 'learn-list-item')]") // Ganti dengan class yang benar
    public List<WebElement> quizLinks;
}