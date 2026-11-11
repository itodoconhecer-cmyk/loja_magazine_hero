package aulas.umc.oo.controller;

import aulas.umc.oo.DTO.*;
import aulas.umc.oo.UseCase.*;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoUseCase crud;
    private final ProcessoPedidoUseCase proc;

    public PedidoController(PedidoUseCase c, ProcessoPedidoUseCase p) {
        crud = c;
        proc = p;
    }

    @PostMapping
    public PedidoResponse criar(@Valid @RequestBody PedidoRequest r) {
        return crud.criar(r);
    }

    @GetMapping
    public List<PedidoResponse> listar() {
        return crud.listar();
    }

    @GetMapping("/{id}")
    public PedidoResponse buscar(@PathVariable UUID id) {
        return crud.buscar(id);
    }

    @PostMapping("/{id}/processos/calcular-frete")
    public PipelineResponse frete(@PathVariable UUID id) {
        return proc.calcularFrete(id);
    }

    @PostMapping("/{id}/processos/aprovar")
    public PipelineResponse aprovar(@PathVariable UUID id) {
        return proc.aprovarPedido(id);
    }

    @PostMapping("/{id}/processos/cancelar")
    public PipelineResponse cancelar(@PathVariable UUID id) {
        return proc.cancelarPedido(id);
    }

    @PostMapping("/{id}/processos/excluir")
    public PipelineResponse excluir(@PathVariable UUID id) {
        return proc.excluirPedido(id);
    }

    @PutMapping("/{id}/processos/editar")
    public PipelineResponse editar(@PathVariable UUID id, @Valid @RequestBody PedidoEdicaoRequest r) {
        return proc.editarPedido(id, r.tipoFrete());
    }
}
