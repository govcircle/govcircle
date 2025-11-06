package gov.govcircle.common.models.exception;

import org.springframework.http.HttpStatus;

public class ContentNotFoundException extends ApplicationException {
    public ContentNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.NOT_FOUND;

    }

}
