package aulas.umc.oo.controller;

import aulas.umc.oo.DTO.*;
import aulas.umc.oo.UseCase.*;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrinhos")
public class CarrinhoController {
    private final CarrinhoUseCase uc;
    private final ProcessoPedidoUseCase proc;

    public CarrinhoController(CarrinhoUseCase u, ProcessoPedidoUseCase p) {
        uc = u;
        proc = p;
    }

    @PostMapping
    public CarrinhoResponse criar(@Valid @RequestBody CarrinhoRequest r) {
        return uc.criar(r);
    }

    @GetMapping
    public List<CarrinhoResponse> listar() {
        return uc.listar();
    }

    @GetMapping("/{id}")
    public CarrinhoResponse buscar(@PathVariable UUID id) {
        return uc.buscar(id);
    }

    @PutMapping("/{id}")
    public CarrinhoResponse atualizar(@PathVariable UUID id, @Valid @RequestBody CarrinhoRequest r) {
        return uc.atualizar(id, r);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable UUID id) {
        uc.excluir(id);
    }

    @PostMapping("/{id}/itens")
    public CarrinhoResponse item(@PathVariable UUID id, @Valid @RequestBody ItemCarrinhoRequest r) {
        return uc.adicionarItem(id, r);
    }

    @PostMapping("/{id}/processos/esvaziar")
    public PipelineResponse esvaziar(@PathVariable UUID id) {
        return proc.esvaziarCarrinho(id);
    }
}
