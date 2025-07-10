package com.zaidan.testng.actions;

import com.zaidan.testng.locators.QuizHistoryPageLocators;
import com.zaidan.testng.model.QuizAttemptDetails;
import com.zaidan.testng.utils.HelperClass;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class QuizHistoryPageActions {
    QuizHistoryPageLocators quizHistoryLocators = new QuizHistoryPageLocators();

    public QuizHistoryPageActions() {
        PageFactory.initElements(HelperClass.getDriver(), this.quizHistoryLocators);
    }

    public void navigateToQuizHistory() {
        quizHistoryLocators.quizHistoryMenuLink.click();
    }

    public String getPageTitle() {
        try {
            return quizHistoryLocators.pageTitle.getText();
        } catch (NoSuchElementException e) {
            return "Judul Halaman Tidak Ditemukan";
        }
    }

    public boolean isCourseSubtitleVisible(String courseName) {
        try {
            String xpath = String.format("//h3[text()='%s']", courseName); // Asumsi sub-judul ada di tag <h3>
            WebElement subtitle = HelperClass.getDriver().findElement(By.xpath(xpath));
            return subtitle.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void clickDetailButtonForCourse(String courseName) {
        try {
            // XPath ini melakukan hal berikut:
            // 1. Cari sel tabel <td> yang berisi teks nama kursus.
            // 2. Naik ke induknya, yaitu baris <tr>.
            // 3. Dari baris itu, cari tombol <button> yang berisi teks 'Lihat Detail'.
            String xpath = String.format(
                    "//td[normalize-space()='%s']/ancestor::tr//button[text()='Lihat Detail']",
                    courseName
            );
            WebElement detailButton = HelperClass.getDriver().findElement(By.xpath(xpath));
            detailButton.click();
        } catch (NoSuchElementException e) {
            throw new AssertionError("Tombol 'Lihat Detail' untuk kursus '" + courseName + "' tidak ditemukan.", e);
        }
    }

    public String getEmptyHistoryMessage() {
        try {
            return quizHistoryLocators.emptyHistoryMessage.getText();
        } catch (NoSuchElementException e) {
            return "Pesan Tidak Ditemukan";
        }
    }
    public String getNoAttemptsMessageForCourse(String courseName) {
        try {
            return quizHistoryLocators.emptyHistoryMessage.getText();
        } catch (NoSuchElementException e) {
            return "Pesan untuk kursus '" + courseName + "' tidak ditemukan.";
        }
    }

    public String getSubPageTitle() {
        try {
            return quizHistoryLocators.subPageTitle.getText();
        } catch (Exception e) {
            return "Judul Sub-Halaman Tidak Ditemukan";
        }
    }

    public List<QuizAttemptDetails> getDisplayedQuizHistory() {
        List<QuizAttemptDetails> uiQuizHistory = new ArrayList<>();

        // Ganti quizHistoryRows menjadi quizHistoryCards sesuai dengan locator baru
        for (WebElement card : quizHistoryLocators.quizHistoryCards) {
            try {
                // Mengambil nama dan deskripsi
                String fullTitle = card.findElement(By.xpath(".//h5[@class='card-title']")).getText();
                String quizName = fullTitle.split(": ")[1]; // Pisahkan berdasarkan ": " dan ambil bagian kedua
                String quizDesc = card.findElement(By.xpath(".//div[@class='card-description']/p")).getText();

                // Mengambil waktu dan memisahkannya dari labelnya
                String startTime = card.findElement(By.xpath(".//p[contains(text(), 'Waktu Mulai:')]")).getText().split(": ")[1];
                String endTime = card.findElement(By.xpath(".//p[contains(text(), 'Waktu Selesai:')]")).getText().split(": ")[1];

                // Mengambil skor dan memisahkannya dari labelnya
                String scoreText = card.findElement(By.xpath(".//p[contains(text(), 'Skor Tertinggi:')]")).getText().split(": ")[1];
                double highestScore = Double.parseDouble(scoreText);

                // Mengambil total percobaan dan membersihkannya dari 'x'
                String attemptsText = card.findElement(By.xpath(".//p[contains(text(), 'Total Percobaan:')]")).getText().split(": ")[1];
                int totalAttempts = Integer.parseInt(attemptsText.replace("x", ""));

                // Membuat objek baru dan menambahkannya ke list
                uiQuizHistory.add(new QuizAttemptDetails(quizName, quizDesc, highestScore, totalAttempts, startTime, endTime));

            } catch (Exception e) {
                System.err.println("Gagal mem-parsing salah satu card riwayat kuis di UI: " + e.getMessage());
            }
        }
        return uiQuizHistory;
    }

    public boolean isQuizHistoryVisible() {
        try {
            // Tunggu hingga 5 detik sampai semua elemen yang cocok dengan locator terlihat
            WebDriverWait wait = new WebDriverWait(HelperClass.getDriver(), Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfAllElements(quizHistoryLocators.quizHistoryCards));

            // Jika wait berhasil, berarti elemen ada dan terlihat.
            // Kembalikan true jika list tidak kosong.
            return !quizHistoryLocators.quizHistoryCards.isEmpty();
        } catch (Exception e) {
            // Jika setelah 5 detik elemen tidak juga terlihat (TimeoutException)
            // atau terjadi error lain, kembalikan false.
            return false;
        }
    }

    public void searchForQuiz(String searchTerm) {
        try {
            quizHistoryLocators.searchQuizInput.clear();
            quizHistoryLocators.searchQuizInput.sendKeys(searchTerm);
        } catch (NoSuchElementException e) {
            // Gagal jika field pencarian tidak ditemukan
            throw new AssertionError("Field pencarian kuis tidak ditemukan di halaman.");
        }
    }

}
