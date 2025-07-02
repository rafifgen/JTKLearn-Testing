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
        for (WebElement item : homePageLocators.navBarItems) {
            sidebarTexts.add(item.getText());
        }
        return sidebarTexts;
    }

    public boolean isUserPhotoDisplayed() {
        return homePageLocators.userPhoto.isDisplayed();
    }

    public boolean isUsernameDisplayed() {
        return homePageLocators.homePageUserName.isDisplayed();
    }

    public void clickedKursusSayaNav() {
        // Click Login button
        homePageLocators.kursusSayaNav.click();
    }
    public void clickedBerandaNav() {
        // Click Login button
        homePageLocators.berandaNav.click();
    }

    public String getCourseTitle() {
        return homePageLocators.courseTitle.getText();
    }

    public List<String> getCourses() {
        List<String> courses = new ArrayList<>();
        for (WebElement item : homePageLocators.courses) {
            courses.add(item.getText());
        }
        return courses;
    }

//    public boolean isCourseListVisible() {
//        return !homePageLocators.courses.isEmpty();
//    }

    public void clickOnSubMenuUsername() {
        homePageLocators.subMenuUsername.click();
    }

    public boolean isKeluarDisplayed() {
        return homePageLocators.subMenuKeluar.isDisplayed();
    }
}