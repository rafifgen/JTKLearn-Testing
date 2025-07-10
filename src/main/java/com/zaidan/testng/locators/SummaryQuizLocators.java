package com.zaidan.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SummaryQuizLocators {
    @FindBy(xpath = "//*[@id=\"root\"]/div/div/div[2]/table/tbody/tr/td[4]/button")    
    public WebElement lihatHasilBtn;
}