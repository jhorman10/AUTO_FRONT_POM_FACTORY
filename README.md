# AUTO_FRONT_POM_FACTORY

Proyecto base del flujo 1 del taller de la semana 5. Este repositorio automatiza pruebas UI con Java, Gradle, Serenity BDD, Cucumber y el patrón POM + Page Factory.

## Alcance

- Este repositorio cubre solo Front-End con POM + Page Factory.
- El flujo objetivo final debe incluir 2 escenarios UI independientes.
- Debe existir al menos 1 escenario positivo y 1 escenario negativo.
- Los flujos de Screenplay UI y API viven en repositorios separados.

## Stack

- Java 21
- Gradle Wrapper 8.14.3
- Serenity BDD
- Cucumber
- Selenium WebDriver

## Estructura

```text
.
├── build.gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
├── serenity.conf
├── src
│   ├── main
│   │   └── java/com/automation/frontpomfactory/pages
│   └── test
│       ├── java/com/automation/frontpomfactory
│       │   ├── runners
│       │   └── stepdefinitions
│       └── resources/features
└── .github
```

## Prerrequisitos

- Java 21 instalado y disponible en `PATH`.
- Gradle (se usa wrapper incluido: `./gradlew`).
- Google Chrome instalado (u otro navegador compatible con Serenity mediante `serenity.conf`).
- La aplicación bajo prueba debe estar corriendo en `http://localhost:3001` (o la URL que definas).
- Red local sin bloqueo de puertos para `localhost:3001`.

## Instalación y puesta en marcha (de forma exacta)

1. Clona el repositorio:

```bash
git clone <tu-url-del-repositorio> AUTO_FRONT_POM_FACTORY
cd AUTO_FRONT_POM_FACTORY
```

2. Verifica Java y Gradle wrapper:

```bash
java -version
./gradlew -v
```

3. Instala dependencias y compila:

```bash
./gradlew clean compileJava compileTestJava
```

4. Abre `serenity.conf` y confirma/ajusta la URL base:

```properties
webdriver {
  base.url = "http://localhost:3001"
  driver = chrome
  autodownload = true
}
```

5. Asegúrate de que la app web esté activa:

```bash
curl -I http://localhost:3001/signup
```

Debe devolver HTTP 200.

6. Ejecuta todos los tests:

```bash
./gradlew clean test --no-daemon
```

6.1. Ejecuta tests y genera el reporte de Serenity con un solo comando:

```bash
./gradlew serenityReport --no-daemon
```

7. (Opcional) Ejecuta solo tags concretos:

```bash
./gradlew clean test --no-daemon -Dcucumber.filter.tags="@signup"
./gradlew clean test --no-daemon -Dcucumber.filter.tags="@positive"
./gradlew clean test --no-daemon -Dcucumber.filter.tags="@negative"
```

8. Abre reporte de los tests (Gradle):

```bash
xdg-open build/reports/tests/test/index.html  # Linux
open build/reports/tests/test/index.html      # macOS
start build/reports/tests/test/index.html     # Windows
```

9. Abre reporte de Serenity:

```bash
xdg-open target/site/serenity/index.html  # Linux
open target/site/serenity/index.html      # macOS
start target/site/serenity/index.html     # Windows
```

9.1. También puedes generar y abrir el reporte con una sola tarea:

```bash
./gradlew openSerenityReport --no-daemon
```

También puedes generarlo directamente con el task `serenityReport`, que ejecuta la suite y deja publicado el HTML en `target/site/serenity/index.html`.

## Estructura con detalle

- `src/main/java/com/automation/frontpomfactory/pages/SignupPage.java`: Page Object con `@FindBy`
- `src/test/java/com/automation/frontpomfactory/stepdefinitions/SignupStepDefinitions.java`: step definitions
- `src/test/resources/features/signup-successful.feature`: escenario positivo
- `src/test/resources/features/signup-weak-password.feature`: escenario negativo
- `src/test/java/com/automation/frontpomfactory/runners/CucumberTestSuite.java`: runner Cucumber+Serenity

## Verificación de carga de URL y selectores

1. Verifica que el HTML de signup contiene:
   - `input[placeholder='Nombre']`
   - `input[placeholder='Email']`
   - `input[placeholder='Contraseña']`
   - `button[type='submit']`

2. Si la aplicación cambia la UI, actualiza `SignupPage` preservando `@FindBy`.

3. Asegura que ambos escenarios sean independientes y no compartan estado.

## Estado actual

- La base técnica ya está creada.
- El proyecto implementa patrón POM + Page Factory.
- Tiene 2 escenarios independientes (positivo y negativo) para la URL `http://localhost:3001/signup`.
- Reportes de Serenity generables en `target/site/serenity/index.html`.
