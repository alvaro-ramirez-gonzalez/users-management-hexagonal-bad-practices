package com.jcaa.usersmanagement.infrastructure.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.experimental.UtilityClass;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

// VIOLACIÓN Regla 4: clase con solo métodos estáticos que NO tiene @UtilityClass ni constructor privado.
// Debería anotarse con @UtilityClass para evitar instanciación accidental y generar el constructor privado automáticamente.
@UtilityClass
public final class ValidatorProvider {

    public static Validator buildValidator() {
        // Clean Code - Regla 25: Claridad sobre ingenio (Try-with-resources)
        // Se asegura el cierre de la fábrica para evitar fugas de recursos.
        try (final ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()) {
            return factory.getValidator();
        }
    }
}