package aulas.umc.oo.UseCase;

import aulas.umc.oo.DTO.CarrinhoRequest;
import aulas.umc.oo.DTO.CarrinhoResponse;
import aulas.umc.oo.DTO.ItemCarrinhoRequest;
import aulas.umc.oo.DTO.ItemCarrinhoResponse;
import aulas.umc.oo.exception.CarrinhoUseCaseException;
import aulas.umc.oo.exception.ErrorCategory;
import aulas.umc.oo.model.Carrinho;
import aulas.umc.oo.model.ItemCarrinho;
import aulas.umc.oo.repository.CarrinhoRepository;
import aulas.umc.oo.repository.ClienteRepository;
import aulas.umc.oo.repository.PedidoRepository;
import aulas.umc.oo.repository.ProdutoRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CarrinhoUseCase {

    private final CarrinhoRepository repository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;

    public CarrinhoUseCase(
            CarrinhoRepository repository,
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository,
            PedidoRepository pedidoRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public CarrinhoResponse criar(CarrinhoRequest request) {
        clienteRepository
                .findById(request.clienteId())
                .orElseThrow(() -> new CarrinhoUseCaseException(
                        ErrorCategory.NOT_FOUND, "CAR-001", "Cliente do carrinho nao encontrado"));

        return toResponse(repository.insert(new Carrinho(request.clienteId())));
    }

    public List<CarrinhoResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public CarrinhoResponse buscar(UUID id) {
        return toResponse(repository
                .findById(id)
                .orElseThrow(() ->
                        new CarrinhoUseCaseException(ErrorCategory.NOT_FOUND, "CAR-404", "Carrinho nao encontrado")));
    }

    public CarrinhoResponse atualizar(UUID id, CarrinhoRequest request) {
        Carrinho carrinho = repository
                .findById(id)
                .orElseThrow(() ->
                        new CarrinhoUseCaseException(ErrorCategory.NOT_FOUND, "CAR-404", "Carrinho nao encontrado"));

        validarCarrinhoNaoConvertidoEmPedido(id);

        clienteRepository
                .findById(request.clienteId())
                .orElseThrow(() -> new CarrinhoUseCaseException(
                        ErrorCategory.NOT_FOUND, "CAR-001", "Cliente do carrinho nao encontrado"));

        carrinho.clienteId = request.clienteId();
        return toResponse(repository.update(carrinho));
    }

    public void excluir(UUID id) {
        repository
                .findById(id)
                .orElseThrow(() ->
                        new CarrinhoUseCaseException(ErrorCategory.NOT_FOUND, "CAR-404", "Carrinho nao encontrado"));

        validarCarrinhoNaoConvertidoEmPedido(id);
        repository.deleteLogical(id);
    }

    public CarrinhoResponse adicionarItem(UUID id, ItemCarrinhoRequest request) {
        repository
                .findById(id)
                .orElseThrow(() ->
                        new CarrinhoUseCaseException(ErrorCategory.NOT_FOUND, "CAR-404", "Carrinho nao encontrado"));

        validarCarrinhoNaoConvertidoEmPedido(id);

        produtoRepository
                .findById(request.produtoId())
                .orElseThrow(() -> new CarrinhoUseCaseException(
                        ErrorCategory.NOT_FOUND, "CAR-002", "Produto do item nao encontrado"));

        repository.addItem(new ItemCarrinho(id, request.produtoId(), request.quantidade()));

        return buscar(id);
    }

    private void validarCarrinhoNaoConvertidoEmPedido(UUID carrinhoId) {
        if (pedidoRepository.findByCarrinhoId(carrinhoId).isPresent()) {
            throw new CarrinhoUseCaseException(
                    ErrorCategory.CONFLICT,
                    "CAR-003",
                    "Carrinho ja foi convertido em pedido e nao pode mais ser alterado");
        }
    }

    private CarrinhoResponse toResponse(Carrinho carrinho) {
        var itens = repository.findItems(carrinho.id).stream()
                .map(item -> new ItemCarrinhoResponse(item.id, item.produtoId, item.quantidade))
                .toList();

        return new CarrinhoResponse(carrinho.id, carrinho.clienteId, carrinho.ativo, itens);
    }
}
