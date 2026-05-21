package gov.govcircle.common.util;

import gov.govcircle.common.security.model.dto.UserDetailsInfoDTO;
import gov.govcircle.common.security.model.exception.AuthenticationDetailsNotFoundException;
import gov.govcircle.common.security.model.exception.AuthenticationNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class GovcircleSecurityUtils {

    public static UserDetailsInfoDTO getAuthenticatedActor(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
            throw new AuthenticationNotFoundException("No authenticated user found for current request"); //TODO: should be handled gracefully

        }
        UserDetailsInfoDTO userDetails = (UserDetailsInfoDTO) authentication.getPrincipal();
        if (Objects.isNull(userDetails)) {
            throw new AuthenticationDetailsNotFoundException("No details found for authenticated user"); //TODO: should be handled gracefully

        }
        return userDetails;

    }

}
