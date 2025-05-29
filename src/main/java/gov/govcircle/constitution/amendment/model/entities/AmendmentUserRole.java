package gov.govcircle.constitution.amendment.model.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class AmendmentUserRole {

    private String actorId;
    private String actorTitle;
}
