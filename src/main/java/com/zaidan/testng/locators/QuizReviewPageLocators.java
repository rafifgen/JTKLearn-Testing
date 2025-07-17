package com.zaidan.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class QuizReviewPageLocators {
    
    // Course "Memasak" locator - specific xpath from user
    @FindBy(xpath = "/html/body/div/div/div/div/div/div[2]/div[6]/div")
    public WebElement courseMemasakCard;
    
    // "Lihat Kursus" button - specific xpath from user
    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[2]/div[5]/button")
    public WebElement lihatKursusButton;
    
    // Section quiz - specific xpath from user (step 3)
    @FindBy(xpath = "/html/body/div/div/div/div/div[1]/nav/div/div/div/ul/li[2]")
    public WebElement sectionQuiz;
    
    // Specific quiz "kuis memasak semester 1" locator
    @FindBy(xpath = "//div[contains(text(),'kuis memasak semester 1')]")
    public WebElement kuisMemasakSemester1;
    
    // Alternative locator for quiz navigation item
    @FindBy(xpath = "//li[contains(text(),'kuis memasak semester 1')]")
    public WebElement kuisMemasakSemester1NavItem;
    
    // "Tinjau Hasil Kuis" button - specific xpath from user
    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[1]/div/div/div[2]/div")
    public WebElement tinjauHasilKuisButton;
    
    // Alternative locators for "Tinjau Hasil Kuis" button
    @FindBy(xpath = "//button[contains(text(),'Tinjau Hasil Kuis')]")
    public WebElement tinjauHasilKuisButtonText;
    
    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[1]/div/div/div[2]/div/button")
    public WebElement tinjauHasilKuisButtonSpecific;

    // Quiz review page elements
    @FindBy(xpath = "//div[contains(@class,'quiz-review')]")
    public WebElement quizReviewContainer;
    
    @FindBy(xpath = "//div[contains(@class,'question-container')]")
    public java.util.List<WebElement> questionContainers;
    
    @FindBy(xpath = "//div[contains(@class,'student-answer')]")
    public java.util.List<WebElement> studentAnswers;
    
    @FindBy(xpath = "//div[contains(@class,'correct-answer')]")
    public java.util.List<WebElement> correctAnswers;
    
    @FindBy(xpath = "//div[contains(@class,'answer-status')]")
    public java.util.List<WebElement> answerStatuses;
    
    // Quiz questions
    @FindBy(xpath = "//div[contains(@class,'quiz-question')]")
    public java.util.List<WebElement> allQuizQuestions;
    
    @FindBy(xpath = "//h3[contains(@class,'question-text')]")
    public java.util.List<WebElement> questionTexts;
    
    @FindBy(xpath = "//div[contains(@class,'question-options')]")
    public java.util.List<WebElement> questionOptions;
    
    // Answer styling elements
    @FindBy(xpath = "//div[contains(@class,'text-success')]")
    public java.util.List<WebElement> correctAnswerElements;
    
    @FindBy(xpath = "//div[contains(@class,'text-danger')]")
    public java.util.List<WebElement> incorrectAnswerElements;
    
    // Navigation and control buttons
    @FindBy(xpath = "//button[contains(text(),'Akhiri Tinjauan')]")
    public WebElement akhiriTinjauanButton;
    
    @FindBy(xpath = "//a[contains(@href,'course-detail')]")
    public WebElement courseDetailLink;
    
    @FindBy(xpath = "//a[contains(@href,'mode=review')]")
    public WebElement quizReviewLink;
    
    // Quiz title and info
    @FindBy(xpath = "//h2[contains(text(),'kuis memasak semester 1')]")
    public WebElement quizTitle;
    
    @FindBy(xpath = "//div[contains(@class,'quiz-info')]")
    public WebElement quizInfo;
    
    @FindBy(xpath = "//div[contains(@class,'quiz-score')]")
    public WebElement quizScore;
    
    // Multiple choice specific elements
    @FindBy(xpath = "//div[contains(@class,'multiple-choice')]")
    public java.util.List<WebElement> multipleChoiceQuestions;
    
    @FindBy(xpath = "//input[@type='radio']")
    public java.util.List<WebElement> radioButtons;
    
    @FindBy(xpath = "//label[contains(@class,'choice-label')]")
    public java.util.List<WebElement> choiceLabels;
    
    // Status and feedback elements
    @FindBy(xpath = "//div[contains(@class,'answer-feedback')]")
    public java.util.List<WebElement> answerFeedbacks;
    
    @FindBy(xpath = "//span[contains(@class,'status-icon')]")
    public java.util.List<WebElement> statusIcons;
    
    @FindBy(xpath = "//div[contains(@class,'border-success')]")
    public java.util.List<WebElement> correctBorders;
    
    @FindBy(xpath = "//div[contains(@class,'border-danger')]")
    public java.util.List<WebElement> incorrectBorders;
    
    // Specific XPath locators for border and status checking (user provided)
    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div/form/div[1]/div/div[1]")
    public WebElement questionBorderContainer;
    
    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div/form/div[1]/div/div[1]/div/span/span[2]")
    public WebElement answerStatusIndicator;
    
    // Multiple question containers for checking 2 questions
    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div/form/div[1]")
    public WebElement firstQuestionContainer;
    
    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div/form/div[2]")
    public WebElement secondQuestionContainer;
    
    // Status indicators for both questions
    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div/form/div[1]/div/div[1]/div/span/span[2]")
    public WebElement firstQuestionStatus;
    
    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div/form/div[2]/div/div[1]/div/span/span[2]")
    public WebElement secondQuestionStatus;
    
    // Border containers for both questions
    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div/form/div[1]/div/div[1]")
    public WebElement firstQuestionBorder;
    
    @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div/form/div[2]/div/div[1]")
    public WebElement secondQuestionBorder;
} 