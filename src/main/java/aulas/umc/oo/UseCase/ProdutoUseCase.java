package aulas.umc.oo.UseCase;

import aulas.umc.oo.DTO.ProdutoRequest;
import aulas.umc.oo.DTO.ProdutoResponse;
import aulas.umc.oo.exception.ProdutoUseCaseException;
import aulas.umc.oo.mapper.ProdutoMapper;
import aulas.umc.oo.model.Produto;
import aulas.umc.oo.repository.ProdutoRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ProdutoUseCase {

    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;

    public ProdutoUseCase(ProdutoRepository repository, ProdutoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProdutoResponse criar(ProdutoRequest request) {
        return mapper.toResponse(repository.insert(mapper.toDomain(request)));
    }

    public List<ProdutoResponse> listar() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    public ProdutoResponse buscar(UUID id) {
        return mapper.toResponse(repository
                .findById(id)
                .orElseThrow(() -> new ProdutoUseCaseException("PRO-404", "Produto nao encontrado")));
    }

    public ProdutoResponse atualizar(UUID id, ProdutoRequest request) {
        Produto produto = repository
                .findById(id)
                .orElseThrow(() -> new ProdutoUseCaseException("PRO-404", "Produto nao encontrado"));

        produto.nome = request.nome();
        produto.preco = request.preco();

        return mapper.toResponse(repository.update(produto));
    }

    public void excluir(UUID id) {
        repository.findById(id).orElseThrow(() -> new ProdutoUseCaseException("PRO-404", "Produto nao encontrado"));
        repository.deleteLogical(id);
    }
}
