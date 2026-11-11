package aulas.umc.oo.controller;

import aulas.umc.oo.DTO.PedidoEdicaoRequest;
import aulas.umc.oo.DTO.PedidoRequest;
import aulas.umc.oo.DTO.PedidoResponse;
import aulas.umc.oo.DTO.PipelineResponse;
import aulas.umc.oo.UseCase.PedidoUseCase;
import aulas.umc.oo.UseCase.ProcessoPedidoUseCase;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoUseCase useCase;
    private final ProcessoPedidoUseCase processoUseCase;

    public PedidoController(PedidoUseCase useCase, ProcessoPedidoUseCase processoUseCase) {
        this.useCase = useCase;
        this.processoUseCase = processoUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PEDIDO_ESCREVER')")
    public PedidoResponse criar(@Valid @RequestBody PedidoRequest request) {
        return useCase.criar(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PEDIDO_LER')")
    public List<PedidoResponse> listar() {
        return useCase.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PEDIDO_LER')")
    public PedidoResponse buscar(@PathVariable UUID id) {
        return useCase.buscar(id);
    }

    @PostMapping("/{id}/processos/calcular-frete")
    @PreAuthorize("hasAuthority('PEDIDO_CALCULAR_FRETE')")
    public PipelineResponse calcularFrete(@PathVariable UUID id) {
        return processoUseCase.calcularFrete(id);
    }

    @PostMapping("/{id}/processos/aprovar")
    @PreAuthorize("hasAuthority('PEDIDO_APROVAR')")
    public PipelineResponse aprovar(@PathVariable UUID id) {
        return processoUseCase.aprovarPedido(id);
    }

    @PostMapping("/{id}/processos/cancelar")
    @PreAuthorize("hasAuthority('PEDIDO_CANCELAR')")
    public PipelineResponse cancelar(@PathVariable UUID id) {
        return processoUseCase.cancelarPedido(id);
    }

    @PostMapping("/{id}/processos/excluir")
    @PreAuthorize("hasAuthority('PEDIDO_EXCLUIR')")
    public PipelineResponse excluir(@PathVariable UUID id) {
        return processoUseCase.excluirPedido(id);
    }

    @PutMapping("/{id}/processos/editar")
    @PreAuthorize("hasAuthority('PEDIDO_EDITAR')")
    public PipelineResponse editar(@PathVariable UUID id, @Valid @RequestBody PedidoEdicaoRequest request) {
        return processoUseCase.editarPedido(id, request.tipoFrete());
    }
}
