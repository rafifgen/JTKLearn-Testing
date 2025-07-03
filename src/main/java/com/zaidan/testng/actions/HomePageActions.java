package com.zaidan.testng.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.PageFactory;
import com.zaidan.testng.locators.HomePageLocators;
import com.zaidan.testng.utils.HelperClass;
import org.openqa.selenium.WebElement;

public class HomePageActions {

    HomePageLocators homePageLocators = null;

    public HomePageActions() {

        this.homePageLocators = new HomePageLocators();

        PageFactory.initElements(HelperClass.getDriver(), homePageLocators);
    }
  
    // Get the Username from Home Page
    public String getHomePageText() {
        return homePageLocators.homePageUserName.getText();
    }

    public List<String> getSidebarItems() {
        try {
            // Jika locator tidak menemukan elemen, homePageLocators.courses akan menjadi list kosong.
            if (homePageLocators.navBarItems.isEmpty()) {
                return new ArrayList<>(); // Kembalikan list kosong secara eksplisit.
            }

            List<String> navBarTexts = new ArrayList<>();
            for (WebElement item : homePageLocators.navBarItems) {
                navBarTexts.add(item.getText());
            }
            return navBarTexts;
        } catch (NoSuchElementException e) {
            // Sebagai pengaman tambahan, jika terjadi error saat mengakses list,
            // kembalikan list kosong.
            return new ArrayList<>();
        }
    }

    public boolean isUserPhotoDisplayed() {
        try {
            return homePageLocators.userPhoto.isDisplayed();
        } catch (NoSuchElementException e) {
            // Jika elemen tidak ada di DOM, maka ia tidak ditampilkan.
            return false;
        }
    }

    public boolean isUsernameDisplayed() {
        try {
            return homePageLocators.homePageUserName.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Simulates fetching expected course data from a database.
     * In a real scenario, this would involve a DB connection and query.
     *
     * @return List of maps representing expected courses from the DB.
     */
    public List<Map<String, String>> getExpectedCoursesFromDatabase() {
        // !!! IMPORTANT: Replace this with actual database fetching logic !!!
        // For now, hardcoding to match the Gherkin example and DOM.
        List<Map<String, String>> dbCourses = new ArrayList<>();

        Map<String, String> course1 = new HashMap<>();
        course1.put("courseName", "Contoh Kursus");
        course1.put("instructorName", "Budi Pengajar");
        dbCourses.add(course1);

        Map<String, String> course2 = new HashMap<>();
        course2.put("courseName", "Pemrograman Web");
        course2.put("instructorName", "Budi Pengajar");
        dbCourses.add(course2);

        Map<String, String> course3 = new HashMap<>();
        course3.put("courseName", "Pemrograman Mobile");
        course3.put("instructorName", "Budi Pengajar");
        dbCourses.add(course3);

        return dbCourses;
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

    // public boolean isCourseListVisible() {
    // return !homePageLocators.courses.isEmpty();
    // }

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

    public void selectCourseByName(String courseName) {
        try {
            // XPath ini mencari <h6> yang berisi nama kursus,
            // lalu memilih "induk"-nya yaitu <div> utama dari card tersebut.
            String dynamicXpath = String.format("//h6[normalize-space()='%s']/ancestor::div[@class='card custom-card']", courseName);

            WebElement courseCard = HelperClass.getDriver().findElement(By.xpath(dynamicXpath));

            courseCard.click();

        } catch (NoSuchElementException e) {
            System.err.println("Course card dengan nama '" + courseName + "' tidak ditemukan.");
            throw new AssertionError("Gagal menemukan kursus: " + courseName, e);
        }
    }
}
