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
import org.openqa.selenium.support.PageFactory;
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
    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    private final MyCourseLocators locators = new MyCourseLocators();

    public MyCourseDefinitions() {
        PageFactory.initElements(driver, locators);
    }

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
    }

    @Given("User has completed courses in database")
    public void setupCompletedCourses() {
        CourseDAO courseDAO = new CourseDAO();
        List<CourseProgress> completedCourses = courseDAO.getCompletedCourses();
        if (completedCourses.isEmpty()) {
            throw new IllegalStateException("No completed courses found in database for testing. Please setup test data first.");
        }
        System.out.println("Found " + completedCourses.size() + " completed courses in test database");
    }

    @Then("System displays active tab {string}")
    public void verifyActiveTab(String expectedTab) {
        WebElement activeTab = wait.until(ExpectedConditions.visibilityOf(locators.activeTab));
        Assert.assertEquals(activeTab.getText(), expectedTab, "Active tab doesn't match expected");
    }

    @Then("System shows message {string}")
    public void verifyEmptyMessage(String expectedMessage) {
        try {
            WebElement emptyMessageElement;
            if (expectedMessage.contains("Belum ada kursus yang sedang dijalani")) {
                emptyMessageElement = wait.until(ExpectedConditions.visibilityOf(locators.emptyStateMessageInProgress));
            } else if (expectedMessage.contains("Belum ada kursus yang selesai")) {
                emptyMessageElement = wait.until(ExpectedConditions.visibilityOf(locators.emptyStateMessageCompleted));
            } else {
                throw new IllegalArgumentException("Unexpected message: " + expectedMessage);
            }
            Assert.assertTrue(emptyMessageElement.getText().contains(expectedMessage),
                    "Empty state message doesn't contain expected text");
        } catch (TimeoutException e) {
            List<WebElement> courseCards = locators.completedcourseCards;
            Assert.assertTrue(courseCards.isEmpty(), "Course cards should not be present when empty message is expected");
        }
    }

    @Then("System shows course list with:")
    public void verifyCourseList(DataTable dataTable) {
        debugCurrentState();
        ensureCompletedTabIsActive();
        List<WebElement> courseCards = waitForCourseCards();
        if (courseCards.isEmpty()) {
            handleEmptyState();
            return;
        }
        for (WebElement card : courseCards) {
            ensureCardIsVisible(card);
            verifyCardComponents(card, dataTable);
        }
    }

    @Given("User session is initialized")
    public void initUserSession() {
        String pelajarLoggedin = HelperClass.getLoggedInUserName();
        Pelajar datapelajar = new PelajarDAO().getPelajarByNama(pelajarLoggedin);
        String namaPelajar = datapelajar.getNama();
        int idpelajar = datapelajar.getIdPelajar();
        HelperClass.setLoggedInUserId(idpelajar);
        System.out.println("Logged in as [" + namaPelajar + "] with id_pelajar=" + idpelajar);
    }

    @Then("All course data matches database records")
    public void verifyDatabaseConsistency() {
        int idPelajar = HelperClass.getLoggedInUserId();
        CourseDAO dao = new CourseDAO();
        List<CourseProgress> dbCoursesProgress = dao.getCompletedCoursesByPelajar(idPelajar);
        List<WebElement> courseCards = locators.completedcourseCards;
        Assert.assertEquals(courseCards.size(), dbCoursesProgress.size(),
                "Jumlah card UI vs record Completed di DB untuk id_pelajar=" + idPelajar);
        for (WebElement card : courseCards) {
            verifyCourseCardAgainstDatabase(card, dbCoursesProgress);
        }
    }

    // ============ HELPER METHODS ============
//    private void waitForTabContentToLoad() {
//        try {
//            wait.until(ExpectedConditions.invisibilityOfElementLocated(
//                    By.cssSelector(".loader, .loading, [aria-busy='true']")
//            ));
//        } catch (TimeoutException e) {}
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }

    private void debugCurrentState() {
        System.out.println("=== DEBUGGING CURRENT STATE ===");
        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Page title: " + driver.getTitle());
        boolean onMyCoursesPage = driver.getCurrentUrl().contains("/my-courses") ||
                driver.getTitle().contains("My Courses");
        System.out.println("On My Courses page: " + onMyCoursesPage);
        try {
            System.out.println("Active tab text: " + locators.activeTab.getText());
        } catch (NoSuchElementException e) {
            System.out.println("No active tab found");
        }
    }

    private void ensureCompletedTabIsActive() {
        System.out.println("=== ENSURING COMPLETED TAB IS ACTIVE ===");
        try {
            String classes = locators.completedTab.getAttribute("class");
            System.out.println("Completed tab classes: " + classes);
            if (!classes.contains("active")) {
                System.out.println("Clicking 'Selesai' tab...");
                locators.completedTab.click();
                wait.until(ExpectedConditions.attributeContains(locators.completedTab, "class", "active"));
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
            // Wait for at least one card to be visible
            wait.until(driver -> {
                // Refresh the list of elements
                PageFactory.initElements(driver, locators);
                return !locators.completedcourseCards.isEmpty() && locators.completedcourseCards.get(0).isDisplayed();
            });
            return locators.completedcourseCards;
        } catch (TimeoutException e) {
            return new ArrayList<>();
        }
    }

    private void handleEmptyState() {
        System.out.println("=== HANDLING EMPTY STATE ===");
        try {
            System.out.println("Empty state message: " + locators.emptyStateMessageCompleted.getText());
            Assert.fail("Expected to find completed courses but found empty state message: " +
                    locators.emptyStateMessageCompleted.getText());
        } catch (NoSuchElementException e) {
            String pageSource = driver.getPageSource();
            System.out.println("Page contains 'course': " + pageSource.contains("course"));
            System.out.println("Page contains 'completed': " + pageSource.contains("completed"));
            Assert.fail("No course cards found and no empty state message displayed");
        }
    }

    private void ensureCardIsVisible(WebElement card) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", card);
        wait.until(ExpectedConditions.visibilityOf(card));
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

    private void verifyCourseImage(WebElement courseCard, String verification) {
        System.out.println("courseCard HTML: " + courseCard.getAttribute("outerHTML"));
        System.out.println("=== VERIFYING COURSE IMAGE ===");
        try {
            WebElement image = courseCard.findElement(MyCourseLocators.COURSE_IMAGE);
            String uiSrc = image.getAttribute("src");
            String fileName = uiSrc.substring(uiSrc.lastIndexOf("/") + 1);
            System.out.println("Extracted image src from UI: " + uiSrc);
            System.out.println("Extracted image file name: " + fileName);

            if (verification.equals("Visible")) {
                Assert.assertTrue(image.isDisplayed(), "Course image should be visible");
                Assert.assertNotNull(uiSrc, "Image source should not be null");
                Assert.assertFalse(uiSrc.isEmpty(), "Image source should not be empty");
            }

            if (verification.equals("Matches database")) {
                String uiName = courseCard.findElement(MyCourseLocators.COURSE_NAME).getText().trim();
                System.out.println("Course name from UI: " + uiName);
                Course course = new CourseDAO().getCourseByName(uiName);
                if (course == null) {
                    Assert.fail("Course with name '" + uiName + "' not found in database.");
                }
                String expectedImage = course.getGambarCourse();
                System.out.println("Expected image from DB: " + expectedImage);
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
                Assert.assertEquals(uiName, course.getNamaCourse(),
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
                String uiCourseName = courseCard.findElement(MyCourseLocators.COURSE_NAME).getText().trim();
                Course course = new CourseDAO().getCourseByName(uiCourseName);
                int idPengajar = course.getIdPengajar();
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
                String style = progressBar.getAttribute("style");
                String width = progressBar.getCssValue("width");
                String ariaValue = progressBar.getAttribute("aria-valuenow");

                boolean isComplete = false;
                if (style != null && style.contains("width: 100%")) isComplete = true;
                else if (width != null && width.equals("100%")) isComplete = true;
                else if (ariaValue != null && ariaValue.equals("100")) isComplete = true;

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

    private void verifyCourseCardAgainstDatabase(WebElement courseCard,
                                                 List<CourseProgress> dbCoursesProgress) {
        try {
            String actualName = courseCard.findElement(MyCourseLocators.COURSE_NAME).getText().trim();
            String actualProgressText = courseCard.findElement(MyCourseLocators.PROGRESS_PERCENT).getText().trim();
            int actualProgress = Integer.parseInt(actualProgressText.replace("%", "").trim());

            CourseProgress match = dbCoursesProgress.stream()
                    .filter(cp ->
                            cp.getNamaCourse().equals(actualName) &&
                                    cp.getPersentase() == actualProgress
                    )
                    .findFirst()
                    .orElse(null);

            Assert.assertNotNull(match,
                    "Course dari UI tidak ditemukan atau progress mismatch: " +
                            actualName + " (" + actualProgress + "%)");

            System.out.println("Verified course: " + actualName + " → " + actualProgress + "%");
        } catch (NoSuchElementException e) {
            Assert.fail("Required course card elements not found: " + e.getMessage());
        }
    }
}