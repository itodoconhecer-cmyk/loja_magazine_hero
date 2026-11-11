package aulas.umc.oo.strategy.frete;

import aulas.umc.oo.model.TipoFrete;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class FreteNormalStrategy implements FreteStrategy {
    public TipoFrete tipo() {
        return TipoFrete.NORMAL;
    }

    public BigDecimal calcular(BigDecimal subtotal) {
        return subtotal.compareTo(new BigDecimal("200")) >= 0 ? BigDecimal.ZERO : new BigDecimal("15.00");
    }
}
