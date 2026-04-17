package com.shoppractice.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("❌ config.properties not found! Please place it in src/test/resources/");
            }

            properties.load(input);
            System.out.println("✅ config.properties loaded successfully. Browser = " + 
                               properties.getProperty("browser", "chrome"));

        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to load config.properties", e);
        }
    }

    /**
     * Get any property from config.properties
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Property '" + key + "' not found in config.properties");
        }
        return value.trim();
    }

    
    public static String getBrowser() {
        return getProperty("browser").toLowerCase();
    }

    public static String getBaseUrl() {
        return getProperty("base.url");        
    }

    public static int getTimeout() {
        return Integer.parseInt(getProperty("timeout"));
    }

    
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}