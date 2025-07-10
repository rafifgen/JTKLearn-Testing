package com.zaidan.testng.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.zaidan.testng.enums.TaskStatus;
import com.zaidan.testng.locators.SummaryProgressLocators;
import com.zaidan.testng.model.StudentProgressUI;
import com.zaidan.testng.utils.HelperClass;

public class SummaryProgressActions {
    SummaryProgressLocators summaryProgressLocators = null;
    WebDriver driver;

    public SummaryProgressActions() {
        this.summaryProgressLocators = new SummaryProgressLocators();
        this.driver = HelperClass.getDriver();
        PageFactory.initElements(HelperClass.getDriver(), summaryProgressLocators);
    }

    public WebElement getPageTitle() {
        return summaryProgressLocators.pageTitle;
    }

    public WebElement getPageSubtitle() {
        return summaryProgressLocators.pageSubtitle;
    }

    public List<StudentProgressUI> getAllStudentProgressFromUI() {
        List<StudentProgressUI> allProgressData = new ArrayList<>();
        
        // Find all rows in the table body
        List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));

        // Get the headers to dynamically find columns (M4, M5, Q3, etc.)
        List<WebElement> headers = driver.findElements(By.xpath("//table/thead/tr/th"));

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() < headers.size()) continue; // Skip malformed rows

            String studentName = cells.get(1).getText(); // Column 2 is Name
            String progressText = cells.get(2).getText().replace(" %", "");
            double progressPercentage = Double.parseDouble(progressText);
            
            Map<String, TaskStatus> taskStatuses = new HashMap<>();
            // Loop from the 4th column onwards to get task statuses
            for (int i = 3; i < headers.size(); i++) {
                String taskHeader = headers.get(i).getText();
                WebElement icon = cells.get(i).findElement(By.tagName("i"));
                String iconClasses = icon.getDomAttribute("class");

                if (iconClasses.contains("text-success")) {
                    taskStatuses.put(taskHeader, TaskStatus.PASSED);
                } else if (iconClasses.contains("text-warning")) {
                    taskStatuses.put(taskHeader, TaskStatus.IN_PROGRESS);
                } else {
                    taskStatuses.put(taskHeader, TaskStatus.NOT_TAKEN);
                }
            }
            allProgressData.add(new StudentProgressUI(studentName, progressPercentage, taskStatuses));
        }
        return allProgressData;
    }

    public List<String> getDisplayedStudentNames() {
        List<String> studentNames = new ArrayList<>();
        
        // --- THIS IS THE FIX ---
        // The XPath now looks for the table with the correct class: 'custom-user-table'
        String xpathForRows = "//table[contains(@class, 'custom-user-table')]/tbody/tr";
        
        List<WebElement> tableRows = driver.findElements(By.xpath(xpathForRows));

        for (WebElement row : tableRows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() > 1) {
                // The name is still in the second cell (index 1)
                studentNames.add(cells.get(1).getText());
            }
        }
        System.out.println("Names found on UI: " + studentNames);
        return studentNames;
    }
}