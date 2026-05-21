package gov.govcircle.common.security.filter;

import gov.govcircle.common.config.Configs;
import gov.govcircle.common.security.service.JWTService;
import gov.govcircle.common.security.model.dto.UserAddressSignatureAuthenticationToken;
import gov.govcircle.common.security.model.dto.UserDetailsInfoDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Service
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JWTService jwtService;

    public JwtAuthorizationFilter(
            @Qualifier("PersistentUserDetailsService") UserDetailsService userDetailsService,
            JWTService jwtService
    ) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userAddress = null;

        if (Objects.nonNull(authHeader) && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            userAddress = jwtService.extractUserAddress(token);

        }
        if (Objects.nonNull(userAddress) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            UserDetailsInfoDTO userDetails = (UserDetailsInfoDTO) userDetailsService.loadUserByUsername(userAddress);
            if (
                    jwtService.validateToken(
                            token,
                            userDetails
                    )
            ) {
                UserAddressSignatureAuthenticationToken authToken = new UserAddressSignatureAuthenticationToken(
                        null,
                        userDetails
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                authToken.setAuthenticated(true);
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);

            }

        }
        filterChain.doFilter(
                request,
                response
        );


    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith(Configs.URLS.REST_VERIFY_SIGNATURE_ENDPOINT) || path.startsWith(Configs.URLS.REST_GENERATE_NONCE_ENDPOINT);

    }

}
