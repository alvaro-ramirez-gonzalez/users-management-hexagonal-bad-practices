package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserIdException extends DomainException {

  // Solución Regla 10: Centralizamos el mensaje en una constante privada.
  // El dominio debe ser el único dueño de sus definiciones de error.
  private static final String USER_ID_EMPTY_MSG = "The user id must not be empty.";

  private InvalidUserIdException(final String message) {
    super(message);
  }

  public static InvalidUserIdException becauseValueIsEmpty() {
    // VIOLACIÓN Regla 10: texto hardcodeado directamente — corregido con constante.
    // Se utiliza la constante descriptiva.
    return new InvalidUserIdException(USER_ID_EMPTY_MSG);
  }
}