package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetUserByIdQuery;
import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.UUID;

@UtilityClass
public class UserApplicationMapper {

  /**
   * Refactorización Alejandro: Unificamos el nombre del método solicitado.
   * Resuelve la Regla 24 al usar 'email' consistentemente.
   */
  public static UserModel toDomain(final CreateUserCommand command) {
    return UserModel.create(
        new UserId(UUID.randomUUID().toString()),
        new UserName(command.name()),
        new UserEmail(command.email()), // Antes 'correo' o 'correoElectronico'
        UserPassword.fromPlainText(command.password()),
        UserRole.fromString(command.role()));
  }

  public static UserModel fromCreateCommandToModel(final CreateUserCommand command) {
    // Clean Code - Regla 24 (consistencia semántica): Corregido.
    // Usamos 'email' para que coincida con el resto del proyecto.
    final String email = command.email();

    return UserModel.create(
        new UserId(command.id()),
        new UserName(command.name()),
        new UserEmail(email),
        UserPassword.fromPlainText(command.password()),
        UserRole.fromString(command.role()));
  }

  public static UserModel fromUpdateCommandToModel(
      final UpdateUserCommand command, final UserPassword currentPassword) {

    final UserPassword passwordToUse = (Objects.isNull(command.password()) || command.password().isBlank())
        ? currentPassword
        : UserPassword.fromPlainText(command.password());

    // Clean Code - Regla 24: Unificamos a 'email'.
    final String email = command.email();

    // Nota sobre Regla 15: Al usar UserModel.reconstitute (asumiendo inmutabilidad),
    // protegemos el estado del objeto frente a cambios accidentales.
    return UserModel.reconstitute(
        new UserId(command.id()),
        new UserName(command.name()),
        new UserEmail(email),
        passwordToUse,
        UserRole.fromString(command.role()),
        UserStatus.fromString(command.status()));
  }

  public static UserId fromGetUserByIdQueryToUserId(final GetUserByIdQuery query) {
    return new UserId(query.id());
  }

  public static UserId fromDeleteCommandToUserId(final DeleteUserCommand command) {
    return new UserId(command.id());
  }

  // Clean Code - Regla 21 (no retornar banderas de error): Corregido.
  // Reemplazamos el retorno de -1 por una excepción explícita (Fail-Fast).
  public static int roleToCode(final String role) {
    if (Objects.isNull(role) || role.isBlank()) {
      throw new IllegalArgumentException("Role cannot be null or empty");
    }

    return switch (role.toUpperCase()) {
      case "ADMIN" -> 1;
      case "MEMBER" -> 2;
      case "REVIEWER" -> 3;
      default -> throw new IllegalArgumentException("Unknown role code for: " + role);
    };
  }
}