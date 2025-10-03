package gov.govcircle.common.security.model.dto;

import gov.govcircle.common.config.Configs;
import gov.govcircle.common.security.model.entity.ApplicationUser;
import gov.govcircle.common.security.model.entity.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
public class UserDetailsInfoDTO implements UserDetails {


    private String nounc;
    private String username;
    private String userAddress;
    private final List<GrantedAuthority> authorities;

    public UserDetailsInfoDTO(ApplicationUser applicationUser) {
        this.nounc = applicationUser.getNonce();
        this.username = applicationUser.getUsername();
        this.userAddress = applicationUser.getUserAddress();
        this.authorities = new ArrayList<>(Objects.nonNull(applicationUser.getRoles())
                ? applicationUser.getRoles()
                .stream()
                .flatMap(userRole -> userRole
                        .getRole()
                        .getAuthorities()
                        .stream()
                )
                .map(userAuthority -> {
                    String title = userAuthority
                            .getAuthority()
                            .getTitle();
                    return new SimpleGrantedAuthority(title);
                })
                .toList()
                : new ArrayList<>());
        authorities.addAll(
                applicationUser
                        .getRoles()
                        .stream()
                        .map(UserRole::getRole)
                        .map(role -> Configs.ROLE + role.getTitle())
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );
        authorities.addAll(
                applicationUser
                        .getAuthorities()
                        .stream()
                        .map(userAuthority -> {
                            String title = userAuthority
                                    .getAuthority()
                                    .getTitle();
                            return new SimpleGrantedAuthority(title);
                        })
                        .toList()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userAddress;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
