---
id: SPEC-002
status: IN_PROGRESS
feature: front-pom-page-factory
created: 2026-03-16
updated: 2026-03-16
author: spec-generator
version: "1.0"
related-specs: []
---

# Spec: Front POM Page Factory

> **Estado:** `IN_PROGRESS`.
> **Ciclo de vida:** DRAFT → APPROVED → IN_PROGRESS → IMPLEMENTED → DEPRECATED

---

## 1. REQUERIMIENTOS

### Descripción
Esta funcionalidad inicia el repositorio `AUTO_FRONT_POM_FACTORY` para cubrir el flujo 1 del taller: automatización Front-End con patrón POM + Page Factory en Serenity BDD. El objetivo es dejar una base técnica lista para automatizar dos escenarios UI independientes, uno positivo y uno negativo, sobre una aplicación web propia definida por el usuario.

### Requerimiento de Negocio
Fuente principal: `.github/requirements/front-pom-page-factory.md`.

Resumen del requerimiento base:
- El proyecto debe usar Java, Gradle, Serenity BDD y Cucumber.
- La automatización UI debe implementar el patrón POM con `@FindBy` de Page Factory.
- Deben existir dos escenarios independientes entre sí.
- Debe haber al menos un flujo positivo y al menos un flujo negativo.
- El repositorio debe incluir instrucciones de ejecución y reportes legibles.
- No se deben mezclar responsabilidades con los repositorios de Screenplay UI y API.

### Historias de Usuario

#### HU-01: Inicializar el proyecto base de automatización UI

```
Como:        Ingeniero de automatización
Quiero:      disponer de un proyecto base en Java con Serenity BDD, Gradle y Cucumber
Para:        construir pruebas UI mantenibles bajo el patrón POM + Page Factory

Prioridad:   Alta
Estimación:  S
Dependencias: Ninguna
Capa:        Frontend
```

#### Criterios de Aceptación — HU-01

**Happy Path**
```gherkin
CRITERIO-1.1: Inicialización técnica del proyecto
  Dado que:  el repositorio AUTO_FRONT_POM_FACTORY está vacío o sin código de automatización
  Cuando:    se prepara la base técnica del proyecto
  Entonces:  existe una estructura Gradle en Java con Serenity BDD y Cucumber
  Y:         la configuración del navegador reside en serenity.conf
  Y:         el README documenta prerrequisitos y comandos de ejecución
```

**Error Path**
```gherkin
CRITERIO-1.2: Bloqueo por falta de definición funcional
  Dado que:  no se ha definido la aplicación objetivo ni los escenarios de negocio
  Cuando:    se intenta cerrar la automatización funcional
  Entonces:  el proyecto no debe asumir URLs, selectores ni datos de prueba inventados
  Y:         la spec registra explícitamente los datos pendientes para completar la implementación
```

**Edge Case** *(si aplica)*
```gherkin
CRITERIO-1.3: Separación de alcance por repositorio
  Dado que:  el taller exige tres repositorios distintos
  Cuando:    se diseña AUTO_FRONT_POM_FACTORY
  Entonces:  la arquitectura y documentación se limitan al patrón POM + Page Factory
  Y:         no incorporan artefactos propios de Screenplay ni de API testing
```

#### HU-02: Automatizar dos escenarios UI independientes con POM

```
Como:        Ingeniero de automatización
Quiero:      automatizar dos escenarios UI desacoplados usando Page Objects
Para:        validar comportamiento funcional sin introducir fragilidad entre pruebas

Prioridad:   Alta
Estimación:  M
Dependencias: HU-01
Capa:        Frontend
```

#### Criterios de Aceptación — HU-02

**Happy Path**
```gherkin
CRITERIO-2.1: Escenario positivo independiente
  Dado que:  existe un flujo de negocio válido en la aplicación objetivo
  Cuando:    el usuario ejecuta el escenario positivo desde Cucumber
  Entonces:  la prueba navega mediante Page Objects construidos con @FindBy
  Y:         valida el resultado esperado sin depender de otro escenario
```

**Error Path**
```gherkin
CRITERIO-2.2: Escenario negativo independiente
  Dado que:  existe una validación de negocio observable en la aplicación objetivo
  Cuando:    el usuario ejecuta el escenario negativo desde Cucumber
  Entonces:  la prueba valida el mensaje, estado o comportamiento de error esperado
  Y:         no necesita que el escenario positivo se haya ejecutado antes
```

**Edge Case** *(si aplica)*
```gherkin
CRITERIO-2.3: Datos desacoplados del código
  Dado que:  los escenarios requieren datos de entrada distintos
  Cuando:    se preparan los features y step definitions
  Entonces:  los datos se parametrizan en Gherkin o utilidades de soporte
  Y:         no quedan valores mágicos dispersos en múltiples clases
```

### Reglas de Negocio
1. `AUTO_FRONT_POM_FACTORY` solo cubre el flujo 1 del taller: automatización UI con patrón POM + Page Factory.
2. Deben existir exactamente dos escenarios funcionales en el alcance inicial de este repositorio: uno positivo y uno negativo.
3. Cada escenario debe ser ejecutable de forma independiente y no compartir estado acoplado con otro.
4. El patrón POM debe implementarse con clases de página y localizadores `@FindBy`.
5. La configuración del navegador y del entorno debe centralizarse en `serenity.conf`.
6. El proyecto debe generar reportes Serenity legibles para una ejecución en vivo.
7. No se permite cerrar la automatización funcional sin que el usuario defina aplicación objetivo, escenarios concretos y datos mínimos de prueba.

---

## 2. DISEÑO

### Modelos de Datos

#### Entidades afectadas
| Entidad | Almacén | Cambios | Descripción |
|---------|---------|---------|-------------|
| `automation_project` | repositorio Gradle | nueva | Proyecto Java de automatización UI basado en Serenity BDD |
| `feature_file` | `src/test/resources/features` | nueva | Escenarios Gherkin declarativos para el flujo positivo y negativo |
| `page_object` | `src/main/java/.../pages` | nueva | Encapsula elementos y acciones de cada pantalla usando `@FindBy` |
| `step_definition` | `src/test/java/.../stepdefinitions` | nueva | Vincula pasos Gherkin con navegación y validaciones |

#### Campos del modelo
| Campo | Tipo | Obligatorio | Validación | Descripción |
|-------|------|-------------|------------|-------------|
| `base_url` | string | sí | URL absoluta válida | URL base de la aplicación objetivo |
| `scenario_name` | string | sí | semántico y único por feature | Nombre del escenario de negocio |
| `browser` | string | sí | chrome / firefox / edge u otro soportado | Navegador configurado en Serenity |
| `execution_mode` | string | sí | local / headless / ci | Estrategia de ejecución |
| `test_data_set` | object | sí | consistente con el feature | Datos de entrada del escenario |

#### Índices / Constraints
- Un feature file no debe contener dependencia lógica entre escenarios.
- Cada Page Object representa una pantalla o componente estable del flujo, no múltiples contextos mezclados.
- Cada Step Definition debe delegar al Page Object y evitar lógica de localización embebida.

### API Endpoints

#### N/A para el flujo 1
- Este repositorio no automatiza servicios REST como objetivo principal.
- Puede consumir únicamente la URL web de la aplicación bajo prueba, pero no define contratos API propios dentro de este alcance.

### Diseño Frontend

#### Componentes nuevos
| Componente | Archivo | Props principales | Descripción |
|------------|---------|------------------|-------------|
| `BasePage` | `src/main/java/.../pages/BasePage.java` | N/A | Clase base con utilidades compartidas de navegación o sincronización |
| `[Feature]Page` | `src/main/java/.../pages/*.java` | N/A | Page Objects por pantalla o vista del flujo |

#### Páginas nuevas
| Página | Archivo | Ruta | Protegida |
|--------|---------|------|-----------|
| `flujo-negocio-1` | `src/main/java/.../pages/*.java` | definida por la app objetivo | según aplique |
| `flujo-negocio-2` | `src/main/java/.../pages/*.java` | definida por la app objetivo | según aplique |

#### Hooks y State
| Hook | Archivo | Retorna | Descripción |
|------|---------|---------|-------------|
| N/A | N/A | N/A | El proyecto no implementa frontend aplicativo; automatiza UI existente |

#### Services (llamadas API)
| Función | Archivo | Endpoint |
|---------|---------|---------|
| N/A | N/A | N/A |

### Arquitectura y Dependencias
- Paquetes nuevos requeridos: `serenity-core`, `serenity-junit`, `serenity-cucumber`, `serenity-screenplay-webdriver` solo si fuera requerido por librerías internas, `selenium-java`, `webdrivermanager` si el equipo decide gestionarlo así.
- Servicios externos: navegador web, WebDriver y la aplicación objetivo definida por el usuario.
- Impacto en punto de entrada de la app: N/A; el repositorio es de automatización, no de producto.

### Notas de Implementación
- Estructura objetivo propuesta:
  - `src/main/java/.../pages`
  - `src/main/java/.../models` para datos de prueba si se requieren objetos fuertemente tipados
  - `src/main/java/.../utils`
  - `src/test/java/.../runners`
  - `src/test/java/.../stepdefinitions`
  - `src/test/resources/features`
- El nombre exacto del paquete Java debe definirse una vez el usuario confirme convención institucional o personal.
- La implementación real queda bloqueada hasta que el usuario confirme:
  - dos escenarios concretos
  - datos de prueba
  - autenticación requerida
  - navegador o modo de ejecución esperado

---

## 3. LISTA DE TAREAS

> Checklist accionable para todos los agentes. Marcar cada ítem (`[x]`) al completarlo.
> El Orchestrator monitorea este checklist para determinar el progreso.

### Backend

#### Implementación
- [ ] N/A para este flujo

#### Tests Backend
- [ ] N/A para este flujo

### Frontend

#### Implementación
- [x] Crear `settings.gradle` y `build.gradle` para proyecto Java con Gradle y Serenity BDD
- [x] Crear `serenity.conf` con configuración base del navegador y `webdriver.base.url`
- [x] Crear estructura de paquetes para `pages`, `stepdefinitions`, `runners`, `utils` y `features`
- [ ] Implementar Page Objects con `@FindBy` para el escenario positivo
- [ ] Implementar Page Objects con `@FindBy` para el escenario negativo
- [ ] Crear feature file con escenario positivo independiente
- [ ] Crear feature file con escenario negativo independiente
- [ ] Implementar step definitions desacopladas de localizadores
- [x] Configurar runner de Cucumber con Serenity
- [x] Documentar la ejecución local en `README.md`

#### Tests Frontend
- [x] Ejecutar suite bootstrap individualmente desde Gradle
- [x] Validar generación de reportes Serenity
- [ ] Confirmar independencia entre escenarios
- [ ] Confirmar que no existen comentarios dentro de clases Java

### QA
- [ ] Ejecutar skill `/gherkin-case-generator` para formalizar ambos escenarios una vez el usuario defina el negocio exacto
- [ ] Ejecutar skill `/risk-identifier` para clasificar riesgos de flakiness, datos y sincronización
- [ ] Revisar cobertura de criterios de aceptación del flujo positivo y negativo
- [ ] Validar que el README contenga prerequisitos y comandos de ejecución
- [ ] Actualizar estado spec: `status: IMPLEMENTED`