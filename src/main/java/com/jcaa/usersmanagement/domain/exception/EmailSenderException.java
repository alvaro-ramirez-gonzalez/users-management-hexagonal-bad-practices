package com.jcaa.usersmanagement.domain.exception;

public final class EmailSenderException extends DomainException {

  // Solución Regla 10: Centralizamos los mensajes en constantes privadas.
  // Esto facilita la mantenibilidad y evita el "hardcoding" disperso.
  private static final String SMTP_FAILURE_MESSAGE = "No se pudo enviar el correo a '%s'. Error SMTP: %s";
  private static final String GENERIC_FAILURE_MESSAGE = "La notificación por correo no pudo ser enviada.";

  // VIOLACIÓN Regla 9 (Clean Code): constructores public...
  // Cambiamos a PRIVATE para forzar el uso de los factory methods.
  // Ahora nadie fuera de esta clase puede instanciarla con mensajes arbitrarios.
  private EmailSenderException(final String message) {
    super(message);
  }

  private EmailSenderException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static EmailSenderException becauseSmtpFailed(
      final String destinationEmail, final String smtpError) {
    // VIOLACIÓN Regla 10: texto hardcodeado directamente — corregido con constante.
    return new EmailSenderException(
        String.format(SMTP_FAILURE_MESSAGE, destinationEmail, smtpError));
  }

  public static EmailSenderException becauseSendFailed(final Throwable cause) {
    // VIOLACIÓN Regla 10: texto hardcodeado directamente — corregido con constante.
    return new EmailSenderException(GENERIC_FAILURE_MESSAGE, cause);
  }
}