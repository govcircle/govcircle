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
public class ApplicationUser extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String userAddress;// staking address
    private String username;

    private UserVerificationStatus status;
    private String email;
    private String nonce;

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE
    )
    private List<UserAuthority> authorities;

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE
    )
    private List<UserRole> roles;

}
