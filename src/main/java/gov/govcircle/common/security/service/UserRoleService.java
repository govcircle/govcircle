package gov.govcircle.common.security.service;
import gov.govcircle.common.security.model.dto.ApplicationUserDTO;
import gov.govcircle.common.security.model.dto.UserRoleDTO;
import gov.govcircle.common.security.model.entity.CardanoActorType;
import gov.govcircle.common.security.model.entity.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRoleService {
    List<UserRoleDTO> findByKeyIdentifier(String keyIdentifier);
    Optional<UserRoleDTO> findByUserIdAndKeyIdentifier(
            Long userId,
            String keyIdentifier
    );
    void revokeUserRolesWithGivenIdentifier(String keyIdentifier);
    List<UserRoleDTO> getOnChainUserRoles(
            String userAddress
    );
    Optional<UserRoleDTO> checkIfUserIsDRep(
            String userAddress
    );
    UserRoleDTO save(UserRoleDTO userRoleDTO);
    List<UserRoleDTO> findByUserId(Long userId);
    UserRoleDTO createNewUserRole(
            ApplicationUserDTO applicationUserDTO,
            CardanoActorType cardanoActorType,
            String keyIdentifier,
            String publicKeyHash,
            String nonce
    );


}
