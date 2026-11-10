package aulas.umc.oo.repository;

import aulas.umc.oo.model.Produto;
import java.util.*;

public interface ProdutoRepository {
    Produto insert(Produto p);
    List<Produto> findAll();
    Optional<Produto> findById(UUID id);
    Optional<Produto> findByIdIncludingInactive(UUID id);
    Produto update(Produto p);
    void deleteLogical(UUID id);
    void atualizarEstoque(UUID id, int novoEstoque);
    void adicionarEstoque(UUID id, int quantidade, boolean exigirAtivo);
    void retirarEstoqueSeDisponivel(UUID id, int quantidade);
}
