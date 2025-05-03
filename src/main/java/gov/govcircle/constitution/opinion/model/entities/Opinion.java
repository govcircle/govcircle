package gov.govcircle.constitution.opinion.model.entities;

import gov.govcircle.base.model.entity.BaseEntity;
import gov.govcircle.comon.models.entity.ApplicationUser;
import gov.govcircle.constitution.constitution.model.entities.ConstitutionBranch;
import gov.govcircle.constitution.opinion.model.enums.OpinionStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Opinion extends BaseEntity {

    @ManyToOne
    private ConstitutionBranch branch;

    @OneToOne
    private ApplicationUser owner;
    private String intention;

    @ElementCollection
    private List<OpinionUserAct> userAct;

    @Enumerated(EnumType.STRING)
    private OpinionStatus status;


}
