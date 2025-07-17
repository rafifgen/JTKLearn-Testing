package com.zaidan.testng.actions;

import com.zaidan.testng.locators.CourseContentPageLocators;
import com.zaidan.testng.model.ContentItem;
import com.zaidan.testng.model.ContentType;
import com.zaidan.testng.utils.HelperClass;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CourseContentPageActions {
    private final CourseContentPageLocators locators;

    public CourseContentPageActions() {
        this.locators = new CourseContentPageLocators();
        PageFactory.initElements(HelperClass.getDriver(), this.locators);
    }

    public boolean isContentHeaderVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(HelperClass.getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(locators.courseTitle));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    public String getCourseTitle() {
        try {
            return locators.courseTitle.getText();
        } catch (Exception e) {
            return "Judul Tidak Ditemukan";
        }
    }

    public boolean isProgressBarVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(HelperClass.getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(locators.progressPercentage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public double getProgressPercentage() {
        try {
            //mengambil angka persenan
            String progressText = locators.progressPercentage.getText();
            //mengambil angka dari string
            String percentageString = progressText.replace("%", "").trim();
            //mengkonversi string ke double
            return Double.parseDouble(percentageString);

        } catch (Exception e) {
            //pesan error
            System.err.println("Error parsing progress percentage: " + e.getMessage());
            return -1; // atau nilai default lainnya jika parsing gagal
        }
    }

    /**
     * Baca semua <li> di sidebar, kembalikan sebagai List<ContentItem>
     */
    public List<ContentItem> getSidebarContentItems() {
        List<ContentItem> items = new ArrayList<>();
        for (WebElement li : locators.allSidebarItems) {
            // 1. Ambil teks item (nama materi/kuis)
            String name = li.getText().trim();

            // 2. Tentukan tipe lewat ikon <img alt="...">
            WebElement img = locators.allSidebarItems
                    .get(locators.allSidebarItems.indexOf(li))
                    .findElement(By.cssSelector("span.icon img"));
            String alt = img.getDomAttribute("alt").toLowerCase();
            ContentType type = alt.contains("materi")
                    ? ContentType.MATERIAL
                    : ContentType.QUIZ;

            // 3. Buat ContentItem dan tambahkan
            items.add(new ContentItem(name, type));
        }
        return items;
    }

    public List<String> getSidebarItemsBackgrounds() {
        CourseContentPageLocators locators = new CourseContentPageLocators();
        try {
            return locators.allSidebarItems.stream()
                    .map(item -> item.getCssValue("background-color"))
                    .toList();
        } catch (Exception e) {
            System.err.println("Error retrieving sidebar item backgrounds: " + e.getMessage());
            return List.of();
        }
    }

    public List<ContentItem> getCompletedSidebarItems() {
        List<ContentItem> completedItems = new ArrayList<>();
        for (WebElement li : locators.completedNavItems) {
            // 1. Ambil teks item (nama materi/kuis)
            String name = li.getText().trim();

            // 2. Tentukan tipe lewat ikon <img alt="...">
            WebElement img = li.findElement(By.cssSelector("span.icon img"));
            String alt = img.getDomAttribute("alt").toLowerCase();
            ContentType type = alt.contains("materi")
                    ? ContentType.MATERIAL
                    : ContentType.QUIZ;

            // 3. Buat ContentItem dan tambahkan
            completedItems.add(new ContentItem(name, type));
        }
        return completedItems;
    }

    // validate order of content
    public boolean isMaterialsBeforeQuizzes(List<ContentItem> items) {
        boolean foundQuizAfterMaterial = false;

        for (ContentItem item : items) {
            if (item.getType() == ContentType.QUIZ) {
                foundQuizAfterMaterial = true;
                break;
            }
            if (item.getType() == ContentType.MATERIAL && foundQuizAfterMaterial) {
                return false; // Found material after quiz
            }
        }
        return foundQuizAfterMaterial; // True if quiz was found after material
    }
    public List<WebElement> getIncompleteNavItems() {
        return locators.incompleteNavItems;
    }


    public double getProgressbarValue() {
        // mengambil aria-valuenow
        String value = locators.progressBar.getDomAttribute("aria-valuenow");
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing progress bar value: " + e.getMessage());
            return -1; // atau nilai default lainnya jika parsing gagal
        }
    }
}
