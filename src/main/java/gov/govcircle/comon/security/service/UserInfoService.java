package gov.govcircle.comon.security.service;

import gov.govcircle.comon.security.model.dto.UserDetailsInfoDTO;
import gov.govcircle.comon.security.model.entity.ApplicationUser;
import gov.govcircle.comon.user.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService implements UserDetailsService {

    private final ApplicationUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String uniqueAddress) throws UsernameNotFoundException {
        return repository.findByUserAddress(uniqueAddress)
                .map(UserDetailsInfoDTO::new)
                .orElseThrow(() -> new UsernameNotFoundException("user wallet not found"));
    }

    public String addUser(ApplicationUser applicationUser) {
        repository.save(applicationUser);
        return "User Added Successfully";

    }
}
