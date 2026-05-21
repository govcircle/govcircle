package gov.govcircle.common.security.model.dto;


import gov.govcircle.common.security.model.entity.CardanoActorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class IdentifierDTO {

    private CardanoActorType cardanoActorType;
    private String keyIdentifier;
}
