package com.automation.frontpomfactory.stepdefinitions;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.frontpomfactory.pages.RegistrationPage;
import com.automation.frontpomfactory.pages.WaitingRoomPage;
import com.automation.frontpomfactory.utils.AutomationConstants;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AppointmentRegistrationStepDefinitions {

    private static final int WAIT_FOR_WAITING_STATE_SECONDS = 20;

    private RegistrationPage registrationPage;
    private WaitingRoomPage waitingRoomPage;

    private boolean lastRegistrationSuccessful;
    private boolean lastAttemptHadIdentityData;
    private boolean lastImmediateValidationError;
    private boolean lastImmediateSubmitErrorVisible;
    private String lastImmediateSubmitErrorMessage;
    private boolean lastAsyncSubmitError;
    private String lastAsyncSubmitErrorMessage;
    private boolean lastSuccessVisible;
    private boolean lastWaitingEvidenceVisible;

    @And("navega a la pagina de registro de turnos")
    public void navegaALaPaginaDeRegistroDeTurnos() {
        registrationPage.navigateToRegistration();
    }

    @When("registra un turno con nombre {string}, cedula {string} y prioridad {string}")
    public void registraUnTurnoConNombreCedulaYPrioridad(String fullName, String idCard, String priority) {
        String anonymizedName = anonymizeName(fullName);

        waitingRoomPage.navigateToWaitingRoom();
        int waitingOccurrencesBefore = countOccurrencesInWaiting(anonymizedName);

        registrationPage.navigateToRegistration();
        registrationPage.registerAppointment(fullName, idCard, priority);
        lastImmediateValidationError = registrationPage.isValidationErrorDisplayed();
        lastImmediateSubmitErrorVisible = false;
        lastImmediateSubmitErrorMessage = "";
        lastAsyncSubmitError = false;
        lastAsyncSubmitErrorMessage = "";
        lastSuccessVisible = false;
        lastWaitingEvidenceVisible = false;

        if (lastImmediateValidationError) {
            lastImmediateSubmitErrorVisible = registrationPage.hasErrorMessageDisplayed();
            if (lastImmediateSubmitErrorVisible) {
                lastImmediateSubmitErrorMessage = registrationPage.getErrorMessage();
            }
        }

        if (!lastImmediateValidationError) {
            waitForRegistrationProcessing();
            lastAsyncSubmitError = registrationPage.hasErrorMessageDisplayed();
            if (lastAsyncSubmitError) {
                lastAsyncSubmitErrorMessage = registrationPage.getErrorMessage();
            }
            lastSuccessVisible = registrationPage.isSuccessMessageDisplayed();

            if (!lastAsyncSubmitError && !lastSuccessVisible) {
                lastWaitingEvidenceVisible = waitForWaitingOccurrenceIncrease(anonymizedName, waitingOccurrencesBefore,
                        WAIT_FOR_WAITING_STATE_SECONDS);
            }
        }

        lastRegistrationSuccessful = !lastImmediateValidationError
                && !lastAsyncSubmitError
                && (lastSuccessVisible || lastWaitingEvidenceVisible);
    }

    @Then("el turno se registra exitosamente y se muestra confirmacion visual")
    public void elTurnoSeRegistraExitosamenteYSeMuestraConfirmacionVisual() {
        Assertions.assertTrue(lastRegistrationSuccessful,
                "No se detecto registro exitoso. Validacion inmediata: " + lastImmediateValidationError
                + ", error inmediato visible: " + lastImmediateSubmitErrorVisible
                + ", mensaje error inmediato: " + safeMessage(lastImmediateSubmitErrorMessage)
                + ", error asincrono: " + lastAsyncSubmitError
                + ", mensaje error asincrono: " + safeMessage(lastAsyncSubmitErrorMessage)
                + ", success visible: " + lastSuccessVisible
                + ", evidencia en waiting: " + lastWaitingEvidenceVisible);
    }

    @When("intenta registrar un turno con nombre {string} y cedula {string} sin seleccionar prioridad")
    public void intentaRegistrarUnTurnoConNombreYCedulaSinSeleccionarPrioridad(String fullName, String idCard) {
        String anonymizedName = anonymizeName(fullName);

        waitingRoomPage.navigateToWaitingRoom();
        int waitingOccurrencesBefore = countOccurrencesInWaiting(anonymizedName);

        registrationPage.navigateToRegistration();
        registrationPage.enterFullName(fullName);
        registrationPage.enterIdCard(idCard);

        lastAttemptHadIdentityData = hasInputValue(AutomationConstants.REG_FULLNAME_INPUT, fullName)
                && hasInputValue(AutomationConstants.REG_IDCARD_INPUT, idCard);

        registrationPage.clickRegisterButton();

        lastImmediateValidationError = registrationPage.isValidationErrorDisplayed();
        lastImmediateSubmitErrorVisible = false;
        lastImmediateSubmitErrorMessage = "";
        boolean hasValidationError = lastImmediateValidationError;
        boolean hasAsyncError = false;
        boolean hasVisualConfirmation = false;

        if (hasValidationError) {
            lastImmediateSubmitErrorVisible = registrationPage.hasErrorMessageDisplayed();
            if (lastImmediateSubmitErrorVisible) {
                lastImmediateSubmitErrorMessage = registrationPage.getErrorMessage();
            }
        }

        if (!hasValidationError) {
            waitForRegistrationProcessing();
            hasAsyncError = registrationPage.hasErrorMessageDisplayed();
            if (!hasAsyncError) {
                hasVisualConfirmation = waitForWaitingOccurrenceIncrease(anonymizedName, waitingOccurrencesBefore,
                        WAIT_FOR_WAITING_STATE_SECONDS);
            }
        }

        lastRegistrationSuccessful = !hasValidationError && !hasAsyncError && hasVisualConfirmation;
    }

    @Then("el formulario muestra validacion de prioridad requerida")
    public void elFormularioMuestraValidacionDePrioridadRequerida() {
        Assertions.assertTrue(lastAttemptHadIdentityData,
                "El escenario debe mantener nombre y cedula informados antes de enviar el formulario");

        Assertions.assertTrue(registrationPage.isValidationErrorDisplayed(),
                "No se mostro validacion al omitir la prioridad obligatoria. "
                + "Diagnostico submit inmediato -> validacionHTML5: " + lastImmediateValidationError
                + ", error asincrono visible: " + lastImmediateSubmitErrorVisible
                + ", mensaje error asincrono: " + safeMessage(lastImmediateSubmitErrorMessage));
    }

    @And("el turno no queda registrado exitosamente")
    public void elTurnoNoQuedaRegistradoExitosamente() {
        Assertions.assertFalse(lastRegistrationSuccessful,
                "El turno no deberia haberse registrado exitosamente. "
                + "Diagnostico submit inmediato -> validacionHTML5: " + lastImmediateValidationError
                + ", error asincrono visible: " + lastImmediateSubmitErrorVisible
                + ", mensaje error asincrono: " + safeMessage(lastImmediateSubmitErrorMessage));
    }

    @Given("registra un turno para {string} con cedula {string} y prioridad {string}")
    public void registraUnTurnoParaConCedulaYPrioridad(String fullName, String idCard, String priority) {
        registrationPage.navigateToRegistration();
        registrationPage.registerAppointment(fullName, idCard, priority);
        boolean hasValidationError = registrationPage.isValidationErrorDisplayed();
        boolean immediateSubmitErrorVisible = false;
        String immediateSubmitErrorMessage = "";
        boolean hasAsyncError = false;
        String asyncErrorMessage = "";
        boolean hasSuccessMessage = false;

        if (hasValidationError) {
            immediateSubmitErrorVisible = registrationPage.hasErrorMessageDisplayed();
            if (immediateSubmitErrorVisible) {
                immediateSubmitErrorMessage = registrationPage.getErrorMessage();
            }
        }

        if (!hasValidationError) {
            waitForRegistrationProcessing();
            hasAsyncError = registrationPage.hasErrorMessageDisplayed();
            if (hasAsyncError) {
                asyncErrorMessage = registrationPage.getErrorMessage();
            }
            hasSuccessMessage = registrationPage.isSuccessMessageDisplayed();
        }

        Assertions.assertFalse(hasValidationError,
                "La precondicion de registro no deberia presentar validaciones inmediatas de formulario. "
                + "Error asincrono inmediato visible: " + immediateSubmitErrorVisible
                + ", mensaje error asincrono inmediato: " + safeMessage(immediateSubmitErrorMessage));

        Assertions.assertFalse(hasAsyncError,
                "La precondicion de registro presento error asincrono del submit. Mensaje: "
                + safeMessage(asyncErrorMessage)
                + ". Validacion inmediata: " + hasValidationError
                + ", error asincrono inmediato visible: " + immediateSubmitErrorVisible
                + ", mensaje error asincrono inmediato: " + safeMessage(immediateSubmitErrorMessage));

        Assertions.assertTrue(hasSuccessMessage,
                "La precondicion de registro no mostro confirmacion de exito. "
                + "Validacion inmediata: " + hasValidationError
                + ", error asincrono: " + hasAsyncError
                + ", mensaje error asincrono: " + safeMessage(asyncErrorMessage));
    }

    @When("un visitante accede a la pantalla publica de turnos")
    public void unVisitanteAccedeALaPantallaPublicaDeTurnos() {
        waitingRoomPage.navigateToWaitingRoom();
    }

    @Then("el turno anonimizado {string} aparece visible en la pantalla publica")
    public void elTurnoAnonimizadoApareceVisibleEnLaPantallaPublica(String anonymizedName) {
        boolean foundInWaiting = waitingRoomPage.waitForAppointmentState(anonymizedName, "waiting", WAIT_FOR_WAITING_STATE_SECONDS);
        boolean inWaiting = waitingRoomPage.isAppointmentInWaiting(anonymizedName);
        boolean inCalled = waitingRoomPage.isAppointmentInCalled(anonymizedName);

        Assertions.assertTrue(inWaiting || inCalled,
                "El turno anonimizado no aparece en la pantalla publica: " + anonymizedName);
    }

    @Then("el turno para {string} con cedula {string} existe en el sistema")
    public void elTurnoParaConCedulaExisteEnElSistema(String fullName, String idCard) {
        // Verify the appointment exists in the backend via REST API
        String apiResponse = (String) ((org.openqa.selenium.JavascriptExecutor) waitingRoomPage.getDriver())
                .executeAsyncScript(
                        "var callback = arguments[arguments.length - 1];"
                        + "fetch('" + AutomationConstants.BASE_URL.replace("3001", "3000") + "/appointments')"
                        + ".then(function(r) { return r.text(); })"
                        + ".then(function(t) { callback(t); })"
                        + ".catch(function(e) { callback('ERROR: ' + e.message); });");

        Assertions.assertNotNull(apiResponse, "No se obtuvo respuesta de la API de turnos");
        Assertions.assertFalse(apiResponse.startsWith("ERROR:"),
                "Error al consultar la API de turnos: " + apiResponse);
        Assertions.assertTrue(apiResponse.contains("\"fullName\":\"" + fullName + "\""),
                "No se encontro un turno con nombre '" + fullName + "' en la API. Respuesta parcial: "
                + apiResponse.substring(0, Math.min(apiResponse.length(), 500)));
        Assertions.assertTrue(apiResponse.contains("\"idCard\":" + idCard),
                "No se encontro un turno con cedula '" + idCard + "' en la API");
    }

    private boolean hasInputValue(String cssSelector, String expectedValue) {
        return registrationPage.getDriver().findElements(By.cssSelector(cssSelector)).stream()
                .findFirst()
                .map(element -> expectedValue.equals(element.getAttribute("value")))
                .orElse(false);
    }

    private boolean waitForWaitingOccurrenceIncrease(String anonymizedName, int previousOccurrences, int timeoutSeconds) {
        waitingRoomPage.navigateToWaitingRoom();
        try {
            new WebDriverWait(waitingRoomPage.getDriver(), Duration.ofSeconds(timeoutSeconds))
                    .until(driver -> {
                        int currentOccurrences = countOccurrencesInWaiting(anonymizedName);
                        if (currentOccurrences > previousOccurrences) {
                            return true;
                        }
                        driver.navigate().refresh();
                        return false;
                    });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void waitForRegistrationProcessing() {
        try {
            new WebDriverWait(registrationPage.getDriver(), Duration.ofSeconds(AutomationConstants.WAIT_FOR_MESSAGE_SECONDS))
                    .until(driver -> !driver.findElements(By.cssSelector(AutomationConstants.REG_SUCCESS_MESSAGE)).isEmpty()
                    || !driver.findElements(By.cssSelector(AutomationConstants.REG_ASYNC_ERROR_MESSAGE))
                            .isEmpty());
        } catch (Exception ignored) {
            // Si no aparece mensaje en el timeout, los checks posteriores determinan el resultado.
        }
    }

    private String safeMessage(String message) {
        return message == null || message.isBlank() ? "(sin mensaje)" : message;
    }

    private int countOccurrencesInWaiting(String anonymizedName) {
        return (int) waitingRoomPage.getWaitingAppointments().stream()
                .filter(item -> item.getText().contains(anonymizedName))
                .count();
    }

    private String anonymizeName(String fullName) {
        String normalized = fullName == null ? "" : fullName.trim().replaceAll("\\s+", " ");
        if (normalized.isBlank()) {
            return "";
        }

        String[] parts = normalized.split(" ");
        if (parts.length <= 1) {
            return normalized;
        }

        StringBuilder sb = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            sb.append(' ').append(Character.toUpperCase(parts[i].charAt(0))).append('.');
        }
        return sb.toString();
    }
}
