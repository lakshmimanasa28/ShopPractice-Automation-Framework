package com.shoppractice.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.shoppractice.base.BaseTest;
import com.shoppractice.pages.CartPage;
import com.shoppractice.pages.DashboardPage;
import com.shoppractice.pages.LoginPage;

public class ProductTest extends BaseTest {

    private static final String VALID_EMAIL = "lakshmimanasak@gmail.com";
    private static final String VALID_PASSWORD = "Automation@26";

    @Test
    public void addToCartTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(VALID_EMAIL, VALID_PASSWORD);

        DashboardPage dashboard = new DashboardPage(driver);
        String product = "ZARA COAT 3";

        dashboard.addProductToCart(product);
        dashboard.goToCart();

        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.verifyProductInCart(product), 
                "Product '" + product + "' not found in cart!");
        
        System.out.println("✅ addToCartTest passed");
    }

    @Test
    public void productListingTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(VALID_EMAIL, VALID_PASSWORD);

        DashboardPage dashboard = new DashboardPage(driver);
        
        dashboard.waitForProductsToLoad();
        boolean result = dashboard.verifyProductsHaveNameAndPrice();

        Assert.assertTrue(result, "Some products are missing name or price on the dashboard");
        
        System.out.println("✅ productListingTest passed");
    }

    @Test
    public void multipleProductTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(VALID_EMAIL, VALID_PASSWORD);

        DashboardPage dashboard = new DashboardPage(driver);
        
        String[] products = {"ZARA COAT 3", "IPHONE 13 PRO"};

        dashboard.addMultipleProducts(products);
        dashboard.goToCart();

        CartPage cartPage = new CartPage(driver);
        
        Assert.assertTrue(cartPage.verifyProductInCart("ZARA COAT 3"), 
                "ZARA COAT 3 not found in cart after adding multiple products");
        Assert.assertTrue(cartPage.verifyProductInCart("IPHONE 13 PRO"), 
                "IPHONE 13 PRO not found in cart after adding multiple products");

        System.out.println("✅ multipleProductTest passed");
    }
}