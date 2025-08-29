package gov.govcircle.common.security.model.entity;

import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity(name = "user_role")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class UserRole extends BaseEntity {

    @ManyToOne(cascade = CascadeType.MERGE)
    private Role role;

    @ManyToOne
    private ApplicationUser user;

    @Enumerated(EnumType.STRING)
    private RoleRegistrationStatus actorRegistrationStatus;

    private int startEpoch;
    private int endEpoch;
}
