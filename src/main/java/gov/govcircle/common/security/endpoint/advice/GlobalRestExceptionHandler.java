package gov.govcircle.common.security.endpoint.advice;

import gov.govcircle.common.models.exception.ApplicationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Object> handleExceptions(ApplicationException ex) {
        return new ResponseEntity<>(
                ex.getMessage(),
                ex.httpStatus()

        );

    }

}
