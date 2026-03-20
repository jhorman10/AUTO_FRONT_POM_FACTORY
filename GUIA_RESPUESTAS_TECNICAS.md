# Guia Tecnica Maestra para Responder Preguntas del Proyecto

## 1) Objetivo de esta guia
Esta guia sirve para responder de forma consistente, rapida y precisa cualquier pregunta tecnica sobre `AUTO_FRONT_POM_FACTORY`.

Cubre:
1. Como ubicar la fuente de verdad segun el tipo de pregunta.
2. Como responder por tema (arquitectura, ejecucion, tests, ASDD, QA, troubleshooting).
3. Comandos listos para validar respuestas antes de afirmarlas.
4. Plantillas para responder con evidencia.

---

## 2) Contexto tecnico del proyecto (snapshot actual)

### 2.1 Proposito del repo
- Repo de automatizacion UI para Front-End.
- Patron principal: POM + Page Factory.
- Stack: Java + Gradle + Serenity BDD + Cucumber + Selenium.

### 2.2 Versiones clave
- Java toolchain: 21
- Serenity plugin Gradle: 4.2.34
- JUnit: 4.13.2
- AssertJ: 3.27.3
- SLF4J simple: 2.0.16

Fuentes:
- `build.gradle`
- `gradle.properties`
- `README.md`

### 2.3 Flujos implementados
- Bootstrap base del proyecto (feature inicial)
- Signup exitoso (positivo)
- Signup con contrasena debil (negativo)

Fuentes:
- `src/test/resources/features/project-bootstrap.feature`
- `src/test/resources/features/signup-successful.feature`
- `src/test/resources/features/signup-weak-password.feature`

---

## 3) Reglas de oro para responder bien

1. Nunca responder de memoria si se puede verificar en archivo o comando.
2. Siempre citar archivo exacto como evidencia.
3. Separar claramente: hecho verificado vs inferencia.
4. Si hay ambiguedad, explicitarla y proponer como resolverla.
5. Si la pregunta es de implementacion nueva, validar primero spec ASDD.

Formato recomendado:
- `Respuesta corta`
- `Evidencia`
- `Impacto tecnico`
- `Siguiente validacion (opcional)`

---

## 4) Mapa de fuentes de verdad (por tema)

### 4.1 Vision general y ejecucion
- `README.md`
- `serenity.conf`
- `build.gradle`
- `gradle.properties`

### 4.2 Arquitectura ASDD y reglas de agentes
- `.github/AGENTS.md`
- `.github/README.md`
- `.github/copilot-instructions.md`
- `.github/skills/asdd-orchestrate/SKILL.md`

### 4.3 Requerimientos y specs
- `.github/requirements/*.md`
- `.github/specs/*.spec.md`

### 4.4 Automatizacion UI (codigo)
- `src/main/java/com/automation/frontpomfactory/pages/*.java`
- `src/test/java/com/automation/frontpomfactory/stepdefinitions/*.java`
- `src/test/java/com/automation/frontpomfactory/runners/CucumberTestSuite.java`
- `src/test/resources/features/*.feature`

### 4.5 Calidad y lineamientos
- `.github/docs/lineamientos/dev-guidelines.md`
- `.github/docs/lineamientos/qa-guidelines.md`
- `.github/instructions/backend.instructions.md`
- `.github/instructions/frontend.instructions.md`
- `.github/instructions/tests.instructions.md`

---

## 5) Procedimiento universal para responder cualquier pregunta tecnica

### Paso 1: Clasificar la pregunta
Clasificar en una de estas categorias:
1. Stack y dependencias
2. Ejecucion local/CI
3. Arquitectura y patrones
4. Cobertura de pruebas
5. ASDD (spec, fases, agentes)
6. Troubleshooting
7. Alcance funcional (que esta y que no esta)

### Paso 2: Buscar evidencia primaria
- Leer archivo fuente relevante.
- Si es comportamiento, revisar tambien feature y step definition.
- Si es estado actual, revisar `git log` y `git status`.

### Paso 3: Corroborar con comando
- Ejecutar comando de verificacion.
- Confirmar que el resultado soporta la respuesta.

### Paso 4: Responder con estructura
Usar:
1. Conclusion concreta.
2. Evidencia en archivos.
3. Comando de reproduccion.
4. Riesgo o condicion (si aplica).

### Paso 5: Cerrar con accion sugerida
Si aplica, proponer 1-3 siguientes pasos ejecutables.

---

## 6) Playbook por tipo de pregunta

## 6.1 Stack y dependencias
Pregunta tipica:
- "Que tecnologia usa este proyecto?"
- "Que version de Serenity/JUnit tenemos?"

Evidencia minima:
- `build.gradle`
- `gradle.properties`
- `README.md`

Comandos:
```bash
./gradlew -v
./gradlew dependencies --configuration testRuntimeClasspath
```

Respuesta tipo:
- "El proyecto usa Java 21 con Gradle Wrapper, Serenity BDD y Cucumber. Serenity esta en 4.2.34 y JUnit en 4.13.2, definido en gradle.properties y consumido en build.gradle."

## 6.2 Ejecucion y reportes
Pregunta tipica:
- "Como ejecuto todas las pruebas?"
- "Donde veo el reporte?"

Evidencia minima:
- `README.md`
- `build.gradle` (task `test.finalizedBy aggregate`)

Comandos:
```bash
./gradlew clean test --no-daemon
ls target/site/serenity
```

Respuesta tipo:
- "Ejecuta `./gradlew clean test --no-daemon`. El reporte se genera en `target/site/serenity/index.html` porque el task `test` finaliza con `aggregate`."

## 6.3 Configuracion de navegador y URL base
Pregunta tipica:
- "Que browser usa?"
- "Como cambio la URL de pruebas?"

Evidencia minima:
- `serenity.conf`

Comandos:
```bash
cat serenity.conf
curl -I http://localhost:3001/signup
```

Respuesta tipo:
- "Por defecto usa Chrome con autodownload y base.url `http://localhost:3001`. Se cambia en `serenity.conf` en el bloque `webdriver.base.url`."

## 6.4 Arquitectura POM + Page Factory
Pregunta tipica:
- "Donde estan los Page Objects?"
- "Se esta usando @FindBy?"

Evidencia minima:
- `src/main/java/com/automation/frontpomfactory/pages/SignupPage.java`
- `src/main/java/com/automation/frontpomfactory/pages/BasePage.java`

Comandos:
```bash
rg "@FindBy" src/main/java
rg "class .*Page" src/main/java/com/automation/frontpomfactory/pages
```

Respuesta tipo:
- "Si, se usa Page Factory con `@FindBy` en `SignupPage`. La base comun extiende `PageObject` en `BasePage`."

## 6.5 Cucumber: features, steps y runner
Pregunta tipica:
- "Que escenarios hay hoy?"
- "Como se conectan features y steps?"

Evidencia minima:
- `src/test/resources/features/*.feature`
- `src/test/java/com/automation/frontpomfactory/stepdefinitions/*.java`
- `src/test/java/com/automation/frontpomfactory/runners/CucumberTestSuite.java`

Comandos:
```bash
ls src/test/resources/features
rg "@signup|Scenario:" src/test/resources/features
rg "@Given|@When|@Then|@And" src/test/java/com/automation/frontpomfactory/stepdefinitions
```

Respuesta tipo:
- "Hay 3 features: bootstrap, signup-successful y signup-weak-password. El runner apunta a `src/test/resources/features` y glue `com.automation.frontpomfactory.stepdefinitions`."

## 6.6 Alcance funcional y specs ASDD
Pregunta tipica:
- "Que feature esta implementado oficialmente?"
- "Esta aprobado o en progreso?"

Evidencia minima:
- `.github/specs/*.spec.md`
- historial git

Comandos:
```bash
ls .github/specs
rg "^status:" .github/specs/*.md
git --no-pager log --date=short --pretty=format:"%h|%ad|%s" -n 20
```

Respuesta tipo:
- "Actualmente existen specs de `conversiones`, `front-pom-page-factory` y `signup-validation-pom`; el estado se toma del frontmatter `status:` de cada archivo."

## 6.7 ASDD workflow y gobernanza
Pregunta tipica:
- "Cual es el orden correcto para desarrollar una funcionalidad nueva?"

Evidencia minima:
- `.github/AGENTS.md`
- `.github/skills/asdd-orchestrate/SKILL.md`

Respuesta tipo:
- "El flujo es secuencial-paralelo: Spec -> (Backend || Frontend || DB) -> (Tests BE || Tests FE) -> QA -> Doc opcional. Sin spec APPROVED no se implementa."

## 6.8 Troubleshooting rapido
Pregunta tipica:
- "Por que fallo el test de signup?"

Checklist tecnico:
1. Verificar app viva en `/signup`.
2. Revisar que selectores de `SignupPage` sigan vigentes.
3. Revisar modo headless y driver en `serenity.conf`.
4. Ejecutar solo tags `@signup` para aislar.
5. Revisar reporte Serenity y stacktrace.

Comandos:
```bash
curl -I http://localhost:3001/signup
./gradlew clean test --no-daemon -Dcucumber.filter.tags="@signup"
find target/site/serenity -maxdepth 2 -type f | head
```

---

## 7) Matriz de preguntas frecuentes (FAQ tecnica)

1. "Este repo prueba API?"
- Respuesta: No. Este repo es de automatizacion UI Front con POM + Page Factory.
- Evidencia: `README.md` (alcance) y estructura de `src/main/java/.../pages` + `src/test/resources/features`.

2. "Se puede usar Screenplay aqui?"
- Respuesta: No para el alcance principal; Screenplay vive en otro repo del taller.
- Evidencia: `README.md` y lineamientos ASDD.

3. "Como ejecuto solo escenario negativo?"
- Respuesta: `./gradlew clean test --no-daemon -Dcucumber.filter.tags="@negative"`.
- Evidencia: tags en `signup-weak-password.feature`.

4. "Donde se define la URL base?"
- Respuesta: `serenity.conf`, bloque `webdriver.base.url`.

5. "Donde esta el runner principal?"
- Respuesta: `src/test/java/com/automation/frontpomfactory/runners/CucumberTestSuite.java`.

6. "Que valida el flujo de signup positivo?"
- Respuesta: Navega a signup, llena nombre/email/contrasena valida, envia y verifica confirmacion/estado de signin.
- Evidencia: feature positivo + steps + `SignupPage`.

7. "Que valida el flujo negativo?"
- Respuesta: Rechazo por contrasena debil y permanencia en signup.
- Evidencia: `signup-weak-password.feature` y assertions en `SignupStepDefinitions`.

8. "Que hago si cambia el HTML de signup?"
- Respuesta: Ajustar `@FindBy` en `SignupPage`, no en steps; luego correr `@signup`.

9. "Como saber que una respuesta esta desactualizada?"
- Respuesta: comparar con `git log`, `git status`, y frontmatter de specs.

---

## 8) Comandos canonicos para soporte tecnico

## 8.1 Diagnostico basico
```bash
java -version
./gradlew -v
git --no-pager status --short
git --no-pager log --date=short --pretty=format:"%h|%ad|%an|%s" -n 20
```

## 8.2 Ejecucion y aislamiento
```bash
./gradlew clean test --no-daemon
./gradlew clean test --no-daemon -Dcucumber.filter.tags="@signup"
./gradlew clean test --no-daemon -Dcucumber.filter.tags="@positive"
./gradlew clean test --no-daemon -Dcucumber.filter.tags="@negative"
```

## 8.3 Inspeccion de codigo y features
```bash
rg "@FindBy" src/main/java
rg "Scenario:|@signup|@positive|@negative" src/test/resources/features
rg "Assert\.assertTrue|@Given|@When|@Then|@And" src/test/java/com/automation/frontpomfactory/stepdefinitions
```

## 8.4 Evidencia de reportes
```bash
find target/site/serenity -type f | head
```

---

## 9) Plantillas de respuesta listas para usar

## 9.1 Plantilla corta (pregunta directa)
```text
Conclusion: <respuesta concreta en 1-2 lineas>
Evidencia: <archivo(s) exactos>
Validacion: <comando para reproducir>
```

## 9.2 Plantilla diagnostico (fallo tecnico)
```text
Hipotesis principal:
Evidencia observada:
Verificacion ejecutada:
Causa probable:
Correccion recomendada:
Riesgo residual:
```

## 9.3 Plantilla ASDD (solicitud de nueva funcionalidad)
```text
Estado actual:
- Existe spec?: <si/no>
- Estado spec: <DRAFT/APPROVED/IN_PROGRESS/IMPLEMENTED>

Siguiente accion valida:
- Si no hay spec APPROVED: generar/aprobar spec primero.
- Luego: implementar por fases ASDD.
```

---

## 10) Checklist de calidad de respuesta (antes de enviar)

1. Respondi exactamente la pregunta?
2. Inclui al menos una evidencia verificable?
3. Diferencie hecho vs suposicion?
4. Evite contradicciones con README/specs/lineamientos?
5. Ofreci siguiente paso util si aplica?

Si cualquier respuesta es "no", re-trabajar la respuesta antes de publicarla.

---

## 11) Limites conocidos y como declararlos

Declarar explicitamente cuando:
1. Falta evidencia en repo para afirmacion absoluta.
2. La pregunta depende del sistema bajo prueba externo (app en localhost:3001) y no esta levantado.
3. El resultado puede variar por entorno (browser, OS, red, CI).

Frase recomendada:
- "Con la evidencia actual del repositorio, esto es lo verificable. Para confirmarlo al 100% en runtime, ejecuta <comando>."

---

## 12) Mantenimiento de esta guia

Actualizar esta guia cuando cambie cualquiera de estos puntos:
1. Stack o versiones de dependencias.
2. Estructura de carpetas de pages/steps/features.
3. Flujo ASDD o reglas en `.github/`.
4. Nuevas features o specs implementadas.
5. Comandos de ejecucion y reporte.

Frecuencia sugerida: en cada merge relevante de testing/arquitectura.
