package gov.govcircle.constitution.constitution.model.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class VoteInfo {

    private int yes;
    private int no;
    private int abstain;
}
