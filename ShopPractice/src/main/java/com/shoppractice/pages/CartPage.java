package com.shoppractice.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.shoppractice.base.BasePage;

import java.util.List;

public class CartPage extends BasePage {

    private final By cartItems = By.cssSelector(".cartWrap");
    private final By productNameInCart = By.cssSelector("h3");
    private final By deleteBtn = By.cssSelector(".btn-danger");
    private final By checkoutBtn = By.xpath("//button[contains(text(),'Checkout') or contains(text(),'checkout') or @routerlink='/dashboard/checkout']");
    private final By placeOrderBtn = By.xpath("//button[contains(text(),'Place Order') or contains(@class,'btn-success')]"); // for later step

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void waitForCartToLoad() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cartItems));
    }

    public boolean verifyProductInCart(String productName) {
        waitForCartToLoad();
        List<WebElement> items = driver.findElements(cartItems);
        for (WebElement item : items) {
            try {
                String name = item.findElement(productNameInCart).getText().trim();
                if (name.equalsIgnoreCase(productName.trim())) {
                    return true;
                }
            } catch (Exception ignored) {}
        }
        return false;
    }

    public int getCartItemsCount() {
        waitForCartToLoad();
        return driver.findElements(cartItems).size();
    }

    public void removeItem(String productName) {
        waitForCartToLoad();
        List<WebElement> items = driver.findElements(cartItems);

        for (WebElement item : items) {
            try {
                String name = item.findElement(productNameInCart).getText().trim();
                if (name.equalsIgnoreCase(productName.trim())) {
                    WebElement delBtn = item.findElement(deleteBtn);
                    wait.until(ExpectedConditions.elementToBeClickable(delBtn)).click();
                    waitForInvisibility(By.cssSelector("#toast-container"));
                    return;
                }
            } catch (Exception ignored) {}
        }
        throw new RuntimeException("Product '" + productName + "' not found in cart");
    }

    public void removeItem() {   
        waitForCartToLoad();
        WebElement delBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".cartWrap .btn-danger")));
        delBtn.click();
        waitForInvisibility(By.cssSelector("#toast-container"));
    }

  
    
    public void checkout() {
        waitForCartToLoad();
        WebElement chkBtn = wait.until(ExpectedConditions.elementToBeClickable(checkoutBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", chkBtn);

        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("order"),
            ExpectedConditions.urlContains("checkout"),
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(),'Place Order')]"))
        ));

        System.out.println("✅ Navigated to: " + driver.getCurrentUrl());
    }
    public boolean isCartEmpty() {
        return driver.findElements(cartItems).isEmpty();
    }

    public void waitForCartToBeEmpty() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(cartItems));
    }
}