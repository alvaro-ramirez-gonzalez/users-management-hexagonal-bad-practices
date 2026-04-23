package com.jcaa.usersmanagement.infrastructure.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.experimental.UtilityClass;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

// VIOLACIÓN Regla 4 (Solucionada): Se usa @UtilityClass para garantizar que la clase sea final,
// genere un constructor privado que lance una excepción y convierta los métodos en estáticos.
@UtilityClass
public final class ValidatorProvider {

    /**
     * Construye una instancia de Validator configurada con un interpolador de parámetros simple.
     * * Clean Code - Regla 25: Claridad sobre ingenio.
     * El uso de try-with-resources garantiza el cierre de ValidatorFactory, previniendo
     * fugas de memoria (Memory Leaks) en el arranque de la infraestructura.
     */
    public static Validator buildValidator() {
        try (final ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()) {
            return factory.getValidator();
        }
    }
}