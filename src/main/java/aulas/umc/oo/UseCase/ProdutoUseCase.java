package aulas.umc.oo.UseCase;

import aulas.umc.oo.DTO.*; import aulas.umc.oo.mapper.ProdutoMapper; import aulas.umc.oo.model.*; import aulas.umc.oo.repository.*; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.*;
@Service
public class ProdutoUseCase {
    private final ProdutoRepository repo; private final ProdutoMapper mapper; private final MovimentacaoEstoqueRepository movRepo;
    public ProdutoUseCase(ProdutoRepository r,ProdutoMapper m,MovimentacaoEstoqueRepository mr){repo=r;mapper=m;movRepo=mr;}
    @Transactional public ProdutoResponse criar(ProdutoRequest r){
        Produto p=mapper.toDomain(r); int estoqueInicial=p.estoque; p.estoque=0; repo.insert(p);
        if(estoqueInicial>0){repo.adicionarEstoque(p.id,estoqueInicial,true);MovimentacaoEstoque mov=new MovimentacaoEstoque();mov.produtoId=p.id;mov.tipo=TipoMovimentacao.ENTRADA;mov.quantidade=estoqueInicial;mov.motivo="ESTOQUE_INICIAL";movRepo.insert(mov);p.estoque=estoqueInicial;}
        return mapper.toResponse(p);
    }
    public List<ProdutoResponse> listar(){return repo.findAll().stream().map(mapper::toResponse).toList();}
    public ProdutoResponse buscar(UUID id){return mapper.toResponse(repo.findById(id).orElseThrow(()->new IllegalArgumentException("Produto nao encontrado")));}
    public ProdutoResponse atualizar(UUID id,ProdutoRequest r){
        Produto existente=repo.findById(id).orElseThrow(()->new IllegalArgumentException("Produto nao encontrado"));
        existente.nome=r.nome();existente.preco=r.preco();repo.update(existente);return mapper.toResponse(existente);
    }
    public void excluir(UUID id){repo.deleteLogical(id);}
}
