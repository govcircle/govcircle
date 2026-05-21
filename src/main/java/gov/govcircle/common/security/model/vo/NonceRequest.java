package gov.govcircle.common.security.model.vo;

import gov.govcircle.common.security.model.entity.CardanoActorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class NonceRequest {
    private CardanoActorType actorType;
    private String identifier;

}
