package com.zaidan.testng.definitions;

import com.zaidan.testng.actions.HomePageActions;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.testng.Assert;

import java.util.ArrayList; // Import ArrayList
import java.util.List;
import java.util.Map;

public class DashboardDefinitions {

    private HomePageActions homePageActions = new HomePageActions();

    @Then("User should see the welcome message {string}")
    public void userShouldSeeTheWelcomeMessage(String expectedMessage) {
        String actualMessage = homePageActions.getHomePageText();
        Assert.assertEquals(actualMessage, expectedMessage, "Welcome message mismatch!");
    }

    @Then("User should see the page title {string}")
    public void userShouldSeeThePageTitle(String expectedTitle) {
        String actualTitle = homePageActions.getHomePageTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Page title mismatch!");
    }

    @Then("User should see the following unjoined courses:")
    public void userShouldSeeTheFollowingUnjoinedCourses(DataTable expectedCoursesTable) {
        // --- FIX FOR expectedCourses START ---
        // Get expected courses from DataTable and immediately create a modifiable copy
        List<Map<String, String>> expectedCoursesFromTable = expectedCoursesTable.asMaps(String.class, String.class);
        List<Map<String, String>> expectedCourses = new ArrayList<>(expectedCoursesFromTable);
        // --- FIX FOR expectedCourses END ---


        List<Map<String, String>> actualCoursesFromUI = homePageActions.getAllUnjoinedCoursesInfo();
        List<Map<String, String>> actualCourses = new ArrayList<>(actualCoursesFromUI);

        // 1. Verify number of courses
        Assert.assertEquals(actualCourses.size(), expectedCourses.size(), "Number of courses displayed mismatch!");

        // 2. Sort lists for consistent comparison if order on UI isn't guaranteed or
        // important
        // Assuming courseName is unique for sorting
        actualCourses.sort((c1, c2) -> c1.get("courseName").compareTo(c2.get("courseName")));
        expectedCourses.sort((c1, c2) -> c1.get("courseName").compareTo(c2.get("courseName"))); // This line was failing


        // 3. Verify details of each course
        for (int i = 0; i < expectedCourses.size(); i++) {
            Map<String, String> expectedCourse = expectedCourses.get(i);
            Map<String, String> actualCourse = actualCourses.get(i);

            // Verify course name
            Assert.assertEquals(actualCourse.get("courseName"), expectedCourse.get("courseName"),
                    "Course name mismatch for course at index " + i);

            // Verify instructor name
            Assert.assertEquals(actualCourse.get("instructorName"), expectedCourse.get("instructorName"),
                    "Instructor name mismatch for course '" + expectedCourse.get("courseName") + "' at index " + i);

            // Optional: Verify image presence/alt text if `coursePic` in your Gherkin
            // implies more than just its existence
            // For example, if the alt text should match the course name
            Assert.assertTrue(actualCourse.get("coursePicAlt").contains(expectedCourse.get("courseName")),
                    "Course image alt text does not match course name for " + expectedCourse.get("courseName"));
        }
    }

    @Then("the displayed course information should match the database records")
    public void theDisplayedCourseInformationShouldMatchTheDatabaseRecords() {
        // These parts were already correctly handled in your previous attempt
        List<Map<String, String>> actualUiCoursesFromUI = homePageActions.getAllUnjoinedCoursesInfo();
        List<Map<String, String>> actualUiCourses = new ArrayList<>(actualUiCoursesFromUI);

        List<Map<String, String>> expectedDbCoursesFromDB = homePageActions.getExpectedCoursesFromDatabase();
        List<Map<String, String>> expectedDbCourses = new ArrayList<>(expectedDbCoursesFromDB);

        // Sort both lists before comparison if order is not strict
        actualUiCourses.sort((c1, c2) -> c1.get("courseName").compareTo(c2.get("courseName")));
        expectedDbCourses.sort((c1, c2) -> c1.get("courseName").compareTo(c2.get("courseName")));

        Assert.assertEquals(actualUiCourses.size(), expectedDbCourses.size(),
                "Count of courses in UI (" + actualUiCourses.size() + ") and DB (" + expectedDbCourses.size()
                        + ") mismatch!");

        for (int i = 0; i < actualUiCourses.size(); i++) {
            Map<String, String> actual = actualUiCourses.get(i);
            Map<String, String> expected = expectedDbCourses.get(i);

            Assert.assertEquals(actual.get("courseName"), expected.get("courseName"),
                    "DB vs UI Course name mismatch for " + expected.get("courseName"));
            Assert.assertEquals(actual.get("instructorName"), expected.get("instructorName"),
                    "DB vs UI Instructor name mismatch for " + expected.get("courseName"));
        }
    }
}
