package aulas.umc.oo.controller;

import aulas.umc.oo.DTO.ClienteRequest;
import aulas.umc.oo.DTO.ClienteResponse;
import aulas.umc.oo.UseCase.ClienteUseCase;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteUseCase useCase;

    public ClienteController(ClienteUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CLIENTE_ESCREVER')")
    public ClienteResponse criar(@Valid @RequestBody ClienteRequest request) {
        return useCase.criar(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CLIENTE_LER')")
    public List<ClienteResponse> listar() {
        return useCase.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENTE_LER')")
    public ClienteResponse buscar(@PathVariable UUID id) {
        return useCase.buscar(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENTE_ESCREVER')")
    public ClienteResponse atualizar(@PathVariable UUID id, @Valid @RequestBody ClienteRequest request) {
        return useCase.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENTE_ESCREVER')")
    public void excluir(@PathVariable UUID id) {
        useCase.excluir(id);
    }
}
