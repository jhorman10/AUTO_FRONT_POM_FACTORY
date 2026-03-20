Feature: Validación de contraseña débil en registro

  Como usuario
  Quiero que el sistema valide la fortaleza de mi contraseña
  Para asegurar que mi cuenta está protegida adecuadamente

  @signup @negative
  Scenario: Rechazo de registro por contraseña débil
    Given el usuario accede a la página de signup
    When ingresa nombre "Maria Garcia" en el formulario
    And ingresa email "maria.garcia@example.com" en el formulario
    And ingresa contraseña "123456" en el formulario
    And hace clic en el botón "Registrarse"
    Then recibe un mensaje de error indicando que la contraseña es débil
    And el usuario permanece en la página de signup
