package aulas.umc.oo.strategy.processo;

import aulas.umc.oo.pipeline.*;
import aulas.umc.oo.strategy.ProcessStrategy;
import aulas.umc.oo.repository.*;
import aulas.umc.oo.model.*;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class AlterarTipoFretePedidoStrategy implements ProcessStrategy {
    private final PedidoRepository repo;
    public AlterarTipoFretePedidoStrategy(PedidoRepository r){repo=r;}
    public Processo processo(){return Processo.EDITAR_PEDIDO;}
    public int ordem(){return 20;}
    public String nome(){return "alterar-tipo-frete-e-invalidar-calculo";}
    public void executar(ProcessContext c){
        Pedido p=c.get("pedido",Pedido.class);
        p.tipoFrete=c.get("tipoFrete",TipoFrete.class);
        p.subtotal=BigDecimal.ZERO;
        p.valorFrete=BigDecimal.ZERO;
        p.total=BigDecimal.ZERO;
        repo.replaceItems(p.id, java.util.List.of());
        repo.update(p);
    }
}
