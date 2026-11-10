package aulas.umc.oo.pipeline;
import aulas.umc.oo.strategy.ProcessStrategy; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.*;
@Service
public class PipelineExecutor {
 private final List<ProcessStrategy> strategies;
 public PipelineExecutor(List<ProcessStrategy> s){strategies=List.copyOf(s);validarConfiguracao();}
 private void validarConfiguracao(){Map<String,List<String>> conflitos=new LinkedHashMap<>();for(var s:strategies){String chave=s.processo()+":"+s.ordem();conflitos.computeIfAbsent(chave,k->new ArrayList<>()).add(s.getClass().getSimpleName());}var duplicados=conflitos.entrySet().stream().filter(e->e.getValue().size()>1).toList();if(!duplicados.isEmpty())throw new IllegalStateException("Strategies com ordem duplicada: "+duplicados);}
 @Transactional public ProcessContext executar(Processo p,ProcessContext c){var tarefas=strategies.stream().filter(s->s.processo()==p).sorted(Comparator.comparingInt(ProcessStrategy::ordem)).toList();if(tarefas.isEmpty())throw new IllegalStateException("Pipeline sem strategies para "+p);for(var s:tarefas){s.executar(c);c.registrar(s.nome());}return c;}
}
