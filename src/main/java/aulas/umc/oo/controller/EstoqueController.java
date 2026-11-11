package aulas.umc.oo.controller;

import aulas.umc.oo.DTO.MovimentacaoEstoqueRequest;
import aulas.umc.oo.DTO.MovimentacaoEstoqueResponse;
import aulas.umc.oo.DTO.PipelineResponse;
import aulas.umc.oo.DTO.SaldoEstoqueResponse;
import aulas.umc.oo.UseCase.EstoqueConsultaUseCase;
import aulas.umc.oo.UseCase.ProcessoPedidoUseCase;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    private final ProcessoPedidoUseCase processoUseCase;
    private final EstoqueConsultaUseCase consultaUseCase;

    public EstoqueController(ProcessoPedidoUseCase processoUseCase, EstoqueConsultaUseCase consultaUseCase) {
        this.processoUseCase = processoUseCase;
        this.consultaUseCase = consultaUseCase;
    }

    @GetMapping("/movimentacoes")
    public List<MovimentacaoEstoqueResponse> listarMovimentacoes(@RequestParam(required = false) UUID produtoId) {
        return consultaUseCase.listarMovimentacoes(produtoId);
    }

    @GetMapping("/produtos/{produtoId}/saldo")
    public SaldoEstoqueResponse consultarSaldo(@PathVariable UUID produtoId) {
        return consultaUseCase.consultarSaldo(produtoId);
    }

    @PostMapping("/entrada")
    public PipelineResponse entrada(@Valid @RequestBody MovimentacaoEstoqueRequest request) {
        return processoUseCase.entrada(request.produtoId(), request.quantidade(), request.motivo());
    }

    @PostMapping("/saida")
    public PipelineResponse saida(@Valid @RequestBody MovimentacaoEstoqueRequest request) {
        return processoUseCase.saida(request.produtoId(), request.quantidade(), request.motivo());
    }
}
