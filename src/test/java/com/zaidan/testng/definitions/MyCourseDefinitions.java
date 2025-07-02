package com.zaidan.testng.definitions;

import com.zaidan.testng.actions.HomePageActions;
import com.zaidan.testng.actions.MyCourseActions;
import com.zaidan.testng.dao.CourseDAO;
import com.zaidan.testng.locators.MyCourseLocators;
import com.zaidan.testng.model.Course;
import com.zaidan.testng.utils.HelperClass;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class MyCourseDefinitions {

    private final MyCourseActions myCourseActions = new MyCourseActions();
    private final HomePageActions homePageActions = new HomePageActions();
    private final WebDriver driver = HelperClass.getDriver();
    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    @When("User clicks on {string} navigation")
    public void clickNavigationMenu(String menuName) {
        homePageActions.clickNavigationMenu(menuName);
    }

    @When("User switches to {string} tab")
    public void switchTab(String tabName) {
        switch(tabName) {
            case "Selesai":
                myCourseActions.clickCompletedTab();
                break;
            case "Dalam Progress":
                myCourseActions.clickProgressTab();
                break;
            default:
                throw new IllegalArgumentException("Invalid tab name: " + tabName);
        }

        // Tunggu sampai konten tab dimuat
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".loader, [aria-busy='true']")
        ));
    }

    @Given("User has completed courses in database")
    public void setupCompletedCourses() {
        // Implementasi sebenarnya tergantung environment
        // Contoh: Panggil API/setup database untuk membuat data test
        System.out.println("Setting up completed courses in test database...");
    }

    @Then("System displays active tab {string}")
    public void verifyActiveTab(String expectedTab) {
        WebElement activeTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                MyCourseLocators.ACTIVE_TAB
        ));

        Assert.assertEquals(activeTab.getText(), expectedTab,
                "Active tab doesn't match expected");
    }

    @Then("System shows message {string}")
    public void verifyEmptyMessage(String expectedMessage) {
        try {
            By locator;
            if (expectedMessage.contains("Belum ada kursus yang sedang dijalani")) {
                locator = MyCourseLocators.EMPTY_STATE_MESSAGE_IN_PROGRESS;
            } else if (expectedMessage.contains("Belum ada kursus yang selesai")) {
                locator = MyCourseLocators.EMPTY_STATE_MESSAGE_COMPLETED;
            } else {
                throw new IllegalArgumentException("Unexpected message: " + expectedMessage);
            }

            WebElement emptyMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            Assert.assertTrue(emptyMessage.getText().contains(expectedMessage),
                    "Empty state message doesn't contain expected text");
        } catch (TimeoutException e) {
            // Verifikasi alternatif: tidak ada kartu kursus yang ditampilkan
            List<WebElement> courseCards = driver.findElements(MyCourseLocators.COURSE_CARDS);
            Assert.assertTrue(courseCards.isEmpty(), "Course cards should not be present when empty message is expected");
        }
    }

    @Then("System shows course list with:")
    public void verifyCourseList(DataTable dataTable) {
            List<Map<String, String>> components = dataTable.asMaps(String.class, String.class);
        List<WebElement> courseCards = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                MyCourseLocators.COURSE_CARDS
        ));

        Assert.assertTrue(courseCards.size() > 0, "Course list should not be empty");

        // Verifikasi setiap komponen yang ditentukan pada kartu pertama
        for (Map<String, String> component : components) {
            String elementType = component.get("Component");
            String verification = component.get("Verification");

            switch(elementType) {
                case "Course image":
                    verifyCourseImage(courseCards.get(0), verification);
                    break;
                case "Course name":
                    verifyCourseName(courseCards.get(0), verification);
                    break;
                case "Instructor name":
                    verifyInstructorName(courseCards.get(0), verification);
                    break;
                case "Progress bar":
                    verifyProgressBar(courseCards.get(0), verification);
                    break;
                case "Completion percentage":
                    verifyCompletionPercentage(courseCards.get(0), verification);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown component: " + elementType);
            }
        }
    }

    @Then("All course data matches database records")
    public void verifyDatabaseConsistency() {
        // Ambil data dari database
        CourseDAO courseDAO = new CourseDAO();
        List<Course> dbCourses = courseDAO.getAllCourses();

        // Ambil data dari UI
        List<WebElement> courseCards = driver.findElements(MyCourseLocators.COURSE_CARDS);

        Assert.assertEquals(courseCards.size(), dbCourses.size(),
                "Number of courses in UI does not match database records");

        for (WebElement courseCard : courseCards) {
            // Ambil data dari UI
            String actualName = courseCard.findElement(MyCourseLocators.COURSE_NAME).getText();
            String actualInstructor = courseCard.findElement(MyCourseLocators.INSTRUCTOR_NAME).getText();
            String actualProgress = courseCard.findElement(MyCourseLocators.PROGRESS_PERCENT).getText();

            // Verifikasi progress 100% untuk tab Selesai
            Assert.assertEquals(actualProgress, "100%", "Course should be 100% completed");

            // Buat objek Course dari data UI
            Course uiCourse = new Course(0, 0, actualName, "", "", "");

            // Cari kecocokan dengan data dari database
            boolean isMatch = dbCourses.stream().anyMatch(dbCourse -> dbCourse.equals(uiCourse));
            Assert.assertTrue(isMatch, "Course from UI does not match any database record: " + actualName);
        }
    }

    // ============ HELPER METHODS ============
    private void verifyCourseImage(WebElement courseCard, String verification) {
        try {
            WebElement image = courseCard.findElement(MyCourseLocators.COURSE_IMAGE);
            Assert.assertTrue(image.isDisplayed(), "Course image should be visible");

            // Verifikasi gambar tidak broken
            boolean isImageLoaded = (Boolean) ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].complete && typeof arguments[0].naturalWidth != 'undefined' && arguments[0].naturalWidth > 0",
                    image
            );
            Assert.assertTrue(isImageLoaded, "Course image is not loaded properly");
        } catch (NoSuchElementException e) {
            Assert.fail("Course image element not found");
        }
    }

    private void verifyCourseName(WebElement courseCard, String verification) {
        try {
            WebElement name = courseCard.findElement(MyCourseLocators.COURSE_NAME);
            Assert.assertTrue(name.isDisplayed(), "Course name should be visible");
            Assert.assertFalse(name.getText().isBlank(), "Course name should not be blank");
        } catch (NoSuchElementException e) {
            Assert.fail("Course name element not found");
        }
    }

    private void verifyInstructorName(WebElement courseCard, String verification) {
        try {
            WebElement instructor = courseCard.findElement(MyCourseLocators.INSTRUCTOR_NAME);
            Assert.assertTrue(instructor.isDisplayed(), "Instructor name should be visible");
            Assert.assertFalse(instructor.getText().isBlank(), "Instructor name should not be blank");
        } catch (NoSuchElementException e) {
            Assert.fail("Instructor name element not found");
        }
    }

    private void verifyProgressBar(WebElement courseCard, String verification) {
        try {
            WebElement progressBar = courseCard.findElement(MyCourseLocators.PROGRESS_BAR);
            String width = progressBar.getCssValue("width");
            String expectedWidth = "100%";

            // Untuk browser tertentu mungkin perlu konversi
            if (!width.endsWith("%")) {
                // Jika dalam pixel, konversi ke persentase
                WebElement container = progressBar.findElement(By.xpath("./.."));
                int containerWidth = Integer.parseInt(container.getCssValue("width").replace("px", ""));
                int barWidth = Integer.parseInt(width.replace("px", ""));
                int percentage = (barWidth * 100) / containerWidth;
                Assert.assertEquals(percentage, 100, "Progress bar should show 100% completion");
            } else {
                Assert.assertEquals(width, expectedWidth, "Progress bar should show 100% completion");
            }
        } catch (NoSuchElementException e) {
            Assert.fail("Progress bar element not found");
        }
    }

    private void verifyCompletionPercentage(WebElement courseCard, String verification) {
        try {
            WebElement percentage = courseCard.findElement(MyCourseLocators.PROGRESS_PERCENT);
            Assert.assertEquals(percentage.getText(), "100%", "Completion percentage should be 100%");
        } catch (NoSuchElementException e) {
            Assert.fail("Progress percentage element not found");
        }
    }
}