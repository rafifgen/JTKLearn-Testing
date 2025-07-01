package com.zaidan.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;;

public class myCourseLocators {
    @FindBy(id = "inprogress-tab")
    public WebElement tabDalamProgres;

    @FindBy(id = "completed-tab")
    public WebElement tabSelesai;

    @FindBy(css = ".card.custom-card")
    public List<WebElement> courseCards;

    @FindBy(css = ".card-title")
    public WebElement courseTitle;

    @FindBy(css = ".card-text")
    public WebElement instructorName;

    @FindBy(css = ".progress-percentage")
    public WebElement progressPercentage;


}
