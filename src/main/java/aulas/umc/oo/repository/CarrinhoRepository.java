package aulas.umc.oo.repository;

import aulas.umc.oo.model.*;
import java.util.*;

public interface CarrinhoRepository {
    Carrinho insert(Carrinho c);

    List<Carrinho> findAll();

    Optional<Carrinho> findById(UUID id);

    Carrinho update(Carrinho c);

    void deleteLogical(UUID id);

    ItemCarrinho addItem(ItemCarrinho item);

    List<ItemCarrinho> findItems(UUID carrinhoId);

    void clearItems(UUID carrinhoId);
}
