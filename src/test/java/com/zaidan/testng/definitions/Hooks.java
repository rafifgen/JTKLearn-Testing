package com.zaidan.testng.definitions;

import com.zaidan.testng.utils.DatabaseUtil;
import com.zaidan.testng.utils.HelperClass;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;

public class Hooks {
    // These run once before/after the entire test suite (all feature files)
    @BeforeAll
    public static void setupSuite() {
        System.out.println("--- Starting Test Suite Setup ---");
        // Open the database connection for the entire suite
        try {
            DatabaseUtil.getConnection(); // This will open the connection
        } catch (Exception e) {
            System.err.println("Failed to open database connection at suite start: " + e.getMessage());
            // Optionally, throw a runtime exception or mark tests to skip
        }
        System.out.println("--- Test Suite Setup Complete ---");
    }

    @AfterAll
    public static void tearDownSuite() {
        System.out.println("--- Starting Test Suite Teardown ---");
        // Close the database connection after all tests are done
        DatabaseUtil.closeConnection();
        System.out.println("--- Test Suite Teardown Complete ---");
    }

    @Before
    public static void setUp() {
        HelperClass.setUpDriver();
    }

    @After
    public static void tearDown() {

        HelperClass.tearDown();
    }
}
