package com.zaidan.testng.actions;

import com.zaidan.testng.locators.CourseDetailsPageLocators;
import com.zaidan.testng.utils.HelperClass;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.PageFactory;

import java.io.File;

public class CourseDetailsPageActions {

    CourseDetailsPageLocators courseDetailsPageLocators = null;

    public CourseDetailsPageActions() {
        this.courseDetailsPageLocators = new CourseDetailsPageLocators();
        PageFactory.initElements(HelperClass.getDriver(), this.courseDetailsPageLocators);
    }

    // Mengambil judul kursus dari halaman detail
    public String getCourseTitle() {
        try {
            return courseDetailsPageLocators.courseTitle.getText();
        } catch (NoSuchElementException e) {
            return "Judul Tidak Ditemukan";
        }
    }

    public String getCourseImageFilename() {
        try {
            // 1. Ambil seluruh URL dari atribut "src"
            String imageUrl = courseDetailsPageLocators.courseImage.getAttribute("src");

            // 2. Gunakan utilitas Java untuk mengekstrak nama file dari URL
            if (imageUrl != null && !imageUrl.isEmpty()) {
                return new File(imageUrl).getName();
            }
        } catch (NoSuchElementException e) {
            System.err.println("Elemen gambar tidak ditemukan.");
        }
        return "Nama File Tidak Ditemukan";
    }

    // Mengambil nama instruktur
    public String getInstructorName() {
        try {
            String rawText = courseDetailsPageLocators.instructorName.getText();
            return rawText.replace("Pengajar: ", "");

        } catch (NoSuchElementException e) {
            return "Instruktur Tidak Ditemukan";
        }
    }

    // Memeriksa apakah nama instruktur terlihat
    public boolean isInstructorVisible() {
        try {
            return courseDetailsPageLocators.instructorName.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isProgressBarVisible() {
        try {
            // Gunakan container yang selalu terlihat
            return courseDetailsPageLocators.progressBar.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Mengambil nilai persentase progres dari teks, membersihkannya,
     * dan mengubahnya menjadi angka (double).
     * @return Nilai progres sebagai double, atau 0.0 jika tidak ditemukan.
     */
    public double getProgressPercentage() {
        try {
            String rawText = courseDetailsPageLocators.progressPercentageText.getText();
            String cleanedText = rawText.replaceAll("[^\\d.]", "");
            return cleanedText.isEmpty() ? 0.0 : Double.parseDouble(cleanedText);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public void enterEnrollmentKey(String key) {
        courseDetailsPageLocators.enrollmentKeyInput.sendKeys(key);
    }

    public void clickEnrollButton() {
        courseDetailsPageLocators.enrollButton.click();
    }

    public String getEnrollmentErrorMessage() {
        try {
            return courseDetailsPageLocators.errorMessage.getText();
        } catch (NoSuchElementException e) {
            return "Pesan Error Tidak Ditemukan";
        }
    }

    //click button "Lanjutkan Kursus"
    public void clickButton(String buttonText) {
        //  menyamakan nama tombol dengan class button.button-overview

            courseDetailsPageLocators.continueButton.click();

    }


}