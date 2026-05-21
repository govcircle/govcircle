package gov.govcircle.common.security.model.entity;

import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Entity
@Table(name = "gc_application_user")
@EqualsAndHashCode(callSuper = true)
public class  ApplicationUser extends BaseEntity {

    @Column(
            name = "username",
            unique = true
    )
    private String username;
    private UserVerificationStatus status;

    @Column(
            name = "email",
            unique = true
    )
    private String email;

    @Column(
            name = "ada_handle",
            unique = true
    )
    private String adaHandle;
    private String nonce;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.MERGE
    )
    private List<UserAuthority> authorities;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.MERGE
    )
    private List<UserRole> roles;

}
