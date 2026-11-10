package aulas.umc.oo.DTO;

import java.util.UUID;
public record ClienteResponse(UUID id, String nome, String email, boolean ativo) {}
