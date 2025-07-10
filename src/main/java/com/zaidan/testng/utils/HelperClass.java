package com.zaidan.testng.utils;

import java.io.InputStream;
import  java.time.Duration;
import java.util.Properties;

import com.zaidan.testng.locators.MyCourseLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HelperClass {

    private static HelperClass helperClass;
    private static WebDriver driver;
    public final static int TIMEOUT = 30;
    private static Properties props = new Properties();
    private static int loggedInUserId;

    private HelperClass() {
        try (InputStream input = HelperClass.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.err.println("HelperClass: ERROR! application.properties not found at " +
                        HelperClass.class.getClassLoader().getResource("application.properties"));
                throw new RuntimeException("application.properties not found! Ensure it's in src/main/resources.");
            }
            props.load(input);
            System.out.println("HelperClass: Properties loaded successfully from application.properties.");

            // Optional: Print loaded properties for debugging (be careful with sensitive data)
//            System.out.println("--- All loaded properties ---");
//            props.forEach((key, value) -> System.out.println(key + " = " + value));
//            System.out.println("-----------------------------");

        } catch (Exception ex) {
            System.err.println("HelperClass: FATAL ERROR loading application.properties: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException("Failed to load application.properties", ex);
        }
        // Setup Edge driver
        WebDriverManager.edgedriver().setup();

        EdgeOptions options = new EdgeOptions();
        driver = new EdgeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
        // Remove navigator.webdriver via JS
        ((JavascriptExecutor) driver).executeScript(
            "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
        );

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
        driver.manage().window().maximize();
    }

    public static void openPage(String url) {
        driver.get(url);

        // Tunggu hingga halaman sepenuhnya dimuat
        new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT)).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete")
        );

        // Tunggu tambahan untuk framework JavaScript
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(d -> {
            try {
                return (Boolean) ((JavascriptExecutor) d)
                        .executeScript("return window.jQuery !== undefined ? jQuery.active === 0 : true");
            } catch (Exception e) {
                return true;
            }
        });
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

    public static void setLoggedInUserId(int id) {
        loggedInUserId = id;
    }
    public static int getLoggedInUserId() {
        return loggedInUserId;
    }

    /**
     * Ambil nama user yang tampil di navbar.
     */
    public static String getLoggedInUserName() {
        WebDriver driver = getDriver();
        return driver.findElement(MyCourseLocators.LOGGED_IN_USERNAME)
                .getText()
                .trim();
    }
}
