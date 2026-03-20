---
id: SPEC-004
status: IMPLEMENTED
feature: tech-modernization
created: 2026-03-20
updated: 2026-03-20
author: spec-generator
version: "1.0"
related-specs:
  - front-pom-page-factory
  - signup-validation-pom
---

# Spec: Tech Modernization

> **Estado:** `IMPLEMENTED`.
> **Ciclo de vida:** DRAFT -> APPROVED -> IN_PROGRESS -> IMPLEMENTED -> DEPRECATED

---

## 1. REQUERIMIENTOS

### Descripción
Esta funcionalidad moderniza el stack técnico de automatización UI para usar tecnologías con soporte activo y reducir deuda técnica. El alcance principal es migrar el framework de pruebas desde JUnit 4 hacia JUnit 5, mantener compatibilidad con Serenity + Cucumber y preservar el comportamiento actual de los escenarios existentes.

### Requerimiento de Negocio
Fuente: solicitud del usuario para "migrar todas las tecnologías a las de mayor soporte actual" y baseline técnico del repositorio.

Stack actual observado:
- Java 21 (LTS)
- Gradle 8.14.3
- Serenity Gradle Plugin 4.2.34
- Serenity Core/Cucumber
- JUnit 4.13.2
- AssertJ 3.27.3
- SLF4J simple 2.0.16

Problemas identificados:
- Uso de JUnit 4 legacy con runner basado en `@RunWith`.
- Necesidad de alineación a plataforma moderna (JUnit 5 / JUnit Platform).
- Riesgo de regresión si la migración no protege las features existentes.

### Historias de Usuario

#### HU-01: Migrar framework de testing de JUnit 4 a JUnit 5

```
Como:        Ingeniero de automatización
Quiero:      migrar los tests desde JUnit 4 a JUnit 5
Para:        mantener soporte activo, compatibilidad moderna y menor deuda técnica

Prioridad:   Alta
Estimación:  M
Dependencias: Ninguna
Capa:        Frontend
```

#### Criterios de Aceptación — HU-01

**Happy Path**
```gherkin
CRITERIO-1.1: Ejecución con JUnit Platform
  Dado que:  el proyecto está configurado con JUnit 5
  Cuando:    se ejecuta gradle clean test
  Entonces:  la suite corre sobre JUnit Platform
  Y:         no hay dependencia directa a junit:junit:4.13.2
```

**Error Path**
```gherkin
CRITERIO-1.2: Falla por runner legacy residual
  Dado que:  existe una clase runner usando @RunWith de JUnit 4
  Cuando:    se intenta ejecutar la suite tras migración
  Entonces:  el build falla de forma explícita
  Y:         se identifica el runner no migrado
```

**Edge Case**
```gherkin
CRITERIO-1.3: Compatibilidad runner Cucumber-Serenity
  Dado que:  se migra a JUnit 5
  Cuando:    se ejecutan features Cucumber con Serenity
  Entonces:  los escenarios se ejecutan y reportan sin pérdida de trazabilidad
```

#### HU-02: Actualizar dependencias a versiones estables con soporte activo

```
Como:        Responsable técnico
Quiero:      actualizar dependencias de test a versiones estables soportadas
Para:        reducir vulnerabilidades, mejorar mantenimiento y evitar componentes obsoletos

Prioridad:   Alta
Estimación:  S
Dependencias: HU-01
Capa:        Frontend
```

#### Criterios de Aceptación — HU-02

**Happy Path**
```gherkin
CRITERIO-2.1: Dependencias de testing actualizadas
  Dado que:  se define matriz de versiones objetivo
  Cuando:    se actualiza gradle.properties y build.gradle
  Entonces:  gradle dependencies resuelve sin conflictos críticos
  Y:         el build compila y ejecuta test task
```

**Error Path**
```gherkin
CRITERIO-2.2: Conflicto de versiones transitivas
  Dado que:  dos librerías requieren versiones incompatibles
  Cuando:    se ejecuta resolución de dependencias
  Entonces:  el conflicto se hace visible en salida de Gradle
  Y:         se corrige mediante versión explícita o exclusión justificada
```

**Edge Case**
```gherkin
CRITERIO-2.3: Tecnología vigente sin cambio
  Dado que:  Java 21 y Gradle 8.14.3 ya están vigentes y soportados
  Cuando:    se evalúa la migración
  Entonces:  se mantienen sin downgrade ni cambios innecesarios
```

#### HU-03: Preservar compatibilidad funcional de features existentes

```
Como:        Ingeniero QA
Quiero:      mantener operativos los escenarios bootstrap y signup
Para:        evitar regresiones funcionales durante la modernización

Prioridad:   Alta
Estimación:  S
Dependencias: HU-01, HU-02
Capa:        Frontend
```

#### Criterios de Aceptación — HU-03

**Happy Path**
```gherkin
CRITERIO-3.1: No regresión de suite existente
  Dado que:  ya existen features bootstrap, signup-successful y signup-weak-password
  Cuando:    se ejecuta la suite después de la migración
  Entonces:  todos los escenarios mantienen su resultado esperado
  Y:         se genera el reporte Serenity
```

**Error Path**
```gherkin
CRITERIO-3.2: Falla por cambio de selector o sincronización
  Dado que:  una prueba falla tras la migración
  Cuando:    se inspecciona el reporte Serenity
  Entonces:  se detecta si la causa es funcional o del cambio de framework
  Y:         se corrige sin alterar el alcance de negocio
```

**Edge Case**
```gherkin
CRITERIO-3.3: Ejecución por tags
  Dado que:  los escenarios usan tags @signup, @positive y @negative
  Cuando:    se ejecuta por filtro de tags
  Entonces:  el comportamiento por tags se mantiene
```

### Reglas de Negocio
1. Sin spec `APPROVED` no se inicia implementación de migración.
2. Se debe retirar JUnit 4 del classpath directo del proyecto.
3. La ejecución de pruebas debe quedar sobre JUnit Platform.
4. Java 21 y Gradle 8.14.3 se mantienen salvo evidencia técnica de mejora compatible.
5. La migración no debe romper los escenarios existentes del repositorio.
6. El reporte Serenity debe seguir disponible en `target/site/serenity/index.html`.

---

## 2. DISEÑO

### Modelos de Datos

#### Entidades afectadas
| Entidad | Almacén | Cambios | Descripción |
|---------|---------|---------|-------------|
| `build_config` | `build.gradle` | modificada | Actualización de dependencias y engine de test |
| `version_catalog` | `gradle.properties` | modificada | Versiones objetivo de librerías |
| `test_runner` | `src/test/java/.../runners` | modificada | Migración de runner legacy JUnit4 a JUnit Platform |
| `reporting_flow` | Gradle task `test` + `aggregate` | modificada | Preserva generación de reportes Serenity |

#### Campos del modelo
| Campo | Tipo | Obligatorio | Validación | Descripción |
|-------|------|-------------|------------|-------------|
| `java_version` | number | sí | `21` | Toolchain Java LTS |
| `gradle_version` | string | sí | `8.14.3` o superior compatible | Versión de build system |
| `junit_major` | number | sí | `5` | Versión mayor de JUnit soportada |
| `serenity_version` | string | sí | `4.2.34` o superior compatible | Framework de reportes y ejecución |
| `cucumber_runner_mode` | string | sí | `junit-platform` | Modo de ejecución Cucumber |

#### Índices / Constraints
- No debe quedar referencia directa a `junit:junit:4.13.2` en dependencias.
- El task de test debe usar plataforma JUnit 5 (`useJUnitPlatform()`).
- El runner debe ser compatible con Cucumber + Serenity en stack vigente.

### API Endpoints

#### N/A
- Esta migración no agrega endpoints de backend ni cambios de API.

### Diseño Frontend

#### Componentes nuevos
| Componente | Archivo | Props principales | Descripción |
|------------|---------|------------------|-------------|
| N/A | N/A | N/A | No aplica; el repositorio automatiza UI, no construye frontend app |

#### Páginas nuevas
| Página | Archivo | Ruta | Protegida |
|--------|---------|------|-----------|
| N/A | N/A | N/A | N/A |

#### Hooks y State
| Hook | Archivo | Retorna | Descripción |
|------|---------|---------|-------------|
| N/A | N/A | N/A | No aplica |

#### Services (llamadas API)
| Función | Archivo | Endpoint |
|---------|---------|---------|
| N/A | N/A | N/A |

### Arquitectura y Dependencias
Objetivo técnico de dependencias:
- Mantener:
  - `net.serenity-bdd:serenity-core`
  - `net.serenity-bdd:serenity-cucumber`
  - `org.assertj:assertj-core`
  - `org.slf4j:slf4j-simple`
- Reemplazar:
  - `junit:junit` -> JUnit 5 (Jupiter y engine de plataforma)
- Ajustar runner:
  - eliminar patrón JUnit4 basado en `@RunWith`
  - migrar a ejecución basada en JUnit Platform compatible con Cucumber

### Notas de Implementación
- La migración debe hacerse incremental:
  1. Dependencias y task de test
  2. Runner
  3. Validación regresiva por tags y suite completa
- El baseline de verificación debe usar:
  - `./gradlew -v`
  - `./gradlew dependencies --configuration testRuntimeClasspath`
  - `./gradlew clean test --no-daemon`

---

## 3. LISTA DE TAREAS

> Checklist accionable para todos los agentes. Marcar cada ítem (`[x]`) al completarlo.

### Backend

#### Implementación
- [x] N/A para este flujo

#### Tests Backend
- [x] N/A para este flujo

### Frontend

#### Implementación
- [x] Actualizar `build.gradle` para remover dependencia directa de JUnit 4
- [x] Agregar dependencias JUnit 5 y engine/plataforma requerida
- [x] Configurar `test` task con `useJUnitPlatform()`
- [x] Migrar `CucumberTestSuite` desde runner JUnit4 a enfoque compatible con JUnit Platform
- [x] Verificar compatibilidad de plugin Serenity y task `aggregate`
- [x] Ejecutar validación de dependencias y resolver conflictos transitivos

#### Tests Frontend
- [x] `project-bootstrap.feature` corre en stack migrado
- [x] `signup-successful.feature` corre en stack migrado
- [x] `signup-weak-password.feature` corre en stack migrado
- [x] Ejecución por tags `@signup`, `@positive`, `@negative` se mantiene
- [x] Reporte `target/site/serenity/index.html` se genera correctamente

### QA
- [ ] Ejecutar `/risk-identifier` para riesgos de migración técnica
- [ ] Ejecutar `/gherkin-case-generator` si se amplían escenarios de regresión
- [ ] Comparar tiempos y estabilidad pre/post migración
- [x] Confirmar ausencia de JUnit4 en classpath final
- [x] Actualizar estado spec según avance (`APPROVED` -> `IN_PROGRESS` -> `IMPLEMENTED`)

### Criterios de Verificación por Comandos
- [x] `./gradlew -v`
- [x] `./gradlew dependencies --configuration testRuntimeClasspath`
- [x] `./gradlew clean test --no-daemon`
- [x] `./gradlew clean test --no-daemon -Dcucumber.filter.tags="@signup"`
- [x] `./gradlew aggregate`
