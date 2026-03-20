package com.automation.frontpomfactory.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import net.serenitybdd.core.annotations.findby.FindBy;

public class SignupPage extends BasePage {

    @FindBy(css = "input[placeholder='Nombre']")
    private WebElement fullNameField;

    @FindBy(css = "input[placeholder='Email']")
    private WebElement emailField;

    @FindBy(css = "input[placeholder='Contraseña']")
    private WebElement passwordField;

    @FindBy(css = "button[type='submit']")
    private WebElement registerButton;

    @FindBy(css = ".SignUpForm-module__NmlLka__input")
    private WebElement anyInputField;

    @FindBy(css = ".SignUpForm-module__NmlLka__button")
    private WebElement submitButton;

    public void navigateToSignup() {
        openUrl("http://localhost:3001/signup");
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
        return !getDriver().findElements(By.cssSelector(".success-message, .alert-success")).isEmpty();
    }

    public String getSuccessMessage() {
        return getDriver().findElements(By.cssSelector(".success-message, .alert-success")).stream()
                .findFirst().map(WebElement::getText).orElse("");
    }

    public boolean isErrorMessageDisplayed() {
        return !getDriver().findElements(By.cssSelector(".error-message, .alert-danger, .error")).isEmpty();
    }

    public String getErrorMessage() {
        return getDriver().findElements(By.cssSelector(".error-message, .alert-danger, .error")).stream()
                .findFirst().map(WebElement::getText).orElse("");
    }

    public boolean isSigninPage() {
        return getDriver().getCurrentUrl().contains("/signin")
                || !getDriver().findElements(By.linkText("Iniciar sesión")).isEmpty();
    }

    public boolean isSignupPage() {
        return getDriver().getCurrentUrl().contains("/signup")
                && !getDriver().findElements(By.cssSelector("input[placeholder='Nombre']")).isEmpty();
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
