package com.jcaa.usersmanagement.domain.exception;

public final class UserNotFoundException extends DomainException {

  // Solución Regla 10: Centralizamos el mensaje en una constante privada.
  // Esto asegura que la narrativa de "recurso no encontrado" sea única y controlada.
  private static final String USER_ID_NOT_FOUND_MSG = "The user with id '%s' was not found.";

  private UserNotFoundException(final String message) {
    super(message);
  }

  public static UserNotFoundException becauseIdWasNotFound(final String userId) {
    // VIOLACIÓN Regla 10: texto de error hardcodeado directamente en el método fábrica.
    // Se utiliza la constante USER_ID_NOT_FOUND_MSG.
    return new UserNotFoundException(String.format(USER_ID_NOT_FOUND_MSG, userId));
  }
}