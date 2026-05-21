package gov.govcircle.common.security.service;

import gov.govcircle.common.security.model.dto.UserDetailsInfoDTO;
import gov.govcircle.common.user.service.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("PersistentUserDetailsService")
@RequiredArgsConstructor
public class PersistentUserDetailsService implements UserDetailsService {
    private final ApplicationUserService applicationUserService;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        return applicationUserService
                .findByUserIdentifier(identifier)
                .map(applicationUser -> new UserDetailsInfoDTO(
                                applicationUser,
                                identifier
                        )
                )
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

    }

}
