package aulas.umc.oo.strategy.frete; import aulas.umc.oo.model.TipoFrete; import org.springframework.stereotype.Component; import java.math.BigDecimal;
@Component public class FreteExpressoStrategy implements FreteStrategy { public TipoFrete tipo(){return TipoFrete.EXPRESSO;} public BigDecimal calcular(BigDecimal subtotal){return new BigDecimal("35.00");} }
