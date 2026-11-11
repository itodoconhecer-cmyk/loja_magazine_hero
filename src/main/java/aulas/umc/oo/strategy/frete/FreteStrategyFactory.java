package aulas.umc.oo.strategy.frete;

import aulas.umc.oo.model.TipoFrete;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class FreteStrategyFactory {
    private final Map<TipoFrete, FreteStrategy> map;

    public FreteStrategyFactory(List<FreteStrategy> all) {
        Map<TipoFrete, FreteStrategy> m = new EnumMap<>(TipoFrete.class);
        for (var s : all) {
            if (m.putIfAbsent(s.tipo(), s) != null)
                throw new IllegalStateException("Mais de uma FreteStrategy para " + s.tipo());
        }
        for (TipoFrete t : TipoFrete.values())
            if (!m.containsKey(t)) throw new IllegalStateException("Frete sem strategy: " + t);
        map = Map.copyOf(m);
    }

    public FreteStrategy obter(TipoFrete tipo) {
        FreteStrategy s = map.get(tipo);
        if (s == null) throw new IllegalArgumentException("Frete sem strategy: " + tipo);
        return s;
    }
}
