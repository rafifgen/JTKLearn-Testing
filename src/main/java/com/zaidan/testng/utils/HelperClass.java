// In: utils/HelperClass.java
package com.zaidan.testng.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import java.time.Duration;

public class HelperClass {

    private static WebDriver driver;
    public final static int TIMEOUT = 30;

    public static void setUpDriver() {
        if (driver == null) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            driver = new EdgeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
            driver.manage().window().maximize();
            
            // Get the application URL from our new ConfigReader
            String url = ConfigReader.getProperty("app.url");
            System.out.println("Navigating to URL: " + url);
            driver.get(url);
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}