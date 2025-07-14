package com.zaidan.testng.utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        String environment = System.getProperty("env", "fix");
        String propertiesFileName = environment + ".properties";

        System.out.println("Loading configuration for environmet: " + environment);

        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(propertiesFileName)){
            if (input == null) {
                System.out.println("Unable to find " + propertiesFileName);
            }
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
