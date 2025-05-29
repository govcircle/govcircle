package gov.govcircle.comon.security.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ErrorResponse {

    private String message;
    private LocalDateTime timestamp;
}
