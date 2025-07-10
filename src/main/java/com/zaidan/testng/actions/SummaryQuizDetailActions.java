package com.zaidan.testng.actions;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.zaidan.testng.locators.SummaryQuizDetailLocators;
import com.zaidan.testng.utils.HelperClass;

public class SummaryQuizDetailActions {
    WebDriver driver;
    SummaryQuizDetailLocators summaryQuizDetailLocators;

    public SummaryQuizDetailActions() {
        this.driver = HelperClass.getDriver();
        this.summaryQuizDetailLocators = new SummaryQuizDetailLocators();
        PageFactory.initElements(driver, summaryQuizDetailLocators);
    }

    public List<String> getDisplayedStudentNames() {
        List<String> studentNames = new ArrayList<>();
        
        // Find all rows in the results table body
        List<WebElement> tableRows = driver.findElements(By.xpath("//table[contains(@class, 'custom-result-table')]/tbody/tr"));

        // Loop through each row to get the name from the second column
        for (WebElement row : tableRows) {
            // Find all cells in the current row
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() > 1) { // Ensure the row has at least two columns
                // The name is in the second cell (index 1)
                studentNames.add(cells.get(1).getText());
            }
        }
        System.out.println("Names found on UI: " + studentNames);
        return studentNames;
    }

    public void selectSortOptionByVisibleText(String optionText) {
        // 1. Find the dropdown WebElement
        WebElement dropdownElement = summaryQuizDetailLocators.sortDropdown;

        // 2. Create a new Select object
        Select sortSelect = new Select(dropdownElement);

        // 3. Select the option by its visible text
        sortSelect.selectByVisibleText(optionText);
        
        System.out.println("Selected dropdown option: " + optionText);
    }
}