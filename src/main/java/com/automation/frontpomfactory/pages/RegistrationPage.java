package com.automation.frontpomfactory.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.frontpomfactory.utils.AutomationConstants;

import net.serenitybdd.core.annotations.findby.FindBy;

public class RegistrationPage extends BasePage {

    @FindBy(css = AutomationConstants.REG_FULLNAME_INPUT)
    private WebElement fullNameField;

    @FindBy(css = AutomationConstants.REG_IDCARD_INPUT)
    private WebElement idCardField;

    @FindBy(css = AutomationConstants.REG_SUBMIT_BUTTON)
    private WebElement registerButton;

    public void navigateToRegistration() {
        openUrl(AutomationConstants.REGISTRATION_URL);
        waitForFormReady();
    }

    public void waitForFormReady() {
        new WebDriverWait(getDriver(), Duration.ofSeconds(AutomationConstants.WAIT_FOR_MESSAGE_SECONDS))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(AutomationConstants.REG_FORM)));
    }

    public void enterFullName(String name) {
        fullNameField.clear();
        fullNameField.sendKeys(name);
    }

    public void enterIdCard(String idCard) {
        idCardField.clear();
        idCardField.sendKeys(idCard);
    }

    /**
     * Selects priority in the dropdown. Handles both native {@code <select>}
     * elements and custom dropdown components via data-testid.
     *
     * <p>
     * For React-controlled {@code <select>} elements (i.e. {@code value} is a
     * controlled prop), Selenium's {@link Select#selectByValue} alone is not
     * sufficient because React replaces the native property setter and only
     * responds to synthetic events. The fix:
     * <ol>
     * <li>Sets the value via the original {@code HTMLSelectElement.prototype}
     * setter (bypasses React's override).</li>
     * <li>Dispatches a bubbling {@code input} event (React 17+ listens
     * here).</li>
     * <li>Dispatches a bubbling {@code change} event (React 16 and older).</li>
     * <li>Waits until the DOM {@code value} attribute reflects the selection
     * before returning, ensuring the component state is settled.</li>
     * </ol>
     *
     * @param priority "high", "medium" or "low"
     */
    public void selectPriority(String priority) {
        List<WebElement> elements = getDriver().findElements(
                By.cssSelector(AutomationConstants.REG_PRIORITY_SELECT));
        if (elements.isEmpty()) {
            return;
        }
        WebElement element = elements.get(0);
        if ("select".equalsIgnoreCase(element.getTagName())) {
            // Use the native prototype setter so React detects the change,
            // then dispatch both input and change events (bubbling) to update
            // the controlled component state before the form is submitted.
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript(
                    "var setter = Object.getOwnPropertyDescriptor(HTMLSelectElement.prototype, 'value').set;"
                    + "setter.call(arguments[0], arguments[1]);"
                    + "arguments[0].dispatchEvent(new Event('input',  { bubbles: true }));"
                    + "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                    element, priority);
            // Wait until the DOM *property* (not the HTML attribute) reflects the
            // expected selection — React keeps the property in sync, not the attribute.
            new WebDriverWait(getDriver(), Duration.ofSeconds(AutomationConstants.WAIT_FOR_MESSAGE_SECONDS))
                    .until(driver -> Boolean.TRUE.equals(((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].value === arguments[1];", element, priority)));
        } else {
            element.click();
            List<WebElement> options = getDriver().findElements(
                    By.cssSelector("[data-value='" + priority + "'], [value='" + priority + "']"));
            if (!options.isEmpty()) {
                options.get(0).click();
            }
        }
    }

    public void clickRegisterButton() {
        registerButton.click();
    }

    public void registerAppointment(String name, String idCard, String priority) {
        enterFullName(name);
        enterIdCard(idCard);
        selectPriority(priority);
        clickRegisterButton();
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(AutomationConstants.WAIT_FOR_MESSAGE_SECONDS))
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector(AutomationConstants.REG_SUCCESS_MESSAGE)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidationErrorDisplayed() {
        // If a success message is already visible the form was submitted
        // successfully — a reset select is not a validation error.
        if (!getDriver().findElements(
                By.cssSelector(AutomationConstants.REG_SUCCESS_MESSAGE)).isEmpty()) {
            return false;
        }
        // 1. Form-scoped error paragraphs (React validationError / async error)
        if (!getDriver().findElements(
                By.cssSelector(AutomationConstants.REG_VALIDATION_ERROR)).isEmpty()) {
            return true;
        }
        // 2. Native HTML5 browser validation on the required priority select
        List<WebElement> selects = getDriver().findElements(
                By.cssSelector(AutomationConstants.REG_PRIORITY_SELECT));
        if (!selects.isEmpty()) {
            Object invalid = ((JavascriptExecutor) getDriver())
                    .executeScript("return !arguments[0].checkValidity() && arguments[0].value === '';", selects.get(0));
            if (Boolean.TRUE.equals(invalid)) {
                return true;
            }
        }
        return false;
    }

    public String getSuccessMessage() {
        return getDriver().findElements(By.cssSelector(AutomationConstants.REG_SUCCESS_MESSAGE))
                .stream()
                .findFirst()
                .map(WebElement::getText)
                .orElse("");
    }

    /**
     * Waits up to {@link AutomationConstants#WAIT_FOR_MESSAGE_SECONDS} seconds
     * for the async error paragraph ({@code <p class*='error'>}) rendered by
     * {@code AppointmentRegistrationForm} after a failed submit. Does NOT rely
     * on {@code checkValidity} — safe to call after HTTP round-trips.
     */
    public boolean hasErrorMessageDisplayed() {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(AutomationConstants.WAIT_FOR_MESSAGE_SECONDS))
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector(AutomationConstants.REG_ASYNC_ERROR_MESSAGE)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the visible text of the async error paragraph rendered by
     * {@code AppointmentRegistrationForm}, or an empty string if absent.
     */
    public String getErrorMessage() {
        return getDriver().findElements(By.cssSelector(AutomationConstants.REG_ASYNC_ERROR_MESSAGE))
                .stream()
                .findFirst()
                .map(WebElement::getText)
                .orElse("");
    }
}
