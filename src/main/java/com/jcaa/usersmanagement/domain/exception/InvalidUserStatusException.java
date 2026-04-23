package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserStatusException extends DomainException {

  // Solución Regla 10: Centralizamos el mensaje en una constante privada.
  // El dominio es el único dueño de la semántica de sus errores.
  private static final String INVALID_STATUS_MSG = "The user status '%s' is not valid.";

  private InvalidUserStatusException(final String message) {
    super(message);
  }

  public static InvalidUserStatusException becauseValueIsInvalid(final String status) {
    // VIOLACIÓN Regla 10: texto hardcodeado directamente — corregido con constante.
    // Utilizamos String.format con la constante definida arriba.
    return new InvalidUserStatusException(String.format(INVALID_STATUS_MSG, status));
  }
}