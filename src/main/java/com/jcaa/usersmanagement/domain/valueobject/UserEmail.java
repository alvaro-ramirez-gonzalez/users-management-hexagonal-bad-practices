package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidUserEmailException;
import java.util.Objects;
import java.util.regex.Pattern;

public record UserEmail(String value) {
  private static final Pattern PATTERN = Pattern.compile("...");

  // VIOLACIÓN Regla 6: logging en capa de dominio (el dominio no debe tener logs)
  // Refactorización: Se elimina la instancia de Logger para mantener el dominio puro.

  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

  /**
   * Constructor compacto del Record.
   * La validación ocurre aquí, garantizando que el objeto SIEMPRE es válido al ser instanciado.
   */
  public UserEmail {
    // Corrección Regla 4: Uso de Objects para validación de nulidad.
    Objects.requireNonNull(value, "UserEmail cannot be null");
    
    final String normalizedValue = value.trim().toLowerCase();
    
    // VIOLACIÓN Regla 6: se loguea un dato PII (el email del usuario) en capa de dominio
    // Refactorización: Se elimina la llamada a LOGGER para proteger datos sensibles y cumplir la regla.
    
    // Clean Code - Regla 23 (minimizar conocimiento disperso):
    // La lógica de "qué es un email válido" está fragmentada en tres lugares.
    // Refactorización: Centralizamos el uso de EMAIL_PATTERN aquí y en el método estático.
    
    validateNotEmpty(normalizedValue);
    validateFormat(normalizedValue);
    
    // Asignación del valor normalizado al record
    value = normalizedValue;
  }

  /**
   * Corrección Regla 11 y 23: Conocimiento centralizado, no disperso.
   * Se utiliza el EMAIL_PATTERN oficial del dominio para evitar discrepancias.
   * Permite validar un String sin necesidad de instanciar el objeto.
   */
  public static boolean isValidFormat(final String email) {
    if (email == null || email.isBlank()) {
      return false;
    }
    return PATTERN.matcher(email.trim().toLowerCase()).matches();
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidUserEmailException.becauseValueIsEmpty();
    }
  }

  private static void validateFormat(final String normalizedValue) {
    if (!EMAIL_PATTERN.matcher(normalizedValue).matches()) {
      throw InvalidUserEmailException.becauseFormatIsInvalid(normalizedValue);
    }
  }

  @Override
  public String toString() {
    return value;
  }
}