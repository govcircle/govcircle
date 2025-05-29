package gov.govcircle.constitution.amendment.model.entities;

import gov.govcircle.comon.security.model.entity.ApplicationUser;
import gov.govcircle.constitution.amendment.model.enums.AmendmentStatus;
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
public class Amendment extends BaseEntity {

    @OneToOne
    private ApplicationUser owner;
    private String intention;

    @Convert(converter = AmendmentUserRoleJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<AmendmentUserRole> userAct;

    @Enumerated(EnumType.STRING)
    private AmendmentStatus status;


}
