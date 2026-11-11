package aulas.umc.oo.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank(message = "nome e obrigatorio")
        @Size(min = 2, max = 120, message = "nome deve ter entre 2 e 120 caracteres")
        String nome,

        @NotBlank(message = "email e obrigatorio")
        @Email(message = "email deve ser valido")
        @Size(max = 180, message = "email deve ter no maximo 180 caracteres")
        String email) {}
