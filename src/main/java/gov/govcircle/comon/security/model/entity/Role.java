package gov.govcircle.comon.security.model.entity;


import gov.govcircle.comon.config.Configs;
import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Entity(name = "Role")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String title;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> authorities;

    public static Role defaultRole() {
        return (Role) new Role()
                .setId(Configs.DEFAULT_ROLE_ID);
    }
}
