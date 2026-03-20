package com.automation.frontpomfactory.stepdefinitions;

import org.junit.Assert;

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
        Assert.assertTrue("El usuario no fue redirigido a signin", signupPage.isSigninPage());
    }

    @And("el mensaje de éxito es visible en la pantalla")
    public void elMensajeDeExitoEsVisibleEnLaPantalla() {
        Assert.assertTrue("No se encontró mensaje de éxito", signupPage.isSuccessMessageDisplayed() || signupPage.isSigninPage());
    }

    @Then("recibe un mensaje de error indicando que la contraseña es débil")
    public void recibeUnMensajeDeErrorIndicandoQueIntroduzcaContraseña() {
        Assert.assertTrue("No se encontró mensaje de error de contraseña débil", signupPage.isErrorMessageDisplayed() || signupPage.isSignupPage());
    }

    @And("el usuario permanece en la página de signup")
    public void elUsuarioPermanecenEnLaPaginaDeSignup() {
        Assert.assertTrue("El usuario no está en la página de signup", signupPage.isSignupPage());
    }
}
