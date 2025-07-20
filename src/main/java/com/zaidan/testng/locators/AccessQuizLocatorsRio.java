package com.zaidan.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AccessQuizLocatorsRio { // TODO: change this
    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[2]/div[5]/button")
    public WebElement continueCourseButton;

    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[1]/div/div/div[2]/p[1]")
    public WebElement scoreElement;
    
    @FindBy(xpath = "//div[contains(@class, 'start-quiz-container')]//p[@class='passed-quiz' and contains(normalize-space(text()), 'Kamu lulus!')]")
    public WebElement successfulResultElement;
    
    @FindBy(xpath = "//div[contains(@class, 'start-quiz-container')]//p[@class='notpass-quiz' and contains(normalize-space(text()), 'Kamu belum lulus.')]")
    public WebElement failedResultElement;
    
    @FindBy(xpath = "//div[contains(@class, 'start-quiz-container')]//p[contains(normalize-space(text()), 'Minimal nilai 80 diperlukan untuk lulus')]")
    public WebElement minScoreElement;
    
    @FindBy(xpath = "//div[contains(@class, 'start-quiz-container')]//p[contains(normalize-space(text()), 'Sempurna! Kamu berhasil lulus kuis. Pertahankan usaha luar biasa ini!')]")
    public WebElement perfectReviewElement;
    
    @FindBy(xpath = "//div[contains(@class, 'start-quiz-container')]//p[contains(normalize-space(text()), 'Tinjau kembali materi pelajaran dan coba lagi!')]")
    public WebElement reviewElement;
}
