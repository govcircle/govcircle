package gov.govcircle.common.security.service;
import gov.govcircle.common.security.model.dto.UserRoleDTO;

import java.util.List;
import java.util.Optional;

public interface UserRoleService {

    List<UserRoleDTO> getOnChainUserRoles(
            String userAddress
    );
    Optional<UserRoleDTO> checkIfUserIsDRep(
            String userAddress
    );

}
