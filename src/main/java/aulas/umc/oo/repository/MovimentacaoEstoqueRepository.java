package aulas.umc.oo.repository;
import aulas.umc.oo.model.MovimentacaoEstoque; import java.util.*;
public interface MovimentacaoEstoqueRepository { MovimentacaoEstoque insert(MovimentacaoEstoque m); List<MovimentacaoEstoque> findAll(); }
