package aulas.umc.oo.controller;

import aulas.umc.oo.DTO.ProdutoResponse;
import aulas.umc.oo.UseCase.ProdutoUseCase;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Catalogo publico, separado das operacoes administrativas de produtos. O carrinho do visitante e mantido no navegador
 * e nao cria registros no banco.
 */
@RestController
@RequestMapping("/api/catalogo")
public class CatalogoPublicoController {

    private final ProdutoUseCase produtos;

    public CatalogoPublicoController(ProdutoUseCase produtos) {
        this.produtos = produtos;
    }

    @GetMapping("/produtos")
    public List<ProdutoResponse> listar() {
        return produtos.listar().stream().filter(ProdutoResponse::ativo).toList();
    }
}
