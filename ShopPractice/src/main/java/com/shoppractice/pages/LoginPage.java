package com.shoppractice.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.shoppractice.base.BasePage;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private final By emailField = By.id("userEmail");
    private final By passwordField = By.id("userPassword");
    private final By loginBtn = By.id("login");
    private final By toastError = By.cssSelector("#toast-container");
    private final By fieldError = By.cssSelector(".ng-star-inserted");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String email, String password) {
        sendKeys(emailField, email);
        sendKeys(passwordField, password);
        click(loginBtn);

        // Wait for success OR any kind of error (including inline validation)
        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("dashboard"),                    // success
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container")),
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ng-star-inserted")),
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".invalid-feedback")), 
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".text-danger")), 
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'danger') or contains(@class,'error')]"))
        ));
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container")),
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ng-star-inserted")),
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".invalid-feedback")),
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".text-danger")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'required') or contains(text(),'Email') or contains(text(),'Password')]"))
            ));
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorText() {
        try {
            if (!driver.findElements(toastError).isEmpty()) {
                return waitForElement(toastError).getText().trim();
            } else if (!driver.findElements(fieldError).isEmpty()) {
                return waitForElement(fieldError).getText().trim();
            }
        } catch (Exception ignored) {}
        return "";
    }
}