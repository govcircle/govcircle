package gov.govcircle.constitution.constitution.model.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ActionInfo {

    private String type;
    private String deposit;
    private String stakeAddress;
}


