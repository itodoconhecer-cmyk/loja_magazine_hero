package aulas.umc.oo.repository;

import aulas.umc.oo.model.*; import org.springframework.jdbc.core.JdbcTemplate; import org.springframework.stereotype.Repository; import java.util.*;
@Repository
public class PagamentoJdbcRepository implements PagamentoRepository {
 private final JdbcTemplate jdbc; public PagamentoJdbcRepository(JdbcTemplate jdbc){this.jdbc=jdbc;}
 public Pagamento insert(Pagamento p){jdbc.update("insert into pagamento(id,pedido_id,valor,status,meio) values(?,?,?,?,?)",p.id,p.pedidoId,p.valor,p.status.name(),p.meio);return p;}
 public List<Pagamento> findAll(){return jdbc.query("select * from pagamento",(rs,n)->map(rs));}
 public Optional<Pagamento> findById(UUID id){return jdbc.query("select * from pagamento where id=?",(rs,n)->map(rs),id).stream().findFirst();}
 public Optional<Pagamento> findByPedidoId(UUID id){return jdbc.query("select * from pagamento where pedido_id=? order by id desc",(rs,n)->map(rs),id).stream().findFirst();}
 public Pagamento update(Pagamento p){int n=jdbc.update("update pagamento set pedido_id=?,valor=?,status=?,meio=? where id=?",p.pedidoId,p.valor,p.status.name(),p.meio,p.id);if(n==0)throw new RepositoryException("Pagamento nao encontrado");return p;}
 public void delete(UUID id){jdbc.update("delete from pagamento where id=?",id);}
 private Pagamento map(java.sql.ResultSet rs)throws java.sql.SQLException{Pagamento p=new Pagamento();p.id=rs.getObject("id",UUID.class);p.pedidoId=rs.getObject("pedido_id",UUID.class);p.valor=rs.getBigDecimal("valor");p.status=StatusPagamento.valueOf(rs.getString("status"));p.meio=rs.getString("meio");return p;}
}
