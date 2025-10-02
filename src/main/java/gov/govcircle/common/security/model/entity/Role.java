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

    @Column(nullable = false, unique = true)
    private String title;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "gc_role_authorities")
    private List<String> authorities;

    public static Role defaultRole() {
        return (Role) new Role()
                .setId(Configs.DEFAULT_ROLE_ID);
    }
}
