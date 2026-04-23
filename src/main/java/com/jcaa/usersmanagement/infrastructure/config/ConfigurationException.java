package com.jcaa.usersmanagement.infrastructure.config;

public final class ConfigurationException extends RuntimeException {

  // Solución Regla 10: Centralizamos el mensaje en una constante privada.
  // Esto permite cambiar la narrativa del error sin tocar la lógica del método fábrica.
  private static final String CONFIG_LOAD_FAILED_MSG = "Failed to load the application configuration.";

  private ConfigurationException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static ConfigurationException becauseLoadFailed(final Throwable cause) {
    // VIOLACIÓN Regla 10: texto de error hardcodeado directamente.
    // Se utiliza la constante CONFIG_LOAD_FAILED_MSG.
    return new ConfigurationException(CONFIG_LOAD_FAILED_MSG, cause);
  }
}