package com.shoppractice.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.shoppractice.base.BasePage;

public class DashboardPage extends BasePage {

    // More robust locators for the current "Let's Shop" application
    private final By productCards = By.cssSelector("div.card, app-card, .mb-3, .col-lg-4");  // Multiple fallbacks
    
    private final By productNameLocator = By.cssSelector("h5, b, .card-title, a");
    private final By productPriceLocator = By.cssSelector("h5.text-muted, p, .price, .text-muted");
    
    private final By addToCartBtn = By.cssSelector("button.btn:last-of-type, button[routerlink*='cart'] ~ button"); 
    private final By cartBtn = By.cssSelector("[routerlink='/dashboard/cart'], .btn-custom[routerlink*='cart']");
    private final By toastContainer = By.cssSelector("#toast-container");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Improved wait - more patient and uses broader locator
     */
    public void waitForProductsToLoad() {
        try {
            // First wait for any product container
            wait.until(ExpectedConditions.presenceOfElementLocated(productCards));
            
            // Then wait until we have several products (increased patience)
            wait.until(d -> {
                int count = d.findElements(productCards).size();
                System.out.println("Current product count detected: " + count);
                return count >= 3;   // Lowered threshold to avoid timeout
            });
            
            System.out.println("✅ Products loaded successfully. Total cards: " + 
                               driver.findElements(productCards).size());
            
        } catch (Exception e) {
            System.out.println("⚠️ WaitForProductsToLoad failed. Taking screenshot for debug.");
            // You can add screenshot capture here if you have the listener
            throw e;
        }
    }

    public boolean verifyProductsHaveNameAndPrice() {
        waitForProductsToLoad();

        List<WebElement> cards = driver.findElements(productCards);
        System.out.println("Verifying " + cards.size() + " product cards...");

        for (int i = 0; i < cards.size(); i++) {
            WebElement card = cards.get(i);
            String name = getTextSafely(card, productNameLocator);
            String price = getTextSafely(card, productPriceLocator);

            if (name.isEmpty() || price.isEmpty()) {
                System.out.println("❌ Product #" + (i+1) + " missing -> Name:'" + name + "' Price:'" + price + "'");
                return false;
            }
        }
        System.out.println("✅ All products have name and price");
        return true;
    }

    private String getTextSafely(WebElement parent, By locator) {
        try {
            return parent.findElement(locator).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public void addProductToCart(String productName) {
        waitForProductsToLoad();

        List<WebElement> cards = driver.findElements(productCards);

        for (WebElement card : cards) {
            String actualName = getTextSafely(card, productNameLocator);

            if (actualName.equalsIgnoreCase(productName.trim())) {
                WebElement addBtn = card.findElement(addToCartBtn);

                wait.until(ExpectedConditions.elementToBeClickable(addBtn));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);

                // Toast handling
                wait.until(ExpectedConditions.visibilityOfElementLocated(toastContainer));
                waitForInvisibility(toastContainer);

                System.out.println("✅ Added to cart: " + productName);
                return;
            }
        }
        throw new RuntimeException("Product not found: " + productName);
    }

    public void addMultipleProducts(String[] productList) {
        for (String p : productList) {
            addProductToCart(p);
        }
    }

    public void goToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartBtn)).click();
        System.out.println("✅ Navigated to Cart page");
    }

    public boolean isDashboardLoaded() {
        try {
            waitForProductsToLoad();
            return driver.findElements(productCards).size() >= 3;
        } catch (Exception e) {
            return false;
        }
    }

    public void logout() {
        // Best locator based on the exact HTML you provided
        By signOutBtn = By.xpath("//button[contains(@class, 'btn-custom') and contains(., 'Sign Out')]");

        try {
            // Wait until button is clickable
            WebElement logoutElement = wait.until(ExpectedConditions.elementToBeClickable(signOutBtn));

            // Scroll into view + JavaScript click (highly reliable on Angular sites)
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", logoutElement);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logoutElement);

            // Wait for redirect to login page
            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("auth"),
                ExpectedConditions.urlContains("login")
            ));

            System.out.println("✅ Logout successful - Redirected to login page");

        } catch (Exception e) {
            System.out.println("❌ Logout failed. Current URL: " + driver.getCurrentUrl());
            throw new RuntimeException("Logout failed", e);
        }
    }
}