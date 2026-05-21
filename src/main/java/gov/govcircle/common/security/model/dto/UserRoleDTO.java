package gov.govcircle.common.security.model.dto;

import gov.govcircle.common.security.model.entity.RoleRegistrationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDTO {

    public UserRoleDTO(
            RoleDTO role,
            ApplicationUserDTO user,
            String publicKeyHash,
            String keyIdentifier
    ) {
        this.role = role;
        this.user = user;
        this.publicKeyHash = publicKeyHash;
        this.keyIdentifier = keyIdentifier;
        this.revoked = Boolean.FALSE;

    }

    private Long id;
    private RoleDTO role;
    private ApplicationUserDTO user;
    @Enumerated(EnumType.STRING)
    private RoleRegistrationStatus actorRegistrationStatus;
    private String publicKeyHash;
    private String keyIdentifier;
    private boolean revoked;

    private Long startSlot;
    private Long endSlot;
}
