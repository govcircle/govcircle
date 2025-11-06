package gov.govcircle.common.security.model.dto;

import gov.govcircle.common.security.model.entity.UserVerificationStatus;
import lombok.Data;

import java.util.List;

@Data
public class ApplicationUserDTO {

    private String userAddress;// payment address
    private String username;

    private UserVerificationStatus status;
    private String email;
    private String nonce;
    private List<UserAuthorityDTO> authorities;
    private List<UserRoleDTO> roles;

}
