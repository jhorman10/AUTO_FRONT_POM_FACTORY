package com.automation.frontpomfactory.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.frontpomfactory.utils.AutomationConstants;

public class WaitingRoomPage extends BasePage {

    public void navigateToWaitingRoom() {
        openUrl(AutomationConstants.WAITING_ROOM_URL);
    }

    // -------------------------------------------------------------------------
    // Section resolution by heading text
    // -------------------------------------------------------------------------
    /**
     * Finds the innermost section/div/aside ancestor whose subtree contains a
     * heading (h1–h4) with {@code headingText}, then returns all {@code li}
     * elements within that container that do NOT carry a skeleton class.
     *
     * <p>
     * XPath rationale:
     * {@code ancestor::*[self::section or self::div or self::aside][1]} selects
     * the closest block-level container regardless of element type — necessary
     * because the "En espera" column is rendered inside an {@code <aside>}
     * (rightPanel) whereas "En consultorio" uses a {@code <section>}
     * (sectionBlock). No {@code [.//li]} predicate is used so that the
     * container is resolved even in empty state (only a {@code <p>} present).
     */
    private List<WebElement> getItemsInSection(String headingText) {
        String headingXpath = String.format(
                "//*[self::h1 or self::h2 or self::h3 or self::h4]"
                + "[contains(normalize-space(.),'%s')]",
                headingText);
        List<WebElement> headings = getDriver().findElements(By.xpath(headingXpath));
        if (headings.isEmpty()) {
            return List.of();
        }
        try {
            WebElement container = headings.get(0).findElement(
                    By.xpath("./ancestor::*[self::section or self::div or self::aside][1]"));
            return container.findElements(
                    By.xpath(".//li[not(contains(@class,'skeleton'))]"));
        } catch (NoSuchElementException e) {
            return List.of();
        }
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------
    public List<WebElement> getWaitingAppointments() {
        return getItemsInSection(AutomationConstants.WR_WAITING_HEADING_TEXT);
    }

    public List<WebElement> getCalledAppointments() {
        return getItemsInSection(AutomationConstants.WR_CALLED_HEADING_TEXT);
    }

    public int getWaitingCount() {
        return getWaitingAppointments().size();
    }

    public int getCalledCount() {
        return getCalledAppointments().size();
    }

    public boolean isAppointmentInWaiting(String anonymizedName) {
        return getWaitingAppointments().stream()
                .anyMatch(item -> item.getText().contains(anonymizedName));
    }

    public boolean isAppointmentInCalled(String anonymizedName) {
        return getCalledAppointments().stream()
                .anyMatch(item -> item.getText().contains(anonymizedName));
    }

    public String getOfficeForCalledAppointment(String anonymizedName) {
        return getCalledAppointments().stream()
                .filter(item -> item.getText().contains(anonymizedName))
                .findFirst()
                .map(item -> {
                    List<WebElement> officeElements = item.findElements(
                            By.cssSelector(AutomationConstants.WR_OFFICE_NUMBER));
                    return officeElements.isEmpty() ? "" : officeElements.get(0).getText();
                })
                .orElse("");
    }

    /**
     * Returns true if the WebSocket status indicator is present and shows a
     * connected state. The component uses
     * {@code data-testid="websocket-status-<state>"} (e.g.
     * {@code websocket-status-connected}, {@code websocket-status-connecting}).
     * Falls back to text/class checks for older versions.
     */
    public boolean isWebSocketConnected() {
        List<WebElement> statusElements = getDriver().findElements(
                By.cssSelector(AutomationConstants.WR_WEBSOCKET_STATUS));
        if (statusElements.isEmpty()) {
            return false;
        }
        WebElement statusEl = statusElements.get(0);
        String testId = statusEl.getAttribute("data-testid");
        String text = statusEl.getText().toLowerCase();
        String cssClass = statusEl.getAttribute("class");
        return (testId != null && testId.endsWith("-connected"))
                || text.contains("conectado") || text.contains("connected")
                || (cssClass != null && cssClass.contains("connected"));
    }

    /**
     * Polls the waiting/called section until an item containing
     * {@code anonymizedName} appears, or the timeout is reached.
     *
     * @param anonymizedName partial name as displayed on the public screen
     * @param state "waiting" or "called"
     * @param timeoutSeconds max wait time
     * @return true if the appointment reached the expected state within timeout
     */
    public boolean waitForAppointmentState(String anonymizedName, String state, int timeoutSeconds) {
        String headingText = "called".equals(state)
                ? AutomationConstants.WR_CALLED_HEADING_TEXT
                : AutomationConstants.WR_WAITING_HEADING_TEXT;

        // First wait for WebSocket to connect and deliver the initial snapshot
        waitForWebSocketConnected(10);

        long deadline = System.currentTimeMillis() + (timeoutSeconds * 1000L);
        while (System.currentTimeMillis() < deadline) {
            boolean found = getItemsInSection(headingText).stream()
                    .anyMatch(item -> item.getText().contains(anonymizedName));
            if (found) {
                return true;
            }
            // Allow data to settle before refreshing
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            getDriver().navigate().refresh();
            waitForWebSocketConnected(10);
        }
        return false;
    }

    /**
     * Waits until the WebSocket status indicator changes to "connected".
     */
    private void waitForWebSocketConnected(int timeoutSeconds) {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds))
                    .until(driver -> {
                        List<WebElement> statuses = driver.findElements(
                                By.cssSelector("[data-testid='websocket-status-connected']"));
                        return !statuses.isEmpty();
                    });
        } catch (Exception ignored) {
            // Continue even if not connected — the check will handle it
        }
    }

    /**
     * Returns the 1-based DOM position of the item matching
     * {@code anonymizedName} within the 'En espera' section, or -1 if not
     * found. Position is determined by element order in the DOM, not by a badge
     * value.
     */
    public int getQueuePosition(String anonymizedName) {
        List<WebElement> items = getWaitingAppointments();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getText().contains(anonymizedName)) {
                return i + 1;
            }
        }
        return -1;
    }
}
