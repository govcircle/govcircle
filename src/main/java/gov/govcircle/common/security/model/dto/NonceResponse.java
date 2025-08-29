package gov.govcircle.common.security.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NonceResponse {

    private String nonce;
}
