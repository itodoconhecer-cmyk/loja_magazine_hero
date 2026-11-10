package aulas.umc.oo.strategy.processo;

import aulas.umc.oo.pipeline.*;
import aulas.umc.oo.strategy.ProcessStrategy;
import aulas.umc.oo.repository.*;
import aulas.umc.oo.model.*;
import aulas.umc.oo.strategy.frete.FreteStrategyFactory;
import org.springframework.stereotype.Component;
import java.util.*;
import java.math.BigDecimal;

@Component
public class ExcluirPedidoStrategy implements ProcessStrategy {
 private final PedidoRepository repo; public ExcluirPedidoStrategy(PedidoRepository r){repo=r;}
 public Processo processo(){return Processo.EXCLUIR_PEDIDO;} public int ordem(){return 20;} public String nome(){return "excluir-pedido";}
 public void executar(ProcessContext c){ repo.deleteLogical(c.get("pedidoId",UUID.class)); }
}
