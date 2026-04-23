package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

import lombok.Builder;

// VIOLACIÓN Regla 2 (Reglas 1.md): se usa una clase mutable con @Data en lugar de record.
// SOLUCIÓN: Se migra a record de Java para garantizar inmutabilidad absoluta y semántica de datos.
// Clean Code - Regla 15 (inmutabilidad como preferencia de diseño):
// El record elimina los setters, previniendo modificaciones accidentales como response.setEmail(...).
@Builder
public record UserResponse(
    String id,
    String name,
    String email,
    String role,
    String status
) {
    // El constructor compacto de Java permite validar o transformar datos si fuera necesario
    public UserResponse {
        // Aseguramos que el DTO nazca con estado consistente
        if (id == null) {
            throw new IllegalArgumentException("UserResponse ID cannot be null");
        }
    }
}