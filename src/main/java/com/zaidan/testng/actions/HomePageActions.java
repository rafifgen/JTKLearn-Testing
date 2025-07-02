package com.zaidan.testng.actions;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
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
  
    // Get the Username from Home Page
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
        try {
            return homePageLocators.courseTitle.getText();
        } catch (NoSuchElementException e) {
            // Jika elemen tidak ditemukan, kembalikan "kosong"
            // agar bisa di-assert di step definition.
            return "kosong";
        }
    }

    public List<String> getCourses() {
        try {
            // Jika locator tidak menemukan elemen, homePageLocators.courses akan menjadi list kosong.
            if (homePageLocators.courses.isEmpty()) {
                return new ArrayList<>(); // Kembalikan list kosong secara eksplisit.
            }

            List<String> courses = new ArrayList<>();
            for (WebElement item : homePageLocators.courses) {
                courses.add(item.getText());
            }
            return courses;
        } catch (NoSuchElementException e) {
            // Sebagai pengaman tambahan, jika terjadi error saat mengakses list,
            // kembalikan list kosong.
            return new ArrayList<>();
        }
    }

//    public boolean isCourseListVisible() {
//        return !homePageLocators.courses.isEmpty();
//    }

    public void clickOnSubMenuUsername() {
        try {
            homePageLocators.subMenuUsername.click();
        } catch (NoSuchElementException e) {
            // Jika elemen tidak ditemukan, abaikan aksi klik.
            // Bisa ditambahkan log untuk debugging.
            System.out.println("Element 'subMenuUsername' tidak ditemukan, aksi klik dilewati.");
        }
    }

    public boolean isKeluarDisplayed() {
        try {
            return homePageLocators.subMenuKeluar.isDisplayed();
        } catch (NoSuchElementException e) {
            // Jika elemen tidak ada di DOM, maka ia tidak ditampilkan.
            return false;
        }
    }
}