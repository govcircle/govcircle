package gov.govcircle.constitution.rule.model.entities;

import gov.govcircle.base.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RuleRelation extends BaseEntity {

    @ManyToOne
    private Rule srcRule;

    @ManyToOne
    private Rule relatedRule;

}
