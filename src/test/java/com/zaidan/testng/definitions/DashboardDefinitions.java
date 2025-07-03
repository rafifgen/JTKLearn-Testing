package com.zaidan.testng.definitions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.zaidan.testng.dao.PengajarDAO;
import org.testng.Assert;

import com.zaidan.testng.actions.HomePageActions;
import com.zaidan.testng.dao.CourseDAO;
import com.zaidan.testng.dao.UserDAO;
import com.zaidan.testng.locators.HomePageLocators;
import com.zaidan.testng.model.Course;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class DashboardDefinitions {
    HomePageLocators homePage = new HomePageLocators();
    HomePageActions homePageActions = new HomePageActions();
    UserDAO userDAO = new UserDAO();
    CourseDAO courseDAO = new CourseDAO();
    static PengajarDAO pengajarDAO = new PengajarDAO();

    @Then("User should see the welcome message for email {string}")
    public void userShouldSeeTheWelcomeMessage(String email) {
        String expectedUserName = null;
        String actualWelcomeText = null;
        String expectedWelcomeText = null;

        try {
            expectedUserName = userDAO.getPelajarNameByEmail(email);
        } catch (Exception e) {
            Assert.fail("Failed to retrieve student name from database for email " + email + ": " + e.getMessage());
        }

        Assert.assertNotNull(expectedUserName, "Expected student name not found in database for email: " + email);

        actualWelcomeText = homePageActions.getHomePageText();
        expectedWelcomeText = "Hai, " + expectedUserName + "!";

        Assert.assertEquals(actualWelcomeText, expectedWelcomeText,
                "Welcome message mismatch. Expected: '" + expectedWelcomeText + "', Actual: '"
                        + actualWelcomeText + "'");
        System.out.println("Welcome message verified");
    }

    @And("User should see the page title {string}")
    public void userShouldSeeThePageTitle(String expectedPageTitle) {
        String actualPageTitle = homePageActions.getPageTitle();
        Assert.assertEquals(actualPageTitle, expectedPageTitle,
                "Page title mismatch. Expected: '" + expectedPageTitle + "', Actual: '" + actualPageTitle + "'");
        System.out.println("Page title verified: " + actualPageTitle);
    }

    @And("User should see all courses matching with the database records")
    public void userShouldSeeAllCoursesMatchingWithTheDatabaseRecords() {
        // 1. Get all courses from the database (CourseDAO remains the same)
        List<Course> dbCourses = courseDAO.getAllCourses();
        // Uncomment this for debugging.
        // System.out.println("Courses fetched from DB (" + dbCourses.size() + "): "
        //  + dbCourses.stream().map(Course::getNamaCourse).collect(Collectors.joining(", ")));

        // 2. Get all courses displayed on the user interface (HomePageActions remains
        // the same)
        List<Course> uiCourses = homePageActions.getAllDisplayedCourses();
        // Uncomment these 2 lines for debugging.
//        System.out.println("Courses fetched from UI (" + uiCourses.size() + "): "
//                + uiCourses.stream().map(Course::getNamaCourse).collect(Collectors.joining(", ")));

        // Basic sanity checks
        Assert.assertFalse(dbCourses.isEmpty(), "No courses found in the database!");

        Assert.assertEquals(uiCourses.size(), dbCourses.size(),
                "Mismatch in number of courses displayed on UI vs. in DB. UI: " + uiCourses.size() + ", DB: "
                        + dbCourses.size());

        // Convert lists to maps for easier comparison by a unique key (namaCourse)
        Map<String, Course> dbCoursesMap = dbCourses.stream()
                .collect(Collectors.toMap(Course::getNamaCourse, course -> course));

        Map<String, Course> uiCoursesMap = uiCourses.stream()
                .collect(Collectors.toMap(Course::getNamaCourse, course -> course));

        compareIndividualCourses(dbCoursesMap, uiCoursesMap);
    }

    private static void compareIndividualCourses(Map<String, Course> dbCoursesMap, Map<String, Course> uiCoursesMap) {
        // Verify that the set of course names is identical
        Assert.assertTrue(dbCoursesMap.keySet().equals(uiCoursesMap.keySet()),
                "Mismatch in course names between UI and DB. DB Names: " + dbCoursesMap.keySet() + ", UI Names: "
                        + uiCoursesMap.keySet());

        // Compare attributes for each matched course
        for (Map.Entry<String, Course> dbEntry : dbCoursesMap.entrySet()) {
            String courseName = dbEntry.getKey();
            Course dbCourse = dbEntry.getValue();
            Course uiCourse = uiCoursesMap.get(courseName);

            System.out.println("Verifying attributes for course: '" + courseName + "'");

            // Compare course names
            Assert.assertEquals(uiCourse.getNamaCourse(), dbCourse.getNamaCourse(),
                    "Course name mismatch for '" + courseName + "'. UI: '" + uiCourse.getNamaCourse() + "', DB: '"
                            + dbCourse.getNamaCourse() + "'");

            // Compare course images name
            String expectedGambarLink = dbCourse.getGambarCourse();
            String actualGambarLink = uiCourse.getGambarCourse().substring(uiCourse.getGambarCourse().
                    indexOf("images/") + 7);
            Assert.assertEquals(actualGambarLink, expectedGambarLink);

            String expectedPengajar = pengajarDAO.getPengajarNameById(dbCourse.getIdPengajar());
            String actualPengajar = uiCourse.getInstructorDisplayText();
            Assert.assertEquals(actualPengajar, expectedPengajar);
        }
        System.out.println("All courses displayed on UI are successfully matched with database records.");
    }

    @And("User should see the joined courses matching with the database records")
    public void userShouldSeeJoinedCourses() {
        // TODO: Implement this logic
        // 1. Query all courses from database
        // 2. Compare each attribute of each course in the user interface with the
        // actual
        // data from the database
    }
}
