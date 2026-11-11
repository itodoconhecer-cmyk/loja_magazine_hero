package aulas.umc.oo.controller;

import aulas.umc.oo.DTO.PagamentoRequest;
import aulas.umc.oo.DTO.PagamentoResponse;
import aulas.umc.oo.DTO.PipelineResponse;
import aulas.umc.oo.UseCase.PagamentoUseCase;
import aulas.umc.oo.UseCase.ProcessoPedidoUseCase;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    private final PagamentoUseCase useCase;
    private final ProcessoPedidoUseCase processoUseCase;

    public PagamentoController(PagamentoUseCase useCase, ProcessoPedidoUseCase processoUseCase) {
        this.useCase = useCase;
        this.processoUseCase = processoUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PAGAMENTO_ESCREVER')")
    public PagamentoResponse criar(@Valid @RequestBody PagamentoRequest request) {
        return useCase.criar(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PAGAMENTO_LER')")
    public List<PagamentoResponse> listar() {
        return useCase.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PAGAMENTO_LER')")
    public PagamentoResponse buscar(@PathVariable UUID id) {
        return useCase.buscar(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PAGAMENTO_ESCREVER')")
    public PagamentoResponse atualizar(@PathVariable UUID id, @Valid @RequestBody PagamentoRequest request) {
        return useCase.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PAGAMENTO_ESCREVER')")
    public void excluir(@PathVariable UUID id) {
        useCase.excluir(id);
    }

    @PostMapping("/{id}/processos/aprovar")
    @PreAuthorize("hasAuthority('PAGAMENTO_APROVAR')")
    public PipelineResponse aprovar(@PathVariable UUID id) {
        return processoUseCase.aprovarPagamento(id);
    }

    @PostMapping("/{id}/processos/estornar")
    @PreAuthorize("hasAuthority('PAGAMENTO_ESTORNAR')")
    public PipelineResponse estornar(@PathVariable UUID id) {
        return processoUseCase.estornarPagamento(id);
    }
}
