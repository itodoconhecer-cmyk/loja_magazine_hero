package aulas.umc.oo.repository;

import aulas.umc.oo.model.Produto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProdutoRepository {
    Produto insert(Produto produto);
    List<Produto> findAll();
    Optional<Produto> findById(UUID id);
    Optional<Produto> findByIdIncludingInactive(UUID id);
    Produto update(Produto produto);
    void deleteLogical(UUID id);
}
