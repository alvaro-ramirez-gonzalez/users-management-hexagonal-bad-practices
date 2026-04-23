package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserNameException extends DomainException {

  // Solución Regla 10: Centralizamos los mensajes en constantes privadas.
  // El dominio debe ser el único dueño de la narrativa de sus errores.
  private static final String NAME_EMPTY_MSG = "The user name must not be empty.";
  private static final String NAME_TOO_SHORT_MSG = "The user name must have at least %d characters.";

  private InvalidUserNameException(final String message) {
    super(message);
  }

  public static InvalidUserNameException becauseValueIsEmpty() {
    // VIOLACIÓN Regla 10: texto hardcodeado directamente — corregido con constante.
    return new InvalidUserNameException(NAME_EMPTY_MSG);
  }

  public static InvalidUserNameException becauseLengthIsTooShort(final int minimumLength) {
    // VIOLACIÓN Regla 10: texto hardcodeado directamente — corregido con constante.
    return new InvalidUserNameException(
        String.format(NAME_TOO_SHORT_MSG, minimumLength));
  }
}