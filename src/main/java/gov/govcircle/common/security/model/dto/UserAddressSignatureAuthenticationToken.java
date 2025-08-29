package gov.govcircle.common.security.model.dto;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;

public class UserAddressSignatureAuthenticationToken implements Authentication {

    private final String signatureString;
    private final String keyString;
    private final UserDetails userDetails;
    private Object details;

    public UserAddressSignatureAuthenticationToken(
            String signatureString,
            String keyString,
            UserDetails userDetails

    ) {
        this.signatureString = signatureString;
        this.keyString = keyString;
        this.userDetails = userDetails;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return signatureString;
    }

    @Override
    public Object getDetails() {
        return keyString;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return "";
    }

    public Object details() {
        return details;
    }

    public UserAddressSignatureAuthenticationToken setDetails(Object details) {
        this.details = details;
        return this;
    }
}

