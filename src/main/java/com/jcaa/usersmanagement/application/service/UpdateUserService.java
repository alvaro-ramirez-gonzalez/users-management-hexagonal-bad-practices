package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.UpdateUserUseCase;
import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.port.out.GetUserByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateUserPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateUserCommand;
import com.jcaa.usersmanagement.application.service.mapper.UserApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.UserAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import com.jcaa.usersmanagement.application.port.out.EmailNotificationPort;

import java.util.Optional;
import java.util.Set;

@Log
@RequiredArgsConstructor
public final class UpdateUserService implements UpdateUserUseCase {

  private final UpdateUserPort updateUserPort;
  private final GetUserByIdPort getUserByIdPort;
  private final GetUserByEmailPort getUserByEmailPort;
  private final EmailNotificationService emailNotificationService;
  private final Validator validator;

  public UpdateUserService(UpdateUserPort updateUserPort, GetUserByIdPort getUserByIdPort, 
      GetUserByEmailPort getUserByEmailPort, EmailNotificationService emailNotificationService, 
      Validator validator) {
    this.updateUserPort = updateUserPort;
    this.getUserByIdPort = getUserByIdPort;
    this.getUserByEmailPort = getUserByEmailPort;
    this.emailNotificationService = emailNotificationService;
    this.validator = validator;
  }

  @Override
  public UserModel execute(final UpdateUserCommand command) {
    // Clean Code - Regla 8 (separar comandos y consultas — CQS):
    // Este método MODIFICA estado (actualiza el usuario en base de datos)
    // Y TAMBIÉN RETORNA el usuario actualizado (consulta).
    // La regla dice: un método que modifica estado no debe presentarse como consulta.
    // Solución: void execute(command) para el comando + UserModel getUpdatedUser(id) para la consulta.
    
    // Mantengo el retorno para cumplir con la interfaz actual, 
    // pero marcamos la violación estructural de CQS.

    validateCommand(command);

    log.info("Actualizando usuario id=" + command.id() + ", email=" + command.email());

    final UserId userId = new UserId(command.id());
    final UserModel current = findExistingUserOrFail(userId);
    final UserEmail newEmail = new UserEmail(command.email());

    ensureEmailIsNotTakenByAnotherUser(newEmail, userId);

    final UserModel userToUpdate =
        UserApplicationMapper.fromUpdateCommandToModel(command, current.getPassword());
    
    final UserModel updatedUser = updateUserPort.update(userToUpdate);

    // Clean Code - Regla 6: parámetro booleano de control (boolean flag).
    // La regla dice: no usar boolean flags para cambiar el comportamiento interno de un método.
    
   // Aunque el execute llama a la versión true, 
    // separamos la lógica en métodos con nombres que expresen intención.
    updateAndNotify(updatedUser);

    return updatedUser;
  }

  // Eliminamos la bandera de control en favor de métodos explícitos.
  private void updateAndNotify(final UserModel user) {
      emailNotificationService.notifyUserUpdated(user);
  }

  private void updateSilently(final UserModel user) {
      log.info("Actualización silenciosa para usuario: " + user.getId().value());
  }

  // Clean Code - Regla 6: método con dos modos de operar según el boolean — viola la regla.
  // Clean Code - Regla 7: efecto secundario oculto — el nombre "notifyIfRequired" no indica
  // que también hace logging cuando notify=false. El nombre es engañoso sobre sus efectos.
  private void notifyIfRequired(final UserModel user, final boolean notify) {
    if (notify) {
      updateAndNotify(user);
    } else {
      updateSilently(user);
    }
  }

  private void validateCommand(final UpdateUserCommand command) {
    final Set<ConstraintViolation<UpdateUserCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private UserModel findExistingUserOrFail(final UserId userId) {
    return getUserByIdPort
        .getById(userId)
        .orElseThrow(() -> UserNotFoundException.becauseIdWasNotFound(userId.value()));
  }

  private void ensureEmailIsNotTakenByAnotherUser(final UserEmail newEmail, final UserId ownerId) {
    // Clean Code - Regla 17: condición booleana excesivamente larga y difícil de leer.
    // Clean Code - Regla 25 (preferir claridad sobre ingenio):
    // Clean Code - Regla 26 (evitar sobrecompactación):
    // Clean Code - Regla 27 (código listo para leer, no solo para ejecutar):

    // 1. Obtenemos el usuario por email UNA SOLA VEZ.
    // 2. Comparamos los IDs para ver si el email pertenece a OTRO usuario.
    
    Optional<UserModel> userWithSameEmail = getUserByEmailPort.getByEmail(newEmail);
    
    if (userWithSameEmail.isPresent()) {
        boolean isOwnedBySomeoneElse = !userWithSameEmail.get().getId().equals(ownerId);
        
        if (isOwnedBySomeoneElse) {
            throw UserAlreadyExistsException.becauseEmailAlreadyExists(newEmail.value());
        }
    }
  }
}