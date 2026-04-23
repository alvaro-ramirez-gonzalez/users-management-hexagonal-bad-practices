package com.jcaa.usersmanagement.infrastructure.config;

import com.jcaa.usersmanagement.application.port.in.*;
import com.jcaa.usersmanagement.application.service.*;
import com.jcaa.usersmanagement.infrastructure.adapter.email.JavaMailEmailSenderAdapter;
import com.jcaa.usersmanagement.infrastructure.adapter.email.SmtpConfig;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.config.DatabaseConfig;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.config.DatabaseConnectionFactory;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository.UserRepositoryMySQL;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.UserController;

import java.sql.Connection;
import jakarta.validation.Validator;

public final class DependencyContainer {

    private static final String DB_HOST = "db.host";
    private static final String DB_PORT = "db.port";
    private static final String DB_NAME = "db.name";
    private static final String DB_USER = "db.username";
    private static final String DB_PASSWORD = "db.password";

    private static final String SMTP_HOST = "smtp.host";
    private static final String SMTP_PORT = "smtp.port";
    private static final String SMTP_USER = "smtp.username";
    private static final String SMTP_PASSWORD = "smtp.password";
    private static final String SMTP_FROM = "smtp.from.address";
    private static final String SMTP_FROM_NAME = "smtp.from.name";

    private final UserController userController;

    public DependencyContainer() {
        final AppProperties properties = new AppProperties();
        final Validator validator = ValidatorProvider.buildValidator();

        // Persistencia
        final Connection connection = buildDatabaseConnection(properties);
        final UserRepositoryMySQL userRepository = new UserRepositoryMySQL(connection);

        // Clean Code - Regla 22 (el código debe ser fácil de borrar y refactorizar):
        // Clean Code - Regla 19 (temporal coupling):
        // SOLUCIÓN: Se mantiene el llamado pero se documenta que la inicialización 
        // es responsabilidad de la infraestructura antes de la inyección.
        userRepository.init();

        // Notificaciones
        final EmailNotificationService emailNotification = buildEmailService(properties);

        // Inyección de Casos de Uso
        final CreateUserUseCase createUserUseCase = 
            new CreateUserService(userRepository, userRepository, emailNotification, validator);
        
        final UpdateUserUseCase updateUserUseCase = 
            new UpdateUserService(userRepository, userRepository, userRepository, emailNotification, validator);
        
        final DeleteUserUseCase deleteUserUseCase = 
            new DeleteUserService(userRepository, userRepository, validator);
        
        final GetUserByIdUseCase getUserByIdUseCase = new GetUserByIdService(userRepository, validator);
        final GetAllUsersUseCase getAllUsersUseCase = new GetAllUsersService(userRepository);
        final LoginUseCase loginUseCase = new LoginService(userRepository, validator);

        this.userController = new UserController(
            createUserUseCase,
            updateUserUseCase,
            deleteUserUseCase,
            getUserByIdUseCase,
            getAllUsersUseCase,
            loginUseCase
        );
    }

    public UserController userController() {
        return userController;
    }

    private static Connection buildDatabaseConnection(final AppProperties properties) {
        final DatabaseConfig config = new DatabaseConfig(
            properties.get(DB_HOST),
            properties.getInt(DB_PORT),
            properties.get(DB_NAME),
            properties.get(DB_USER),
            properties.get(DB_PASSWORD));
        
        // VIOLACIÓN Regla 4 (consecuencia): DatabaseConnectionFactory ya no tiene @UtilityClass,
        // por lo que debe instanciarse para llamar a createConnection.
        // SOLUCIÓN: Instanciación explícita para asegurar la disponibilidad de la conexión.
        return new DatabaseConnectionFactory().createConnection(config);
    }

    private static EmailNotificationService buildEmailService(final AppProperties properties) {
        final SmtpConfig config = new SmtpConfig(
            properties.get(SMTP_HOST),
            properties.getInt(SMTP_PORT),
            properties.get(SMTP_USER),
            properties.get(SMTP_PASSWORD),
            properties.get(SMTP_FROM),
            properties.get(SMTP_FROM_NAME));
        
        return new EmailNotificationService(new JavaMailEmailSenderAdapter(config));
    }
}