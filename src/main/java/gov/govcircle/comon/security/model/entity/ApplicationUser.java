package gov.govcircle.comon.security.model.entity;

import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class ApplicationUser extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String userAddress;// staking address
    private String username;

    private UserVerificationStatus status;
    private String email;
    private String nonce;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> authorities;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<UserRole> userRoles;

}
