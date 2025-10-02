package gov.govcircle.common.security.model.entity;

import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@Table(name = "gc_role_authority")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class RoleAuthority extends BaseEntity {

    @ManyToOne
    @JoinColumn(
            name = "role_id",
            foreignKey = @ForeignKey(name = "ra_role_fk_id"),
            nullable = false
    )
    private Role role;

    @ManyToOne
    @JoinColumn(
            name = "authority_id",
            foreignKey = @ForeignKey(name = "ra_authority_fk_id"),
            nullable = false
    )
    private Authority authority;

    private int startSlot;
    private int endSlot;
}
