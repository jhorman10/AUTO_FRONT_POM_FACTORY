Feature: Validación de usuario ya registrado en signup

  Como usuario
  Quiero ver un mensaje claro cuando intento registrarme con un correo ya existente
  Para entender que debo iniciar sesión o recuperar mi acceso

  @signup @negative
  Scenario: Rechazo de registro por usuario ya registrado
    Given existe un usuario registrado con nombre "Juan Perez", email "juan.perez@example.com" y contraseña "SecurePass123!"
    When ingresa nombre "Juan Perez" en el formulario
    And ingresa email "juan.perez@example.com" en el formulario
    And ingresa contraseña "SecurePass123!" en el formulario
    And hace clic en el botón "Registrarse"
    Then recibe un mensaje indicando que el usuario ya está registrado
    And el usuario permanece en la página de signup
