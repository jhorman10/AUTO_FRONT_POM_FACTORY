Feature: Registro de turnos con prioridad clinica

  Como recepcionista autenticada
  Quiero registrar turnos con nivel de urgencia
  Para priorizar la atencion medica segun criticidad

  Background:
    Given la recepcionista esta autenticada en el sistema
    And navega a la pagina de registro de turnos

  @appointment @priority @positive @hu01
  Scenario Outline: Registro exitoso de turno con prioridad valida
    When registra un turno con nombre "<nombre>", cedula "<cedula>" y prioridad "<prioridad>"
    Then el turno se registra exitosamente y se muestra confirmacion visual

    Examples:
      | nombre        | cedula     | prioridad |
      | Carlos Lopez  | 1234567890 | high      |
      | Ana Rodriguez | 9876543210 | medium    |
      | Pedro Gomez   | 5555555555 | low       |

  @appointment @priority @negative @hu01
  Scenario: Rechazo de registro sin seleccionar prioridad
    When intenta registrar un turno con nombre "Luisa Martinez" y cedula "1122334455" sin seleccionar prioridad
    Then el formulario muestra validacion de prioridad requerida
    And el turno no queda registrado exitosamente

  @appointment @priority @edge @hu01
  Scenario: El turno registrado aparece en la pantalla publica como waiting
    Given registra un turno para "Carlos Lopez" con cedula "1234567890" y prioridad "high"
    When un visitante accede a la pantalla publica de turnos
    Then el turno para "Carlos Lopez" con cedula "1234567890" existe en el sistema
