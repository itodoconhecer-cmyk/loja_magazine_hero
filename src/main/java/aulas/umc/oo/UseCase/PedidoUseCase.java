package aulas.umc.oo.UseCase;

import aulas.umc.oo.DTO.PedidoRequest;
import aulas.umc.oo.DTO.PedidoResponse;
import aulas.umc.oo.exception.PedidoUseCaseException;
import aulas.umc.oo.mapper.PedidoMapper;
import aulas.umc.oo.model.Carrinho;
import aulas.umc.oo.model.Pedido;
import aulas.umc.oo.repository.CarrinhoRepository;
import aulas.umc.oo.repository.ClienteRepository;
import aulas.umc.oo.repository.PedidoRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PedidoUseCase {

    private final PedidoRepository repository;
    private final ClienteRepository clienteRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final PedidoMapper mapper;

    public PedidoUseCase(
            PedidoRepository repository,
            ClienteRepository clienteRepository,
            CarrinhoRepository carrinhoRepository,
            PedidoMapper mapper) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.mapper = mapper;
    }

    public PedidoResponse criar(PedidoRequest request) {
        clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> new PedidoUseCaseException("PED-001", "Cliente do pedido nao encontrado"));

        Carrinho carrinho = carrinhoRepository.findById(request.carrinhoId())
                .orElseThrow(() -> new PedidoUseCaseException("PED-002", "Carrinho do pedido nao encontrado"));

        if (!carrinho.clienteId.equals(request.clienteId())) {
            throw new PedidoUseCaseException("PED-003", "Carrinho pertence a outro cliente");
        }

        if (repository.findByCarrinhoId(request.carrinhoId()).isPresent()) {
            throw new PedidoUseCaseException("PED-004", "Carrinho ja possui pedido ativo");
        }

        Pedido pedido = new Pedido();
        pedido.clienteId = request.clienteId();
        pedido.carrinhoId = request.carrinhoId();
        pedido.tipoFrete = request.tipoFrete();
        return mapper.toResponse(repository.insert(pedido));
    }

    public List<PedidoResponse> listar() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    public PedidoResponse buscar(UUID id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new PedidoUseCaseException("PED-404", "Pedido nao encontrado")));
    }

    public void excluirCrud(UUID id) {
        throw new PedidoUseCaseException(
                "PED-005", "Exclusao de pedido deve usar o processo EXCLUIR_PEDIDO");
    }
}
