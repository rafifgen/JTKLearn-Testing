package com.zaidan.testng.actions;

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
        if (vidLink != null && vidLink.contains("embed")) { // A simplified check for blob URLs
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            System.out.println("Attempting to play YouTube embedded video...");

            // 1. Wait for the iframe to be present and switch to it
            WebElement videoIframe = wait.until(
                ExpectedConditions.visibilityOfElementLocated(learnCoursePageLocators.exampleVidIframeLocator)
            );
            System.out.println("Video iframe found and visible.");
            
            driver.switchTo().frame(videoIframe);
            System.out.println("Switched to video iframe context.");

            // 2. Wait for the YouTube Play button to be clickable and click it
            System.out.println("Waiting for the YouTube play button to be clickable...");
            WebElement playButton = wait.until(
                ExpectedConditions.elementToBeClickable(learnCoursePageLocators.youTubePlayButton)
            );
            System.out.println("YouTube play button found. Clicking it now.");
            playButton.click();

            // 3. (Verification) Wait for the video to enter the "playing" state.
            // After clicking play, the main player element's state changes. We can check this with JavaScript.
            // A player state of '1' means the video is playing.
            System.out.println("Verifying video playback status...");
            wait.until(d -> {
                JavascriptExecutor js = (JavascriptExecutor) d;
                // This script accesses the YouTube player's internal API to get the current state
                Long playerState = (Long) js.executeScript(
                    "return document.getElementById('movie_player').getPlayerState()"
                );
                System.out.println("Current YouTube player state: " + playerState + " (1 = playing)");
                return playerState == 1; // 1 means playing, 2 means paused
            });
            
            System.out.println("Video is confirmed to be playing.");

        } catch (Exception e) {
            System.err.println("Error playing embedded YouTube video: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to play YouTube video due to: " + e.getMessage(), e);
        } finally {
            // CRITICAL: Always switch back to the main page content
            try {
                driver.switchTo().defaultContent();
                System.out.println("Switched back to main page content.");
            } catch (Exception ex) {
                System.err.println("Failed to switch back to default content after iframe interaction: " + ex.getMessage());
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
            
            System.out.println("Attempting to find the PDF content iframe and wait for it to be ready...");

            wait.until(ExpectedConditions.attributeContains(
                learnCoursePageLocators.contentIframe, "src", ".pdf"
            ));
            
            // Now that we know the element is fully ready, we can safely find and return it.
            pdfIframe = driver.findElement(learnCoursePageLocators.contentIframe);

            System.out.println("PDF iframe is fully rendered and found successfully.");
            
        } catch (Exception e) {
            System.err.println("Failed to get example PDF material: " + e.getMessage());
        }
        return pdfIframe;
    }

    public void goToNextPage() {
        learnCoursePageLocators.nextButton.click();
    }

    public double getProgressBarPercentage() {
        double progress = 0.0;
        try {
            // Find the progress bar element using the locator
            WebElement progressBarElement = learnCoursePageLocators.progressBar;
            
            // Get the value from the 'aria-valuenow' attribute, which holds the precise number
            String progressValue = progressBarElement.getDomAttribute("aria-valuenow");
            
            System.out.println("UI Progress Bar 'aria-valuenow' attribute found: " + progressValue);
            
            // Convert the string value to a double so it can be used in assertions
            if (progressValue != null && !progressValue.isEmpty()) {
                progress = Double.parseDouble(progressValue);
            }
        } catch (Exception e) {
            System.err.println("Could not get or parse the progress bar percentage: " + e.getMessage());
            // You might want to throw the exception or handle it as needed
            // For now, it will return 0.0
        }
        return progress;
    }

    public String getVidNavStatusColor() {
        String color = null;
        try {
            // Get the specific navigation element using the locator
            WebElement vidNavItem = learnCoursePageLocators.exampleVidInNavBar;
            
            // It's good practice to ensure the element is visible before checking its style
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(HelperClass.TIMEOUT));
            wait.until(ExpectedConditions.visibilityOf(vidNavItem));

            // Get the computed background color property
            color = vidNavItem.getCssValue("background-color");
            
            System.out.println("Navigation item background color is: " + color);

        } catch (Exception e) {
            System.err.println("Could not get background color of navigation item: " + e.getMessage());
        }
        return color;
    }

    public String getPDFNavStatusColor() {
        String color = null;
        try {
            // Get the specific navigation element using the locator
            WebElement PDFNavItem = learnCoursePageLocators.examplePDFInNavBar;
            
            // It's good practice to ensure the element is visible before checking its style
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(HelperClass.TIMEOUT));
            wait.until(ExpectedConditions.visibilityOf(PDFNavItem));

            // Get the computed background color property
            color = PDFNavItem.getCssValue("background-color");
            
            System.out.println("Navigation item background color is: " + color);

        } catch (Exception e) {
            System.err.println("Could not get background color of navigation item: " + e.getMessage());
        }
        return color;
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }
}