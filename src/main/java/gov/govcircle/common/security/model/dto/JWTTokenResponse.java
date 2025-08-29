package gov.govcircle.common.security.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTTokenResponse {

    private String token;
}
