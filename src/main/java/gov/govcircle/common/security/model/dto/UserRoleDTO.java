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

    private RoleDTO role;
    private ApplicationUserDTO user;
    @Enumerated(EnumType.STRING)
    private RoleRegistrationStatus actorRegistrationStatus;

    private Long startSlot;
    private Long endSlot;
}
