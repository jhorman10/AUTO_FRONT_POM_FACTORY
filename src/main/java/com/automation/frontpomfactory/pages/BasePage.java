package com.automation.frontpomfactory.pages;

import net.serenitybdd.core.pages.PageObject;

public abstract class BasePage extends PageObject {

    public void openBaseUrl() {
        open();
    }

    public String pageTitle() {
        return getTitle();
    }
}