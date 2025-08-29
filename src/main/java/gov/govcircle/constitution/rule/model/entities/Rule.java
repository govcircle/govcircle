package gov.govcircle.constitution.rule.model.entities;

import gov.govcircle.constitution.rule.model.enums.RuleStatus;
import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Rule extends BaseEntity {

    private String rule;

    @Enumerated(EnumType.STRING)
    private RuleStatus status;
}
