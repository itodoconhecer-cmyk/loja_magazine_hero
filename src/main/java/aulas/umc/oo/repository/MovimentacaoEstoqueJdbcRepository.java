package aulas.umc.oo.repository;

import aulas.umc.oo.model.*; import org.springframework.jdbc.core.JdbcTemplate; import org.springframework.stereotype.Repository; import java.util.*;
@Repository
public class MovimentacaoEstoqueJdbcRepository implements MovimentacaoEstoqueRepository {
 private final JdbcTemplate jdbc; public MovimentacaoEstoqueJdbcRepository(JdbcTemplate jdbc){this.jdbc=jdbc;}
 public MovimentacaoEstoque insert(MovimentacaoEstoque m){jdbc.update("insert into movimentacao_estoque(id,produto_id,tipo,quantidade,motivo,data_hora) values(?,?,?,?,?,?)",m.id,m.produtoId,m.tipo.name(),m.quantidade,m.motivo,m.dataHora);return m;}
 public List<MovimentacaoEstoque> findAll(){return jdbc.query("select * from movimentacao_estoque order by data_hora desc",(rs,n)->{MovimentacaoEstoque m=new MovimentacaoEstoque();m.id=rs.getObject("id",UUID.class);m.produtoId=rs.getObject("produto_id",UUID.class);m.tipo=TipoMovimentacao.valueOf(rs.getString("tipo"));m.quantidade=rs.getInt("quantidade");m.motivo=rs.getString("motivo");m.dataHora=rs.getTimestamp("data_hora").toLocalDateTime();return m;});}
}
