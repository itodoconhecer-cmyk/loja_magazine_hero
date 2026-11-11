package aulas.umc.oo;

import static org.junit.jupiter.api.Assertions.*;

import aulas.umc.oo.strategy.frete.*;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class FreteStrategyTest {
    @Test
    void normalGratisAcimaDe200() {
        assertEquals(new BigDecimal("0"), new FreteNormalStrategy().calcular(new BigDecimal("250")));
    }

    @Test
    void expressoSempre35() {
        assertEquals(new BigDecimal("35.00"), new FreteExpressoStrategy().calcular(new BigDecimal("10")));
    }
}
