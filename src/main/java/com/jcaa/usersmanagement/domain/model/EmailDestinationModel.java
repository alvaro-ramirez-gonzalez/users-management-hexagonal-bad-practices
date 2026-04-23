package com.jcaa.usersmanagement.domain.model;

import lombok.Value;
import java.util.Objects;

@Value
public class EmailDestinationModel {

  // Solución Regla 10: Centralizamos los mensajes en constantes privadas.
  private static final String EMAIL_REQUIRED_MSG = "El email del destinatario es requerido.";
  private static final String NAME_REQUIRED_MSG = "El nombre del destinatario es requerido.";
  private static final String SUBJECT_REQUIRED_MSG = "El asunto es requerido.";
  private static final String BODY_REQUIRED_MSG = "El cuerpo del mensaje es requerido.";

  String destinationEmail;
  String destinationName;
  String subject;
  String body;

  public EmailDestinationModel(
      final String destinationEmail,
      final String destinationName,
      final String subject,
      final String body) {
    this.destinationEmail = validateNotBlank(destinationEmail, EMAIL_REQUIRED_MSG);
    this.destinationName  = validateNotBlank(destinationName,  NAME_REQUIRED_MSG);
    this.subject          = validateNotBlank(subject,          SUBJECT_REQUIRED_MSG);
    this.body             = validateNotBlank(body,             BODY_REQUIRED_MSG);
  }

  private static String validateNotBlank(final String value, final String errorMessage) {
    // VIOLACIÓN Regla 4: corregida usando Objects.requireNonNull().
    // Para objetos siempre debe usarse Objects, nunca operadores == o !=.
    Objects.requireNonNull(value, errorMessage);

    // VIOLACIÓN Regla 10: corregida usando constantes descriptivas.
    if (value.trim().isEmpty()) {
      throw new IllegalArgumentException(errorMessage);
    }
    return value;
  }
}