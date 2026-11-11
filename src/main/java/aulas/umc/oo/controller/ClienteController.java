package aulas.umc.oo.controller;

import aulas.umc.oo.DTO.*;
import aulas.umc.oo.UseCase.ClienteUseCase;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteUseCase uc;

    public ClienteController(ClienteUseCase u) {
        uc = u;
    }

    @PostMapping
    public ClienteResponse criar(@Valid @RequestBody ClienteRequest r) {
        return uc.criar(r);
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return uc.listar();
    }

    @GetMapping("/{id}")
    public ClienteResponse buscar(@PathVariable UUID id) {
        return uc.buscar(id);
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizar(@PathVariable UUID id, @Valid @RequestBody ClienteRequest r) {
        return uc.atualizar(id, r);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable UUID id) {
        uc.excluir(id);
    }
}
