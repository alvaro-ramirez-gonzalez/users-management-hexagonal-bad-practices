package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidUserIdException;
import java.util.Objects;

public record UserId(String value) {

  /**
   * Constructor compacto del Record.
   * Garantiza que cualquier instancia de UserId en el sistema sea válida.
   */
  public UserId {
    // VIOLACIÓN Regla 4: corregida usando Objects.requireNonNull().
   // Eliminamos la comparación directa (== null).
    Objects.requireNonNull(value, "UserId cannot be null");

    final String normalizedValue = value.trim();
    
    validateNotEmpty(normalizedValue);
    
    // Asigna el valor normalizado al componente del record
    value = normalizedValue;
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidUserIdException.becauseValueIsEmpty();
    }
  }

  @Override
  public String toString() {
    return value;
  }
}