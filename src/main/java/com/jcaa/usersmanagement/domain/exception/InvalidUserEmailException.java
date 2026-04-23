package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserEmailException extends DomainException {

  // Solución Regla 10: Centralizamos los mensajes en constantes privadas.
  // El dominio es el único dueño de la definición de sus errores.
  private static final String EMAIL_EMPTY_MSG = "The user email must not be empty.";
  private static final String EMAIL_INVALID_FORMAT_MSG = "The user email format is invalid: '%s'.";

  private InvalidUserEmailException(final String message) {
    super(message);
  }

  public static InvalidUserEmailException becauseValueIsEmpty() {
    // VIOLACIÓN Regla 10: texto hardcodeado directamente — corregido con constante.
    return new InvalidUserEmailException(EMAIL_EMPTY_MSG);
  }

  public static InvalidUserEmailException becauseFormatIsInvalid(final String email) {
    // VIOLACIÓN Regla 10: texto hardcodeado directamente — corregido con constante.
    return new InvalidUserEmailException(String.format(EMAIL_INVALID_FORMAT_MSG, email));
  }
}