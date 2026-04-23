package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetUserByIdUseCase;
import com.jcaa.usersmanagement.application.port.out.GetUserByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetUserByIdQuery;
import com.jcaa.usersmanagement.application.service.mapper.UserApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public final class GetUserByIdService implements GetUserByIdUseCase {

  private final GetUserByIdPort getUserByIdPort;
  private final Validator validator;

  // VIOLACIÓN Regla 3: @Valid declarado en la implementación (@Override).
  // Las constraints (@Valid, @NotNull, etc.) solo deben declararse en las interfaces (puertos),
  // nunca en las clases concretas que las implementan.
  
  // La validación programática se mantiene en validateQuery para asegurar la integridad,
  // pero la anotación de Bean Validation debe vivir en el UseCase (Puerto de entrada).
  @Override
  public UserModel execute(final GetUserByIdQuery query) {
    // Regla 2: "Fail Fast". Validamos antes de cualquier otra operación.
    validateQuery(query);

    // Transformación: El servicio delega el mapeo para no ensuciarse con detalles de DTOs.
    final UserId userId = UserApplicationMapper.fromGetUserByIdQueryToUserId(query);
    
    // Regla 14: Tell, Don't Ask. 
    // Buscamos el usuario y, si no existe, lanzamos la excepción de dominio inmediatamente.
    return getUserByIdPort
        .getById(userId)
        .orElseThrow(() -> UserNotFoundException.becauseIdWasNotFound(userId.value()));
  }

  /**
   * Refactorización Alejandro: Aunque lo ideal es que el puerto de entrada (Interface) 
   * dispare la validación, mantenemos este método privado para asegurar que ningún 
   * Query inválido procese lógica de negocio, cumpliendo con la Regla de Robustez.
   */
  private void validateQuery(final GetUserByIdQuery query) {
    final Set<ConstraintViolation<GetUserByIdQuery>> violations = validator.validate(query);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}