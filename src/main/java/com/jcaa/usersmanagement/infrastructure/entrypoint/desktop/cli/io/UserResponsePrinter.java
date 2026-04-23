package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UserResponsePrinter {

    private static final String SEPARATOR = "-".repeat(52);
    private static final String ROW_FORMAT = "  %-10s : %s%n";
    
    // Clean Code - Regla 16: Sustitución de cascada if/else por un mapa de traducción (Data Driven)
    private static final Map<String, String> STATUS_LABELS = Map.of(
        "ACTIVE",   "Activo",
        "INACTIVE", "Inactivo",
        "PENDING",  "Pendiente de activacion",
        "BLOCKED",  "Bloqueado",
        "DELETED",  "Eliminado"
    );
    private static final String UNKNOWN_STATUS = "Estado desconocido";

    private final ConsoleIO console;

    public void print(final UserResponse response) {
        console.println(SEPARATOR);
        console.printf(ROW_FORMAT, "ID",     response.getId());
        console.printf(ROW_FORMAT, "Name",   response.getName());
        console.printf(ROW_FORMAT, "Email",  response.getEmail());
        console.printf(ROW_FORMAT, "Role",   response.getRole());
        console.printf(ROW_FORMAT, "Status", getStatusLabel(response.getStatus()));
        console.println(SEPARATOR);
    }

    public void printList(final List<UserResponse> users) {
        // VIOLACIÓN Regla 5: Null Safety y Robustez.
        // SOLUCIÓN: Se protege contra null y se evita la lógica de negocio invertida.
        if (users == null || users.isEmpty()) {
            console.println("  No users found.");
            return;
        }
        
        console.printf("%n  Total: %d user(s)%n", users.size());
        users.forEach(this::print);
    }

    // Clean Code - Regla 27: Código listo para leer, no para descifrar.
    // SOLUCIÓN: Refactorización a un bucle tradicional mucho más legible que el Stream complejo.
    public void printSummary(final List<UserResponse> users) {
        if (users == null || users.isEmpty()) {
            console.println("  No users found.");
            return;
        }

        for (final UserResponse user : users) {
            console.printf("  %s (%s)%n", 
                user.getName(), 
                getStatusLabel(user.getStatus()));
        }
    }

    // Clean Code - Regla 16: Eliminación de condicionales repetitivas.
    // SOLUCIÓN: Acceso directo al mapa de constantes.
    private static String getStatusLabel(final String status) {
        return STATUS_LABELS.getOrDefault(status, UNKNOWN_STATUS);
    }
}