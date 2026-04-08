---
id: SPEC-005
status: IN_PROGRESS
feature: intelligent-turn-assignment-automation
created: 2026-04-07
updated: 2026-04-08
author: spec-generator
version: "1.0"
related-specs:
  - front-pom-page-factory
  - signup-validation-pom
---

# Spec: Automatización E2E — Sistema Inteligente de Asignación de Turnos Médicos

> **Estado:** `IN_PROGRESS`.
> **Ciclo de vida:** DRAFT → APPROVED → IN_PROGRESS → IMPLEMENTED → DEPRECATED

---

## 1. REQUERIMIENTOS

### Descripción
Automatización E2E con Serenity BDD + Cucumber + POM/Page Factory del nuevo flujo de gestión inteligente de turnos médicos. Cubre tres flujos críticos: registro de turno con nivel de urgencia (Alta/Media/Baja), visualización de posición en cola en tiempo real y notificación de asignación de médico/consultorio al paciente. Los tests validan el ciclo completo desde el login del recepcionista hasta la confirmación visual del paciente en la pantalla pública.

### Requerimiento de Negocio
Fuente: `conextt_nuevo.md` — Sección "Historias de usuario" (HU-01, HU-02, HU-03).

Resumen:
- **HU-01**: El recepcionista registra un turno con nivel de urgencia (Alta, Media, Baja). Si falta urgencia, el registro se rechaza. El turno queda en estado `waiting`.
- **HU-02**: El paciente ve su posición en la cola en tiempo real desde la pantalla pública. Si cambia la cola, la posición se actualiza sin recargar. Si se pierde conexión, se muestra estado de reconexión.
- **HU-03**: Cuando el turno es asignado a un médico, la pantalla pública muestra nombre del médico, consultorio y el turno pasa a `called` en ≤2 segundos.

### Historias de Usuario

#### HU-01: Registro de turno con nivel de urgencia

```
Como:        Recepcionista autenticada
Quiero:      registrar un turno con nivel de urgencia (Alta, Media, Baja)
Para:        que el sistema priorice la atención según criticidad clínica

Prioridad:   Alta
Estimación:  M
Dependencias: Ninguna
Capa:        Frontend (automatización E2E)
```

#### Criterios de Aceptación — HU-01

**Happy Path**
```gherkin
CRITERIO-1.1: Registro exitoso de turno con prioridad alta
  Dado que:  la recepcionista está autenticada y en la página de registro de turnos
  Cuando:    ingresa nombre "Carlos López", cédula "1234567890" y selecciona prioridad "high"
  Y:         hace clic en el botón de registrar
  Entonces:  el turno se registra exitosamente y aparece confirmación visual
```

```gherkin
CRITERIO-1.2: Registro exitoso de turno con prioridad media
  Dado que:  la recepcionista está autenticada y en la página de registro de turnos
  Cuando:    ingresa nombre "Ana Rodríguez", cédula "9876543210" y selecciona prioridad "medium"
  Y:         hace clic en el botón de registrar
  Entonces:  el turno se registra exitosamente y aparece confirmación visual
```

```gherkin
CRITERIO-1.3: Registro exitoso de turno con prioridad baja
  Dado que:  la recepcionista está autenticada y en la página de registro de turnos
  Cuando:    ingresa nombre "Pedro Gómez", cédula "5555555555" y selecciona prioridad "low"
  Y:         hace clic en el botón de registrar
  Entonces:  el turno se registra exitosamente y aparece confirmación visual
```

**Error Path**
```gherkin
CRITERIO-1.4: Rechazo de registro sin campos obligatorios
  Dado que:  la recepcionista está autenticada y en la página de registro de turnos
  Cuando:    deja los campos vacíos y hace clic en registrar
  Entonces:  el formulario muestra validación indicando campos requeridos
  Y:         el turno no se registra
```

**Edge Case**
```gherkin
CRITERIO-1.5: El turno registrado aparece en la pantalla pública como waiting
  Dado que:  la recepcionista registró un turno con prioridad "high" para "Carlos López"
  Cuando:    un visitante accede a la pantalla pública de turnos
  Entonces:  el turno aparece en la columna de turnos en espera con datos anonimizados
```

#### HU-02: Visualización de posición en cola en tiempo real

```
Como:        Paciente en sala de espera
Quiero:      ver mi posición en la cola en tiempo real desde la pantalla pública
Para:        conocer mi progreso sin preguntar en recepción

Prioridad:   Alta
Estimación:  L
Dependencias: HU-01
Capa:        Frontend (automatización E2E)
```

#### Criterios de Aceptación — HU-02

**Happy Path**
```gherkin
CRITERIO-2.1: Visualización de posición en cola por cédula
  Dado que:  existe un turno en estado "waiting" para la cédula "1234567890"
  Cuando:    se consulta la posición en cola desde la pantalla pública
  Entonces:  se muestra la posición actual del turno y su estado "waiting"
```

```gherkin
CRITERIO-2.2: La posición se actualiza cuando cambia la cola
  Dado que:  existen múltiples turnos en estado "waiting" con diferentes prioridades
  Cuando:    un turno previo es atendido o cancelado
  Entonces:  las posiciones de los turnos restantes se actualizan sin recargar la página
```

**Error Path**
```gherkin
CRITERIO-2.3: Consulta de posición con cédula inexistente
  Dado que:  no existe turno activo para la cédula "0000000000"
  Cuando:    se consulta la posición en cola
  Entonces:  se muestra un estado consistente indicando que no hay turno activo
```

**Edge Case**
```gherkin
CRITERIO-2.4: Estado de conexión WebSocket visible
  Dado que:  el visitante está en la pantalla pública de turnos
  Cuando:    la conexión WebSocket está activa
  Entonces:  se muestra un indicador de conexión activa (componente WebSocketStatus)
```

#### HU-03: Notificación de asignación de médico al paciente

```
Como:        Paciente en sala de espera
Quiero:      ver en la pantalla pública que mi turno fue asignado a un médico y consultorio
Para:        saber a dónde dirigirme sin preguntar

Prioridad:   Alta
Estimación:  XL
Dependencias: HU-01, HU-02
Capa:        Frontend (automatización E2E)
```

#### Criterios de Aceptación — HU-03

**Happy Path**
```gherkin
CRITERIO-3.1: Turno pasa de waiting a called con doctor y consultorio asignado
  Dado que:  existe un turno en estado "waiting" en la pantalla pública
  Y:         un doctor ha hecho check-in seleccionando un consultorio disponible
  Cuando:    el scheduler o la asignación reactiva procesa el turno
  Entonces:  el turno aparece en la columna de "llamados" con el consultorio asignado
  Y:         la transición se refleja en la pantalla sin recargar
```

```gherkin
CRITERIO-3.2: Asignación respeta prioridad Alta > Media > Baja
  Dado que:  existen turnos en espera con prioridades "low", "medium" y "high"
  Y:         un doctor está disponible con consultorio asignado
  Cuando:    el scheduler ejecuta la asignación
  Entonces:  el turno de prioridad "high" es asignado primero
```

**Edge Case**
```gherkin
CRITERIO-3.3: Sin doctor disponible, el turno permanece en waiting
  Dado que:  existe un turno en estado "waiting"
  Y:         no hay doctores con disponibilidad (ningún check-in activo)
  Cuando:    el scheduler intenta asignar
  Entonces:  el turno permanece en estado "waiting" en la pantalla pública
```

### Reglas de Negocio
1. Solo roles `admin` y `recepcionista` pueden registrar turnos — la automatización debe autenticarse primero vía `/login`.
2. Los niveles de urgencia válidos son: `high` (Alta), `medium` (Media), `low` (Baja).
3. La priorización sigue la regla: Alta > Media > Baja. En empate, FIFO por `created_at`.
4. La pantalla pública (`/`) muestra datos anonimizados: nombres y cédulas parcialmente ocultos.
5. La asignación automática la ejecuta el scheduler cada 15 segundos o de forma reactiva al check-in del doctor.
6. Un turno sigue el ciclo: `waiting` → `called` → `completed`. Alternativamente `waiting` → `cancelled`.
7. El doctor selecciona consultorio al hacer check-in. Solo consultorios habilitados y no ocupados aparecen en el selector.
8. La pantalla pública usa WebSocket (`/ws/appointments`) con shared secret para tiempo real.
9. La latencia esperada de notificación al paciente es ≤2 segundos en condiciones normales.
10. El formulario de registro captura: nombre completo (`fullName`), cédula (`idCard`) y prioridad (`priority`).

---

## 2. DISEÑO

### Modelos de Datos

> No aplica creación de modelos en la capa de automatización. Los datos se gestionan a través de la UI existente y la API en `http://localhost:3000`.

#### Datos de prueba requeridos

| Dato | Valor | Propósito |
|------|-------|-----------|
| Recepcionista email | `recepcion@clinica.com` | Login para registrar turnos |
| Recepcionista password | `Recep.2026!` | Autenticación Firebase |
| Doctor email | `doctor@clinica.com` | Login para check-in doctor |
| Doctor password | `Doctor.2026!` | Autenticación Firebase |
| Admin email | `admin@clinica.com` | Login para gestión (si necesario) |
| Admin password | `Admin.2026!` | Autenticación Firebase |
| Paciente 1 nombre | `Carlos López` | Registro turno prioridad alta |
| Paciente 1 cédula | `1234567890` | Identificación |
| Paciente 2 nombre | `Ana Rodríguez` | Registro turno prioridad media |
| Paciente 2 cédula | `9876543210` | Identificación |
| Paciente 3 nombre | `Pedro Gómez` | Registro turno prioridad baja |
| Paciente 3 cédula | `5555555555` | Identificación |

### API Endpoints

> No se crean nuevos endpoints. Los tests E2E interactúan con la UI que consume los endpoints existentes documentados en `conextt_nuevo.md` sección 7.

Endpoints relevantes para los flujos automatizados:
- `POST /auth/session` — resuelve sesión (ejecutado internamente por el frontend al hacer login)
- `POST /appointments` — crea turno (ejecutado internamente al enviar formulario de registro)
- `GET /appointments/queue-position/{idCard}` — posición en cola (ruta pública)
- `PATCH /doctors/{id}/check-in` — check-in del doctor con consultorio
- `PATCH /appointments/{id}/complete` — completar turno

### Diseño Frontend (Automatización — Page Objects)

#### Constantes nuevas en `AutomationConstants.java`

| Constante | Valor estimado | Descripción |
|-----------|---------------|-------------|
| `LOGIN_URL` | `BASE_URL + "/login"` | URL de login |
| `REGISTRATION_URL` | `BASE_URL + "/registration"` | URL registro de turnos |
| `WAITING_ROOM_URL` | `BASE_URL + "/"` | URL pantalla pública |
| `DASHBOARD_URL` | `BASE_URL + "/dashboard"` | URL dashboard turnos |
| `DOCTOR_DASHBOARD_URL` | `BASE_URL + "/doctor/dashboard"` | URL panel doctor |
| `LOGIN_EMAIL_INPUT` | `"input[type='email']"` | Campo email en login |
| `LOGIN_PASSWORD_INPUT` | `"input[type='password']"` | Campo password en login |
| `LOGIN_SUBMIT_BUTTON` | `"button[type='submit']"` | Botón de login |
| `REG_FULLNAME_INPUT` | `"input[name='fullName'], input[placeholder*='ombre']"` | Campo nombre en registro turno |
| `REG_IDCARD_INPUT` | `"input[name='idCard'], input[placeholder*='édula']"` | Campo cédula en registro turno |
| `REG_PRIORITY_SELECT` | `"select[name='priority'], [data-testid='priority-select']"` | Selector de prioridad |
| `REG_SUBMIT_BUTTON` | `"button[type='submit']"` | Botón de registrar turno |
| `REG_SUCCESS_MESSAGE` | `".success-message, .toast-success, .Toastify__toast--success"` | Mensaje de éxito registro |
| `REG_VALIDATION_ERROR` | `".error-message, [role='alert'], .field-error"` | Error de validación |
| `WR_WAITING_COLUMN` | `"[data-status='waiting'], .waiting-appointments"` | Columna turnos en espera |
| `WR_CALLED_COLUMN` | `"[data-status='called'], .called-appointments"` | Columna turnos llamados |
| `WR_APPOINTMENT_CARD` | `".appointment-card, [data-testid='appointment-card']"` | Tarjeta de turno |
| `WR_QUEUE_POSITION_BADGE` | `".queue-position-badge, [data-testid='queue-position']"` | Badge posición en cola |
| `WR_WEBSOCKET_STATUS` | `".websocket-status, [data-testid='ws-status']"` | Indicador WebSocket |
| `WR_OFFICE_NUMBER` | `".office-number, [data-testid='office-number']"` | Número consultorio en tarjeta |
| `DR_OFFICE_SELECTOR` | `"select[data-testid='office-selector'], .office-selector select"` | Selector de consultorio (doctor) |
| `DR_CHECKIN_BUTTON` | `"button[data-testid='checkin-btn'], button:has-text('disponibilidad')"` | Botón check-in doctor |
| `DR_COMPLETE_BUTTON` | `"button[data-testid='complete-btn'], button:has-text('Finalizar')"` | Botón finalizar atención |
| `DR_CHECKOUT_BUTTON` | `"button[data-testid='checkout-btn'], button:has-text('Check-out')"` | Botón check-out doctor |
| `DR_CURRENT_PATIENT` | `".current-patient, [data-testid='current-patient']"` | Info paciente actual |
| `WAIT_FOR_ASSIGNMENT_SECONDS` | `20` | Timeout para asignación (scheduler 15s + margen) |
| `WAIT_FOR_WS_UPDATE_SECONDS` | `5` | Timeout para actualización WebSocket |

> **Nota:** Los selectores CSS son estimaciones basadas en la documentación del frontend. Deben verificarse contra la aplicación real durante implementación y ajustarse según los `data-testid`, clases CSS Modules o estructura DOM efectiva.

#### Page Objects nuevos

| Page Object | Archivo | Extiende | Descripción |
|-------------|---------|----------|-------------|
| `LoginPage` | `pages/LoginPage.java` | `BasePage` | Autenticación con email/password vía Firebase |
| `RegistrationPage` | `pages/RegistrationPage.java` | `BasePage` | Registro de turnos con nombre, cédula, prioridad |
| `WaitingRoomPage` | `pages/WaitingRoomPage.java` | `BasePage` | Pantalla pública: cola en tiempo real, posición, notificaciones |
| `DoctorDashboardPage` | `pages/DoctorDashboardPage.java` | `BasePage` | Panel doctor: check-in con consultorio, finalizar, check-out |
| `DashboardPage` | `pages/DashboardPage.java` | `BasePage` | Dashboard de turnos con todos los estados |

#### Detalle de Page Objects

**`LoginPage`**
- `navigateToLogin()` → abre `LOGIN_URL`
- `enterEmail(String email)` → escribe en campo email
- `enterPassword(String password)` → escribe en campo password
- `clickLoginButton()` → clic en submit
- `loginAs(String email, String password)` → shortcut: fill + click
- `waitForRedirect(String expectedPath)` → espera redirección post-login

**`RegistrationPage`**
- `navigateToRegistration()` → abre `REGISTRATION_URL`
- `enterFullName(String name)` → escribe nombre paciente
- `enterIdCard(String idCard)` → escribe cédula
- `selectPriority(String priority)` → selecciona prioridad en dropdown (high/medium/low)
- `clickRegisterButton()` → clic en submit
- `registerAppointment(String name, String idCard, String priority)` → shortcut completo
- `isSuccessMessageDisplayed()` → verifica toast de éxito
- `isValidationErrorDisplayed()` → verifica error de validación
- `getSuccessMessage()` → obtiene texto del mensaje de éxito

**`WaitingRoomPage`**
- `navigateToWaitingRoom()` → abre `WAITING_ROOM_URL`
- `getWaitingAppointments()` → lista de turnos en espera
- `getCalledAppointments()` → lista de turnos llamados
- `getWaitingCount()` → cantidad de turnos waiting
- `getCalledCount()` → cantidad de turnos called
- `isAppointmentInWaiting(String anonymizedName)` → verifica turno en columna waiting
- `isAppointmentInCalled(String anonymizedName)` → verifica turno en columna called
- `getOfficeForCalledAppointment(String anonymizedName)` → consultorio asignado
- `isWebSocketConnected()` → estado del indicador de conexión
- `waitForAppointmentState(String anonymizedName, String state, int timeoutSeconds)` → espera cambio de estado con timeout
- `getQueuePosition(String anonymizedName)` → posición en cola del badge

**`DoctorDashboardPage`**
- `navigateToDoctorDashboard()` → abre `DOCTOR_DASHBOARD_URL`
- `selectOffice(String officeNumber)` → selecciona consultorio del dropdown
- `clickCheckIn()` → clic en botón check-in
- `checkInWithOffice(String officeNumber)` → shortcut: select + click
- `isCurrentPatientDisplayed()` → verifica si hay paciente asignado
- `getCurrentPatientInfo()` → texto del paciente actual
- `clickCompleteAppointment()` → finalizar atención
- `clickCheckOut()` → check-out
- `waitForPatientAssignment(int timeoutSeconds)` → espera a que el scheduler asigne paciente

**`DashboardPage`**
- `navigateToDashboard()` → abre `DASHBOARD_URL`
- `getAppointmentsByStatus(String status)` → turnos filtrados por estado
- `isAppointmentVisible(String patientInfo, String status)` → turno específico visible en estado
- `clickCancelAppointment(String patientInfo)` → cancelar turno (admin/recepcionista)

#### Features (Gherkin) nuevos

| Feature file | Tag | Descripción |
|-------------|-----|-------------|
| `appointment-registration-priority.feature` | `@appointment @priority @positive @negative` | HU-01: Registro de turno con urgencia |
| `queue-position-realtime.feature` | `@queue @realtime @positive @negative` | HU-02: Posición en cola en tiempo real |
| `assignment-notification.feature` | `@assignment @notification @positive @e2e` | HU-03: Notificación de asignación doctor+consultorio |

#### Step Definitions nuevos

| Clase | Archivo | Page Objects usados | Cubre |
|-------|---------|-------------------|-------|
| `LoginStepDefinitions` | `stepdefinitions/LoginStepDefinitions.java` | `LoginPage` | Steps compartidos de autenticación |
| `AppointmentRegistrationStepDefinitions` | `stepdefinitions/AppointmentRegistrationStepDefinitions.java` | `LoginPage`, `RegistrationPage`, `WaitingRoomPage` | HU-01 |
| `QueuePositionStepDefinitions` | `stepdefinitions/QueuePositionStepDefinitions.java` | `WaitingRoomPage` | HU-02 |
| `AssignmentNotificationStepDefinitions` | `stepdefinitions/AssignmentNotificationStepDefinitions.java` | `LoginPage`, `RegistrationPage`, `WaitingRoomPage`, `DoctorDashboardPage` | HU-03 |

### Arquitectura y Dependencias

- **Paquetes nuevos requeridos:** Ninguno. El stack actual (Serenity BDD 4.2.34, Cucumber 7.22.2, JUnit 5) soporta todos los escenarios.
- **Servicios externos:** La aplicación completa debe estar corriendo (`docker compose up -d`): Frontend (:3001), Producer (:3000), Consumer, MongoDB, RabbitMQ.
- **Impacto en runner:** Ninguno. `CucumberTestSuite.java` ya escanea `features/` recursivamente y el glue package `com.automation.frontpomfactory.stepdefinitions` incluirá los nuevos step definitions automáticamente.
- **Impacto en `serenity.conf`:** Ninguno. `base.url` ya apunta a `http://localhost:3001`.

### Notas de Implementación

1. **Selectores CSS:** Los selectores definidos en este spec son estimaciones basadas en la documentación del frontend (CSS Modules + componentes React). Durante la implementación, usar las DevTools del navegador para confirmar los selectores reales. Priorizar `data-testid` si están disponibles; si no, usar selectores CSS estables (no hashes de CSS Modules).

2. **Tiempos de espera:** La asignación automática depende del scheduler (cada 15s) o de la asignación reactiva (al check-in del doctor). Los waits explícitos deben usar `WebDriverWait` con timeout de al menos 20 segundos para escenarios de asignación.

3. **Datos anonimizados:** La pantalla pública muestra datos parcialmente ocultos (ej: "Car*** Ló***" para "Carlos López"). Los assertions sobre la pantalla pública deben comparar contra versiones anonimizadas, no contra el nombre completo.

4. **Precondiciones E2E:** Los escenarios de HU-03 requieren orquestación multi-actor (recepcionista registra, doctor hace check-in, pantalla pública verifica). Usar múltiples Page Objects en un mismo scenario, no tabs/ventanas paralelas. El flujo secuencial es: Login recepcionista → Registrar turno → Logout → Login doctor → Check-in → Verificar en pantalla pública.

5. **WebSocket en Selenium:** Selenium no puede verificar directamente la conexión WebSocket. Verificar los efectos colaterales: que los datos se actualizan sin `F5`, que el indicador de estado WebSocket está presente en el DOM.

6. **State cleanup:** Los tests deben considerar que la base de datos puede tener datos de ejecuciones anteriores. Usar cédulas únicas o con timestamp para evitar colisiones, o documentar la necesidad de un ambiente limpio antes de la ejecución.

7. **Flujo multi-sesión:** Para HU-03, se necesita login como recepcionista, luego como doctor. Dado que Serenity maneja una sola sesión de navegador por default, el flujo es: login recepcionista → registrar → navegar a pantalla pública (sin login) → en nueva ventana o secuencialmente login doctor → check-in → volver a pantalla pública para verificar.

---

## 3. LISTA DE TAREAS

> Checklist accionable para todos los agentes. Marcar cada ítem (`[x]`) al completarlo.
> El Orchestrator monitorea este checklist para determinar el progreso.

### Backend (Automatización — Pages + Utils)

#### Implementación
- [ ] Actualizar `AutomationConstants.java` con todas las constantes nuevas (URLs, selectores, timeouts)
- [ ] Crear `LoginPage.java` — Page Object para `/login` con métodos de autenticación
- [ ] Crear `RegistrationPage.java` — Page Object para `/registration` con registro de turnos y selección de prioridad
- [ ] Crear `WaitingRoomPage.java` — Page Object para `/` con lectura de cola, posiciones y estados
- [ ] Crear `DoctorDashboardPage.java` — Page Object para `/doctor/dashboard` con check-in, completar y check-out
- [ ] Crear `DashboardPage.java` — Page Object para `/dashboard` con visualización por estados

#### Tests (Features Gherkin)
- [ ] Crear `appointment-registration-priority.feature` — escenarios HU-01 (happy + error + edge)
- [ ] Crear `queue-position-realtime.feature` — escenarios HU-02 (happy + error + edge)
- [ ] Crear `assignment-notification.feature` — escenarios HU-03 (happy + edge)

#### Tests (Step Definitions)
- [ ] Crear `LoginStepDefinitions.java` — steps compartidos de login/logout
- [ ] Crear `AppointmentRegistrationStepDefinitions.java` — steps de registro con prioridad (HU-01)
- [ ] Crear `QueuePositionStepDefinitions.java` — steps de visualización de posición (HU-02)
- [ ] Crear `AssignmentNotificationStepDefinitions.java` — steps de notificación asignación (HU-03)

### Frontend

> No aplica. Este spec es de automatización E2E, no de desarrollo de funcionalidad frontend.

### QA

#### Verificación
- [ ] Confirmar selectores CSS contra la aplicación real ejecutándose en `http://localhost:3001`
- [ ] Verificar que los usuarios de prueba existen (recepcionista, doctor) y pueden autenticarse
- [ ] Validar que el scheduler de asignación funciona (crear turno + doctor check-in → turno pasa a called)
- [ ] Confirmar formato de anonimización de datos en pantalla pública
- [ ] Ejecutar suite completa con `./gradlew clean test` y verificar reporte Serenity
- [ ] Validar que los escenarios existentes (signup) no se rompen (regresión)
