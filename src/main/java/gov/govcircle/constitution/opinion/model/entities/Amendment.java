package gov.govcircle.constitution.opinion.model.entities;

import gov.govcircle.constitution.constitution.model.entities.ConstitutionBranch;
import gov.govcircle.constitution.opinion.model.enums.AmendmentType;
import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Entity
@Table(name = "gc_amendment")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Amendment extends BaseEntity {

    @ManyToOne
    private ConstitutionBranch constitution;

    @ManyToOne
    private Opinion opinion;

    @Enumerated(EnumType.STRING)
    private AmendmentType amendmentType;

    @Convert(converter = RuleChangeDataJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<RuleChangeData> srcRules;

    @Convert(converter = RuleChangeDataJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<RuleChangeData> targetRules;
}
