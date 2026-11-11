package aulas.umc.oo.controller;

import aulas.umc.oo.DTO.ApiErrorResponse;
import aulas.umc.oo.DTO.FieldValidationError;
import aulas.umc.oo.exception.ErrorCategory;
import aulas.umc.oo.exception.InitialFlowValidationException;
import aulas.umc.oo.exception.ResponseValidationException;
import aulas.umc.oo.exception.UseCaseException;
import aulas.umc.oo.repository.RepositoryException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger ERROR_LOG = LoggerFactory.getLogger("ERRORS");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErrorResponse> requestInvalido(
            MethodArgumentNotValidException exception, HttpServletRequest request) {

        List<FieldValidationError> campos = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldValidationError(error.getField(), error.getDefaultMessage()))
                .toList();

        return resposta(HttpStatus.BAD_REQUEST, "REQ-001", "Campos da requisicao invalidos", request, campos);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiErrorResponse> parametroInvalido(
            ConstraintViolationException exception, HttpServletRequest request) {

        List<FieldValidationError> campos = exception.getConstraintViolations().stream()
                .map(violation ->
                        new FieldValidationError(violation.getPropertyPath().toString(), violation.getMessage()))
                .toList();

        return resposta(HttpStatus.BAD_REQUEST, "REQ-002", "Parametros da requisicao invalidos", request, campos);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiErrorResponse> corpoInvalido(
            HttpMessageNotReadableException exception, HttpServletRequest request) {
        return resposta(
                HttpStatus.BAD_REQUEST,
                "REQ-003",
                "Corpo JSON ausente, malformado ou com valor de tipo invalido",
                request,
                List.of());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ApiErrorResponse> tipoParametroInvalido(
            MethodArgumentTypeMismatchException exception, HttpServletRequest request) {
        return resposta(
                HttpStatus.BAD_REQUEST, "REQ-004", "Parametro invalido: " + exception.getName(), request, List.of());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<ApiErrorResponse> parametroObrigatorioAusente(
            MissingServletRequestParameterException exception, HttpServletRequest request) {
        return resposta(
                HttpStatus.BAD_REQUEST,
                "REQ-005",
                "Parametro obrigatorio ausente: " + exception.getParameterName(),
                request,
                List.of());
    }

    @ExceptionHandler(UseCaseException.class)
    ResponseEntity<ApiErrorResponse> casoDeUso(UseCaseException exception, HttpServletRequest request) {

        HttpStatus status = toHttpStatus(exception.getCategoria());

        ERROR_LOG.warn(
                "use_case_error useCase={} category={} code={} message={}",
                exception.getUseCase(),
                exception.getCategoria(),
                exception.getCodigo(),
                exception.getMessage());

        return resposta(status, exception.getCodigo(), exception.getMessage(), request, List.of());
    }

    @ExceptionHandler(InitialFlowValidationException.class)
    ResponseEntity<ApiErrorResponse> fluxoInicial(
            InitialFlowValidationException exception, HttpServletRequest request) {

        ERROR_LOG.warn(
                "initial_flow_validation_error code={} message={}", exception.getCodigo(), exception.getMessage());

        return resposta(
                HttpStatus.UNPROCESSABLE_ENTITY, exception.getCodigo(), exception.getMessage(), request, List.of());
    }

    @ExceptionHandler(ResponseValidationException.class)
    ResponseEntity<ApiErrorResponse> responseInvalido(
            ResponseValidationException exception, HttpServletRequest request) {

        ERROR_LOG.error("response_contract_error violations={}", exception.getViolacoes());

        List<FieldValidationError> campos = exception.getViolacoes().stream()
                .map(violation -> new FieldValidationError("response", violation))
                .toList();

        return resposta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "RESP-001",
                "Resposta interna invalida. Contrato de API nao atendido",
                request,
                campos);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, RepositoryException.class})
    ResponseEntity<ApiErrorResponse> persistencia(RuntimeException exception, HttpServletRequest request) {

        ERROR_LOG.error("persistence_error", exception);

        return resposta(HttpStatus.CONFLICT, "DB-001", "Operacao viola uma regra de persistencia", request, List.of());
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    ResponseEntity<ApiErrorResponse> regraLegada(RuntimeException exception, HttpServletRequest request) {

        ERROR_LOG.warn("legacy_business_error message={}", exception.getMessage());

        return resposta(HttpStatus.UNPROCESSABLE_ENTITY, "BUS-LEGACY", exception.getMessage(), request, List.of());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiErrorResponse> erroNaoTratado(Exception exception, HttpServletRequest request) {

        ERROR_LOG.error("unhandled_error", exception);

        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "SYS-001", "Erro interno inesperado", request, List.of());
    }

    private HttpStatus toHttpStatus(ErrorCategory categoria) {
        return switch (categoria) {
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case BUSINESS_RULE -> HttpStatus.UNPROCESSABLE_ENTITY;
        };
    }

    private ResponseEntity<ApiErrorResponse> resposta(
            HttpStatus status,
            String codigo,
            String mensagem,
            HttpServletRequest request,
            List<FieldValidationError> campos) {

        return ResponseEntity.status(status)
                .body(new ApiErrorResponse(
                        OffsetDateTime.now(),
                        status.value(),
                        codigo,
                        mensagem,
                        request.getRequestURI(),
                        MDC.get("correlationId"),
                        campos));
    }
}
