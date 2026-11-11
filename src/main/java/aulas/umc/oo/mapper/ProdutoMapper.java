package aulas.umc.oo.mapper;

import aulas.umc.oo.DTO.ProdutoRequest;
import aulas.umc.oo.DTO.ProdutoResponse;
import aulas.umc.oo.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public Produto toDomain(ProdutoRequest request) {
        return new Produto(request.nome(), request.preco());
    }

    public ProdutoResponse toResponse(Produto produto) {
        return new ProdutoResponse(
                produto.id,
                produto.nome,
                produto.preco,
                produto.ativo
        );
    }
}
