package gov.govcircle.constitution.rule.model.entities;

import gov.govcircle.constitution.opinion.model.enums.AmendmentType;
import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RuleChangeHistory extends BaseEntity {

    @ManyToOne
    private ConstitutionRule baseRule;

    @ManyToOne
    private ConstitutionRule revisedRule;

    @Enumerated(EnumType.STRING)
    private AmendmentType amendmentType;

}
