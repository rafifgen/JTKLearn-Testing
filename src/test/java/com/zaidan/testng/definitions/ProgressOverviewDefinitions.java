package com.zaidan.testng.definitions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.zaidan.testng.actions.HomePageActions;
import com.zaidan.testng.actions.SummaryCourseActions;
import com.zaidan.testng.actions.SummaryProgressActions;
import com.zaidan.testng.actions.SummaryQuizActions;
import com.zaidan.testng.actions.SummaryQuizDetailActions;
import com.zaidan.testng.dao.CourseDAO;
import com.zaidan.testng.dao.HistoryMateriDAO;
import com.zaidan.testng.dao.HistoryQuizDAO;
import com.zaidan.testng.dao.PelajarDAO;
import com.zaidan.testng.dao.QuizDAO;
import com.zaidan.testng.enums.TaskStatus;
import com.zaidan.testng.model.Pelajar;
import com.zaidan.testng.model.StudentProgressUI;
import com.zaidan.testng.utils.HelperClass;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class ProgressOverviewDefinitions {
    HomePageActions homePageActions = new HomePageActions();
    List<Integer> discoveredMaterialIds = new ArrayList<Integer>();
    List<Integer> discoveredQuizIds = new ArrayList<Integer>();
    List<Integer> discoveredStudentIds = new ArrayList<Integer>();
    List<String> discoveredTaskHeaders = new ArrayList<String>();
    SummaryCourseActions summaryCourseActions = new SummaryCourseActions();
    SummaryProgressActions summaryProgressActions = new SummaryProgressActions();
    SummaryQuizActions summaryQuizActions = new SummaryQuizActions();
    SummaryQuizDetailActions summaryQuizDetailActions = new SummaryQuizDetailActions();
    HistoryMateriDAO historyMateriDAO = new HistoryMateriDAO();
    CourseDAO courseDAO = new CourseDAO();
    HistoryQuizDAO historyQuizDAO = new HistoryQuizDAO();
    PelajarDAO pelajarDAO = new PelajarDAO();
    QuizDAO quizDAO = new QuizDAO();
    int idMateri9 = 9;
    int idMateri10 = 10;

    @And("User clicks on Pemantauan")
    public void userClicksOnProgressPage() {
        homePageActions.clickOnPemantauan();
    }

    @And("User clicks on {string} button of {string} course")
    public void userClicksOnProgresBtn(String buttonName, String courseName) throws SQLException {
        // summaryCourseActions.clickOnKomputerGrafikProgresBtn();
        summaryCourseActions.clickCourseActionButton(courseName, buttonName);
        if (buttonName.equals("Progres")) {
            setupAndDiscoverData();
        }
    }

    public void setupAndDiscoverData() throws SQLException {

        // 2. Scrape the UI
        List<StudentProgressUI> uiProgressList = summaryProgressActions.getAllStudentProgressFromUI();
        Assert.assertFalse(uiProgressList.isEmpty(), "Could not find any students on the overview page.");

        List<WebElement> headerElements = HelperClass.getDriver().findElements(By.xpath("//table[contains(@class, 'custom-user-table')]/thead/tr/th"));
        // We start at index 3 to skip the "No", "Nama Pelajar", and "Progres" columns
        for (int i = 3; i < headerElements.size(); i++) {
            this.discoveredTaskHeaders.add(headerElements.get(i).getText());
        }

        // 3. Discover and Store Student IDs
        for (StudentProgressUI student : uiProgressList) {
            int id = pelajarDAO.getIdByName(student.getStudentName());
            if (id != -1) {
                discoveredStudentIds.add(id);
            }
        }

        // 4. Discover and Store Task IDs from the first student's data
        if (!uiProgressList.isEmpty()) {
            StudentProgressUI firstStudent = uiProgressList.get(0);
            for (String taskName : firstStudent.getTaskStatuses().keySet()) {
                if (taskName.startsWith("M")) {
                    int id = Integer.parseInt(taskName.substring(1));
                    discoveredMaterialIds.add(id);
                    // taskNameToIdMap.put(taskName, id);
                } else if (taskName.startsWith("Q")) {
                    int id = Integer.parseInt(taskName.substring(1));
                    discoveredQuizIds.add(id);
                    // taskNameToIdMap.put(taskName, id);
                }
            }
        }
        System.out.println("Discovered Student IDs: " + discoveredStudentIds);
        System.out.println("Discovered Material IDs: " + discoveredMaterialIds);
        System.out.println("Discovered Quiz IDs: " + discoveredQuizIds);
    }

    @And("Student tasks finish status should be set like these:")
    public void setStudentTasksFinishStatus(DataTable dataTable) throws SQLException {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {
            // --- Get data from the Gherkin table row ---
            int studentPos = Integer.parseInt(row.get("student_position")) - 1; // -1 for 0-based index
            int taskPos = Integer.parseInt(row.get("task_position")) - 1;
            String status = row.get("status");
            float score = Float.parseFloat(row.get("score"));

            // --- Get the dynamically discovered IDs and names from instance variables ---
            int studentId = this.discoveredStudentIds.get(studentPos);
            String taskName = this.discoveredTaskHeaders.get(taskPos); // e.g., "M9" or "Q7"
            
            System.out.println("Setting status for student " + studentId + " on task " + taskName + " to " + status);

            // --- Logic to call the correct DAO method based on task type and status ---
            if (taskName.startsWith("M")) {
                // It's a Material
                int materialId = Integer.parseInt(taskName.substring(1));
                
                switch (status) {
                    case "FINISHED":
                        // Assuming a default duration of 5 minutes to be considered "finished"
                        historyMateriDAO.setFinishTimeAfterDuration(studentId, materialId, 5);
                        break;
                    case "IN_PROGRESS":
                        historyMateriDAO.setMaterialInProgress(studentId, materialId);
                        break;
                    case "NOT_STARTED":
                        historyMateriDAO.resetMateriProgressByStudentAndMateriId(studentId, materialId);
                        break;
                }
            } else if (taskName.startsWith("Q")) {
                // It's a Quiz
                int quizId = Integer.parseInt(taskName.substring(1));

                if ("FINISHED".equals(status)) {
                    // Uses the score from the data table
                    historyQuizDAO.setQuizFinished(studentId, quizId, score);
                } else { // "NOT_TAKEN" status
                    historyQuizDAO.resetQuizProgressByStudentAndQuizId(studentId, quizId);
                }
            }
        }
    }

    @And("Student progress of {string} course should be set like these:")
    public void setStudentProgressForCourse(String courseName, DataTable dataTable) throws SQLException {
        // First, find the ID of the course from its name
        int courseId = courseDAO.getCourseByName(courseName).getIdCourse();
        Assert.assertNotEquals(courseId, -1, "Could not find course with name: " + courseName);

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {
            int studentPos = Integer.parseInt(row.get("student_position")) - 1; // -1 for 0-based index
            float percentage = Float.parseFloat(row.get("percentage"));

            // Get the student ID that was discovered in the @Before hook
            int studentId = this.discoveredStudentIds.get(studentPos);
            
            System.out.println("Setting progress for student " + studentId + " in course " + courseId + " to " + percentage + "%");
            
            // Call the DAO to set the overall course progress
            courseDAO.setCourseProgressByStudentAndCourseId(studentId, courseId, percentage);
        }
        HelperClass.getDriver().navigate().refresh();
    }

    @Then("The page title should be {string}")
    public void verifyPageTitle(String expectedPageSubtitle) {
        String actualPageSubtitle = summaryProgressActions.getPageTitle().getText();
        Assert.assertEquals(actualPageSubtitle, expectedPageSubtitle);
    }

    @And("The page subtitle should be the same with {string} course")
    public void verifyPageSubtitle(String courseName) throws SQLException {
        String expectedPageTitle = courseDAO.getCourseByName(courseName).getNamaCourse();
        String actualPageTitle = summaryProgressActions.getPageSubtitle().getText();
        Assert.assertEquals(actualPageTitle, expectedPageTitle);
    }

    @And("The quiz results for {string} in course {string} are set as follows:")
    public void setQuizResultsForCourse(String quizName, String courseName, DataTable dataTable) throws SQLException {
        // Step 1: Discover the IDs for the course and quiz from their names
        int courseId = courseDAO.getCourseByName(courseName).getIdCourse();
        int quizId = quizDAO.getIdByName(quizName); // Assuming you have a QuizDAO with this method

        // Step 2: Discover all students enrolled in this course
        List<Pelajar> enrolledStudents = pelajarDAO.getEnrolledStudentsByCourseId(courseId);
        
        // Step 3: Parse the Data Table
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // Step 4: Loop through the Data Table to set the score for each specified student
        for (Map<String, String> row : rows) {
            int studentPosition = Integer.parseInt(row.get("student_position")) - 1; // -1 for 0-based index
            float score = Float.parseFloat(row.get("score"));

            // Get the student from the discovered list based on their position
            if (studentPosition < enrolledStudents.size()) {
                Pelajar student = enrolledStudents.get(studentPosition);
                int studentId = student.getIdPelajar();

                // Use the discovered IDs to set the quiz result
                historyQuizDAO.setQuizFinished(studentId, quizId, score);
            } else {
                System.out.println("WARNING: student_position " + (studentPosition + 1) + " is out of bounds. Only " + enrolledStudents.size() + " students are enrolled.");
            }
        }
    }

    @And("The study progress of every student should be displayed")
    public void verifyDisplayedStudentProgressMatchesDatabase() throws SQLException {
        // Step 1: Scrape all visible data from the UI table
        List<StudentProgressUI> uiProgressList = summaryProgressActions.getAllStudentProgressFromUI();
        Assert.assertFalse(uiProgressList.isEmpty(), "No student progress data was found on the page.");
        int courseId = courseDAO.getCourseByName("Komputer Grafik").getIdCourse();

        // Step 2: Loop through each student found on the UI
        for (StudentProgressUI uiProgress : uiProgressList) {
            String studentName = uiProgress.getStudentName();
            System.out.println("--- Verifying data for student: " + studentName + " ---");

            int studentId = pelajarDAO.getIdByName(studentName);
            Assert.assertNotEquals(studentId, -1, "Could not find ID for student: " + studentName);

            float dbProgress = courseDAO.getCourseProgressByStudentAndCourseId(studentId, courseId); // Assuming course ID 4
            Assert.assertEquals(uiProgress.getProgressPercentage(), dbProgress, 0.1,
                "Overall progress mismatch for student: " + studentName);

            // Step 3: Loop through each task found for that student
            for (Map.Entry<String, TaskStatus> uiTaskEntry : uiProgress.getTaskStatuses().entrySet()) {
                String taskName = uiTaskEntry.getKey(); // This will be "M10", "M9", "Q7", etc.
                TaskStatus uiStatus = uiTaskEntry.getValue();
                TaskStatus dbStatus;

                // Step 4: Check if the task is a Material or a Quiz and parse its ID
                if (taskName.startsWith("M")) {
                    // For "M9", this gets "9" and converts it to an integer
                    int materialId = Integer.parseInt(taskName.substring(1));
                    
                    // You can add logic here if different materials have different required times
                    int requiredDuration = 5; // Defaulting to 5 minutes
                    dbStatus = historyMateriDAO.getDBMaterialStatus(studentId, materialId, requiredDuration);
                } else if (taskName.startsWith("Q")) {
                    // For "Q7", this gets "7" and converts it to an integer
                    int quizId = Integer.parseInt(taskName.substring(1));
                    int passingScore = 80; // Default passing score
                    dbStatus = historyQuizDAO.getDBQuizStatus(studentId, quizId, passingScore);
                } else {
                    System.out.println("WARNING: Unknown task header '" + taskName + "' found on UI. Skipping.");
                    continue; // Skip to the next task
                }

                // Step 5: Assert that the UI status matches the DB status for that specific task
                Assert.assertEquals(uiStatus, dbStatus,
                    "Task '" + taskName + "' status mismatch for student: " + studentName);
            }
        }
    }

    @And("User clicks on Lihat Hasil button of Tes Progres quiz")
    public void userClicksOnLihatHasil() {
        summaryQuizActions.clickOnLihatHasilBtn();
    }

    @And("User clicks on {string} sorting button")
    public void userClicksOnSortingButton(String sorting) {
        HelperClass.getDriver().navigate().refresh();
        if (sorting.equals("ascending")) {
            summaryQuizDetailActions.selectSortOptionByVisibleText("A-Z");
        } else {
            summaryQuizDetailActions.selectSortOptionByVisibleText("Z-A");
        }
    }

    @Then("The displayed names should be sorted in {string} order")
    public void verifyDisplayedNames(String sorting) {
        if (sorting.equals("ascending")) {
            verifyAscendingSorting();
        } else {
            verifyDescendingSorting();
        }
    }

    @Then("The displayed names on Pemantau Progres Belajar page should be sorted in descending order")
    public void verifyPemantauProgresDescendingSorting() {
        // 1. Get the list of names from the UI
        List<String> uiNames = summaryProgressActions.getDisplayedStudentNames();
        Assert.assertFalse(uiNames.isEmpty(), "No student names were found on the page.");

        // 2. Create a copy and sort it in reverse alphabetical order (Z-A)
        List<String> sortedNames = new ArrayList<>(uiNames);
        sortedNames.sort(String.CASE_INSENSITIVE_ORDER.reversed());

        System.out.println("Original UI Order: " + uiNames);
        System.out.println("Expected Sorted Order (Desc): " + sortedNames);

        // 3. Assert that the original list matches the reverse-sorted copy
        Assert.assertEquals(uiNames, sortedNames, "The student names are not sorted in descending order.");
    }

    public void verifyAscendingSorting() {
        // 1. Get the list of names from the UI
        List<String> uiNames = summaryQuizDetailActions.getDisplayedStudentNames();
        Assert.assertFalse(uiNames.isEmpty(), "No student names were found on the page.");

        // 2. Create a copy and sort it alphabetically, ignoring case
        List<String> sortedNames = new ArrayList<>(uiNames);
        // --- THIS IS THE FIX ---
        sortedNames.sort(String.CASE_INSENSITIVE_ORDER);

        System.out.println("Original UI Order: " + uiNames);
        System.out.println("Expected Sorted Order: " + sortedNames);

        // 3. Assert that the lists are identical
        Assert.assertEquals(uiNames, sortedNames, "The student names are not sorted case-insensitively (A-Z).");
    }


    public void verifyDescendingSorting() {
        // 1. Get the list of names from the UI
        List<String> uiNames = summaryQuizDetailActions.getDisplayedStudentNames();
        Assert.assertFalse(uiNames.isEmpty(), "No student names were found on the page.");

        // 2. Create a copy and sort it in reverse alphabetical order, ignoring case
        List<String> sortedNames = new ArrayList<>(uiNames);
        // --- THIS IS THE FIX ---
        sortedNames.sort(String.CASE_INSENSITIVE_ORDER.reversed());

        System.out.println("Original UI Order: " + uiNames);
        System.out.println("Expected Sorted Order (Desc): " + sortedNames);

        // 3. Assert that the lists are identical
        Assert.assertEquals(uiNames, sortedNames, "The student names are not sorted case-insensitively (Z-A).");
    }
}