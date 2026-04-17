package com.shoppractice.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.shoppractice.base.BaseTest;
import com.shoppractice.utils.ScreenshotUtil;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.util.Objects;

public class ExtentTestNGListener implements ITestListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReport.html");
        spark.config().setDocumentTitle("ShopPractice Automation Report");
        spark.config().setReportName("GUVI Hackathon - Selenium Framework");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Browser", "Chrome");
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
        test.get().log(Status.INFO, "Test Started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test Passed");

       
        try {
            Object testInstance = result.getInstance();
            if (testInstance instanceof BaseTest) {
                WebDriver driver = ((BaseTest) testInstance).getDriver();
                if (driver != null) {
                    String screenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getName() + "_PASS");
                    if (screenshotPath != null) {
                        test.get().addScreenCaptureFromPath(screenshotPath);
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, "Test Failed: " + result.getThrowable());

        // Capture screenshot and attach to report
        try {
            Object testInstance = result.getInstance();
            if (testInstance instanceof BaseTest) {
                WebDriver driver = ((BaseTest) testInstance).getDriver();   // You need getDriver() in BaseTest

                if (driver != null) {
                    String screenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getName());
                    if (screenshotPath != null) {
                        test.get().addScreenCaptureFromPath(screenshotPath);
                    }
                }
            }
        } catch (Exception e) {
            test.get().log(Status.WARNING, "Failed to attach screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().log(Status.SKIP, "Test Skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
        System.out.println("✅ Extent Report generated: test-output/ExtentReport.html");
    }

    
    public static ExtentTest getTest() {
        return test.get();
    }
}