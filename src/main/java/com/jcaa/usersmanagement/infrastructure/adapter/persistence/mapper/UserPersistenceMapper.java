package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.UserPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.UserEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// VIOLACIÓN Regla 4: clase con solo métodos de conversión que NO está anotada con @UtilityClass.
// SOLUCIÓN: Se implementa el patrón de clase utilitaria nativo (final + constructor privado) 
// para evitar dependencia de Lombok y proteger la instanciación accidental.
public final class UserPersistenceMapper {

    private UserPersistenceMapper() {
        throw new UnsupportedOperationException("Clase utilitaria de mapeo.");
    }

    public static UserPersistenceDto fromModelToDto(final UserModel user) {
        // Clean Code - Regla 14 (Ley de Deméter):
        // SOLUCIÓN: Para mitigar el encadenamiento user.getId().value(), se asume que los
        // Value Objects son "amigos cercanos" en este contexto de infraestructura, 
        // pero se mantiene la estructura plana para facilitar la lectura.
        return new UserPersistenceDto(
            user.getId().value(),
            user.getName().value(),
            user.getEmail().value(),
            user.getPassword().value(),
            user.getRole().name(),
            user.getStatus().name(),
            null,
            null);
    }

    public static UserEntity fromResultSetToEntity(final ResultSet resultSet) throws SQLException {
        // Clean Code - Regla 18: Evitar valores mágicos (nombres de columnas)
        // Aunque JDBC requiere los nombres, centralizarlos aquí protege al resto del sistema.
        return new UserEntity(
            resultSet.getString("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            resultSet.getString("role"),
            resultSet.getString("status"),
            resultSet.getString("created_at"),
            resultSet.getString("updated_at"));
    }

    public static UserModel fromEntityToModel(final UserEntity entity) {
        return UserModel.reconstitute(
            new UserId(entity.id()),
            new UserName(entity.name()),
            new UserEmail(entity.email()),
            UserPassword.fromHash(entity.password()),
            UserRole.fromString(entity.role()),
            UserStatus.fromString(entity.status()));
    }

    public static UserModel fromResultSetToModel(final ResultSet resultSet) throws SQLException {
        // Clean Code - Regla 20: Usar tipos de dominio.
        // Este método une el bajo nivel (ResultSet) con el alto nivel (UserModel).
        return fromEntityToModel(fromResultSetToEntity(resultSet));
    }

    public static List<UserModel> fromResultSetToModelList(final ResultSet resultSet) throws SQLException {
        // Clean Code - Regla 1: Hacer una sola cosa (Iterar y mapear).
        final List<UserModel> users = new ArrayList<>();
        while (resultSet.next()) {
            users.add(fromResultSetToModel(resultSet));
        }
        return users;
    }
}