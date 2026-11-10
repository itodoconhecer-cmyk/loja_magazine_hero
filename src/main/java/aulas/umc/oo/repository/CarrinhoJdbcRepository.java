package aulas.umc.oo.repository;

import aulas.umc.oo.model.*; import org.springframework.jdbc.core.JdbcTemplate; import org.springframework.stereotype.Repository; import java.util.*;
@Repository
public class CarrinhoJdbcRepository implements CarrinhoRepository {
 private final JdbcTemplate jdbc; public CarrinhoJdbcRepository(JdbcTemplate jdbc){this.jdbc=jdbc;}
 public Carrinho insert(Carrinho c){jdbc.update("insert into carrinho(id,cliente_id,ativo) values(?,?,?)",c.id,c.clienteId,c.ativo);return c;}
 public List<Carrinho> findAll(){return jdbc.query("select * from carrinho where ativo=true",(rs,n)->map(rs));}
 public Optional<Carrinho> findById(UUID id){return jdbc.query("select * from carrinho where id=? and ativo=true",(rs,n)->map(rs),id).stream().findFirst();}
 public Carrinho update(Carrinho c){int n=jdbc.update("update carrinho set cliente_id=? where id=? and ativo=true",c.clienteId,c.id);if(n==0)throw new RepositoryException("Carrinho nao encontrado");return c;}
 public void deleteLogical(UUID id){jdbc.update("update carrinho set ativo=false where id=?",id);}
 public ItemCarrinho addItem(ItemCarrinho i){jdbc.update("insert into item_carrinho(id,carrinho_id,produto_id,quantidade) values(?,?,?,?)",i.id,i.carrinhoId,i.produtoId,i.quantidade);return i;}
 public List<ItemCarrinho> findItems(UUID id){return jdbc.query("select * from item_carrinho where carrinho_id=?",(rs,n)->{ItemCarrinho i=new ItemCarrinho();i.id=rs.getObject("id",UUID.class);i.carrinhoId=rs.getObject("carrinho_id",UUID.class);i.produtoId=rs.getObject("produto_id",UUID.class);i.quantidade=rs.getInt("quantidade");return i;},id);}
 public void clearItems(UUID id){jdbc.update("delete from item_carrinho where carrinho_id=?",id);}
 private Carrinho map(java.sql.ResultSet rs)throws java.sql.SQLException{Carrinho c=new Carrinho();c.id=rs.getObject("id",UUID.class);c.clienteId=rs.getObject("cliente_id",UUID.class);c.ativo=rs.getBoolean("ativo");return c;}
}
