package aulas.umc.oo.controller;
import aulas.umc.oo.repository.RepositoryException; import org.springframework.dao.DataIntegrityViolationException; import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler({IllegalArgumentException.class,IllegalStateException.class}) ResponseEntity<Map<String,Object>> regra(RuntimeException e){return ResponseEntity.badRequest().body(Map.of("erro",e.getMessage()));}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<Map<String,Object>> validacao(MethodArgumentNotValidException e){return ResponseEntity.badRequest().body(Map.of("erro","Dados invalidos","campos",e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).toList()));}
 @ExceptionHandler({DataIntegrityViolationException.class,RepositoryException.class}) ResponseEntity<Map<String,Object>> persistencia(RuntimeException e){return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro","Operacao viola uma regra de persistencia","detalhe",e.getMessage()==null?"erro de banco":e.getMessage()));}
}
