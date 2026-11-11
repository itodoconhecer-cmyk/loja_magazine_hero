package aulas.umc.oo.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ClienteResponse(
        @NotNull UUID id,
        @NotBlank String nome,
        @NotBlank @Email String email,
        boolean ativo) {}
