package com.jcaa.usersmanagement;

import com.jcaa.usersmanagement.infrastructure.config.DependencyContainer;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.UserManagementCli;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import java.util.Scanner;
import java.util.logging.Level;
import lombok.extern.java.Log;

// Clean Code - Regla 24 (Consistencia semántica):
// SOLUCIÓN: Se unifica el uso de java.util.logging vía @Log de Lombok, eliminando SLF4J 
// para que todo el proyecto hable el mismo idioma de trazabilidad.
@Log
public final class Main {

    private static final String START_MSG = "Starting Users Management System...";

    private Main() {
        // Estilo Java - Regla 4: Clase de entrada, no instanciable.
        throw new UnsupportedOperationException("Punto de entrada de la aplicación.");
    }

    public static void main(final String[] args) {
        // Clean Code - Regla 1 (Una sola cosa):
        // main() ahora solo orquesta el arranque delegando en métodos especializados.
        log.log(Level.INFO, START_MSG);
        
        final DependencyContainer container = bootstrapDependencies();
        launchApplication(container);
    }

    private static DependencyContainer bootstrapDependencies() {
        // Clean Code - Regla 22 (Fácil de refactorizar):
        // Se encapsula la creación del contenedor. Si el wiring cambia, main() no se entera.
        return new DependencyContainer();
    }

    private static void launchApplication(final DependencyContainer container) {
        // Clean Code - Regla 1: Responsabilidad de arranque de infraestructura.
        try (final Scanner scanner = new Scanner(System.in)) {
            final ConsoleIO consoleIO = createConsoleIO(scanner);
            startCliEntrypoint(container, consoleIO);
        }
    }

    private static ConsoleIO createConsoleIO(final Scanner scanner) {
        return new ConsoleIO(scanner, System.out);
    }

    private static void startCliEntrypoint(final DependencyContainer container, final ConsoleIO consoleIO) {
        // Clean Code - Regla 22: El acoplamiento a UserManagementCli se aisla en este método.
        new UserManagementCli(container.userController(), consoleIO).start();
    }
}