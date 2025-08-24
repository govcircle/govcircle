package gov.govcircle.constitution.opinion.model.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class OpinionUserRole {

    private String actorId;
    private String actorTitle;
}
