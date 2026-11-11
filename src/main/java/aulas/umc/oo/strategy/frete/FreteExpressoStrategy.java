package aulas.umc.oo.strategy.frete;

import aulas.umc.oo.model.TipoFrete;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class FreteExpressoStrategy implements FreteStrategy {
    public TipoFrete tipo() {
        return TipoFrete.EXPRESSO;
    }

    public BigDecimal calcular(BigDecimal subtotal) {
        return new BigDecimal("35.00");
    }
}
