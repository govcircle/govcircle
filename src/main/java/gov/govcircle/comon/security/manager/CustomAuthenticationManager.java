package gov.govcircle.comon.security.manager;

import gov.govcircle.comon.security.model.exception.AuthenticationTokenIsNotProvided;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final List<AuthenticationProvider> authenticationProviderList;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        for (AuthenticationProvider provider : authenticationProviderList) {
            if (provider.supports(authentication.getClass())) {
                return provider.authenticate(authentication);

            }

        }
        throw new AuthenticationTokenIsNotProvided("Authentication token is not provided");

    }
}
