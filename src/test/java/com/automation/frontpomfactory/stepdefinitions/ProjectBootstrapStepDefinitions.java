package com.automation.frontpomfactory.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class ProjectBootstrapStepDefinitions {

    private boolean projectReady;

    @Given("el proyecto de automatizacion esta inicializado")
    public void elProyectoDeAutomatizacionEstaInicializado() {
        projectReady = true;
    }

    @Then("la base tecnica de Serenity y Cucumber esta disponible")
    public void laBaseTecnicaDeSerenityYCucumberEstaDisponible() {
        if (!projectReady) {
            throw new IllegalStateException("La base del proyecto no quedo inicializada correctamente");
        }
    }
}