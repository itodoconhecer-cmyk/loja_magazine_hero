package aulas.umc.oo.repository;

import aulas.umc.oo.model.Pagamento;
import java.util.*;

public interface PagamentoRepository {
    Pagamento insert(Pagamento p);

    List<Pagamento> findAll();

    Optional<Pagamento> findById(UUID id);

    Optional<Pagamento> findByPedidoId(UUID pedidoId);

    Pagamento update(Pagamento p);

    void delete(UUID id);
}
