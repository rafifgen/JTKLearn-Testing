package com.zaidan.testng.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor; // Important for executing JavaScript
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import com.zaidan.testng.locators.LearnCoursePageLocators;
import com.zaidan.testng.utils.HelperClass;

public class LearnCoursePageActions {
    private WebDriver driver;
    LearnCoursePageLocators learnCoursePageLocators = null;

    public LearnCoursePageActions() {
        this.driver = HelperClass.getDriver();
        this.learnCoursePageLocators = new LearnCoursePageLocators();
        PageFactory.initElements(this.driver, this.learnCoursePageLocators);
    }

    public String getCourseTitle() {
        String courseTitle = null;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(HelperClass.TIMEOUT));
            WebElement titleElement = wait.until(ExpectedConditions.visibilityOf(learnCoursePageLocators.courseTitle));
            courseTitle = titleElement.getText();
            System.out.println("Course Title found: " + courseTitle);
        } catch (Exception e) {
            System.err.println("Judul kursus tidak berhasil ditemukan: " + e.getMessage());
        }
        return courseTitle;
    }

    public String getMaterialTitle() {
        String materialTitle = null;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(HelperClass.TIMEOUT));
            WebElement titleElement = wait.until(ExpectedConditions.visibilityOf(learnCoursePageLocators.materialTitle));
            materialTitle = titleElement.getText();
            System.out.println("Material Title found: " + materialTitle);
        } catch (Exception e) {
            System.err.println("Judul materi tidak berhasil ditemukan: " + e.getMessage());
        }
        return materialTitle;
    }

    public boolean verifyExampleVideoCanBePlayed() {
        String vidLink = null;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(HelperClass.TIMEOUT));
            WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(learnCoursePageLocators.exampleVidIframeLocator));
            // <<< CHANGE APPLIED HERE: getDomProperty("src") >>>
            vidLink = iframe.getDomProperty("src");
        } catch (Exception e) {
            System.err.println("Could not get video iframe src: " + e.getMessage());
            return false;
        }

        // Check if the src indicates playable content based on the blob URL pattern
        if (vidLink != null && vidLink.startsWith("blob:")) { // A simplified check for blob URLs
            System.out.println("Video link indicates playable content (blob URL): " + vidLink);
            return true;
        } else {
            System.out.println("Video link does NOT indicate playable content or is null: " + vidLink);
            return false;
        }
    }

    public void clickExampleVidMaterial() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(HelperClass.TIMEOUT));
            WebElement vidNavButton = wait.until(ExpectedConditions.elementToBeClickable(learnCoursePageLocators.exampleVidInNavBar));
            vidNavButton.click();
            System.out.println("Clicked example video material in nav bar.");
        } catch (Exception e) {
            System.err.println("Failed to click example video material: " + e.getMessage());
            throw new RuntimeException("Failed to click video nav material", e);
        }
    }

    public void playExampleVideo() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Generous timeout for video elements

        try {
            System.out.println("Attempting to play embedded video...");

            // 1. Wait for the iframe to be present and visible on the page
            WebElement videoIframe = wait.until(
                ExpectedConditions.visibilityOfElementLocated(learnCoursePageLocators.exampleVidIframeLocator)
            );
            System.out.println("Video iframe found and visible.");

            // 2. Switch Selenium's context to the iframe
            driver.switchTo().frame(videoIframe);
            System.out.println("Switched to video iframe context.");

            // 3. Wait for the HTML5 <video> element itself to be present in the DOM
            // Using presenceOfElementLocated is robust as visibility might depend on video loading.
            WebElement videoElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(learnCoursePageLocators.html5VideoPlayer)
            );
            System.out.println("HTML5 video element found inside iframe.");

            // Get JavascriptExecutor for interacting with video properties
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // 4. Wait for the video's readyState to indicate it's ready to play
            // readyState 4 = HAVE_ENOUGH_DATA (enough data to play through without interruption)
            System.out.println("Waiting for video readyState to be 4 (HAVE_ENOUGH_DATA)...");
            wait.until(d -> {
                Long readyState = (Long) js.executeScript("return arguments[0].readyState;", videoElement);
                System.out.println("Current video readyState: " + readyState);
                return readyState == 4L;
            });
            System.out.println("Video is ready to play (readyState 4).");

            // 5. Use JavaScript to play the video
            // This is the most reliable way to interact with HTML5 media elements.
            js.executeScript("arguments[0].play();", videoElement);
            System.out.println("Executed JavaScript to play video.");

            // 6. Optional: Verify that the video is actually playing
            // Wait until the 'paused' property is false AND 'currentTime' is greater than 0
            System.out.println("Verifying video playback status...");
            wait.until(d -> {
                Double currentTime = (Double) js.executeScript("return arguments[0].currentTime;", videoElement);
                Boolean paused = (Boolean) js.executeScript("return arguments[0].paused;", videoElement);
                System.out.println("Video playback check: currentTime=" + currentTime + ", paused=" + paused);
                return !paused && currentTime > 0; // Not paused AND time has advanced
            });
            System.out.println("Video is confirmed playing (currentTime > 0 and not not paused).");

        } catch (Exception e) {
            System.err.println("Error playing embedded video via HTML5 controls: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to play video due to: " + e.getMessage(), e);
        } finally {
            // CRITICAL: Always switch back to the default content (main page)
            try {
                driver.switchTo().defaultContent();
                System.out.println("Switched back to main content.");
            } catch (Exception ex) {
                System.err.println("Failed to switch back to default content after iframe interaction: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public boolean verifyNextOrPrevButton() {
        boolean status = false;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(HelperClass.TIMEOUT));
        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.elementToBeClickable(learnCoursePageLocators.nextButton),
                ExpectedConditions.elementToBeClickable(learnCoursePageLocators.prevButton)
            ));
            status = true;
            System.out.println("Next or Previous button found and clickable.");
        } catch (Exception e) {
            System.out.println("Neither Next nor Previous button found/clickable.");
        }
        return status;
    }

    public void clickExamplePDFMaterial() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(HelperClass.TIMEOUT));
            WebElement pdfNavButton = wait.until(ExpectedConditions.elementToBeClickable(learnCoursePageLocators.examplePDFInNavBar));
            pdfNavButton.click();
            System.out.println("Clicked example PDF material in nav bar.");
        } catch (Exception e) {
            System.err.println("Failed to click example PDF material: " + e.getMessage());
            throw new RuntimeException("Failed to click PDF nav material", e);
        }
    }

    public WebElement getExamplePDFMaterial() {
        WebElement pdfIframe = null;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(HelperClass.TIMEOUT));
            // Assuming examplePDF is a WebElement @FindBy(xpath = "/html/body/div/div/div/div/div[2]/div[1]/div/iframe")
            // This locator is IDENTICAL to exampleVidIframe. You MUST verify it's the correct one for PDF.
            pdfIframe = wait.until(ExpectedConditions.visibilityOf(learnCoursePageLocators.examplePDF));
            System.out.println("PDF iframe found.");
        } catch (Exception e) {
            System.err.println("Failed to get example PDF material: " + e.getMessage());
        }
        return pdfIframe;
    }

    public void goToNextPage() {
        learnCoursePageLocators.nextButton.click();
    }
}