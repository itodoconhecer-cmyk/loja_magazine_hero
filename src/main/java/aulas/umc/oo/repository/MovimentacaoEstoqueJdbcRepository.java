package aulas.umc.oo.repository;

import aulas.umc.oo.model.MovimentacaoEstoque;
import aulas.umc.oo.model.TipoMovimentacao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class MovimentacaoEstoqueJdbcRepository implements MovimentacaoEstoqueRepository {

    private final JdbcTemplate jdbc;

    public MovimentacaoEstoqueJdbcRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public MovimentacaoEstoque insert(MovimentacaoEstoque movimentacao) {
        jdbc.update(
                """
                insert into movimentacao_estoque
                    (id, produto_id, tipo, quantidade, motivo, data_hora)
                values (?, ?, ?, ?, ?, ?)
                """,
                movimentacao.id,
                movimentacao.produtoId,
                movimentacao.tipo.name(),
                movimentacao.quantidade,
                movimentacao.motivo,
                movimentacao.dataHora
        );
        return movimentacao;
    }

    @Override
    public List<MovimentacaoEstoque> findAll() {
        return jdbc.query(
                """
                select id, produto_id, tipo, quantidade, motivo, data_hora
                  from movimentacao_estoque
                 order by data_hora desc, id desc
                """,
                (rs, rowNum) -> map(rs)
        );
    }

    @Override
    public List<MovimentacaoEstoque> findByProdutoId(UUID produtoId) {
        return jdbc.query(
                """
                select id, produto_id, tipo, quantidade, motivo, data_hora
                  from movimentacao_estoque
                 where produto_id = ?
                 order by data_hora desc, id desc
                """,
                (rs, rowNum) -> map(rs),
                produtoId
        );
    }

    @Override
    public long calcularSaldo(UUID produtoId) {
        Long saldo = jdbc.queryForObject(
                """
                select coalesce(sum(
                    case
                        when tipo = 'ENTRADA' then quantidade
                        when tipo = 'SAIDA' then -quantidade
                        else 0
                    end
                ), 0)
                  from movimentacao_estoque
                 where produto_id = ?
                """,
                Long.class,
                produtoId
        );

        return saldo == null ? 0L : saldo;
    }

    @Override
    public void bloquearProduto(UUID produtoId) {
        List<UUID> ids = jdbc.query(
                "select id from produto where id = ? for update",
                (rs, rowNum) -> rs.getObject("id", UUID.class),
                produtoId
        );

        if (ids.isEmpty()) {
            throw new RepositoryException("Produto nao encontrado para movimentacao de estoque");
        }
    }

    private MovimentacaoEstoque map(ResultSet rs) throws SQLException {
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.id = rs.getObject("id", UUID.class);
        movimentacao.produtoId = rs.getObject("produto_id", UUID.class);
        movimentacao.tipo = TipoMovimentacao.valueOf(rs.getString("tipo"));
        movimentacao.quantidade = rs.getInt("quantidade");
        movimentacao.motivo = rs.getString("motivo");
        movimentacao.dataHora = rs.getTimestamp("data_hora").toLocalDateTime();
        return movimentacao;
    }
}
