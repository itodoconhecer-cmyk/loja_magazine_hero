package aulas.umc.oo.UseCase;

import aulas.umc.oo.DTO.*;
import aulas.umc.oo.model.*;
import aulas.umc.oo.repository.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CarrinhoUseCase {
    private final CarrinhoRepository repo;
    private final ClienteRepository clienteRepo;
    private final ProdutoRepository produtoRepo;
    private final PedidoRepository pedidoRepo;

    public CarrinhoUseCase(CarrinhoRepository r, ClienteRepository c, ProdutoRepository p, PedidoRepository pr) {
        repo = r; clienteRepo = c; produtoRepo = p; pedidoRepo = pr;
    }

    public CarrinhoResponse criar(CarrinhoRequest r) {
        clienteRepo.findById(r.clienteId()).orElseThrow(() -> new IllegalArgumentException("Cliente nao encontrado"));
        return resp(repo.insert(new Carrinho(r.clienteId())));
    }

    public List<CarrinhoResponse> listar() { return repo.findAll().stream().map(this::resp).toList(); }
    public CarrinhoResponse buscar(UUID id) { return resp(repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Carrinho nao encontrado"))); }

    public CarrinhoResponse atualizar(UUID id, CarrinhoRequest r) {
        validarCarrinhoNaoConvertidoEmPedido(id);
        clienteRepo.findById(r.clienteId()).orElseThrow(() -> new IllegalArgumentException("Cliente nao encontrado"));
        Carrinho c = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Carrinho nao encontrado"));
        c.clienteId = r.clienteId();
        return resp(repo.update(c));
    }

    public void excluir(UUID id) {
        validarCarrinhoNaoConvertidoEmPedido(id);
        repo.deleteLogical(id);
    }

    public CarrinhoResponse adicionarItem(UUID id, ItemCarrinhoRequest r) {
        validarCarrinhoNaoConvertidoEmPedido(id);
        repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Carrinho nao encontrado"));
        produtoRepo.findById(r.produtoId()).orElseThrow(() -> new IllegalArgumentException("Produto nao encontrado"));
        repo.addItem(new ItemCarrinho(id, r.produtoId(), r.quantidade()));
        return buscar(id);
    }

    private void validarCarrinhoNaoConvertidoEmPedido(UUID carrinhoId) {
        if (pedidoRepo.findByCarrinhoId(carrinhoId).isPresent()) {
            throw new IllegalStateException("Carrinho ja foi convertido em pedido e nao pode mais ser alterado");
        }
    }

    private CarrinhoResponse resp(Carrinho c) {
        var itens = repo.findItems(c.id).stream().map(i -> new ItemCarrinhoResponse(i.id, i.produtoId, i.quantidade)).toList();
        return new CarrinhoResponse(c.id, c.clienteId, c.ativo, itens);
    }
}
