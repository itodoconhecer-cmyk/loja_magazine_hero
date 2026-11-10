package aulas.umc.oo.model;

import java.util.UUID;

public class Cliente extends Domain {
    public String nome; public String email; public boolean ativo = true;
    public Cliente() { this.id = UUID.randomUUID(); }
    public Cliente(String nome, String email) { this(); this.nome = nome; this.email = email; }
}
