package aulas.umc.oo.UseCase;

import aulas.umc.oo.DTO.ClienteRequest;
import aulas.umc.oo.DTO.ClienteResponse;
import aulas.umc.oo.exception.ClienteUseCaseException;
import aulas.umc.oo.exception.ErrorCategory;
import aulas.umc.oo.mapper.ClienteMapper;
import aulas.umc.oo.model.Cliente;
import aulas.umc.oo.repository.ClienteRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ClienteUseCase {

    private final ClienteRepository repository;
    private final ClienteMapper mapper;

    public ClienteUseCase(ClienteRepository repository, ClienteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ClienteResponse criar(ClienteRequest request) {
        return mapper.toResponse(repository.insert(mapper.toDomain(request)));
    }

    public List<ClienteResponse> listar() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    public ClienteResponse buscar(UUID id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new ClienteUseCaseException(
                        ErrorCategory.NOT_FOUND,
                        "CLI-404",
                        "Cliente nao encontrado")));
    }

    public ClienteResponse atualizar(UUID id, ClienteRequest request) {
        repository.findById(id)
                .orElseThrow(() -> new ClienteUseCaseException(
                        ErrorCategory.NOT_FOUND,
                        "CLI-404",
                        "Cliente nao encontrado"));

        Cliente cliente = mapper.toDomain(request);
        cliente.id = id;

        return mapper.toResponse(repository.update(cliente));
    }

    public void excluir(UUID id) {
        repository.findById(id)
                .orElseThrow(() -> new ClienteUseCaseException(
                        ErrorCategory.NOT_FOUND,
                        "CLI-404",
                        "Cliente nao encontrado"));

        repository.deleteLogical(id);
    }
}
