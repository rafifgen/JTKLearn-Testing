package com.zaidan.testng.utils;

import  java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class HelperClass {

    private static HelperClass helperClass;
    private static WebDriver driver;
    public final static int TIMEOUT = 10;
    private static Properties props = new Properties();

    private HelperClass() {
        // Setup Edge driver
        WebDriverManager.edgedriver().setup();

        EdgeOptions options = new EdgeOptions();
        driver = new EdgeDriver(options);

        // Remove navigator.webdriver via JS
        ((JavascriptExecutor) driver).executeScript(
            "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
        );

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
        driver.manage().window().maximize();
    }

    public static void openPage(String url) {
        driver.get(url);
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void setUpDriver() {
        if (helperClass == null) {
            helperClass = new HelperClass();
        }
    }

    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        helperClass = null;
    }

    /**
     * Retrieves a property value from the loaded application.properties file.
     * @param key The key of the property to retrieve.
     * @return The String value of the property, or null if the key is not found.
     */
    public static String getProperty(String key) {
        // Ensure properties are loaded before attempting to retrieve
        if (props.isEmpty() && helperClass == null) {
            // Attempt to load properties if not already loaded (e.g., if getProperty is called before setUpDriver)
            setUpDriver(); // This will ensure props are loaded
        }
        return props.getProperty(key);
    }
}
