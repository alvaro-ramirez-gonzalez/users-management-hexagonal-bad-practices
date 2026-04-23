package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidUserNameException;
import java.util.Objects;

public record UserName(String value) {

  // Solución Regla 10: Definimos la constante para evitar Magic Numbers.
  // Esto hace que la regla de negocio sea explícita y fácil de cambiar.
  private static final int MINIMUM_LENGTH = 3;

  public UserName {
    // VIOLACIÓN Regla 4: Corregida usando Objects.requireNonNull().
   // Eliminamos la comparación directa (== null).
    Objects.requireNonNull(value, "UserName cannot be null");

    final String normalizedValue = value.trim();
    
    validateNotEmpty(normalizedValue);
    validateMinimumLength(normalizedValue);
    
    // Asignación del valor normalizado al componente del record.
    value = normalizedValue;
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidUserNameException.becauseValueIsEmpty();
    }
  }

  private static void validateMinimumLength(final String normalizedValue) {
    // VIOLACIÓN Regla 10: Corregida usando la constante MINIMUM_LENGTH.
    if (normalizedValue.length() < MINIMUM_LENGTH) {
      throw InvalidUserNameException.becauseLengthIsTooShort(MINIMUM_LENGTH);
    }
  }

  @Override
  public String toString() {
    return value;
  }
}