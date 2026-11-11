package aulas.umc.oo.repository;

import aulas.umc.oo.model.Produto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProdutoJdbcRepository implements ProdutoRepository {

    private final JdbcTemplate jdbc;

    public ProdutoJdbcRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Produto insert(Produto produto) {
        jdbc.update(
                "insert into produto(id, nome, preco, ativo) values(?, ?, ?, ?)",
                produto.id,
                produto.nome,
                produto.preco,
                produto.ativo);
        return produto;
    }

    @Override
    public List<Produto> findAll() {
        return jdbc.query(
                "select id, nome, preco, ativo from produto where ativo = true order by nome", (rs, rowNum) -> map(rs));
    }

    @Override
    public Optional<Produto> findById(UUID id) {
        return jdbc
                .query(
                        "select id, nome, preco, ativo from produto where id = ? and ativo = true",
                        (rs, rowNum) -> map(rs),
                        id)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Produto> findByIdIncludingInactive(UUID id) {
        return jdbc
                .query("select id, nome, preco, ativo from produto where id = ?", (rs, rowNum) -> map(rs), id)
                .stream()
                .findFirst();
    }

    @Override
    public Produto update(Produto produto) {
        int linhas = jdbc.update(
                "update produto set nome = ?, preco = ? where id = ? and ativo = true",
                produto.nome,
                produto.preco,
                produto.id);

        if (linhas == 0) {
            throw new RepositoryException("Produto nao encontrado");
        }

        return produto;
    }

    @Override
    public void deleteLogical(UUID id) {
        jdbc.update("update produto set ativo = false where id = ?", id);
    }

    private Produto map(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.id = rs.getObject("id", UUID.class);
        produto.nome = rs.getString("nome");
        produto.preco = rs.getBigDecimal("preco");
        produto.ativo = rs.getBoolean("ativo");
        return produto;
    }
}
