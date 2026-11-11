package aulas.umc.oo.controller;

import aulas.umc.oo.DTO.ProdutoRequest;
import aulas.umc.oo.DTO.ProdutoResponse;
import aulas.umc.oo.UseCase.ProdutoUseCase;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoUseCase useCase;

    public ProdutoController(ProdutoUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRODUTO_ESCREVER')")
    public ProdutoResponse criar(@Valid @RequestBody ProdutoRequest request) {
        return useCase.criar(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRODUTO_LER')")
    public List<ProdutoResponse> listar() {
        return useCase.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUTO_LER')")
    public ProdutoResponse buscar(@PathVariable UUID id) {
        return useCase.buscar(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUTO_ESCREVER')")
    public ProdutoResponse atualizar(@PathVariable UUID id, @Valid @RequestBody ProdutoRequest request) {
        return useCase.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUTO_ESCREVER')")
    public void excluir(@PathVariable UUID id) {
        useCase.excluir(id);
    }
}
