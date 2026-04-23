package com.jcaa.usersmanagement.application.service.dto.query;

import jakarta.validation.constraints.NotBlank;
// VIOLACIÓN Regla 3: se mezcla @Builder de Lombok con un record.
// Los records ya tienen constructor canónico; usar @Builder es redundante e innecesario en este caso.
// Refactorización: Se elimina el import de lombok.Builder y la anotación @Builder.

public record GetUserByIdQuery(
    // VIOLACIÓN Regla 3: se usa mensaje personalizado en la constraint.
    // La regla indica dejar los mensajes por default — no usar atributo message=.
    // Refactorización: Se elimina el atributo message para usar el estándar.
    @NotBlank String id
) {
    
}