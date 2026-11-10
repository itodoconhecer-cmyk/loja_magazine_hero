package aulas.umc.oo.UseCase;

import aulas.umc.oo.DTO.*; import aulas.umc.oo.mapper.PagamentoMapper; import aulas.umc.oo.model.*; import aulas.umc.oo.repository.*; import org.springframework.stereotype.Service; import java.util.*;
@Service
public class PagamentoUseCase {
    private final PagamentoRepository repo; private final PedidoRepository pedidoRepo; private final PagamentoMapper mapper;
    public PagamentoUseCase(PagamentoRepository r,PedidoRepository p,PagamentoMapper m){repo=r;pedidoRepo=p;mapper=m;}
    public PagamentoResponse criar(PagamentoRequest r){
        Pedido pedido=pedidoRepo.findById(r.pedidoId()).orElseThrow(()->new IllegalArgumentException("Pedido nao encontrado"));
        if(pedido.status!=StatusPedido.AGUARDANDO_PAGAMENTO) throw new IllegalStateException("Pagamento so pode ser criado para pedido aguardando pagamento");
        if(r.valor().compareTo(pedido.total)!=0) throw new IllegalArgumentException("Valor do pagamento diferente do total do pedido");
        if(repo.findByPedidoId(r.pedidoId()).isPresent()) throw new IllegalStateException("Pedido ja possui pagamento");
        Pagamento p=new Pagamento();p.pedidoId=r.pedidoId();p.valor=r.valor();p.meio=r.meio();return mapper.toResponse(repo.insert(p));
    }
    public List<PagamentoResponse> listar(){return repo.findAll().stream().map(mapper::toResponse).toList();}
    public PagamentoResponse buscar(UUID id){return mapper.toResponse(repo.findById(id).orElseThrow(()->new IllegalArgumentException("Pagamento nao encontrado")));}
    public PagamentoResponse atualizar(UUID id,PagamentoRequest r){
        Pagamento p=repo.findById(id).orElseThrow(()->new IllegalArgumentException("Pagamento nao encontrado"));
        if(p.status!=StatusPagamento.PENDENTE) throw new IllegalStateException("Somente pagamento PENDENTE pode ser editado");
        if(!p.pedidoId.equals(r.pedidoId())) throw new IllegalArgumentException("Nao e permitido trocar o pedido de um pagamento");
        Pedido pedido=pedidoRepo.findById(p.pedidoId).orElseThrow(()->new IllegalArgumentException("Pedido nao encontrado"));
        if(r.valor().compareTo(pedido.total)!=0) throw new IllegalArgumentException("Valor do pagamento diferente do total do pedido");
        p.valor=r.valor();p.meio=r.meio();return mapper.toResponse(repo.update(p));
    }
    public void excluir(UUID id){
        Pagamento p=repo.findById(id).orElseThrow(()->new IllegalArgumentException("Pagamento nao encontrado"));
        if(p.status!=StatusPagamento.PENDENTE) throw new IllegalStateException("Somente pagamento PENDENTE pode ser excluido");
        repo.delete(id);
    }
}
