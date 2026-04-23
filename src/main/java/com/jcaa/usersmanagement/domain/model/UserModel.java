package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.AccessLevel;

/**
 * Entidad de Dominio Principal. 
 * Aplica Inmutabilidad (Regla 15) y Encapsulamiento de Lógica de Negocio (Regla 13).
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserModel {

    private final UserId id;
    private final UserName name;
    private final UserEmail email;
    private final UserPassword password;
    private final UserRole role;
    private final UserStatus status;

    /**
     * Fábrica estática para creación de nuevos usuarios.
     * Garantiza que todo usuario nuevo nazca en estado PENDING.
     */
    public static UserModel create(
            final UserId id,
            final UserName name,
            final UserEmail email,
            final UserPassword password,
            final UserRole role) {
        return new UserModel(id, name, email, password, role, UserStatus.PENDING);
    }

    /**
     * Reconstituye el objeto desde la capa de persistencia.
     * Permite restaurar el estado sin aplicar reglas de creación inicial.
     */
    public static UserModel reconstitute(
            final UserId id,
            final UserName name,
            final UserEmail email,
            final UserPassword password,
            final UserRole role,
            final UserStatus status) {
        return new UserModel(id, name, email, password, role, status);
    }

    // --- Métodos de Cambio de Estado (Pattern: Persistent Data Structures) ---

    public UserModel activate() {
        return new UserModel(id, name, email, password, role, UserStatus.ACTIVE);
    }

    public UserModel deactivate() {
        return new UserModel(id, name, email, password, role, UserStatus.INACTIVE);
    }

    // --- Lógica de Negocio (Domain Logic) ---

    public boolean isAllowedToLogin() {
        return this.status == UserStatus.ACTIVE;
    }

    public boolean hasAdministrativePrivileges() {
        return this.role == UserRole.ADMIN;
    }

    public boolean canPerformAction(int maxInactivityDays) {
        return (this.status == UserStatus.ACTIVE || this.status == UserStatus.PENDING) 
                && maxInactivityDays >= 0;
    }

    /**
     * Cumple Regla 14 (Tell, Don't Ask): No pedimos el password para compararlo fuera,
     * le preguntamos al modelo si el password coincide.
     */
    public boolean hasPassword(String plainPassword) {
        return this.password.verifyPlain(plainPassword);
    }
}