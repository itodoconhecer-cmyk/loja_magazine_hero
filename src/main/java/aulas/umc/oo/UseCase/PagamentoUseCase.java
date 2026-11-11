package aulas.umc.oo.UseCase;

import aulas.umc.oo.DTO.PagamentoRequest;
import aulas.umc.oo.DTO.PagamentoResponse;
import aulas.umc.oo.exception.ErrorCategory;
import aulas.umc.oo.exception.PagamentoUseCaseException;
import aulas.umc.oo.mapper.PagamentoMapper;
import aulas.umc.oo.model.Pagamento;
import aulas.umc.oo.model.Pedido;
import aulas.umc.oo.model.StatusPagamento;
import aulas.umc.oo.model.StatusPedido;
import aulas.umc.oo.repository.PagamentoRepository;
import aulas.umc.oo.repository.PedidoRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PagamentoUseCase {

    private final PagamentoRepository repository;
    private final PedidoRepository pedidoRepository;
    private final PagamentoMapper mapper;

    public PagamentoUseCase(
            PagamentoRepository repository,
            PedidoRepository pedidoRepository,
            PagamentoMapper mapper) {
        this.repository = repository;
        this.pedidoRepository = pedidoRepository;
        this.mapper = mapper;
    }

    public PagamentoResponse criar(PagamentoRequest request) {
        Pedido pedido = pedidoRepository.findById(request.pedidoId())
                .orElseThrow(() -> new PagamentoUseCaseException(
                        ErrorCategory.NOT_FOUND,
                        "PAG-001",
                        "Pedido nao encontrado"));

        if (pedido.status != StatusPedido.AGUARDANDO_PAGAMENTO) {
            throw new PagamentoUseCaseException(
                    ErrorCategory.BUSINESS_RULE,
                    "PAG-002",
                    "Pagamento so pode ser criado para pedido aguardando pagamento");
        }

        if (request.valor().compareTo(pedido.total) != 0) {
            throw new PagamentoUseCaseException(
                    ErrorCategory.BUSINESS_RULE,
                    "PAG-003",
                    "Valor do pagamento diferente do total do pedido");
        }

        if (repository.findByPedidoId(request.pedidoId()).isPresent()) {
            throw new PagamentoUseCaseException(
                    ErrorCategory.CONFLICT,
                    "PAG-004",
                    "Pedido ja possui pagamento");
        }

        Pagamento pagamento = new Pagamento();
        pagamento.pedidoId = request.pedidoId();
        pagamento.valor = request.valor();
        pagamento.meio = request.meio();

        return mapper.toResponse(repository.insert(pagamento));
    }

    public List<PagamentoResponse> listar() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    public PagamentoResponse buscar(UUID id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new PagamentoUseCaseException(
                        ErrorCategory.NOT_FOUND,
                        "PAG-404",
                        "Pagamento nao encontrado")));
    }

    public PagamentoResponse atualizar(UUID id, PagamentoRequest request) {
        Pagamento pagamento = repository.findById(id)
                .orElseThrow(() -> new PagamentoUseCaseException(
                        ErrorCategory.NOT_FOUND,
                        "PAG-404",
                        "Pagamento nao encontrado"));

        if (pagamento.status != StatusPagamento.PENDENTE) {
            throw new PagamentoUseCaseException(
                    ErrorCategory.CONFLICT,
                    "PAG-005",
                    "Somente pagamento PENDENTE pode ser editado");
        }

        if (!pagamento.pedidoId.equals(request.pedidoId())) {
            throw new PagamentoUseCaseException(
                    ErrorCategory.BUSINESS_RULE,
                    "PAG-006",
                    "Nao e permitido trocar o pedido de um pagamento");
        }

        Pedido pedido = pedidoRepository.findById(pagamento.pedidoId)
                .orElseThrow(() -> new PagamentoUseCaseException(
                        ErrorCategory.NOT_FOUND,
                        "PAG-001",
                        "Pedido nao encontrado"));

        if (request.valor().compareTo(pedido.total) != 0) {
            throw new PagamentoUseCaseException(
                    ErrorCategory.BUSINESS_RULE,
                    "PAG-003",
                    "Valor do pagamento diferente do total do pedido");
        }

        pagamento.valor = request.valor();
        pagamento.meio = request.meio();

        return mapper.toResponse(repository.update(pagamento));
    }

    public void excluir(UUID id) {
        Pagamento pagamento = repository.findById(id)
                .orElseThrow(() -> new PagamentoUseCaseException(
                        ErrorCategory.NOT_FOUND,
                        "PAG-404",
                        "Pagamento nao encontrado"));

        if (pagamento.status != StatusPagamento.PENDENTE) {
            throw new PagamentoUseCaseException(
                    ErrorCategory.CONFLICT,
                    "PAG-007",
                    "Somente pagamento PENDENTE pode ser excluido");
        }

        repository.delete(id);
    }
}
