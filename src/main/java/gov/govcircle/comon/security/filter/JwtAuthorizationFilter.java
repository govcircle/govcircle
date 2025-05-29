package gov.govcircle.comon.security.filter;

import gov.govcircle.comon.security.service.JWTService;
import gov.govcircle.comon.security.model.dto.UserAddressSignatureAuthenticationToken;
import gov.govcircle.comon.security.model.dto.UserDetailsInfoDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JWTService jwtService;

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
        return path.startsWith("/verify-signature") || path.startsWith("/generate-nonce");

    }

}
