package gov.govcircle.common.security.service;

import gov.govcircle.common.repository.cache.user.ApplicationUserCache;
import gov.govcircle.common.security.model.entity.ApplicationUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("InMemoryCacheUserDetailsService")
@RequiredArgsConstructor
public class InMemoryCacheUserDetailsService implements UserDetailsService {

    private final ApplicationUserCache cache;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        return cache
                .findByIdentifier(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("user wallet not found"));
    }

    public String addUser(ApplicationUser applicationUser) {
        return "User Added Successfully";

    }

    public String removeUser(String identifier) {
        return "User Added Successfully";

    }

}
