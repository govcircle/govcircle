package gov.govcircle.constitution.rule.model.entities;

import gov.govcircle.base.model.entity.BaseEntity;
import gov.govcircle.constitution.constitution.model.entities.ConstitutionBranch;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Rule extends BaseEntity {

    @ManyToOne
    private ConstitutionBranch constitutionBranch;

    @ManyToOne
    private Rule parent;

    private String rule;
}
