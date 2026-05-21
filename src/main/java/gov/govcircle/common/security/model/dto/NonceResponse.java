package gov.govcircle.common.security.model.dto;

import gov.govcircle.common.security.model.entity.CardanoActorType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NonceResponse {

    private CardanoActorType actorType;
    private String nonce;
}
