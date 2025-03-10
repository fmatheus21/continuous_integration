package br.com.fmatheus.app.controller.exception.handler;

import br.com.fmatheus.app.controller.exception.NotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String MESSAGE = "massage";
    private static final String UNEXPECTED_ERROR = "Ocorreu um erro inesperado.";
    private final MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleBadRequestException(Exception ex) {
        var httpStatus = HttpStatus.BAD_REQUEST;
        var problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, UNEXPECTED_ERROR);
        problemDetail.setProperty(MESSAGE, this.returnMessage(ex));
        return ResponseEntity.status(httpStatus).body(problemDetail);
    }

    private String returnMessage(Exception ex) {
        try {
            return this.messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return ex.getMessage();
        }
    }
}
