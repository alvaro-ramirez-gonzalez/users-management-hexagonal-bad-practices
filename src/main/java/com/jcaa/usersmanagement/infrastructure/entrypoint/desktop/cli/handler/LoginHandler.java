package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.InvalidCredentialsException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.UserResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.UserController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.LoginRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;

import java.util.logging.Logger;

public final class LoginHandler implements OperationHandler {

    // Estilo Java - Regla 7: Logger nativo explícito para evitar fallos de Lombok
    private static final Logger LOGGER = Logger.getLogger(LoginHandler.class.getName());

    private final UserController userController;
    private final ConsoleIO console;
    private final UserResponsePrinter printer;

    // Estilo Java - Regla 4: Constructor manual para garantizar la inyección ante fallos de Lombok
    public LoginHandler(
            final UserController userController,
            final ConsoleIO console,
            final UserResponsePrinter printer) {
        this.userController = userController;
        this.console = console;
        this.printer = printer;
    }

    @Override
    public void handle() {
        final LoginRequest request = captureLoginData();
        executeLogin(request);
    }

    private LoginRequest captureLoginData() {
        // Clean Code - Regla 3: Detalle técnico de lectura encapsulado (Bajo nivel)
        final String email    = console.readRequired("Email   : ");
        final String password = console.readRequired("Password: ");
        return new LoginRequest(email, password);
    }

    private void executeLogin(final LoginRequest request) {
        try {
            final UserResponse user = userController.login(request);
            console.println("\n  Login successful. Welcome!");
            printer.print(user);
        } catch (final InvalidCredentialsException exception) {
            // VIOLACIÓN Regla 6: se loguea el email del usuario (PII) al registrar el fallo de login.
            // Los datos de negocio/cliente son PII y NO deben loguearse nunca.
            // SOLUCIÓN: Log anonimizado que registra el evento de seguridad sin exponer PII.
            LOGGER.warning("Intento de login fallido: Credenciales inválidas proporcionadas.");
            console.println("  Error: " + exception.getMessage());
        }
    }
}