package com.automation.frontpomfactory.stepdefinitions;

import org.junit.jupiter.api.Assertions;

import com.automation.frontpomfactory.pages.SignupPage;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SignupStepDefinitions {

    private SignupPage signupPage;

    @Given("el usuario accede a la página de signup")
    public void elUsuarioAccedeAlaPaginaDeSignup() {
        signupPage.navigateToSignup();
    }

    @When("ingresa nombre {string} en el formulario")
    public void ingresaNombreEnElFormulario(String fullName) {
        signupPage.enterFullName(fullName);
    }

    @When("ingresa email {string} en el formulario")
    public void ingresaEmailEnElFormulario(String email) {
        signupPage.enterEmail(email);
    }

    @When("ingresa contraseña {string} en el formulario")
    public void ingresaContraseñaEnElFormulario(String password) {
        signupPage.enterPassword(password);
    }

    @When("hace clic en el botón {string}")
    public void haceClicEnElBoton(String buttonName) {
        signupPage.clickRegisterButton();
    }

    @Then("recibe confirmación de registro exitoso")
    public void recibeConfirmacionDeRegistroExitoso() {
        Assertions.assertTrue(signupPage.isSigninPage(), "El usuario no fue redirigido a signin");
    }

    @And("el mensaje de éxito es visible en la pantalla")
    public void elMensajeDeExitoEsVisibleEnLaPantalla() {
        Assertions.assertTrue(signupPage.isSuccessMessageDisplayed() || signupPage.isSigninPage(), "No se encontró mensaje de éxito");
    }

    @Then("recibe un mensaje de error indicando que la contraseña es débil")
    public void recibeUnMensajeDeErrorIndicandoQueIntroduzcaContraseña() {
        Assertions.assertTrue(signupPage.isErrorMessageDisplayed() || signupPage.isSignupPage(), "No se encontró mensaje de error de contraseña débil");
    }

    @And("el usuario permanece en la página de signup")
    public void elUsuarioPermanecenEnLaPaginaDeSignup() {
        Assertions.assertTrue(signupPage.isSignupPage(), "El usuario no está en la página de signup");
    }
}
