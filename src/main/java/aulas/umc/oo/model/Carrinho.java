package aulas.umc.oo.model;

import java.util.UUID;

public class Carrinho extends Domain {
    public UUID clienteId;
    public boolean ativo = true;

    public Carrinho() {
        this.id = UUID.randomUUID();
    }

    public Carrinho(UUID clienteId) {
        this();
        this.clienteId = clienteId;
    }
}
