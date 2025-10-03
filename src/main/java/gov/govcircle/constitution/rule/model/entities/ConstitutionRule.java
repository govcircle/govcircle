package gov.govcircle.constitution.rule.model.entities;

import gov.govcircle.constitution.constitution.model.entities.ConstitutionBranch;
import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@Table(name = "gc_constitution_rule")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ConstitutionRule extends BaseEntity {

    @ManyToOne
    @JoinColumn(
            name = "rule_id",
            foreignKey = @ForeignKey(name = "constitution_rule_fk_id"),
            nullable = false
    )
    private Rule rule;

    @ManyToOne
    @JoinColumn(
            name = "constitution_branch_id",
            foreignKey = @ForeignKey(name = "constitution_rule_branch_fk_id"),
            nullable = false
    )
    private ConstitutionBranch constitution;

    private int ruleOrder;

    @ManyToOne
    @JoinColumn(
            name = "parent_rule_id",
            foreignKey = @ForeignKey(name = "constitution_rule_parent_fk_id"),
            nullable = false
    )
    private Rule parentRule;
}
