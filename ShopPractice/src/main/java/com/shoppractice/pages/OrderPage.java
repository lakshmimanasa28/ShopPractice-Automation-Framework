package com.shoppractice.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.shoppractice.base.BasePage;

import java.util.List;

public class OrderPage extends BasePage {

    
    private final By ordersLink = By.xpath("//a[contains(text(),'Orders') or contains(@routerlink,'order')]");
    private final By orderRows = By.cssSelector("tbody tr");
    private final By orderIdColumn = By.cssSelector("th[scope='row']");
    private final By productNameInOrder = By.cssSelector("td:nth-child(2)");
    private final By viewButton = By.cssSelector("button.btn-primary");

    public OrderPage(WebDriver driver) {
        super(driver);
    }

    
    public void goToMyOrders() {
        
        By[] possibleLinks = {
            By.xpath("//a[contains(text(),'Orders')]"),
            By.xpath("//a[contains(@routerlink,'order')]"),
            By.xpath("//a[contains(@routerlink,'myorders')]"),
            By.partialLinkText("Orders")
        };

        for (By locator : possibleLinks) {
            try {
                WebElement link = wait.until(ExpectedConditions.elementToBeClickable(locator));
                link.click();
                System.out.println("✅ Clicked Orders link using: " + locator);
                break;
            } catch (Exception e) {
                
            }
        }

        
        wait.until(ExpectedConditions.visibilityOfElementLocated(orderRows));
    }

    public boolean isOrderHistoryDisplayed() {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(orderRows));
            return !driver.findElements(orderRows).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public String getLatestOrderId() {
        goToMyOrders();
        List<WebElement> rows = driver.findElements(orderRows);
        if (rows.isEmpty()) return "No orders found";
        return rows.get(0).findElement(orderIdColumn).getText().trim();
    }

    public void viewFirstOrder() {
        goToMyOrders();
        List<WebElement> rows = driver.findElements(orderRows);
        if (!rows.isEmpty()) {
            WebElement viewBtn = rows.get(0).findElement(viewButton);
            wait.until(ExpectedConditions.elementToBeClickable(viewBtn)).click();
        }
    }

    public boolean verifyProductInOrder(String expectedProduct) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productNameInOrder));
        List<WebElement> products = driver.findElements(productNameInOrder);
        for (WebElement p : products) {
            if (p.getText().trim().equalsIgnoreCase(expectedProduct.trim())) {
                return true;
            }
        }
        return false;
    }
}