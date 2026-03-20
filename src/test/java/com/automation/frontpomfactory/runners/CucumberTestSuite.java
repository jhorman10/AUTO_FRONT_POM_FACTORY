package com.automation.frontpomfactory.runners;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.automation.frontpomfactory.stepdefinitions",
        snippets = SnippetType.CAMELCASE,
        plugin = {"pretty"}
)
public class CucumberTestSuite {
}