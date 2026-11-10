package aulas.umc.oo.strategy;
import aulas.umc.oo.pipeline.*;
public interface ProcessStrategy { Processo processo(); int ordem(); String nome(); void executar(ProcessContext context); }
