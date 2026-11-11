package aulas.umc.oo.validation;

import aulas.umc.oo.DTO.ApiErrorResponse;
import aulas.umc.oo.exception.ResponseValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseValidationAdvice implements ResponseBodyAdvice<Object> {

    private static final String API_CONTROLLER_PACKAGE = "aulas.umc.oo.controller";

    private final Validator validator;

    public ResponseValidationAdvice(Validator validator) {
        this.validator = validator;
    }

    @Override
    public boolean supports(
            MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        Package controllerPackage = returnType.getContainingClass().getPackage();
        return controllerPackage != null
                && API_CONTROLLER_PACKAGE.equals(controllerPackage.getName());
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        if (body == null || body instanceof ApiErrorResponse) {
            return body;
        }

        List<String> violacoes = new ArrayList<>();

        if (body instanceof Collection<?> collection) {
            for (Object item : collection) {
                validate(item, violacoes);
            }
        } else {
            validate(body, violacoes);
        }

        if (!violacoes.isEmpty()) {
            throw new ResponseValidationException(violacoes);
        }

        return body;
    }

    private void validate(Object body, List<String> violacoes) {
        if (body == null) {
            violacoes.add("Resposta contem item nulo");
            return;
        }

        for (ConstraintViolation<Object> violation : validator.validate(body)) {
            violacoes.add(violation.getPropertyPath() + ": " + violation.getMessage());
        }
    }
}
