package br.com.paulohenrique.delivery_orders_api.handlers;

import br.com.paulohenrique.delivery_orders_api.domain.exception.base.NotFoundException;
import br.com.paulohenrique.delivery_orders_api.domain.exception.base.UnprocessableContentException;
import br.com.paulohenrique.delivery_orders_api.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse exceptionHandler(BadCredentialsException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public ErrorResponse exceptionHandler(NoResourceFoundException ignored) {
        return new ErrorResponse("Recurso não encontrado");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse exceptionHandler(HttpMessageNotReadableException ignored) {
        return new ErrorResponse("Corpo da requisição inválido ou malformado");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse exceptionHandler(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        return new ErrorResponse("Erro de validação", errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponse exceptionHandler(MethodArgumentTypeMismatchException exception) {
        String error = String.format("O parâmetro '%s' recebeu o valor '%s', que é inválido",
                exception.getName(),
                exception.getValue()
        );

        return new ErrorResponse("Erro de tipo de argumento", List.of(error));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse exceptionHandler(Exception exception) {
        log.error("Erro interno inesperado", exception);
        return new ErrorResponse("Erro interno do servidor");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse exceptionHandler(NotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    @ExceptionHandler(UnprocessableContentException.class)
    public ErrorResponse exceptionHandler(UnprocessableContentException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
