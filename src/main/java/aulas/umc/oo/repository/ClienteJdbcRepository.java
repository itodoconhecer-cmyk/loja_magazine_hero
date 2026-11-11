package aulas.umc.oo.repository;

import aulas.umc.oo.model.Cliente;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteJdbcRepository implements ClienteRepository {
    private final JdbcTemplate jdbc;

    public ClienteJdbcRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Cliente insert(Cliente c) {
        jdbc.update("insert into cliente(id,nome,email,ativo) values(?,?,?,?)", c.id, c.nome, c.email, c.ativo);
        return c;
    }

    public List<Cliente> findAll() {
        return jdbc.query("select * from cliente where ativo=true order by nome", (rs, n) -> map(rs));
    }

    public Optional<Cliente> findById(UUID id) {
        return jdbc.query("select * from cliente where id=? and ativo=true", (rs, n) -> map(rs), id).stream()
                .findFirst();
    }

    public Cliente update(Cliente c) {
        int n = jdbc.update("update cliente set nome=?,email=? where id=? and ativo=true", c.nome, c.email, c.id);
        if (n == 0) throw new RepositoryException("Cliente nao encontrado");
        return c;
    }

    public void deleteLogical(UUID id) {
        jdbc.update("update cliente set ativo=false where id=?", id);
    }

    private Cliente map(java.sql.ResultSet rs) throws java.sql.SQLException {
        Cliente c = new Cliente();
        c.id = rs.getObject("id", UUID.class);
        c.nome = rs.getString("nome");
        c.email = rs.getString("email");
        c.ativo = rs.getBoolean("ativo");
        return c;
    }
}
