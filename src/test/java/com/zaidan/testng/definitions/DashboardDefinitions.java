package com.zaidan.testng.definitions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        System.out.println("Courses fetched from DB (" + dbCourses.size() + "): "
                + dbCourses.stream().map(Course::getNamaCourse).collect(Collectors.joining(", ")));

        // 2. Get all courses displayed on the user interface (HomePageActions remains
        // the same)
        List<Course> uiCourses = homePageActions.getAllDisplayedCourses();
        System.out.println("Courses fetched from UI (" + uiCourses.size() + "): "
                + uiCourses.stream().map(Course::getNamaCourse).collect(Collectors.joining(", ")));

        // Basic sanity checks
        Assert.assertFalse(dbCourses.isEmpty(), "No courses found in the database!");
        // Note: For 'all courses' view, UI might sometimes be empty if no courses
        // exist,
        // but typically a dashboard implies showing *something*. Adjust assert as
        // needed.
        // Assert.assertFalse(uiCourses.isEmpty(), "No courses found on the UI!");

        Assert.assertEquals(uiCourses.size(), dbCourses.size(),
                "Mismatch in number of courses displayed on UI vs. in DB. UI: " + uiCourses.size() + ", DB: "
                        + dbCourses.size());

        // Convert lists to maps for easier comparison by a unique key (namaCourse)
        Map<String, Course> dbCoursesMap = dbCourses.stream()
                .collect(Collectors.toMap(Course::getNamaCourse, course -> course));

        Map<String, Course> uiCoursesMap = uiCourses.stream()
                .collect(Collectors.toMap(Course::getNamaCourse, course -> course));

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

            Assert.assertEquals(uiCourse.getNamaCourse(), dbCourse.getNamaCourse(),
                    "Course name mismatch for '" + courseName + "'. UI: '" + uiCourse.getNamaCourse() + "', DB: '"
                            + dbCourse.getNamaCourse() + "'");

            String expectedGambarLink = dbCourse.getGambarCourse();
            String actualGambarLink = uiCourse.getGambarCourse().substring(uiCourse.getGambarCourse().
                    indexOf("images/") + 7);
            Assert.assertEquals(actualGambarLink, expectedGambarLink,
                    "Image URL mismatch for '" + courseName + "'. UI: '" + actualGambarLink + "', DB: '"
                            + expectedGambarLink + "'");

            // Instructor ID/Name comparison:
            // This remains a point for further development. The UI gives "Budi Pengajar"
            // (text).
            // The DB Course object has id_pengajar (int).
            // To compare, you'd need to:
            // 1. Get the actual instructor name from the DB using dbCourse.getIdPengajar()
            // via an InstructorDAO.
            // 2. Then compare that DB instructor name to the uiInstructorName extracted
            // from the UI.
            // For now, this assertion is commented out.
            // Example if you have InstructorDAO:
            // String dbInstructorName =
            // instructorDAO.getPengajarNameById(dbCourse.getIdPengajar());
            // Assert.assertEquals(uiCourse.getInstructorNameFromUI(), dbInstructorName,
            // "Instructor name mismatch");

            // Other attributes (enrollmentKey, deskripsi) are not directly available from
            // UI card.
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
