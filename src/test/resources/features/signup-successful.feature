Feature: Registro exitoso en la plataforma

  Como usuario
  Quiero registrarme en la plataforma con datos válidos
  Para acceder a los servicios de la aplicación

  @signup @positive
  Scenario: Registro exitoso con nombre, email y contraseña válida
    Given el usuario accede a la página de signup
    When ingresa nombre "Juan Perez" en el formulario
    And ingresa email "juan.perez@example.com" en el formulario
    And ingresa contraseña "SecurePass123!" en el formulario
    And hace clic en el botón "Registrarse"
    Then recibe confirmación de registro exitoso
    And el mensaje de éxito es visible en la pantalla
