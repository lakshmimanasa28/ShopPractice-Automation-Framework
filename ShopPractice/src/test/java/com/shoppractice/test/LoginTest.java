package com.shoppractice.test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.shoppractice.base.BaseTest;
import com.shoppractice.pages.DashboardPage;
import com.shoppractice.pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test(dataProvider = "loginData")
    public void loginTest(String email, String password, String expectedResult) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(email, password);

        if ("valid".equalsIgnoreCase(expectedResult)) {
            // Valid login - verify dashboard loads successfully
            DashboardPage dashboard = new DashboardPage(driver);
            dashboard.waitForProductsToLoad();                    // Just wait (void method)

            boolean isDashboardLoaded = dashboard.isDashboardLoaded();

            Assert.assertTrue(isDashboardLoaded,
                    "❌ Login failed for valid credentials! Dashboard did not load properly.");

            System.out.println("✅ Valid login successful with: " + email);

        } else {
            // Invalid/Empty login - verify error message is displayed
            boolean errorShown = loginPage.isErrorMessageDisplayed();
            System.out.println("Invalid/Empty login - Error displayed: " + errorShown);

            Assert.assertTrue(errorShown,
                    "❌ Expected validation error not shown for invalid/empty credentials");
        }
    }

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][] {
            {"lakshmimanasak@gmail.com", "Automation@26", "valid"},
            {"wrong@gmail.com", "Pass@2026", "invalid"},
            {"", "", "invalid"}
        };
    }

    @Test
    public void logoutTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("lakshmimanasak@gmail.com", "Automation@26");

        DashboardPage dashboard = new DashboardPage(driver);
        
        // Wait and verify dashboard is loaded
        dashboard.waitForProductsToLoad();
        
        Assert.assertTrue(dashboard.isDashboardLoaded(),
                "❌ Dashboard failed to load after login");

        dashboard.logout();

        // Verify redirect to login/auth page
        boolean redirected = driver.getCurrentUrl().contains("auth") ||
                             driver.getCurrentUrl().contains("login");

        Assert.assertTrue(redirected,
                "❌ Logout failed! Current URL: " + driver.getCurrentUrl());

        System.out.println("✅ Logout successful - Redirected to login page");
    }
}