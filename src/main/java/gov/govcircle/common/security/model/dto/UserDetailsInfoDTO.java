package gov.govcircle.common.security.model.dto;

import gov.govcircle.common.security.model.entity.ApplicationUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        authorities = new ArrayList<>();
//        this.authorities = new ArrayList<>(Objects.nonNull(applicationUser.getUserRoles())
//                ? applicationUser.getUserRoles()
//                .stream()
//                .flatMap(userRole -> userRole
//                        .getRole()
//                        .getAuthorities()
//                        .stream()
//                )
//                .map(SimpleGrantedAuthority::new)
//                .toList()
//                : new ArrayList<>());
//        authorities.addAll(
//                applicationUser
//                        .getUserRoles()
//                        .stream()
//                        .map(UserRole::getRole)
//                        .map(role -> Configs.ROLE + role.getTitle())
//                        .map(SimpleGrantedAuthority::new)
//                        .toList()
//        );
        authorities.addAll(
                applicationUser
                        .getAuthorities()
                        .stream()
                        .map(SimpleGrantedAuthority::new)
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
