package gov.govcircle.constitution.amendment.model.entities;

import gov.govcircle.constitution.constitution.model.entities.Constitution;
import gov.govcircle.constitution.amendment.model.enums.RuleChangeType;
import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AmendmentChange extends BaseEntity {

    @ManyToOne
    private Constitution constitution;

    @ManyToOne
    private Amendment opinion;

    @Enumerated(EnumType.STRING)
    private RuleChangeType ruleChangeType;

    @Convert(converter = RuleChangeDataJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<RuleChangeData> srcRules;

    @Convert(converter = RuleChangeDataJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<RuleChangeData> targetRules;
}
