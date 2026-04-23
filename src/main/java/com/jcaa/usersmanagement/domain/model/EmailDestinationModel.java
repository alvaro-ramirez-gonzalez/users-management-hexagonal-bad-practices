package com.jcaa.usersmanagement.domain.model;

import lombok.Value;
import java.util.Objects;

@Value
public class EmailDestinationModel {

    // Clean Code - Regla 10: Constantes descriptivas para evitar "Magic Strings".
    private static final String EMAIL_REQUIRED_MSG = "El email del destinatario es requerido.";
    private static final String NAME_REQUIRED_MSG = "El nombre del destinatario es requerido.";
    private static final String SUBJECT_REQUIRED_MSG = "El asunto es requerido.";
    private static final String BODY_REQUIRED_MSG = "El cuerpo del mensaje es requerido.";

    String destinationEmail;
    String destinationName;
    String subject;
    String body;

    // Estilo Java - Regla 16: Constructor explícito con parámetros final.
    // Aunque @Value genera un constructor, definirlo manualmente permite realizar 
    // la validación de negocio en el momento del nacimiento del objeto.
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
        // Estilo Java - Regla 4: Uso de Objects.requireNonNull para validación de nulos.
        Objects.requireNonNull(value, errorMessage);

        // Clean Code - Regla 21: Lanzar excepciones claras en lugar de permitir estados inválidos.
        if (value.isBlank()) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value.trim(); // Clean Code - Regla 24: Consistencia (aseguramos datos limpios).
    }
}