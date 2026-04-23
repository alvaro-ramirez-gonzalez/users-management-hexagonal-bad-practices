package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.DeleteUserUseCase;
import com.jcaa.usersmanagement.application.port.out.DeleteUserPort;
import com.jcaa.usersmanagement.application.port.out.GetUserByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteUserCommand;
import com.jcaa.usersmanagement.application.service.mapper.UserApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Set;

// Refactorización Regla 6: Se utiliza @Log de Lombok para consistencia y limpieza.
@Log
@RequiredArgsConstructor
public final class DeleteUserService implements DeleteUserUseCase {

  // VIOLACIÓN Regla 6: se agrega un Logger manual en vez de usar @Log de Lombok,
  // y se loguea información técnica mezclada con una captura de excepción no recuperable.
  // Refactorización: Se elimina el Logger manual.

  private final DeleteUserPort deleteUserPort;
  private final GetUserByIdPort getUserByIdPort;
  private final Validator validator;

  @Override
  public void execute(final DeleteUserCommand command) {
    // VIOLACIÓN Regla 6: try-catch sin posibilidad real de recuperar el flujo.
    // Las excepciones no recuperables deben propagarse al manejador global, no capturarse aquí.
    
    // Refactorización: Eliminamos el try-catch innecesario. Dejamos que las excepciones 
    // (como UserNotFound o ConstraintViolation) fluyan hacia los adaptadores de entrada 
    // o un Global Exception Handler.
    
    validateCommand(command);
    
    final UserId userId = UserApplicationMapper.fromDeleteCommandToUserId(command);
    
    ensureUserExists(userId);
    
    deleteUserPort.delete(userId);
  }

  private void validateCommand(final DeleteUserCommand command) {
    final Set<ConstraintViolation<DeleteUserCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureUserExists(final UserId userId) {
    getUserByIdPort
        .getById(userId)
        .orElseThrow(() -> UserNotFoundException.becauseIdWasNotFound(userId.value()));
  }
}