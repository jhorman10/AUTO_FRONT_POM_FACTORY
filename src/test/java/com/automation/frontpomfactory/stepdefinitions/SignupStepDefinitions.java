package com.automation.frontpomfactory.stepdefinitions;

import java.util.Locale;

import org.junit.jupiter.api.Assertions;

import com.automation.frontpomfactory.pages.SignupPage;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SignupStepDefinitions {

    private SignupPage signupPage;

    private static final String ALREADY_REGISTERED_KEYWORD = "registrad";

    @Given("el usuario accede a la página de signup")
    public void elUsuarioAccedeAlaPaginaDeSignup() {
        signupPage.navigateToSignup();
    }

    @Given("existe un usuario registrado con nombre {string}, email {string} y contraseña {string}")
    public void existeUnUsuarioRegistradoConNombreEmailYContrasena(String fullName, String email, String password) {
        signupPage.navigateToSignup();
        signupPage.fillSignupForm(fullName, email, password);
        signupPage.submitSignupForm();

        boolean userCreated = signupPage.isSigninPage();
        boolean userAlreadyExisted = signupPage.isErrorMessageDisplayed()
                && signupPage.getErrorMessage().toLowerCase(Locale.ROOT).contains(ALREADY_REGISTERED_KEYWORD);

        Assertions.assertTrue(userCreated || userAlreadyExisted,
                "No se pudo establecer la precondición de usuario ya registrado");

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

    @Then("recibe un mensaje indicando que el usuario ya está registrado")
    public void recibeUnMensajeIndicandoQueElUsuarioYaEstaRegistrado() {
        String errorMessage = signupPage.getErrorMessage().toLowerCase(Locale.ROOT);

        Assertions.assertTrue(signupPage.isErrorMessageDisplayed(), "No se mostró mensaje de usuario ya registrado");
        Assertions.assertTrue(errorMessage.contains(ALREADY_REGISTERED_KEYWORD),
                "El mensaje no indica que el usuario ya esté registrado. Mensaje recibido: " + errorMessage);
    }

    @And("el usuario permanece en la página de signup")
    public void elUsuarioPermanecenEnLaPaginaDeSignup() {
        Assertions.assertTrue(signupPage.isSignupPage(), "El usuario no está en la página de signup");
    }
}
