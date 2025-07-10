package com.zaidan.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CourseContentPageLocators {
    // Judul kursus (sidebar)
    @FindBy(css = "h4.course-title")
    public WebElement courseTitle;

    // Progress bar
    @FindBy(css = "div.progress-bar")
    public WebElement progressBar;

    // Persentase progress
    @FindBy(css = ".text-muted.ms-2")
    public WebElement progressPercentage;

    // Semua item di sidebar navigation (materi + kuis)
    @FindBy(css = "ul.learn-list li.learn-list-item")
    public List<WebElement> allSidebarItems;

    // Item aktif (highlighted)
    @FindBy(css = "ul.learn-list li.learn-list-item.active")
    public WebElement activeSidebarItem;

    // 1. Pengenalan Pemrograman Web (materi pertama)
    @FindBy(xpath = "//ul[contains(@class,'learn-list')]/li[1]")
    public WebElement itemPengenalanPemrogramanWeb;

    // 2. HTML (materi kedua)
    @FindBy(xpath = "//ul[contains(@class,'learn-list')]/li[2]")
    public WebElement itemHTMLMateri;

    // 3. CSS (materi ketiga)
    @FindBy(xpath = "//ul[contains(@class,'learn-list')]/li[3]")
    public WebElement itemCSSMateri;

    // 4. Pemrograman Web (kuis pertama)
    @FindBy(xpath = "//ul[contains(@class,'learn-list')]/li[4]")
    public WebElement itemPemrogramanWebQuiz;

    // 5. HTML (kuis kedua, dengan background hijau)
    @FindBy(xpath = "//ul[contains(@class,'learn-list')]/li[5]")
    public WebElement itemHTMLQuiz;

    // — tambahan helper locators jika ingin mencari berdasarkan icon type —
    @FindBy(css = "ul.learn-list li.learn-list-item span.icon img[alt='Materi']")
    public List<WebElement> allMateriIcons;

    @FindBy(css = "ul.learn-list li.learn-list-item span.icon img[alt='Quiz']")
    public List<WebElement> allQuizIcons;

    @FindBy(xpath = "//ul[contains(@class,'learn-list')]/li[contains(@style,'background-color: rgb(255, 255, 255)')]")
    public static List<WebElement> incompleteNavItems;

    /**
     * Semua navigation item dengan background hijau (sudah diselesaikan)
     * style-nya mengandung  'background-color: rgb(162, 245, 200)'
     */
    @FindBy(xpath = "//ul[contains(@class,'learn-list')]/li[contains(@style,'background-color: rgb(162, 245, 200)')]")
    public static List<WebElement> completedNavItems;

    // WebElement img = li.findElement(By.cssSelector("span.icon img"));
    //            String alt = img.getAttribute("alt").toLowerCase();
    @FindBy(css = "ul.learn-list li.learn-list-item span.icon img")
    public List<WebElement> allSidebarIcons;

}
