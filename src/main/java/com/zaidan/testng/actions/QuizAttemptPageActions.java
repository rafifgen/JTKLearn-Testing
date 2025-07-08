package com.zaidan.testng.actions;

import com.zaidan.testng.locators.QuizAttemptPageLocators;
import com.zaidan.testng.utils.HelperClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import java.util.Arrays;
import java.util.List;

public class QuizAttemptPageActions {
    QuizAttemptPageLocators locators = new QuizAttemptPageLocators();

    public QuizAttemptPageActions() {
        PageFactory.initElements(HelperClass.getDriver(), this.locators);
    }

    public void answerQuestionsForScore80() {
        List<String> correctAnswers = Arrays.asList("a", "b", "c", "d", "e");
        List<WebElement> questions = locators.questionContainers;

        for (int i = 0; i < 4; i++) { // Jawab 4 soal dengan benar
            WebElement answerInput = questions.get(i).findElement(By.xpath(".//input"));
            answerInput.sendKeys(correctAnswers.get(i));
        }
        // Jawab 1 soal dengan salah
        WebElement lastAnswerInput = questions.get(4).findElement(By.xpath(".//input"));
        lastAnswerInput.sendKeys("jawaban_salah");
    }

    // Di dalam kelas QuizAttemptPageActions
    public void startQuiz(String quizName) {
        try {
            // Loop melalui semua link kuis yang ditemukan
            for (WebElement quizLink : locators.quizLinks) {
                // Jika teks di dalam link cocok dengan nama kuis yang dicari
                if (quizLink.getText().trim().equalsIgnoreCase(quizName)) {
                    quizLink.click(); // Klik link tersebut
                    return; // Keluar dari method setelah mengklik
                }
            }
            // Gagal jika setelah loop selesai, tidak ada kuis yang cocok
//            Assert.fail("Link untuk kuis bernama '" + quizName + "' tidak ditemukan.");
        } catch (Exception e) {
            throw new AssertionError("Gagal memulai kuis: " + quizName, e);
        }
    }

    public void clickSubmitButton() {
        locators.submitButton.click();
    }
}