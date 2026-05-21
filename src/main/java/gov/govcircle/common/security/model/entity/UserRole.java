package gov.govcircle.common.security.model.entity;

import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Table(name = "gc_user_role")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class UserRole extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "role_id",
            foreignKey = @ForeignKey(name = "ur_role_fk_id"),
            nullable = false
    )
    private Role role;

    @Column(
            name = "public_key_hash",
            nullable = false
    )
    private String publicKeyHash;

    @Column(
            name = "key_identifier",
            nullable = false
    )
    private String keyIdentifier; // payment address, DRepId, CCId, PoolId
    private boolean revoked;

    @ManyToOne(fetch = FetchType.EAGER)
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
