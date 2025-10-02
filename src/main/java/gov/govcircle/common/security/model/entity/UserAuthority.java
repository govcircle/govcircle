package gov.govcircle.common.security.model.entity;

import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@Table(name = "gc_user_authority")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserAuthority extends BaseEntity {

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "ua_user_fk_id"),
            nullable = false
    )
    private ApplicationUser user;

    @ManyToOne
    @JoinColumn(
            name = "authority_id",
            foreignKey = @ForeignKey(name = "ua_authority_fk_id"),
            nullable = false
    )
    private Authority authority;

    @Column(name = "start_slot")
    private int startSlot;

    @Column(name = "end_slot")
    private int endSlot;

}
