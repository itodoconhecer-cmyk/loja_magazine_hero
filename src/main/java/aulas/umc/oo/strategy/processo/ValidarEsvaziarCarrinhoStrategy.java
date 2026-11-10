package aulas.umc.oo.strategy.processo;
import aulas.umc.oo.pipeline.*; import aulas.umc.oo.strategy.ProcessStrategy; import aulas.umc.oo.repository.*; import org.springframework.stereotype.Component; import java.util.*;
@Component
public class ValidarEsvaziarCarrinhoStrategy implements ProcessStrategy {
 private final CarrinhoRepository repo; private final PedidoRepository pedidoRepo; public ValidarEsvaziarCarrinhoStrategy(CarrinhoRepository r,PedidoRepository p){repo=r;pedidoRepo=p;}
 public Processo processo(){return Processo.ESVAZIAR_CARRINHO;} public int ordem(){return 10;} public String nome(){return "validar-carrinho";}
 public void executar(ProcessContext c){UUID id=c.get("carrinhoId",UUID.class);repo.findById(id).orElseThrow(()->new IllegalArgumentException("Carrinho nao encontrado"));if(pedidoRepo.findByCarrinhoId(id).isPresent())throw new IllegalStateException("Carrinho vinculado a pedido ativo nao pode ser esvaziado");}
}
