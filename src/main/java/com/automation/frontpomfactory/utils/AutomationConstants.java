package com.automation.frontpomfactory.utils;

public final class AutomationConstants {

    private AutomationConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Navigation and environment
    public static final String BASE_URL = "http://localhost:3001";
    public static final String SIGNUP_PATH = "/signup";
    public static final String SIGNIN_PATH = "/signin";
    public static final String SIGNUP_URL = BASE_URL + SIGNUP_PATH;
    public static final int DEFAULT_TIMEOUT_MILLIS = 10000;

    // Signup page locators
    public static final String FULL_NAME_INPUT = "input[placeholder='Nombre']";
    public static final String EMAIL_INPUT = "input[placeholder='Email']";
    public static final String PASSWORD_INPUT = "input[placeholder='Contraseña']";
    public static final String REGISTER_BUTTON = "button[type='submit']";
    public static final String FALLBACK_SUBMIT_BUTTON = ".SignUpForm-module__NmlLka__button";
    public static final String SUCCESS_MESSAGE = ".success-message, .alert-success";
    public static final String ERROR_MESSAGE = ".error-message, .alert-danger, .error, [role='alert'], .toast-error, .Toastify__toast--error, .notification-error";
    public static final String SIGNIN_LINK_TEXT = "Iniciar sesión";
    public static final int WAIT_FOR_MESSAGE_SECONDS = 5;

    // Reporting
    public static final String SERENITY_REPORT_DIRECTORY = "target/site/serenity";
    public static final String SERENITY_REPORT_FILE_NAME = "index.html";
    public static final String SERENITY_REPORT_PATH = SERENITY_REPORT_DIRECTORY + "/" + SERENITY_REPORT_FILE_NAME;
    public static final String SERENITY_REPORT_TASK = "serenityReport";
    public static final String OPEN_SERENITY_REPORT_TASK = "openSerenityReport";
}