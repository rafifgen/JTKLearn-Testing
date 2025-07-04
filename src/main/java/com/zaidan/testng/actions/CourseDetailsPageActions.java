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

    // Memeriksa apakah progress bar terlihat
    public boolean isProgressBarVisible() {
        try {
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
            String rawText = courseDetailsPageLocators.progressPercentageText.getText(); // Contoh: "75.5%"

            // Membersihkan teks dari karakter non-numerik (kecuali titik)
            String cleanedText = rawText.replaceAll("[^\\d.]", "");

            if (!cleanedText.isEmpty()) {
                return Double.parseDouble(cleanedText);
            }
        } catch (NoSuchElementException | NumberFormatException e) {
            System.err.println("Gagal mengambil atau mem-parsing persentase progres: " + e.getMessage());
        }
        return 0.0; // Mengembalikan nilai default jika terjadi error
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
}