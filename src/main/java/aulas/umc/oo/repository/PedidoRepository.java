package aulas.umc.oo.repository;

import aulas.umc.oo.model.*;
import java.util.*;

public interface PedidoRepository {
    Pedido insert(Pedido p);

    List<Pedido> findAll();

    Optional<Pedido> findById(UUID id);

    Optional<Pedido> findByCarrinhoId(UUID carrinhoId);

    Pedido update(Pedido p);

    void deleteLogical(UUID id);

    List<ItemPedido> findItems(UUID pedidoId);

    void replaceItems(UUID pedidoId, List<ItemPedido> itens);
}
