package com.zaidan.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class QuizHistoryPageLocators {
    @FindBy(xpath = "/html/body/div/div/div/nav/div/div/ul/li[3]/a") // Locator untuk link menu Riwayat Kuis
    public WebElement quizHistoryMenuLink;

    @FindBy(xpath = "/html/body/div/div/div/div/div/div[1]/h3") // Locator untuk judul halaman
    public WebElement pageTitle;

    @FindBy(xpath = "/html/body/div/div/div/div/div/div[2]/p") // Locator untuk pesan 'belum ada kursus'
    public WebElement emptyHistoryMessage;

    @FindBy(xpath = "/html/body/div/div/div/div/div/h3") // Asumsi judul sub-halaman ada di tag <h2>
    public WebElement subPageTitle;
}
