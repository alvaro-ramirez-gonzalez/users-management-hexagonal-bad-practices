package com.jcaa.usersmanagement.domain.exception;

public final class UserAlreadyExistsException extends DomainException {

  // Solución Regla 10: Centralizamos el mensaje en una constante privada.
  // El dominio es el único que define la narrativa de colisión de datos.
  private static final String EMAIL_ALREADY_EXISTS_MSG = "A user with email '%s' already exists.";

  private UserAlreadyExistsException(final String message) {
    super(message);
  }

  public static UserAlreadyExistsException becauseEmailAlreadyExists(final String email) {
    // VIOLACIÓN Regla 10: texto de error hardcodeado directamente en el método fábrica.
   // Se utiliza la constante EMAIL_ALREADY_EXISTS_MSG.
    return new UserAlreadyExistsException(String.format(EMAIL_ALREADY_EXISTS_MSG, email));
  }
}