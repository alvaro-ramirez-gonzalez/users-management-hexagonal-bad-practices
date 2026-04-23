package com.jcaa.usersmanagement.infrastructure.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class AppProperties {

  private static final String PROPERTIES_FILE = "application.properties";

  private final Properties properties;

  public AppProperties() {
    this(AppProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
  }

  // Package-private — test entry point
  AppProperties(final InputStream stream) {
    this.properties = doLoad(stream);
  }

  private static Properties doLoad(final InputStream stream) {
    // VIOLACIÓN Regla 4: se usa == null en lugar de Objects.requireNonNull().
    // Refactorización Alejandro: Corregido usando la API de Objects.
    Objects.requireNonNull(stream, "File not found in classpath: " + PROPERTIES_FILE);
    
    // VIOLACIÓN Regla 4: nombre abreviado "props" en lugar de "properties".
    // Nombre descriptivo completo.
    final Properties properties = new Properties();
    try (stream) {
      properties.load(stream);
    } catch (final IOException exception) {
      throw ConfigurationException.becauseLoadFailed(exception);
    }
    return properties;
  }

  public String get(final String key) {
    // VIOLACIÓN Regla 4: nombre abreviado "val" en lugar de "value".
    // Nombre descriptivo "propertyValue".
    final String propertyValue = properties.getProperty(key);
    
    // VIOLACIÓN Regla 4: se usa == null en lugar de Objects.requireNonNull().
   // Corregido usando Objects.
    Objects.requireNonNull(propertyValue, "Property not found in " + PROPERTIES_FILE + ": " + key);
    
    return propertyValue;
  }

  public int getInt(final String key) {
    return Integer.parseInt(get(key));
  }
}