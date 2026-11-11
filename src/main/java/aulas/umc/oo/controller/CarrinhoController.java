package aulas.umc.oo.controller;

import aulas.umc.oo.DTO.CarrinhoRequest;
import aulas.umc.oo.DTO.CarrinhoResponse;
import aulas.umc.oo.DTO.ItemCarrinhoRequest;
import aulas.umc.oo.DTO.PipelineResponse;
import aulas.umc.oo.UseCase.CarrinhoUseCase;
import aulas.umc.oo.UseCase.ProcessoPedidoUseCase;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrinhos")
public class CarrinhoController {

    private final CarrinhoUseCase useCase;
    private final ProcessoPedidoUseCase processoUseCase;

    public CarrinhoController(CarrinhoUseCase useCase, ProcessoPedidoUseCase processoUseCase) {
        this.useCase = useCase;
        this.processoUseCase = processoUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CARRINHO_ESCREVER')")
    public CarrinhoResponse criar(@Valid @RequestBody CarrinhoRequest request) {
        return useCase.criar(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CARRINHO_LER')")
    public List<CarrinhoResponse> listar() {
        return useCase.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CARRINHO_LER')")
    public CarrinhoResponse buscar(@PathVariable UUID id) {
        return useCase.buscar(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CARRINHO_ESCREVER')")
    public CarrinhoResponse atualizar(@PathVariable UUID id, @Valid @RequestBody CarrinhoRequest request) {
        return useCase.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CARRINHO_ESCREVER')")
    public void excluir(@PathVariable UUID id) {
        useCase.excluir(id);
    }

    @PostMapping("/{id}/itens")
    @PreAuthorize("hasAuthority('CARRINHO_ESCREVER')")
    public CarrinhoResponse adicionarItem(@PathVariable UUID id, @Valid @RequestBody ItemCarrinhoRequest request) {
        return useCase.adicionarItem(id, request);
    }

    @PostMapping("/{id}/processos/esvaziar")
    @PreAuthorize("hasAuthority('CARRINHO_PROCESSAR')")
    public PipelineResponse esvaziar(@PathVariable UUID id) {
        return processoUseCase.esvaziarCarrinho(id);
    }
}
