package com.zaidan.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SummaryCourseLocators {
    @FindBy(xpath = "//*[@id=\"root\"]/div/div/div[2]/table/tbody/tr[4]/td[6]/button[1]")
    public WebElement komputerGrafikProgresBtn;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div/div[2]/table/tbody/tr[4]/td[6]/button[2]")
    public WebElement komputerGrafikDetailKuisBtn;
}
