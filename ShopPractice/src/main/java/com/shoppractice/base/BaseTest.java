package com.shoppractice.base;

import com.shoppractice.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        String browser = ConfigReader.getProperty("browser").toLowerCase();
        int timeout = Integer.parseInt(ConfigReader.getProperty("timeout"));

        try {
            if (browser.contains("chrome")) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            } else if (browser.contains("firefox")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            } else {
                System.out.println("⚠️ Unsupported browser: " + browser + ". Defaulting to Chrome.");
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            }

            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            
            
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

            wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));

            String baseUrl = ConfigReader.getProperty("base.url");
            driver.get(baseUrl);

            System.out.println("✅ Browser launched successfully: " + browser);

        } catch (Exception e) {
            System.out.println("❌ Failed to initialize WebDriver: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("✅ Browser closed successfully");
            } catch (Exception e) {
                System.out.println("⚠️ Error while closing browser: " + e.getMessage());
            }
        }
    }

  
    public WebDriver getDriver() {
        return driver;
    }

   
    public WebDriverWait getWait() {
        return wait;
    }
}