package com.zaidan.testng.definitions;

import com.zaidan.testng.actions.CourseContentPageActions;
import com.zaidan.testng.actions.CourseDetailsPageActions;
import com.zaidan.testng.actions.MyCourseActions;
import com.zaidan.testng.dao.CourseDAO;
import com.zaidan.testng.dao.CourseDetailsDAO;
import com.zaidan.testng.locators.CourseContentPageLocators;
import com.zaidan.testng.locators.MyCourseLocators;
import com.zaidan.testng.model.ContentItem;
import com.zaidan.testng.model.Course;
import com.zaidan.testng.model.CourseProgress;
import com.zaidan.testng.utils.HelperClass;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CourseContentPageDefinitions {
    private final MyCourseActions myCourseActions = new MyCourseActions();
    private final CourseDetailsPageActions detailsActions = new CourseDetailsPageActions();
    private final CourseContentPageActions contentActions = new CourseContentPageActions();
    private final CourseDAO courseDAO = new CourseDAO();
    private final CourseDetailsDAO courseDetailsDAO = new CourseDetailsDAO();

    private String courseNameFromCard;

    @Given("the user is on the courses page")
    public void userOnCoursesPage() {
        HelperClass.waitForPageLoad();
    }

    @And("the first course card shows a progress of {string}")
    public void firstCourseCardShowsProgress(String progressText) {
        WebElement card = myCourseActions.findCourseCardByProgress(progressText);
        Assert.assertNotNull(card, "Tidak ada course card dengan progress " + progressText);
        courseNameFromCard = card.findElement(MyCourseLocators.COURSE_NAME).getText().trim();
        try {
            card.click();
        } catch (Exception e) {
            ((JavascriptExecutor) HelperClass.getDriver()).executeScript("arguments[0].click();", card);
        }

    }
    @And("the first course card shows a progress between {string} and {string}")
    public void firstCourseCardShowsProgressBetween(String min, String max) {
        WebElement card = myCourseActions.findCourseCardByProgressBetween(min, max);
        Assert.assertNotNull(card, "Tidak ada course card dengan progress antara " + min + " dan " + max);
        courseNameFromCard = card.findElement(MyCourseLocators.COURSE_NAME).getText().trim();
        try {
            card.click();
        } catch (Exception e) {
            ((JavascriptExecutor) HelperClass.getDriver()).executeScript("arguments[0].click();", card);
        }
    }
    @And("the progress bar is partially filled")
    public void validatePartiallyFilledProgressBar() {
        Assert.assertTrue(contentActions.isProgressBarVisible(),
                "Progress bar container tidak terlihat.");
        double percentage = contentActions.getProgressbarValue();
        Assert.assertTrue(percentage > 0.0 && percentage < 100.0,
                "Progress bar terisi sebagian sebanyak" + percentage + "%");
    }
    @And("the progress bar is full")
    public void validateFullProgressBar() {
        Assert.assertTrue(contentActions.isProgressBarVisible(),
                "Progress bar container tidak terlihat.");
        double percentage = contentActions.getProgressbarValue();
        Assert.assertEquals(percentage, 100.0, "Progress bar tidak terisi penuh");
    }

    @And("the progress percentage is between {string} and {string}")
    public void validateProgressBetween(String min, String max) {
        double percentage = contentActions.getProgressPercentage();
        double minVal = Double.parseDouble(min.replace("%", ""));
        double maxVal = Double.parseDouble(max.replace("%", ""));
        Assert.assertTrue(percentage > minVal && percentage < maxVal,
                "Progress tidak berada dalam rentang yang diharapkan: " + percentage + "%");
    }

//    @When("the user clicks the first course card")
//    public void clickOnFirstCourseCard() {
//        WebElement card = myCourseActions.fi(courseNameFromCard);
//        try {
//            card.click();
//        } catch (Exception e) {
//            ((JavascriptExecutor) HelperClass.getDriver()).executeScript("arguments[0].click();", card);
//        }
//    }

    @Then("the overview page for that course is displayed")
    public void overviewPageIsDisplayed() {
        HelperClass.waitForPageLoad();
        String title = detailsActions.getCourseTitle();
        Assert.assertTrue(title.contains(courseNameFromCard),
                "Halaman overview tidak muncul atau judul kursus tidak sesuai");
    }

    @When("the user clicks the {string} button on the overview page")
    public void clickContinueButton(String buttonText) {
        detailsActions.clickButton(buttonText);
    }

    @Then("the course content page is displayed")
    public void courseContentPageIsDisplayed() {
        Assert.assertTrue(
                contentActions.isContentHeaderVisible(),
                "Content page tidak muncul setelah klik tombol"
        );
    }

    @Then("the navigation title displays the course name from the database \\(column “nama_course”\\)")
    public void validateNavigationTitle() {
        String actual = contentActions.getCourseTitle();
        System.out.println(actual);
        Course course = courseDAO.getCourseByName(courseNameFromCard);
        Assert.assertNotNull(course, "Course tidak ditemukan di database");
        Assert.assertEquals(actual, course.getNamaCourse(), "Judul kursus tidak sama dengan DB");
    }

    @Then("the progress bar is empty")
    public void validateEmptyProgressBar() {
        Assert.assertTrue(contentActions.isProgressBarVisible(),
                "Progress bar container tidak terlihat.");
        Assert.assertEquals(contentActions.getProgressPercentage(), 0.0, 0.01,
                "Progress bar tidak menunjukkan 0%%");
    }
    @Then("the progress percentage is {string}")
    public void validateProgressPercentage(String expectedPct) {
        String actualPct = String.format("%.0f%%", contentActions.getProgressPercentage());
        Assert.assertEquals(actualPct, expectedPct, "Persentase progres tidak sesuai");
    }

    @Then("the learning achievement details match the database")
    public void validateLearningAchievementsMatchDB() {
        int idPelajar = HelperClass.getLoggedInUserId();
        String courseName = contentActions.getCourseTitle();
        CourseProgress dbProgress = courseDAO.getProgressByPelajarAndCourse(idPelajar, courseName);
        Assert.assertNotNull(dbProgress,
                "Data progress tidak ditemukan di DB untuk id_pelajar=" + idPelajar
                        + " dan course=" + courseName);
        Assert.assertEquals(contentActions.getProgressPercentage(), (double) dbProgress.getPersentase(), 0.1,
                String.format("Persentase progres UI (%.1f%%) tidak sesuai DB (%.1f%%)",
                        contentActions.getProgressPercentage(), (double) dbProgress.getPersentase()));
    }

    @Then("the navigation menu lists materials first, then quizzes")
    public void validateOrderOfContent() {
      //use actions isquizaftermaterial
        List<ContentItem> items = contentActions.getSidebarContentItems();
        Assert.assertTrue(contentActions.isMaterialsBeforeQuizzes(items),
                "Urutan item salah, Materi harus sebelum Kuis");
    }

    @Then("all navigation items have a white background")
    public void validateWhiteBackground() {
        List<WebElement> whiteItems = CourseContentPageLocators.incompleteNavItems;
        Assert.assertFalse(whiteItems.isEmpty(), "Tidak ada item dengan background putih");
    }

    @And("some navigation items have a white background \\(incomplete)")
    public void validateincompleteItemsPresent() {
        List<WebElement> incompleteItems = CourseContentPageLocators.incompleteNavItems;
        Assert.assertFalse(incompleteItems.isEmpty(), "Tidak ada item dengan background putih (incomplete)");
    }

    @And("some navigation items have a green background \\(completed)")
    public void validateCompletedItemsPresent() {
        List<WebElement> completedItems = CourseContentPageLocators.completedNavItems;
        Assert.assertFalse(completedItems.isEmpty(), "Tidak ada item dengan background hijau (completed)");
    }

    @And("all navigation items have a green background \\(completed)")
    public void validateAllItemsCompleted() {
        List<WebElement> completedItems = CourseContentPageLocators.completedNavItems;
        Assert.assertFalse(completedItems.isEmpty(), "Tidak ada item dengan background hijau (completed)");
        for (WebElement item : completedItems) {
            Assert.assertTrue(item.getDomAttribute("style").contains("background-color: rgb(162, 245, 200)"),
                    "Item tidak memiliki background hijau");
        }
    }

    @Then("each material and quiz name matches the database")
    public void validateNamesMatchDatabase() {
        String courseName = detailsActions.getCourseTitle();
        List<ContentItem> uiItems = contentActions.getSidebarContentItems();
        List<ContentItem> dbItems = courseDetailsDAO.getCourseContentItems(courseName);

        // Jika urutan penting, perbaiki DAO.
        // Jika urutan tidak penting:
        Set<ContentItem> uiSet = new HashSet<>(uiItems);
        Set<ContentItem> dbSet = new HashSet<>(dbItems);

        Assert.assertEquals(uiSet, dbSet, "Daftar Materi+Quiz tidak sesuai DB (tanpa urutan)");
    }

    @Then("Completed items in the navigation should match the database")
    public void validateCompletedItemsMatchDatabase() {
        int idPelajar = HelperClass.getLoggedInUserId();
        String courseName = contentActions.getCourseTitle();

        // Get from UI
        List<ContentItem> uiCompletedItems = contentActions.getCompletedSidebarItems();

        // Get from DB
        List<ContentItem> dbCompletedItems = courseDetailsDAO.getCompletedContentItemsByPelajar(idPelajar, courseName);

        // Ubah jadi Set biar nggak peduli urutan
        Set<ContentItem> uiSet = new HashSet<>(uiCompletedItems);
        Set<ContentItem> dbSet = new HashSet<>(dbCompletedItems);


        // Periksa apakah set UI dan DB cocok
        Assert.assertFalse(uiSet.isEmpty(), "Tidak ada item yang ditandai selesai di UI.");
        Assert.assertFalse(dbSet.isEmpty(), "Tidak ada item yang ditandai selesai di database.");

        Assert.assertEquals(uiSet, dbSet, "Item yang ditandai selesai di UI tidak cocok dengan database.");
    }


}
