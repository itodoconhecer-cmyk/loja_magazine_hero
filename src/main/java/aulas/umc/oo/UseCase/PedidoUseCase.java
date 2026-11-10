package aulas.umc.oo.UseCase;

import aulas.umc.oo.DTO.*;
import aulas.umc.oo.mapper.PedidoMapper;
import aulas.umc.oo.model.*;
import aulas.umc.oo.repository.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PedidoUseCase {
    private final PedidoRepository repo; private final ClienteRepository clienteRepo; private final CarrinhoRepository carrinhoRepo; private final PedidoMapper mapper;
    public PedidoUseCase(PedidoRepository r,ClienteRepository c,CarrinhoRepository ca,PedidoMapper m){repo=r;clienteRepo=c;carrinhoRepo=ca;mapper=m;}
    public PedidoResponse criar(PedidoRequest r){
        clienteRepo.findById(r.clienteId()).orElseThrow(()->new IllegalArgumentException("Cliente nao encontrado"));
        Carrinho carrinho=carrinhoRepo.findById(r.carrinhoId()).orElseThrow(()->new IllegalArgumentException("Carrinho nao encontrado"));
        if(!carrinho.clienteId.equals(r.clienteId())) throw new IllegalArgumentException("Carrinho pertence a outro cliente");
        if(repo.findByCarrinhoId(r.carrinhoId()).isPresent()) throw new IllegalStateException("Carrinho ja possui pedido ativo");
        Pedido p=new Pedido();p.clienteId=r.clienteId();p.carrinhoId=r.carrinhoId();p.tipoFrete=r.tipoFrete();return mapper.toResponse(repo.insert(p));
    }
    public List<PedidoResponse> listar(){return repo.findAll().stream().map(mapper::toResponse).toList();}
    public PedidoResponse buscar(UUID id){return mapper.toResponse(repo.findById(id).orElseThrow(()->new IllegalArgumentException("Pedido nao encontrado")));}
    public void excluirCrud(UUID id){throw new IllegalStateException("Exclusao de pedido deve usar o processo EXCLUIR_PEDIDO");}
}
