package gov.govcircle.constitution.amendment.model.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class RuleChangeData {

    private Long constitutionRuleId;
    private String title;
    private Long parentId;
    private Integer order;
}
