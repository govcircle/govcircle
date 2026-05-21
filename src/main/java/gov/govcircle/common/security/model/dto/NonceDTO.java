package gov.govcircle.common.security.model.dto;

import gov.govcircle.common.security.model.entity.CardanoActorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class NonceDTO {
    private CardanoActorType actorType;
    private String consent;
    private String nonce;

}
