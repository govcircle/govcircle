package gov.govcircle.common.security.model.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class InMemoryActorAuthenticationIdentifierDTO implements UserDetails {
    private final String keyIdentifier;
    private final String nonce;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
        
    }

    @Override
    public String getPassword() {
        return nonce;
    
    }

    @Override
    public String getUsername() {
        return keyIdentifier;
    
    }
    
}
