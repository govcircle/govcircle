package gov.govcircle.common.security.endpoint.advice;

import gov.govcircle.common.models.exception.ApplicationException;
import gov.govcircle.common.security.model.entity.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleExceptions(ApplicationException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        ex.getMessage(),
                        LocalDateTime
                                .now()
                                .toString()
                ),
                ex.httpStatus()
        );

    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleExceptions(UsernameNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        ex.getMessage(),
                        LocalDateTime
                                .now()
                                .toString()
                ),
                HttpStatus.FORBIDDEN
        );

    }

}
