package com.jcaa.usersmanagement.domain.exception;

public final class InvalidCredentialsException extends DomainException {

  // Solución Regla 10: Centralizamos los mensajes en constantes.
  // Esto permite que el dominio sea el único dueño de la narrativa de error.
  private static final String INVALID_CREDENTIALS_MSG = "Correo o contraseña incorrectos.";
  private static final String USER_NOT_ACTIVE_MSG = "Tu cuenta no está activa. Contacta al administrador.";

  private InvalidCredentialsException(final String message) {
    super(message);
  }

  public static InvalidCredentialsException becauseCredentialsAreInvalid() {
    // VIOLACIÓN Regla 10: texto de error hardcodeado directamente.
   // Se utiliza la constante INVALID_CREDENTIALS_MSG.
    return new InvalidCredentialsException(INVALID_CREDENTIALS_MSG);
  }

  public static InvalidCredentialsException becauseUserIsNotActive() {
    // VIOLACIÓN Regla 10: texto de error hardcodeado directamente.
    // Se utiliza la constante USER_NOT_ACTIVE_MSG.
    return new InvalidCredentialsException(USER_NOT_ACTIVE_MSG);
  }
}