package aulas.umc.oo.repository;

import aulas.umc.oo.model.*;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PedidoJdbcRepository implements PedidoRepository {
    private final JdbcTemplate jdbc;

    public PedidoJdbcRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Pedido insert(Pedido p) {
        jdbc.update(
                "insert into pedido(id,cliente_id,carrinho_id,status,tipo_frete,subtotal,valor_frete,total,ativo) values(?,?,?,?,?,?,?,?,?)",
                p.id,
                p.clienteId,
                p.carrinhoId,
                p.status.name(),
                p.tipoFrete.name(),
                p.subtotal,
                p.valorFrete,
                p.total,
                p.ativo);
        return p;
    }

    public List<Pedido> findAll() {
        return jdbc.query("select * from pedido where ativo=true order by id", (rs, n) -> map(rs));
    }

    public Optional<Pedido> findById(UUID id) {
        return jdbc.query("select * from pedido where id=? and ativo=true", (rs, n) -> map(rs), id).stream()
                .findFirst();
    }

    public Optional<Pedido> findByCarrinhoId(UUID carrinhoId) {
        return jdbc
                .query("select * from pedido where carrinho_id=? and ativo=true", (rs, n) -> map(rs), carrinhoId)
                .stream()
                .findFirst();
    }

    public Pedido update(Pedido p) {
        int n = jdbc.update(
                "update pedido set cliente_id=?,carrinho_id=?,status=?,tipo_frete=?,subtotal=?,valor_frete=?,total=?,ativo=? where id=?",
                p.clienteId,
                p.carrinhoId,
                p.status.name(),
                p.tipoFrete.name(),
                p.subtotal,
                p.valorFrete,
                p.total,
                p.ativo,
                p.id);
        if (n == 0) throw new RepositoryException("Pedido nao encontrado");
        return p;
    }

    public void deleteLogical(UUID id) {
        jdbc.update("update pedido set ativo=false,status='EXCLUIDO' where id=?", id);
    }

    public List<ItemPedido> findItems(UUID id) {
        return jdbc.query(
                "select * from item_pedido where pedido_id=? order by id",
                (rs, n) -> {
                    ItemPedido i = new ItemPedido();
                    i.id = rs.getObject("id", UUID.class);
                    i.pedidoId = rs.getObject("pedido_id", UUID.class);
                    i.produtoId = rs.getObject("produto_id", UUID.class);
                    i.quantidade = rs.getInt("quantidade");
                    i.precoUnitario = rs.getBigDecimal("preco_unitario");
                    return i;
                },
                id);
    }

    public void replaceItems(UUID id, List<ItemPedido> itens) {
        jdbc.update("delete from item_pedido where pedido_id=?", id);
        for (ItemPedido i : itens)
            jdbc.update(
                    "insert into item_pedido(id,pedido_id,produto_id,quantidade,preco_unitario) values(?,?,?,?,?)",
                    i.id,
                    id,
                    i.produtoId,
                    i.quantidade,
                    i.precoUnitario);
    }

    private Pedido map(java.sql.ResultSet rs) throws java.sql.SQLException {
        Pedido p = new Pedido();
        p.id = rs.getObject("id", UUID.class);
        p.clienteId = rs.getObject("cliente_id", UUID.class);
        p.carrinhoId = rs.getObject("carrinho_id", UUID.class);
        p.status = StatusPedido.valueOf(rs.getString("status"));
        p.tipoFrete = TipoFrete.valueOf(rs.getString("tipo_frete"));
        p.subtotal = rs.getBigDecimal("subtotal");
        p.valorFrete = rs.getBigDecimal("valor_frete");
        p.total = rs.getBigDecimal("total");
        p.ativo = rs.getBoolean("ativo");
        return p;
    }
}
