package aulas.umc.oo.repository;
import aulas.umc.oo.model.Cliente; import java.util.*;
public interface ClienteRepository { Cliente insert(Cliente c); List<Cliente> findAll(); Optional<Cliente> findById(UUID id); Cliente update(Cliente c); void deleteLogical(UUID id); }
