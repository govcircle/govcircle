package gov.govcircle.common.security.model.entity;

import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@Table(name = "gc_user_role")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class UserRole extends BaseEntity {

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(
            name = "role_id",
            foreignKey = @ForeignKey(name = "ur_role_fk_id"),
            nullable = false
    )
    private Role role;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "ur_user_fk_id"),
            nullable = false
    )
    private ApplicationUser user;

    @Enumerated(EnumType.STRING)
    private RoleRegistrationStatus actorRegistrationStatus;

    @Column(name = "start_slot")
    private int startSlot;

    @Column(name = "end_slot")
    private int endSlot;

}
