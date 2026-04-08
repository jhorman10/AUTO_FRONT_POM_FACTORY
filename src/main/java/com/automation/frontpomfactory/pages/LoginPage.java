package com.automation.frontpomfactory.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.frontpomfactory.utils.AutomationConstants;

import net.serenitybdd.core.annotations.findby.FindBy;

public class LoginPage extends BasePage {

    @FindBy(css = AutomationConstants.LOGIN_EMAIL_INPUT)
    private WebElement emailField;

    @FindBy(css = AutomationConstants.LOGIN_PASSWORD_INPUT)
    private WebElement passwordField;

    @FindBy(css = AutomationConstants.LOGIN_SUBMIT_BUTTON)
    private WebElement loginButton;

    public void navigateToLogin() {
        openUrl(AutomationConstants.LOGIN_URL);
    }

    /**
     * Waits until the login form is visible and the submit button is enabled.
     * Guards against InvalidElementStateException caused by Firebase auth-state
     * verification that initially disables all form controls.
     */
    public void waitForFormReady() {
        WebDriverWait wait = new WebDriverWait(
                getDriver(), Duration.ofMillis(AutomationConstants.DEFAULT_TIMEOUT_MILLIS));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(AutomationConstants.LOGIN_FORM)));
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(AutomationConstants.LOGIN_SUBMIT_BUTTON)));
    }

    public void enterEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLoginButton() {
        loginButton.click();
    }

    public void loginAs(String email, String password) {
        waitForFormReady();
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    public void waitForRedirect(String expectedPath) {
        new WebDriverWait(getDriver(), Duration.ofMillis(AutomationConstants.DEFAULT_TIMEOUT_MILLIS))
                .until(ExpectedConditions.urlContains(expectedPath));
    }
}
