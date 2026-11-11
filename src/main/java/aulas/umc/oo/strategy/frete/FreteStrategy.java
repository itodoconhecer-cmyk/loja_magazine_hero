package aulas.umc.oo.strategy.frete;

import aulas.umc.oo.model.TipoFrete;
import java.math.BigDecimal;

public interface FreteStrategy {
    TipoFrete tipo();

    BigDecimal calcular(BigDecimal subtotal);
}
