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

- Java 21 instalado y disponible en `PATH`
- Google Chrome instalado
- Acceso a la aplicación web objetivo

## Configuración inicial

Antes de automatizar los escenarios reales debes definir:

1. La URL base de la aplicación en `serenity.conf` o por propiedad de sistema.
2. Los dos escenarios funcionales del taller.
3. Los datos de prueba requeridos.
4. Las credenciales si el flujo necesita autenticación.

La configuración actual usa `http://localhost:3001` como valor base para `webdriver.base.url`.

## Comandos de ejecución

Ejecutar la suite base:

```bash
./gradlew clean test aggregate
```

Sobrescribir la URL base en ejecución:

```bash
./gradlew clean test aggregate -Dwebdriver.base.url="https://tu-aplicacion"
```

Ejecutar solo el escenario de bootstrap:

```bash
./gradlew clean test -Dcucumber.filter.tags="@bootstrap"
```

## Reportes

Después de ejecutar las pruebas, Serenity genera el reporte en:

```text
target/site/serenity/index.html
```

## Estado actual

- La base técnica del proyecto ya está creada.
- La spec aprobada del flujo 1 está en `.github/specs/front-pom-page-factory.spec.md`.
- Falta parametrizar la aplicación objetivo y desarrollar los dos escenarios funcionales reales del taller.
