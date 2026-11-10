package aulas.umc.oo.repository;

import aulas.umc.oo.model.Produto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class ProdutoJdbcRepository implements ProdutoRepository {
    private final JdbcTemplate jdbc;
    public ProdutoJdbcRepository(JdbcTemplate jdbc){this.jdbc=jdbc;}

    public Produto insert(Produto p){
        jdbc.update("insert into produto(id,nome,preco,estoque,ativo) values(?,?,?,?,?)",p.id,p.nome,p.preco,p.estoque,p.ativo);
        return p;
    }
    public List<Produto> findAll(){return jdbc.query("select * from produto where ativo=true order by nome",(rs,n)->map(rs));}
    public Optional<Produto> findById(UUID id){return jdbc.query("select * from produto where id=? and ativo=true",(rs,n)->map(rs),id).stream().findFirst();}
    public Optional<Produto> findByIdIncludingInactive(UUID id){return jdbc.query("select * from produto where id=?",(rs,n)->map(rs),id).stream().findFirst();}

    // Estoque nao e alterado pelo CRUD. Movimentacoes usam metodos especificos abaixo.
    public Produto update(Produto p){
        int n=jdbc.update("update produto set nome=?,preco=? where id=? and ativo=true",p.nome,p.preco,p.id);
        if(n==0)throw new RepositoryException("Produto nao encontrado");
        return p;
    }
    public void deleteLogical(UUID id){jdbc.update("update produto set ativo=false where id=?",id);}
    public void atualizarEstoque(UUID id,int novo){
        if(novo < 0) throw new IllegalArgumentException("Estoque nao pode ser negativo");
        int n=jdbc.update("update produto set estoque=? where id=? and ativo=true",novo,id);
        if(n==0)throw new RepositoryException("Produto nao encontrado");
    }
    public void adicionarEstoque(UUID id,int quantidade,boolean exigirAtivo){
        if(quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser positiva");
        String sql=exigirAtivo
            ? "update produto set estoque=estoque+? where id=? and ativo=true"
            : "update produto set estoque=estoque+? where id=?";
        int n=jdbc.update(sql,quantidade,id);
        if(n==0)throw new RepositoryException("Produto nao encontrado para entrada/devolucao");
    }
    public void retirarEstoqueSeDisponivel(UUID id,int quantidade){
        if(quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser positiva");
        int n=jdbc.update("update produto set estoque=estoque-? where id=? and ativo=true and estoque>=?",quantidade,id,quantidade);
        if(n==0)throw new IllegalStateException("Estoque insuficiente ou produto inativo");
    }
    private Produto map(java.sql.ResultSet rs)throws java.sql.SQLException{
        Produto p=new Produto();p.id=rs.getObject("id",UUID.class);p.nome=rs.getString("nome");p.preco=rs.getBigDecimal("preco");p.estoque=rs.getInt("estoque");p.ativo=rs.getBoolean("ativo");return p;
    }
}
