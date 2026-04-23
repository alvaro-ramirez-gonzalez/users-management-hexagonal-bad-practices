package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;

// VIOLACIÓN Regla 9 (Hexagonal): el dominio importa una clase de infraestructura.
// Las dependencias siempre deben ir hacia el centro — nunca desde el dominio hacia afuera.
// Se ha eliminado la importación de UserEntity y el método toEntity().
// La conversión debe ocurrir en un Mapper en la capa de Infraestructura.

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.AccessLevel;

// Clean Code - Regla 15 (inmutabilidad como preferencia de diseño):
// Se cambió @Value por @Data + @AllArgsConstructor, lo que expone setters públicos
// para todos los campos. Un modelo de dominio debe ser inmutable...

// Refactorización Alejandro: Usamos @Getter y campos FINAL para garantizar inmutabilidad.
// Cambiamos el acceso del constructor a PRIVATE para obligar el uso de métodos de fábrica estáticos.
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserModel {

    private final UserId id;
    private final UserName name;
    private final UserEmail email;
    private final UserPassword password;
    private final UserRole role;
    private final UserStatus status;

    /**
     * Crea un nuevo usuario con estado inicial PENDING.
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
     * Reconstruye el objeto desde persistencia sin alterar sus reglas de creación.
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

    public UserModel activate() {
        return new UserModel(id, name, email, password, role, UserStatus.ACTIVE);
    }

    public UserModel deactivate() {
        return new UserModel(id, name, email, password, role, UserStatus.INACTIVE);
    }

    // VIOLACIÓN Regla 9 (Hexagonal): método de conversión a entidad de infraestructura dentro del dominio.
    // El dominio NO debe saber nada sobre cómo se persisten sus datos.
    // (Se eliminó el método toEntity() para cumplir con la regla).

    /**
     * Corrección Regla 13 y 12: Cohesión alta.
     * La lógica de si un usuario puede entrar al sistema vive aquí.
     */
    public boolean isAllowedToLogin() {
        return this.status == UserStatus.ACTIVE;
    }

    /**
     * Corrección Regla 13: Encapsulación de lógica de negocio.
     */
    public boolean hasAdministrativePrivileges() {
        return this.role == UserRole.ADMIN;
    }

    /**
     * Corrección Regla 17, 18 y 20: Sustituye a canPerformAction de la clase Utils.
     * Ya no recibimos Strings sueltos, usamos el estado interno validado del dominio.
     */
    public boolean canPerformAction(int maxInactivityDays) {
        return (this.status == UserStatus.ACTIVE || this.status == UserStatus.PENDING) 
                && maxInactivityDays >= 0;
    }

    /**
     * Refactorización para LoginService:
     * Aplica "Tell, Don't Ask" (Regla 14) y encapsula la verificación del password.
     */
    public boolean hasPassword(String plainPassword) {
        return this.password.verifyPlain(plainPassword);
    }
}