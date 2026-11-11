package aulas.umc.oo.UseCase;

import aulas.umc.oo.DTO.MovimentacaoEstoqueResponse;
import aulas.umc.oo.DTO.SaldoEstoqueResponse;
import aulas.umc.oo.model.MovimentacaoEstoque;
import aulas.umc.oo.repository.MovimentacaoEstoqueRepository;
import aulas.umc.oo.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EstoqueConsultaUseCase {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository;

    public EstoqueConsultaUseCase(
            ProdutoRepository produtoRepository,
            MovimentacaoEstoqueRepository movimentacaoRepository
    ) {
        this.produtoRepository = produtoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    public SaldoEstoqueResponse consultarSaldo(UUID produtoId) {
        validarProduto(produtoId);
        return new SaldoEstoqueResponse(
                produtoId,
                movimentacaoRepository.calcularSaldo(produtoId)
        );
    }

    public List<MovimentacaoEstoqueResponse> listarMovimentacoes(UUID produtoId) {
        if (produtoId == null) {
            return movimentacaoRepository.findAll().stream()
                    .map(this::toResponse)
                    .toList();
        }

        validarProduto(produtoId);
        return movimentacaoRepository.findByProdutoId(produtoId).stream()
                .map(this::toResponse)
                .toList();
    }

    private void validarProduto(UUID produtoId) {
        produtoRepository.findByIdIncludingInactive(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto nao encontrado"));
    }

    private MovimentacaoEstoqueResponse toResponse(MovimentacaoEstoque movimentacao) {
        return new MovimentacaoEstoqueResponse(
                movimentacao.id,
                movimentacao.produtoId,
                movimentacao.tipo,
                movimentacao.quantidade,
                movimentacao.motivo,
                movimentacao.dataHora
        );
    }
}
