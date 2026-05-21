package gov.govcircle.common.security.model.dto;

import gov.govcircle.common.security.model.entity.UserVerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUserDTO {

    private Long id;
    private String username;

    private UserVerificationStatus status;
    private String email;
    private String nonce;
    private List<UserAuthorityDTO> authorities;
    private List<UserRoleDTO> roles;

}
