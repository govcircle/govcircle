package gov.govcircle.constitution.rule.model.entities;

import gov.govcircle.constitution.constitution.model.entities.Constitution;
import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ConstitutionRule extends BaseEntity {

    @ManyToOne
    private Rule rule;

    @ManyToOne
    private Constitution constitution;

    private int order;

    @ManyToOne
    private Rule parentRule;
}
