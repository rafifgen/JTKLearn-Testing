package com.zaidan.testng.definitions;

import com.zaidan.testng.actions.HomePageActions;
import com.zaidan.testng.actions.MyCourseActions;
import com.zaidan.testng.dao.CourseDAO;
import com.zaidan.testng.dao.PelajarDAO;
import com.zaidan.testng.dao.PengajarDAO;
import com.zaidan.testng.locators.MyCourseLocators;
import com.zaidan.testng.model.Course;
import com.zaidan.testng.model.CourseProgress;
import com.zaidan.testng.model.Pelajar;
import com.zaidan.testng.model.Pengajar;
import com.zaidan.testng.utils.HelperClass;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyCourseDefinitions {

    private final MyCourseActions myCourseActions = new MyCourseActions();
    private final HomePageActions homePageActions = new HomePageActions();
    private final WebDriver driver = HelperClass.getDriver();
    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Increased timeout
    private int userId = 0;

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

        // Wait for tab content to load
        waitForTabContentToLoad();
    }

    @Given("User has completed courses in database")
    public void setupCompletedCourses() {
        // Verify that test data exists
        CourseDAO courseDAO = new CourseDAO();
        List<CourseProgress> completedCourses = courseDAO.getCompletedCourses();

        if (completedCourses.isEmpty()) {
            throw new IllegalStateException("No completed courses found in database for testing. Please setup test data first.");
        }

        System.out.println("Found " + completedCourses.size() + " completed courses in test database");
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
            // Verify no course cards are present
            List<WebElement> courseCards = driver.findElements(MyCourseLocators.COURSE_CARDS);
            Assert.assertTrue(courseCards.isEmpty(), "Course cards should not be present when empty message is expected");
        }
    }

    @Then("System shows course list with:")
    public void verifyCourseList(DataTable dataTable) {
        // Debug current state
        debugCurrentState();

        // Ensure we're on the correct tab
        ensureCompletedTabIsActive();

        // Wait for course cards with multiple strategies
        List<WebElement> courseCards = waitForCourseCards();

        if (courseCards.isEmpty()) {
            handleEmptyState();
            return;
        }

        // Verify the first card is properly loaded
        WebElement firstCard = courseCards.get(0);
        ensureCardIsVisible(firstCard);

        // Verify each component as specified in the data table
        verifyCardComponents(firstCard, dataTable);
    }

    @Given("User session is initialized")
    public void initUserSession() {
        String pelajarLoggedin = MyCourseActions.getLoggedInUserName();
        Pelajar datapelajar = new PelajarDAO().getPelajarByNama(pelajarLoggedin);
        String namaPelajar = datapelajar.getNama();
        int idpelajar = datapelajar.getIdPelajar();

        // HelperClass.setLoggedInUserId(idpelajar);
        userId = idpelajar;
        System.out.println("Logged in as [" + namaPelajar + "] with id_pelajar=" + idpelajar);
    }


    @Then("All course data matches database records")
    public void verifyDatabaseConsistency() {
        // int idPelajar = HelperClass.getLoggedInUserId();
        int idPelajar = this.userId;
        CourseDAO dao = new CourseDAO();
        List<CourseProgress> dbCoursesProgress =
                dao.getCompletedCoursesByPelajar(idPelajar);

        List<WebElement> courseCards =
                driver.findElements(MyCourseLocators.COURSE_CARDS);

        Assert.assertEquals(courseCards.size(),
                dbCoursesProgress.size(),
                "Jumlah card UI vs record Completed di DB untuk id_pelajar=" + idPelajar);

        for (WebElement card : courseCards) {
            verifyCourseCardAgainstDatabase(card, dbCoursesProgress);
        }
    }



    // ============ HELPER METHODS ============

    private void waitForTabContentToLoad() {
        try {
            // Wait for any loading indicators to disappear
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector(".loader, .loading, [aria-busy='true']")
            ));
        } catch (TimeoutException e) {
            // Loading indicator might not exist, continue
        }

        // Additional wait for content to stabilize
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void debugCurrentState() {
        System.out.println("=== DEBUGGING CURRENT STATE ===");
        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Page title: " + driver.getTitle());

        // Check if we're on the my courses page
        boolean onMyCoursesPage = driver.getCurrentUrl().contains("/my-courses") ||
                driver.getTitle().contains("My Courses");
        System.out.println("On My Courses page: " + onMyCoursesPage);

        // Check active tab
        try {
            WebElement activeTab = driver.findElement(MyCourseLocators.ACTIVE_TAB);
            System.out.println("Active tab text: " + activeTab.getText());
        } catch (NoSuchElementException e) {
            System.out.println("No active tab found");
        }
    }

    private void ensureCompletedTabIsActive() {
        System.out.println("=== ENSURING COMPLETED TAB IS ACTIVE ===");

        try {
            WebElement completedTab = driver.findElement(MyCourseLocators.COMPLETED_TAB);

            // Check if tab is already active
            String classes = completedTab.getDomAttribute("class");
            System.out.println("Completed tab classes: " + classes);

            if (!classes.contains("active")) {
                System.out.println("Clicking 'Selesai' tab...");
                completedTab.click();

                // Wait for tab to become active
                wait.until(ExpectedConditions.attributeContains(completedTab, "class", "active"));

                // Wait for content to load
                waitForTabContentToLoad();
            } else {
                System.out.println("'Selesai' tab is already active");
            }

        } catch (NoSuchElementException e) {
            throw new RuntimeException("Could not find completed tab element", e);
        }
    }

    private List<WebElement> waitForCourseCards() {
        System.out.println("=== WAITING FOR COURSE CARDS ===");
        try {
            // Tunggu sampai ada >0 card di dalam panel completed
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                    MyCourseLocators.COURSE_CARDS, 0
            ));
            List<WebElement> courseCards = driver.findElements(MyCourseLocators.COURSE_CARDS);
            System.out.println("Found " + courseCards.size() + " course cards");
            return courseCards;
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for course cards with selector: "
                    + MyCourseLocators.COURSE_CARDS);
            return new ArrayList<>();
        }
    }


    private void handleEmptyState() {
        System.out.println("=== HANDLING EMPTY STATE ===");

        // Check if there's an empty state message
        try {
            WebElement emptyMessage = driver.findElement(MyCourseLocators.EMPTY_STATE_MESSAGE_COMPLETED);
            System.out.println("Empty state message: " + emptyMessage.getText());

            // If we expect courses but get empty state, this is a test failure
            Assert.fail("Expected to find completed courses but found empty state message: " + emptyMessage.getText());

        } catch (NoSuchElementException e) {
            // No empty state message found either
            System.out.println("No course cards and no empty state message found");

            // Check if there's any content at all
            String pageSource = driver.getPageSource();
            System.out.println("Page contains 'course': " + pageSource.contains("course"));
            System.out.println("Page contains 'completed': " + pageSource.contains("completed"));

            Assert.fail("No course cards found and no empty state message displayed");
        }
    }

    private void ensureCardIsVisible(WebElement card) {
        // Scroll card into view
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", card);

        // Wait for card to be visible
        wait.until(ExpectedConditions.visibilityOf(card));

        // Additional check
        if (!card.isDisplayed()) {
            throw new RuntimeException("Course card is not displayed after scrolling");
        }

        System.out.println("First card is visible and ready for verification");
    }

    private void verifyCardComponents(WebElement card, DataTable dataTable) {
        List<Map<String, String>> components = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> component : components) {
            String elementType = component.get("Component");
            String verification = component.get("Verification");

            System.out.println("Verifying: " + elementType + " - " + verification);

            switch (elementType) {
                case "Course image":
                    verifyCourseImage(card, verification);
                    break;
                case "Course name":
                    verifyCourseName(card, verification);
                    break;
                case "Instructor name":
                    verifyInstructorName(card, verification);
                    break;
                case "Progress bar":
                    verifyProgressBar(card, verification);
                    break;
                case "Completion percentage":
                    verifyCompletionPercentage(card, verification);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown component: " + elementType);
            }
        }
    }

//    private void verifyCourseImage(WebElement courseCard, String verification) {
//        System.out.println("=== VERIFYING COURSE IMAGE ===");
//
//        try {
//            WebElement image = courseCard.findElement(MyCourseLocators.COURSE_IMAGE);
//
//            if (verification.equals("Visible")) {
//                Assert.assertTrue(image.isDisplayed(), "Course image should be visible");
//
//                // Check image source
//                String src = image.getDomAttribute("src");
//                Assert.assertNotNull(src, "Image source should not be null");
//                Assert.assertFalse(src.isEmpty(), "Image source should not be empty");
//
//                System.out.println("Course image verified. Src: " + src);
//            }
//
//        } catch (NoSuchElementException e) {
//            // Try alternative selectors
//            List<WebElement> images = courseCard.findElements(By.tagName("img"));
//            if (images.isEmpty()) {
//                Assert.fail("Course image element not found");
//            }
//
//            // Use the first image found
//            WebElement image = images.get(0);
//            Assert.assertTrue(image.isDisplayed(), "Course image should be visible");
//            System.out.println("Course image found with alternative selector");
//        }
//    }
//
//    private void verifyCourseName(WebElement courseCard, String verification) {
//        try {
//            WebElement name = courseCard.findElement(MyCourseLocators.COURSE_NAME);
//            Assert.assertTrue(name.isDisplayed(), "Course name should be visible");
//            Assert.assertFalse(name.getText().trim().isEmpty(), "Course name should not be empty");
//
//            System.out.println("Course name verified: " + name.getText());
//
//        } catch (NoSuchElementException e) {
//            Assert.fail("Course name element not found");
//        }
//    }
//
//    private void verifyInstructorName(WebElement courseCard, String verification) {
//        try {
//            WebElement instructor = courseCard.findElement(MyCourseLocators.INSTRUCTOR_NAME);
//            Assert.assertTrue(instructor.isDisplayed(), "Instructor name should be visible");
//            Assert.assertFalse(instructor.getText().trim().isEmpty(), "Instructor name should not be empty");
//
//            System.out.println("Instructor name verified: " + instructor.getText());
//
//        } catch (NoSuchElementException e) {
//            Assert.fail("Instructor name element not found");
//        }
//    }

    private void verifyCourseImage(WebElement courseCard, String verification) {
        System.out.println("courseCard HTML: " + courseCard.getDomAttribute("outerHTML"));

        System.out.println("=== VERIFYING COURSE IMAGE ===");

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(courseCard, MyCourseLocators.COURSE_IMAGE));
            WebElement image = courseCard.findElement(MyCourseLocators.COURSE_IMAGE);
            System.out.println(image.getTagName() + " element found for course image");
            String uiSrc = image.getDomAttribute("src");


            // Ekstrak nama file dari URL
            String fileName = uiSrc.substring(uiSrc.lastIndexOf("/") + 1);
            System.out.println("Extracted image src from UI: " + uiSrc);
            System.out.println("Extracted image file name: " + fileName);

            if (verification.equals("Visible")) {
                Assert.assertTrue(image.isDisplayed(), "Course image should be visible");
                Assert.assertNotNull(uiSrc, "Image source should not be null");
                Assert.assertFalse(uiSrc.isEmpty(), "Image source should not be empty");
            }

            if (verification.equals("Matches database")) {
                Assert.assertTrue(image.isDisplayed(), "Course image should be visible");
                Assert.assertNotNull(uiSrc, "Image source should not be null");
                Assert.assertFalse(uiSrc.isEmpty(), "Image source should not be empty");
                // Ambil nama course dari UI
                String uiName = courseCard.findElement(MyCourseLocators.COURSE_NAME)
                        .getText().trim();
                System.out.println("Course name from UI: " + uiName);

                // Ambil data dari database
                Course course = new CourseDAO().getCourseByName(uiName);

                if (course == null) {
                    Assert.fail("Course with name '" + uiName + "' not found in database.");
                }

                String expectedImage = course.getGambarCourse();
                System.out.println("Expected image from DB: " + expectedImage);

                // Bandingkan nama file dari src dengan value di database
                Assert.assertEquals(fileName, expectedImage,
                        "Image file name mismatch. UI: " + fileName + ", DB: " + expectedImage);
            }

            System.out.println("Course image verification PASSED for src: " + uiSrc);

        } catch (NoSuchElementException e) {
            Assert.fail("Course image element not found");
        }
    }


    private void verifyCourseName(WebElement courseCard, String verification) {
        try {
            WebElement name = courseCard.findElement(MyCourseLocators.COURSE_NAME);
            String uiName = name.getText().trim();

            Assert.assertTrue(name.isDisplayed(), "Course name should be visible");
            Assert.assertFalse(uiName.isEmpty(), "Course name should not be empty");

            if (verification.equals("Matches database")) {
                Course course = new CourseDAO().getCourseByName(uiName);
                Assert.assertEquals(uiName, course.getNamaCourse(), // Use getter method
                        "Course name UI does not match DB");
            }

            System.out.println("Course name verified: " + uiName);

        } catch (NoSuchElementException e) {
            Assert.fail("Course name element not found");
        }
    }

    private void verifyInstructorName(WebElement courseCard, String verification) {
        try {
            WebElement instructorEl = courseCard.findElement(MyCourseLocators.INSTRUCTOR_NAME);
            String uiInstructor = instructorEl.getText().trim();

            Assert.assertTrue(instructorEl.isDisplayed(), "Instructor name should be visible");
            Assert.assertFalse(uiInstructor.isEmpty(), "Instructor name should not be empty");

            if (verification.equals("Matches database")) {
                // Get course name first to retrieve idPengajar
                String uiCourseName = courseCard.findElement(MyCourseLocators.COURSE_NAME)
                        .getText().trim();
                Course course = new CourseDAO().getCourseByName(uiCourseName);
                int idPengajar = course.getIdPengajar(); // Use getter method

                // matching the uiInstuctor with retrieved instructor name from DB
                Pengajar expectedInstructor = new PengajarDAO().getPengajarById(idPengajar);
                String expectedInstructorName = expectedInstructor.getNamaPengajar();
                Assert.assertEquals(uiInstructor, expectedInstructorName,
                        "Instructor name UI does not match DB");
            }

            System.out.println("Instructor name verified: " + uiInstructor);

        } catch (NoSuchElementException e) {
            Assert.fail("Instructor name element not found");
        }
    }


    private void verifyProgressBar(WebElement courseCard, String verification) {
        try {
            WebElement progressBar = courseCard.findElement(MyCourseLocators.PROGRESS_BAR);

            if (verification.equals("Shows 100% completion")) {
                // Check various ways the progress might be displayed
                String style = progressBar.getDomAttribute("style");
                String width = progressBar.getCssValue("width");
                String ariaValue = progressBar.getDomAttribute("aria-valuenow");

                boolean isComplete = false;

                if (style != null && style.contains("width: 100%")) {
                    isComplete = true;
                } else if (width != null && width.equals("100%")) {
                    isComplete = true;
                } else if (ariaValue != null && ariaValue.equals("100")) {
                    isComplete = true;
                }

                Assert.assertTrue(isComplete, "Progress bar should show 100% completion");
                System.out.println("Progress bar verified as 100% complete");
            }

        } catch (NoSuchElementException e) {
            Assert.fail("Progress bar element not found");
        }
    }

    private void verifyCompletionPercentage(WebElement courseCard, String verification) {
        try {
            WebElement percentage = courseCard.findElement(MyCourseLocators.PROGRESS_PERCENT);

            if (verification.equals("Shows 100%")) {
                String percentText = percentage.getText().trim();
                Assert.assertTrue(percentText.contains("100"),
                        "Completion percentage should show 100%, but found: " + percentText);

                System.out.println("Completion percentage verified: " + percentText);
            }

        } catch (NoSuchElementException e) {
            Assert.fail("Progress percentage element not found");
        }
    }

    /**
     * Verifikasi tiap card di UI cocok dengan data di tabel course (tanpa detail progress).
     * @param courseCard elemen card di UI
     * @param allCourses list hasil CourseDAO.getAllCourses()
     */
    /**
     * Verifikasi tiap card di UI cocok dengan CourseProgress (nama + progress).
     */
    private void verifyCourseCardAgainstDatabase(WebElement courseCard,
                                                 List<CourseProgress> dbCoursesProgress) {
        try {
            String actualName = courseCard
                    .findElement(MyCourseLocators.COURSE_NAME)
                    .getText().trim();
            String actualProgressText = courseCard
                    .findElement(MyCourseLocators.PROGRESS_PERCENT)
                    .getText().trim();
            int actualProgress = Integer.parseInt(
                    actualProgressText.replace("%", "").trim());

            // Cari di daftar CourseProgress
            CourseProgress match = dbCoursesProgress.stream()
                    .filter(cp ->
                            cp.getNamaCourse().equals(actualName) &&
                                    cp.getPersentase() == actualProgress
                    )
                    .findFirst()
                    .orElse(null);

            Assert.assertNotNull(match,
                    "Course dari UI tidak ditemukan atau progress mismatch: "
                            + actualName + " (" + actualProgress + "%)");

            System.out.println("Verified course: "
                    + actualName + " → " + actualProgress + "%");

        } catch (NoSuchElementException e) {
            Assert.fail("Required course card elements not found: " + e.getMessage());
        }
    }



}