package aulas.umc.oo.mapper;

import aulas.umc.oo.DTO.*;
import aulas.umc.oo.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {
    public Cliente toDomain(ClienteRequest r) {
        return new Cliente(r.nome(), r.email());
    }

    public ClienteResponse toResponse(Cliente c) {
        return new ClienteResponse(c.id, c.nome, c.email, c.ativo);
    }
}
