# Requerimiento: Flujo 1 Front-End con POM + Page Factory

## Descripcion General

Se debe iniciar el repositorio `AUTO_FRONT_POM_FACTORY` para automatizar el flujo 1 del taller de la semana 5 de la maestria en automatizacion. Este repositorio cubre exclusivamente pruebas UI sobre una aplicacion web propia construida en fases anteriores, usando Java, Serenity BDD, Gradle, Cucumber y el patron POM con `@FindBy` de Page Factory.

## Problema / Necesidad

El entregable exige demostrar dominio del patron POM tradicional antes de pasar a Screenplay. La automatizacion debe ser mantenible, legible y desacoplada, evitando scripts fragiles o dependientes entre si. El repositorio debe quedar listo para ejecutar dos escenarios UI independientes y generar reportes Serenity legibles en una sesion de evaluacion en vivo.

## Solucion Propuesta

### 1. Inicializacion del proyecto de automatizacion

Crear un proyecto Gradle en Java con Serenity BDD y Cucumber como base de ejecucion.

- Configuracion del driver en `serenity.conf`.
- Estructura de paquetes orientada a POM + Step Definitions + Runners + utilidades de datos.
- Integracion con reportes Serenity.
- README con prerequisitos y comandos de ejecucion.

### 2. Automatizacion UI con POM + Page Factory

Implementar dos escenarios de prueba independientes sobre una aplicacion propia ya existente.

- Un escenario debe validar un flujo positivo.
- Un escenario debe validar un flujo negativo.
- Ningun escenario puede depender del resultado de otro.
- El patron POM debe usar `@FindBy` y encapsular acciones y elementos por pantalla.
- La escritura Gherkin debe enfocarse en comportamiento de negocio y evitar pasos tecnicos innecesarios.

### 3. Restricciones del taller que impactan este repositorio

- Este repositorio corresponde solo al flujo 1 del entregable global.
- Los otros entregables (`AUTO_FRONT_SCREENPLAY` y `AUTO_API_SCREENPLAY`) no se implementan aqui, pero la arquitectura y la documentacion deben dejar claro el alcance para no mezclar patrones.
- No debe existir codigo comentado ni comentarios dentro de las clases.
- La nomenclatura de clases, metodos y variables debe ser semantica.

## Datos pendientes para cerrar la automatizacion

1. URL base de la aplicacion web a automatizar.
2. Definicion exacta de los dos escenarios de negocio a cubrir.
3. Datos de prueba requeridos para el flujo positivo y el flujo negativo.
4. Credenciales o estrategia de autenticacion, si el flujo las necesita.
5. Criterio de ejecucion del navegador objetivo y modo de ejecucion local o CI.

## Criterios de Aceptacion (Alto Nivel)

1. Existe un proyecto Java con Gradle configurado para Serenity BDD y Cucumber.
2. La configuracion del navegador vive en `serenity.conf`.
3. La solucion adopta POM con Page Factory usando `@FindBy`.
4. Se automatizan dos escenarios UI independientes: al menos uno positivo y uno negativo.
5. Los escenarios producen reportes Serenity ejecutables para demo.
6. El README documenta la ejecucion local de los tests.

## Restricciones

- No reutilizar en este repositorio escenarios destinados a Screenplay.
- No asumir la aplicacion ni los selectores si no han sido definidos por el usuario.
- No implementar escenarios dependientes entre si.
- Mantener el proyecto preparado para evolucionar sin romper la separacion por patron.

## Prioridad

Alta.