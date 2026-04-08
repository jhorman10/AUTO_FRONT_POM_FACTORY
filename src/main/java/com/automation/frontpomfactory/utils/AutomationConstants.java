package com.automation.frontpomfactory.utils;

public final class AutomationConstants {

    private AutomationConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // -------------------------------------------------------------------------
    // Navigation and environment
    // -------------------------------------------------------------------------
    public static final String BASE_URL = "http://localhost:3001";

    // Existing paths
    public static final String SIGNUP_PATH = "/signup";
    public static final String SIGNIN_PATH = "/signin";
    public static final String SIGNUP_URL = BASE_URL + SIGNUP_PATH;

    // New paths (SPEC-005)
    public static final String LOGIN_PATH = "/login";
    public static final String REGISTRATION_PATH = "/registration";
    public static final String WAITING_ROOM_PATH = "/";
    public static final String DASHBOARD_PATH = "/dashboard";
    public static final String DOCTOR_DASHBOARD_PATH = "/doctor/dashboard";

    // New URLs (SPEC-005)
    public static final String LOGIN_URL = BASE_URL + LOGIN_PATH;
    public static final String REGISTRATION_URL = BASE_URL + REGISTRATION_PATH;
    public static final String WAITING_ROOM_URL = BASE_URL + WAITING_ROOM_PATH;
    public static final String DASHBOARD_URL = BASE_URL + DASHBOARD_PATH;
    public static final String DOCTOR_DASHBOARD_URL = BASE_URL + DOCTOR_DASHBOARD_PATH;

    public static final int DEFAULT_TIMEOUT_MILLIS = 10000;

    // -------------------------------------------------------------------------
    // Signup page locators
    // -------------------------------------------------------------------------
    public static final String FULL_NAME_INPUT = "input[placeholder='Nombre']";
    public static final String EMAIL_INPUT = "input[placeholder='Email']";
    public static final String PASSWORD_INPUT = "input[placeholder='Contraseña']";
    public static final String REGISTER_BUTTON = "button[type='submit']";
    public static final String FALLBACK_SUBMIT_BUTTON = ".SignUpForm-module__NmlLka__button";
    public static final String SUCCESS_MESSAGE = ".success-message, .alert-success";
    public static final String ERROR_MESSAGE = ".error-message, .alert-danger, .error, [role='alert'], .toast-error, .Toastify__toast--error, .notification-error";
    public static final String SIGNIN_LINK_TEXT = "Iniciar sesión";
    public static final int WAIT_FOR_MESSAGE_SECONDS = 5;

    // -------------------------------------------------------------------------
    // Login page locators (SPEC-005 — /login)
    // data-testid selectors match the real DOM exposed by /login
    // -------------------------------------------------------------------------
    public static final String LOGIN_FORM = "[data-testid='login-form']";
    public static final String LOGIN_EMAIL_INPUT = "[data-testid='email-input']";
    public static final String LOGIN_PASSWORD_INPUT = "[data-testid='password-input']";
    public static final String LOGIN_SUBMIT_BUTTON = "[data-testid='submit-button']";

    // -------------------------------------------------------------------------
    // Registration page locators (SPEC-005 — /registration)
    // -------------------------------------------------------------------------
    public static final String REG_FORM = "[class*='AppointmentRegistrationForm']";
    public static final String REG_FULLNAME_INPUT = "input[placeholder='Nombre Completo']";
    public static final String REG_IDCARD_INPUT = "input[inputmode='numeric']";
    public static final String REG_PRIORITY_SELECT = "[class*='AppointmentRegistrationForm'] select[required]";
    public static final String REG_SUBMIT_BUTTON = "[class*='AppointmentRegistrationForm'] button";
    public static final String REG_SUCCESS_MESSAGE = "[class*='AppointmentRegistrationForm'] p[class*='success'], .success-message, .toast-success, .Toastify__toast--success";
    public static final String REG_VALIDATION_ERROR = "[class*='AppointmentRegistrationForm'] p[class*='error']";
    /**
     * Async error paragraph rendered by AppointmentRegistrationForm after a
     * failed submit.
     */
    public static final String REG_ASYNC_ERROR_MESSAGE = "[class*='AppointmentRegistrationForm'] p[class*='error']";

    // -------------------------------------------------------------------------
    // Waiting room page locators (SPEC-005 — / public screen)
    // -------------------------------------------------------------------------
    // Section heading texts used to locate columns by DOM structure
    public static final String WR_WAITING_HEADING_TEXT = "En espera";
    public static final String WR_CALLED_HEADING_TEXT = "En consultorio";
    // Non-skeleton list item selector used within a located section
    public static final String WR_APPOINTMENT_ITEM = "li:not([class*='skeleton'])";
    // WebSocket indicator: data-testid uses prefix 'websocket-status-' followed by state
    // e.g. websocket-status-connecting, websocket-status-connected, websocket-status-error
    public static final String WR_WEBSOCKET_STATUS = "[data-testid^='websocket-status-']";
    public static final String WR_OFFICE_NUMBER = ".office-number, [data-testid='office-number']";
    // Legacy selectors kept for backward compatibility
    public static final String WR_WAITING_COLUMN = "[data-status='waiting'], .waiting-appointments";
    public static final String WR_CALLED_COLUMN = "[data-status='called'], .called-appointments";
    public static final String WR_APPOINTMENT_CARD = ".appointment-card, [data-testid='appointment-card']";
    public static final String WR_QUEUE_POSITION_BADGE = ".queue-position-badge, [data-testid='queue-position']";

    // -------------------------------------------------------------------------
    // Doctor dashboard locators (SPEC-005 — /doctor/dashboard)
    // -------------------------------------------------------------------------
    public static final String DR_OFFICE_SELECTOR = "select[data-testid='office-selector'], .office-selector select";
    public static final String DR_CHECKIN_BUTTON = "button[data-testid='checkin-btn'], button.btn-checkin, .checkin-button";
    public static final String DR_COMPLETE_BUTTON = "button[data-testid='complete-btn'], button.btn-complete, .complete-button";
    public static final String DR_CHECKOUT_BUTTON = "button[data-testid='checkout-btn'], button.btn-checkout, .checkout-button";
    public static final String DR_CURRENT_PATIENT = ".current-patient, [data-testid='current-patient']";

    // -------------------------------------------------------------------------
    // Dashboard page locators (SPEC-005 — /dashboard)
    // -------------------------------------------------------------------------
    public static final String DASH_APPOINTMENT_CARD = ".appointment-card, [data-testid='appointment-card']";
    public static final String DASH_CANCEL_BUTTON = "button[data-testid='cancel-btn'], button[aria-label='Cancelar']";

    // -------------------------------------------------------------------------
    // Timeouts (SPEC-005)
    // -------------------------------------------------------------------------
    public static final int WAIT_FOR_ASSIGNMENT_SECONDS = 20;
    public static final int WAIT_FOR_WS_UPDATE_SECONDS = 5;

    // -------------------------------------------------------------------------
    // Test data — credentials (SPEC-005)
    // -------------------------------------------------------------------------
    public static final String RECEPCIONISTA_EMAIL = "recepcion@clinica.com";
    public static final String RECEPCIONISTA_PASSWORD = "Recep.2026!";
    public static final String DOCTOR_EMAIL = "doctor@clinica.com";
    public static final String DOCTOR_PASSWORD = "Doctor.2026!";
    public static final String ADMIN_EMAIL = "admin@clinica.com";
    public static final String ADMIN_PASSWORD = "Admin.2026!";

    // -------------------------------------------------------------------------
    // Reporting
    // -------------------------------------------------------------------------
    public static final String SERENITY_REPORT_DIRECTORY = "target/site/serenity";
    public static final String SERENITY_REPORT_FILE_NAME = "index.html";
    public static final String SERENITY_REPORT_PATH = SERENITY_REPORT_DIRECTORY + "/" + SERENITY_REPORT_FILE_NAME;
    public static final String SERENITY_REPORT_TASK = "serenityReport";
    public static final String OPEN_SERENITY_REPORT_TASK = "openSerenityReport";
}
