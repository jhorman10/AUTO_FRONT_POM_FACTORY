---
id: SPEC-003
status: IMPLEMENTED
feature: signup-validation-pom
created: 2026-03-19
updated: 2026-03-19
author: spec-generator
version: "1.0"
related-specs:
  - front-pom-page-factory
---

# Spec: Signup Validation POM

> **Estado:** `IMPLEMENTED`.
> **Ciclo de vida:** DRAFT -> APPROVED -> IN_PROGRESS -> IMPLEMENTED -> DEPRECATED

---

## 1. REQUERIMIENTOS

### Descripcion
Esta funcionalidad agrega automatizacion UI del flujo de registro (signup) usando POM + Page Factory dentro del repositorio `AUTO_FRONT_POM_FACTORY`. Cubre dos escenarios independientes: uno positivo (registro exitoso) y uno negativo (rechazo por contrasena debil), alineados con el taller y ejecutables con Serenity + Cucumber.

### Requerimiento de Negocio
Fuente reconstruida desde historial Git:
- Commit funcional `a1fe647` (2026-03-19): "Add signup functionality with validation and feature tests".
- Archivos agregados:
  - `src/main/java/com/automation/frontpomfactory/pages/SignupPage.java`
  - `src/test/java/com/automation/frontpomfactory/stepdefinitions/SignupStepDefinitions.java`
  - `src/test/resources/features/signup-successful.feature`
  - `src/test/resources/features/signup-weak-password.feature`

Resumen:
- Implementar flujo de signup sobre `http://localhost:3001/signup`.
- Mantener patron POM con localizadores `@FindBy`.
- Cubrir al menos 1 escenario positivo y 1 negativo desacoplados.
- Mantener pruebas legibles en Gherkin y ejecutables por Cucumber/Serenity.

### Historias de Usuario

#### HU-01: Registrar usuario con datos validos

```
Como:        Usuario
Quiero:      registrarme con nombre, email y contrasena valida
Para:        crear una cuenta y continuar al inicio de sesion

Prioridad:   Alta
Estimacion:  S
Dependencias: Ninguna
Capa:        Frontend
```

#### Criterios de Aceptacion - HU-01

**Happy Path**
```gherkin
CRITERIO-1.1: Registro exitoso en signup
  Dado que:  el usuario accede a la pagina de signup
  Cuando:    ingresa nombre, email y contrasena valida y envia el formulario
  Entonces:  recibe confirmacion de registro exitoso
  Y:         es redirigido o queda en contexto de inicio de sesion
```

**Error Path**
```gherkin
CRITERIO-1.2: Falla por elementos no interactivos
  Dado que:  el usuario completo el formulario con datos validos
  Cuando:    el boton principal no esta habilitado visualmente
  Entonces:  el flujo intenta enviar con el boton alterno del formulario
  Y:         no se rompe la ejecucion automatizada
```

**Edge Case**
```gherkin
CRITERIO-1.3: Selectores alternos de mensajes
  Dado que:  la UI puede variar clases CSS de alerta
  Cuando:    el sistema muestra feedback de exito
  Entonces:  la automatizacion detecta mensajes por selectores alternos compatibles
```

#### HU-02: Rechazar registro con contrasena debil

```
Como:        Usuario
Quiero:      recibir rechazo cuando uso una contrasena debil
Para:        corregir mis datos y reintentar el registro de forma segura

Prioridad:   Alta
Estimacion:  S
Dependencias: HU-01
Capa:        Frontend
```

#### Criterios de Aceptacion - HU-02

**Happy Path**
```gherkin
CRITERIO-2.1: Validacion de contrasena debil
  Dado que:  el usuario accede a signup
  Cuando:    ingresa una contrasena debil y envia el formulario
  Entonces:  recibe un mensaje de error de validacion
  Y:         permanece en la pagina de signup
```

**Error Path**
```gherkin
CRITERIO-2.2: Error generico de UI
  Dado que:  la UI no expone un mensaje textual exacto
  Cuando:    el registro falla por contrasena debil
  Entonces:  el escenario valida presencia de estado de error o permanencia en signup
```

**Edge Case**
```gherkin
CRITERIO-2.3: Independencia entre escenarios
  Dado que:  existe un escenario positivo y uno negativo
  Cuando:    se ejecutan en cualquier orden
  Entonces:  ninguno depende del estado persistido del otro
```

### Reglas de Negocio
1. El repositorio mantiene alcance Front UI con patron POM + Page Factory.
2. Deben existir dos escenarios funcionales independientes: positivo y negativo.
3. El flujo de signup usa URL `http://localhost:3001/signup` para ejecucion local.
4. Los localizadores de pagina se definen con `@FindBy` en el Page Object.
5. Step definitions delegan acciones y validaciones al Page Object.
6. El escenario negativo debe validar rechazo y permanencia en signup.
7. Los mensajes de exito/error pueden detectarse por selectores alternos compatibles para robustez.

---

## 2. DISENO

### Modelos de Datos

#### Entidades afectadas
| Entidad | Almacen | Cambios | Descripcion |
|---------|---------|---------|-------------|
| `signup_page` | `src/main/java/.../pages/SignupPage.java` | nueva | Page Object de signup con campos, envio y validaciones |
| `signup_steps` | `src/test/java/.../stepdefinitions/SignupStepDefinitions.java` | nueva | Definicion de pasos Gherkin para flujo positivo y negativo |
| `signup_success_feature` | `src/test/resources/features/signup-successful.feature` | nueva | Escenario positivo de registro |
| `signup_weak_password_feature` | `src/test/resources/features/signup-weak-password.feature` | nueva | Escenario negativo por contrasena debil |

#### Campos del modelo
| Campo | Tipo | Obligatorio | Validacion | Descripcion |
|-------|------|-------------|------------|-------------|
| `full_name` | string | si | no vacio | Nombre del usuario en formulario |
| `email` | string | si | formato email esperado por la app | Email del usuario |
| `password` | string | si | evaluada por reglas de fortaleza de la app | Contrasena ingresada |
| `result_state` | string | si | `success` o `error` | Resultado observado en UI |

#### Indices / Constraints
- Los escenarios deben ejecutarse en aislamiento, sin dependencia entre ellos.
- Los selectores de UI deben centralizarse en el Page Object y no en los steps.

### API Endpoints

#### N/A para este flujo
- Esta funcionalidad es automatizacion UI del frontend.
- No se definieron endpoints API nuevos en los cambios analizados.

### Diseno Frontend

#### Componentes nuevos
| Componente | Archivo | Props principales | Descripcion |
|------------|---------|------------------|-------------|
| `SignupPage` | `src/main/java/com/automation/frontpomfactory/pages/SignupPage.java` | N/A | Modela campos, submit y validaciones de estado de signup |

#### Paginas nuevas
| Pagina | Archivo | Ruta | Protegida |
|--------|---------|------|-----------|
| `signup` | `src/main/java/com/automation/frontpomfactory/pages/SignupPage.java` | `/signup` | segun app bajo prueba |

#### Hooks y State
| Hook | Archivo | Retorna | Descripcion |
|------|---------|---------|-------------|
| N/A | N/A | N/A | El repositorio automatiza UI; no implementa app React en este cambio |

#### Services (llamadas API)
| Funcion | Archivo | Endpoint |
|---------|---------|---------|
| N/A | N/A | N/A |

### Arquitectura y Dependencias
- Stack observado: Java 21, Gradle, Serenity BDD, Cucumber, Selenium WebDriver.
- Runner existente: `src/test/java/com/automation/frontpomfactory/runners/CucumberTestSuite.java`.
- Sin cambios de base de datos ni backend en el commit funcional.

### Notas de Implementacion
- `SignupPage` implementa localizadores principales y alternos para robustez de UI.
- `SignupStepDefinitions` cubre pasos parametrizados para nombre, email y contrasena.
- Features implementadas:
  - `signup-successful.feature` (`@signup @positive`)
  - `signup-weak-password.feature` (`@signup @negative`)

---

## 3. LISTA DE TAREAS

> Checklist reconstruido desde historial de cambios. Los items marcados `[x]` estan implementados en git.

### Backend

#### Implementacion
- [x] N/A para este flujo

#### Tests Backend
- [x] N/A para este flujo

### Frontend

#### Implementacion
- [x] Crear Page Object `SignupPage` con `@FindBy`
- [x] Implementar metodos para completar nombre/email/contrasena
- [x] Implementar envio de formulario con fallback de boton
- [x] Implementar validaciones de estado de exito y error
- [x] Crear `SignupStepDefinitions` con pasos Gherkin parametrizados
- [x] Crear escenario positivo `signup-successful.feature`
- [x] Crear escenario negativo `signup-weak-password.feature`
- [x] Mantener independencia entre escenario positivo y negativo

#### Tests Frontend
- [x] Validar registro exitoso (redireccion o estado de exito)
- [x] Validar rechazo por contrasena debil
- [x] Validar permanencia en signup ante error
- [ ] Agregar casos borde adicionales (email invalido, campos vacios)

### QA
- [ ] Ejecutar `/gherkin-case-generator` para ampliar casos borde
- [ ] Ejecutar `/risk-identifier` para clasificar riesgo de flakiness por selectores CSS
- [ ] Confirmar ejecucion estable en CI con reporte Serenity
- [ ] Alinear README con detalle de escenarios de signup
