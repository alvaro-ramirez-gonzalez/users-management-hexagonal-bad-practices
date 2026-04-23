package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserRoleException extends DomainException {

  // Solución Regla 10: Centralizamos el mensaje en una constante privada.
  // Esto evita la dispersión de literales y facilita la coherencia del dominio.
  private static final String INVALID_ROLE_MSG = "The user role '%s' is not valid.";

  private InvalidUserRoleException(final String message) {
    super(message);
  }

  public static InvalidUserRoleException becauseValueIsInvalid(final String role) {
    // VIOLACIÓN Regla 10: texto hardcodeado directamente — corregido con constante.
    // Utilizamos String.format con la constante definida arriba.
    return new InvalidUserRoleException(String.format(INVALID_ROLE_MSG, role));
  }
}