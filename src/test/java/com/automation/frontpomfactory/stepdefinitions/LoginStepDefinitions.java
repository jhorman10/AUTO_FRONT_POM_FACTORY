package com.automation.frontpomfactory.stepdefinitions;

import org.junit.jupiter.api.Assertions;

import com.automation.frontpomfactory.pages.LoginPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginStepDefinitions {

    private static final String RECEPCIONISTA_EMAIL = "recepcion@clinica.com";
    private static final String RECEPCIONISTA_PASSWORD = "Recep.2026!";
    private static final String DOCTOR_EMAIL = "doctor@clinica.com";
    private static final String DOCTOR_PASSWORD = "Doctor.2026!";

    private static final String RECEPCIONISTA_REDIRECT_PATH = "/registration";
    private static final String DOCTOR_REDIRECT_PATH = "/doctor/dashboard";

    private LoginPage loginPage;

    @Given("la recepcionista esta autenticada en el sistema")
    public void laRecepcionistaEstaAutenticadaEnElSistema() {
        autenticarUsuario(RECEPCIONISTA_EMAIL, RECEPCIONISTA_PASSWORD, RECEPCIONISTA_REDIRECT_PATH);
    }

    @Given("el doctor esta autenticado en el sistema")
    public void elDoctorEstaAutenticadoEnElSistema() {
        autenticarUsuario(DOCTOR_EMAIL, DOCTOR_PASSWORD, DOCTOR_REDIRECT_PATH);
    }

    @When("inicia sesion con email {string} y password {string}")
    public void iniciaSesionConEmailYPassword(String email, String password) {
        loginPage.navigateToLogin();
        loginPage.loginAs(email, password);
    }

    @Then("la sesion redirige a la ruta {string}")
    public void laSesionRedirigeALaRuta(String expectedPath) {
        loginPage.waitForRedirect(expectedPath);
        Assertions.assertTrue(loginPage.getDriver().getCurrentUrl().contains(expectedPath),
                "La sesion no redirigio a la ruta esperada: " + expectedPath);
    }

    private void autenticarUsuario(String email, String password, String expectedPath) {
        loginPage.navigateToLogin();
        loginPage.loginAs(email, password);
        loginPage.waitForRedirect(expectedPath);
        Assertions.assertTrue(loginPage.getDriver().getCurrentUrl().contains(expectedPath),
                "No fue posible autenticar al usuario con email: " + email);
    }
}
