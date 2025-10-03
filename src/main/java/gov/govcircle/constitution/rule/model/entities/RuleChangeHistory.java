package gov.govcircle.constitution.rule.model.entities;

import gov.govcircle.constitution.opinion.model.enums.AmendmentType;
import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@Table(name = "gc_rule_change_history")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RuleChangeHistory extends BaseEntity {

    @ManyToOne
    @JoinColumn(
            name = "base_rule_id",
            foreignKey = @ForeignKey(name = "rule_change_base_rule_fk_id"),
            nullable = false
    )
    private ConstitutionRule baseRule;

    @ManyToOne
    @JoinColumn(
            name = "revised_rule_id",
            foreignKey = @ForeignKey(name = "rule_change_revised_rule_fk_id"),
            nullable = false
    )
    private ConstitutionRule revisedRule;

    @Enumerated(EnumType.STRING)
    private AmendmentType amendmentType;

}
