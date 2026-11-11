package aulas.umc.oo.repository;

import aulas.umc.oo.model.MovimentacaoEstoque;

import java.util.List;
import java.util.UUID;

public interface MovimentacaoEstoqueRepository {

    MovimentacaoEstoque insert(MovimentacaoEstoque movimentacao);

    List<MovimentacaoEstoque> findAll();

    List<MovimentacaoEstoque> findByProdutoId(UUID produtoId);

    /**
     * Calcula o saldo exclusivamente a partir do historico:
     * ENTRADA soma; SAIDA subtrai.
     */
    long calcularSaldo(UUID produtoId);

    /**
     * Serializa movimentacoes concorrentes do mesmo produto.
     * O lock e mantido pela transacao do PipelineExecutor.
     */
    void bloquearProduto(UUID produtoId);
}
