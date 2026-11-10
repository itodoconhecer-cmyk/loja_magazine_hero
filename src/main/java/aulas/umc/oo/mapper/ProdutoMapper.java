package aulas.umc.oo.mapper; import aulas.umc.oo.DTO.*; import aulas.umc.oo.model.Produto; import org.springframework.stereotype.Component;
@Component public class ProdutoMapper { public Produto toDomain(ProdutoRequest r){return new Produto(r.nome(),r.preco(),r.estoque());} public ProdutoResponse toResponse(Produto p){return new ProdutoResponse(p.id,p.nome,p.preco,p.estoque,p.ativo);} }
