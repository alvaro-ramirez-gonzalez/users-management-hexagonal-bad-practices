package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io;

import java.io.PrintStream;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ConsoleIO {

    // Clean Code - Regla 18: Evitar valores mágicos y textos hardcodeados.
    // SOLUCIÓN Regla 10: Se extraen los mensajes a constantes descriptivas.
    private static final String ERR_BLANK_VALUE = "  Value cannot be blank. Please try again.";
    private static final String ERR_INVALID_NUMBER = "  Invalid input. Please enter a number.";

    private final Scanner scanner;
    private final PrintStream out;

    public String readRequired(final String prompt) {
        // VIOLACIÓN Regla 4: nombre abreviado "v" en lugar del nombre descriptivo "value".
        // SOLUCIÓN Regla 24: Consistencia semántica. Se renombra "v" a "userInput".
        String userInput;
        do {
            out.print(prompt);
            userInput = scanner.nextLine().trim();
            if (userInput.isBlank()) {
                // VIOLACIÓN Regla 10: texto hardcodeado directamente — debe ser una constante.
                out.println(ERR_BLANK_VALUE);
            }
        } while (userInput.isBlank());
        return userInput;
    }

    public String readOptional(final String prompt) {
        out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(final String prompt) {
        while (true) {
            out.print(prompt);
            // VIOLACIÓN Regla 4: nombre abreviado "r" en lugar del nombre descriptivo "rawInput".
            // SOLUCIÓN Regla 24: Consistencia semántica utilizando "userInput" para el mismo concepto.
            final String userInput = scanner.nextLine().trim();
            try {
                return Integer.parseInt(userInput);
            } catch (final NumberFormatException ignored) {
                // VIOLACIÓN Regla 10: texto hardcodeado directamente — debe ser una constante.
                out.println(ERR_INVALID_NUMBER);
            }
        }
    }

    public void println(final String message) { out.println(message); }
    public void println() { out.println(); }
    public void printf(final String format, final Object... args) { out.printf(format, args); }
}