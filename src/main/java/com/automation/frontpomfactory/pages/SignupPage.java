package com.automation.frontpomfactory.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.automation.frontpomfactory.utils.AutomationConstants;

import net.serenitybdd.core.annotations.findby.FindBy;

public class SignupPage extends BasePage {

    @FindBy(css = AutomationConstants.FULL_NAME_INPUT)
    private WebElement fullNameField;

    @FindBy(css = AutomationConstants.EMAIL_INPUT)
    private WebElement emailField;

    @FindBy(css = AutomationConstants.PASSWORD_INPUT)
    private WebElement passwordField;

    @FindBy(css = AutomationConstants.REGISTER_BUTTON)
    private WebElement registerButton;

    @FindBy(css = AutomationConstants.FALLBACK_SUBMIT_BUTTON)
    private WebElement submitButton;

    public void navigateToSignup() {
        openUrl(AutomationConstants.SIGNUP_URL);
    }

    public void enterFullName(String fullName) {
        fullNameField.clear();
        fullNameField.sendKeys(fullName);
    }

    public void enterEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickRegisterButton() {
        if (registerButton.isEnabled()) {
            registerButton.click();
        } else {
            submitButton.click();
        }
    }

    public boolean isSuccessMessageDisplayed() {
        return !getDriver().findElements(By.cssSelector(AutomationConstants.SUCCESS_MESSAGE)).isEmpty();
    }

    public String getSuccessMessage() {
        return getDriver().findElements(By.cssSelector(AutomationConstants.SUCCESS_MESSAGE)).stream()
                .findFirst().map(WebElement::getText).orElse("");
    }

    public boolean isErrorMessageDisplayed() {
        return !getDriver().findElements(By.cssSelector(AutomationConstants.ERROR_MESSAGE)).isEmpty();
    }

    public String getErrorMessage() {
        return getDriver().findElements(By.cssSelector(AutomationConstants.ERROR_MESSAGE)).stream()
                .findFirst().map(WebElement::getText).orElse("");
    }

    public boolean isSigninPage() {
        return getDriver().getCurrentUrl().contains(AutomationConstants.SIGNIN_PATH)
                || !getDriver().findElements(By.linkText(AutomationConstants.SIGNIN_LINK_TEXT)).isEmpty();
    }

    public boolean isSignupPage() {
        return getDriver().getCurrentUrl().contains(AutomationConstants.SIGNUP_PATH)
                && !getDriver().findElements(By.cssSelector(AutomationConstants.FULL_NAME_INPUT)).isEmpty();
    }

    public void fillSignupForm(String fullName, String email, String password) {
        enterFullName(fullName);
        enterEmail(email);
        enterPassword(password);
    }

    public void submitSignupForm() {
        clickRegisterButton();
    }
}
