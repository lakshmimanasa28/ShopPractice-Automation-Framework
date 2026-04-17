package com.shoppractice.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.shoppractice.base.BaseTest;
import com.shoppractice.pages.CartPage;
import com.shoppractice.pages.DashboardPage;
import com.shoppractice.pages.LoginPage;

public class CartTest extends BaseTest {

    private static final String PRODUCT = "ZARA COAT 3";

    @Test(priority = 1)
    public void cartTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("lakshmimanasak@gmail.com", "Automation@26");

        DashboardPage dashboard = new DashboardPage(driver);
        dashboard.addProductToCart(PRODUCT);
        dashboard.goToCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.waitForCartToLoad();

        Assert.assertTrue(cartPage.verifyProductInCart(PRODUCT), "Product not found in cart!");

        cartPage.checkout();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("order") || 
                          currentUrl.contains("checkout") || 
                          currentUrl.contains("payment"),
                "Failed to reach order page. Current URL: " + currentUrl);
    }

    @Test(priority = 2)
    public void checkoutTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("lakshmimanasak@gmail.com", "Automation@26");

        DashboardPage dashboard = new DashboardPage(driver);
        dashboard.addProductToCart(PRODUCT);
        dashboard.goToCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.checkout();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("order") || 
                          currentUrl.contains("checkout") || 
                          currentUrl.contains("payment"),
                "Failed to reach checkout/order page. Current URL: " + currentUrl);
    }

    @Test(priority = 3)
    public void removeItemTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("lakshmimanasak@gmail.com", "Automation@26");

        DashboardPage dashboard = new DashboardPage(driver);
        dashboard.addProductToCart(PRODUCT);
        dashboard.goToCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.removeItem(PRODUCT);
        cartPage.waitForCartToBeEmpty();

        Assert.assertTrue(cartPage.isCartEmpty(), "Item was not removed successfully!");
    }
}