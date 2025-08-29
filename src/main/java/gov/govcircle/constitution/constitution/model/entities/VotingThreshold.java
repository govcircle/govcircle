package gov.govcircle.constitution.constitution.model.entities;

import gov.govcircle.common.security.model.entity.Role;
import gov.govcircle.constitution.constitution.model.enums.ActionType;
import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class VotingThreshold extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @ManyToOne
    private Role role;

    private int percentage;

}
