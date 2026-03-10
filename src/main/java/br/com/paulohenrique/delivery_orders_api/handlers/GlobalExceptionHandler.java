package br.com.paulohenrique.delivery_orders_api.handlers;

import br.com.paulohenrique.delivery_orders_api.domain.exception.NotFoundException;
import br.com.paulohenrique.delivery_orders_api.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericError(Exception exception) {
        log.error("Erro interno inesperado", exception);
        return new ErrorResponse("Erro interno do servidor");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnreadableMessage(HttpMessageNotReadableException exception) {
        return new ErrorResponse("Corpo da requisição inválido ou malformado");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationErrors(MethodArgumentNotValidException methodArgumentNotValidException) {
        List<String> errors = methodArgumentNotValidException.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        return new ErrorResponse("Erro de validação", errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationErrors(MethodArgumentTypeMismatchException exception) {
        String error = String.format("O parâmetro '%s' recebeu o valor '%s', que é inválido",
                exception.getName(),
                exception.getValue()
        );

        return new ErrorResponse("Erro de tipo de argumento", List.of(error));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
