package com.zaidan.testng.actions;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.support.PageFactory;
import com.zaidan.testng.locators.HomePageLocators;
import com.zaidan.testng.utils.HelperClass;
import org.openqa.selenium.WebElement;
 
public class HomePageActions {
 
    HomePageLocators homePageLocators = null;
    
    public HomePageActions() {
         
        this.homePageLocators = new HomePageLocators();
 
        PageFactory.initElements(HelperClass.getDriver(),homePageLocators);
    }
  
    // Get the User name from Home Page
    public String getHomePageText() {
        return homePageLocators.homePageUserName.getText();
    }

    public List<String> getSidebarItems() {
        List<String> sidebarTexts = new ArrayList<>();
        for (WebElement item : homePageLocators.sidebarItems) {
            sidebarTexts.add(item.getText());
        }
        return sidebarTexts;
    }

}