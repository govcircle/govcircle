package gov.govcircle.common.security.model.entity;


import gov.govcircle.common.config.Configs;
import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Entity
@Table(name = "gc_role")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

    @Column(
            nullable = false,
            unique = true
    )
    private String title;
    private String description;

    @Column(
            nullable = false,
            unique = true
    )
    private Integer code;


    @OneToMany(
            mappedBy = "role",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<RoleAuthority> authorities;

    public static Role defaultRole() {
        return (Role) new Role()
                .setId(Configs.DEFAULT_ROLE_ID);
    }

}
