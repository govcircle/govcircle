package gov.govcircle.common.security.model.exception;


import gov.govcircle.common.models.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserAddressVerificationException extends ApplicationException {
    public UserAddressVerificationException(String message) {
        super(message);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.UNAUTHORIZED;

    }

}
