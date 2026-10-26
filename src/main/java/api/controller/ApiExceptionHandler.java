package api.controller;

import api.dto.error.CampoErroDTO;
import api.dto.error.ErroDTO;
import api.service.exception.RecursoNaoEncontradoException;
import api.service.exception.RegraDeNegocioException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Converte exceções da aplicação em respostas HTTP padronizadas.
 */

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({
            RecursoNaoEncontradoException.class
    })
    public ResponseEntity<ErroDTO> handleNotFound(RecursoNaoEncontradoException exception,
                                                   HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, exception.getMessage(), request, null);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroDTO> handleConflict(RegraDeNegocioException exception,
                                                  HttpServletRequest request) {
        return response(HttpStatus.CONFLICT, exception.getMessage(), request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDTO> handleArgumentNotValid(MethodArgumentNotValidException exception,
                                                          HttpServletRequest request) {
        List<CampoErroDTO> fields = exception.getBindingResult().getFieldErrors().stream()
                .map(this::toFieldError)
                .toList();
        return response(HttpStatus.BAD_REQUEST, validationMessage(fields), request, fields);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErroDTO> handleMethodValidation(HandlerMethodValidationException exception,
                                                          HttpServletRequest request) {
        List<CampoErroDTO> fields = exception.getParameterValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream()
                        .map(error -> new CampoErroDTO(
                                result.getMethodParameter().getParameterName(),
                                error.getDefaultMessage())))
                .toList();
        String message = fields.isEmpty() ? "Requisição inválida" : validationMessage(fields);
        return response(HttpStatus.BAD_REQUEST, message, request, fields.isEmpty() ? null : fields);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroDTO> handleConstraintViolation(ConstraintViolationException exception,
                                                              HttpServletRequest request) {
        List<CampoErroDTO> fields = exception.getConstraintViolations().stream()
                .map(violation -> new CampoErroDTO(
                        violation.getPropertyPath().toString(), violation.getMessage()))
                .toList();
        return response(HttpStatus.BAD_REQUEST, validationMessage(fields), request, fields);
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErroDTO> handleBadRequest(Exception exception, HttpServletRequest request) {
        String message = exception.getMessage() == null ? "Requisição inválida" : exception.getMessage();
        return response(HttpStatus.BAD_REQUEST, message, request, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroDTO> handleDataIntegrity(DataIntegrityViolationException exception,
                                                       HttpServletRequest request) {
        return response(HttpStatus.CONFLICT, "A operação viola uma restrição dos dados", request, null);
    }

    private CampoErroDTO toFieldError(FieldError error) {
        return new CampoErroDTO(error.getField(), error.getDefaultMessage());
    }

    private String validationMessage(List<CampoErroDTO> fields) {
        return fields.stream()
                .map(field -> field.campo() + ": " + field.mensagem())
                .collect(Collectors.joining("; "));
    }

    private ResponseEntity<ErroDTO> response(HttpStatus status, String message,
                                             HttpServletRequest request,
                                             List<CampoErroDTO> fields) {
        ErroDTO body = new ErroDTO(
                Instant.now(), status.value(), status.getReasonPhrase(), message,
                request.getRequestURI(), fields);
        return ResponseEntity.status(status).body(body);
    }
}
