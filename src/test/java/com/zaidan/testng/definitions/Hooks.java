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
//        HelperClass.setUpDriver();
        System.out.println("--- Starting Test Suite Setup ---");
        try {
            // This will now establish the SSH tunnel AND the JDBC connection
            DatabaseUtil.getConnection();
        } catch (Exception e) {
            System.err.println("Failed to open database connection (or SSH tunnel) at suite start: " + e.getMessage());
            throw new RuntimeException("Test suite cannot proceed without database connection.", e);
        }
        System.out.println("--- Test Suite Setup Complete ---");
    }

    @Before
    public static void setUp() {
        HelperClass.setUpDriver();
    }

    @After
    public static void tearDown() {
        HelperClass.tearDown();
    }

    @AfterAll
    public static void tearDownSuite() {
        System.out.println("--- Starting Test Suite Teardown ---");
        // Close both JDBC connection and SSH tunnel
        DatabaseUtil.closeAllConnections();
//        HelperClass.tearDown();
        System.out.println("--- Test Suite Teardown Complete ---");
    }
}