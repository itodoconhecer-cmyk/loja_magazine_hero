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
        String image = produto.imagemStoragePath != null && produto.imagemSha256 != null
                ? "/api/catalogo/produtos/" + produto.id + "/imagem"
                : null;
        return new ProdutoResponse(produto.id, produto.nome, produto.preco, produto.ativo, produto.departamento, image);
    }
}
