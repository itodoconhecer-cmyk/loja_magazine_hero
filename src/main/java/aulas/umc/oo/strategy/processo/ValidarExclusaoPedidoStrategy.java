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
public class ValidarExclusaoPedidoStrategy implements ProcessStrategy {
 private final PedidoRepository repo; public ValidarExclusaoPedidoStrategy(PedidoRepository r){repo=r;}
 public Processo processo(){return Processo.EXCLUIR_PEDIDO;} public int ordem(){return 10;} public String nome(){return "validar-exclusao-pedido";}
 public void executar(ProcessContext c){ Pedido p=repo.findById(c.get("pedidoId",UUID.class)).orElseThrow(); if(p.status!=StatusPedido.CANCELADO&&p.status!=StatusPedido.RASCUNHO)throw new IllegalStateException("Apenas pedido RASCUNHO ou CANCELADO pode ser excluido"); }
}
