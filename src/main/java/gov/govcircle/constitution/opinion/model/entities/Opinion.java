package gov.govcircle.constitution.opinion.model.entities;

import gov.govcircle.common.security.model.entity.ApplicationUser;
import gov.govcircle.constitution.opinion.model.enums.OpinionStatus;
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
public class Opinion extends BaseEntity {

    @OneToOne
    private ApplicationUser owner;
    private String intention;

    @Convert(converter = OpinionUserRoleJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<OpinionUserRole> userAct;

    @Enumerated(EnumType.STRING)
    private OpinionStatus status;


}
