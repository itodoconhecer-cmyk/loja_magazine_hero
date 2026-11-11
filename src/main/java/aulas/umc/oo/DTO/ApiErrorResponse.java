package aulas.umc.oo.DTO;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String codigo,
        String mensagem,
        String path,
        String correlationId,
        List<FieldValidationError> campos) {}
