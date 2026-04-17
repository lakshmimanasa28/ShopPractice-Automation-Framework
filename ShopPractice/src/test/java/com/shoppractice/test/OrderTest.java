package com.shoppractice.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.shoppractice.base.BaseTest;
import com.shoppractice.pages.CartPage;
import com.shoppractice.pages.DashboardPage;
import com.shoppractice.pages.LoginPage;
import com.shoppractice.pages.OrderPage;

public class OrderTest extends BaseTest {

    private static final String PRODUCT = "ZARA COAT 3";

    @Test(description = "Verify Order History and Order Details")
    public void verifyOrderHistory() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("lakshmimanasak@gmail.com", "Automation@26");

      
        DashboardPage dashboard = new DashboardPage(driver);
        dashboard.addProductToCart(PRODUCT);
        dashboard.goToCart();

       
        CartPage cartPage = new CartPage(driver);
        cartPage.checkout();

       
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("order"), 
                "Did not reach order confirmation page. Current URL: " + currentUrl);

        System.out.println("✅ Reached Order Confirmation page successfully!");

        
    }
}