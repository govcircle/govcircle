package gov.govcircle.common.security.model.dto;


import gov.govcircle.common.security.model.vo.AuthenticationRequest;
import gov.govcircle.common.security.model.vo.DataSignatureVO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.stream.Collectors;

public class UserAddressSignatureAuthenticationToken implements Authentication {

    private final AuthenticationRequest authenticationRequest;
    private final UserDetails userDetails;
    private Object details;
    private boolean isAuthenticated;


    public UserAddressSignatureAuthenticationToken(
            AuthenticationRequest authenticationRequest,
            UserDetails userDetails

    ) {
        this.authenticationRequest = authenticationRequest;
        this.userDetails = userDetails;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public DataSignatureVO getCredentials() {
        return authenticationRequest.getDataSignature();
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }

    @Override
    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;

    }

    @Override
    public String getName() {
        return "";
    }

    public UserAddressSignatureAuthenticationToken setDetails(Object details) {
        this.details = details;
        return this;

    }

}

