package gov.govcircle.common.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.govcircle.common.config.Configs;
import gov.govcircle.common.security.model.vo.AuthenticationRequest;
import gov.govcircle.common.security.model.vo.DataSignatureVO;
import gov.govcircle.common.security.model.dto.UserAddressSignatureAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.ObjectInputFilter;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper mapper = new ObjectMapper();
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authenticationRequest = request
                .getReader()
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
        AuthenticationRequest authenticationRequestVO = mapper.readValue(
                authenticationRequest,
                AuthenticationRequest.class
        );
        UserAddressSignatureAuthenticationToken authenticationToken = new UserAddressSignatureAuthenticationToken(
                authenticationRequestVO,
                null
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(
                request,
                response
        );


    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !(path.startsWith(Configs.URLS.REST_VERIFY_SIGNATURE_ENDPOINT) && request.getMethod().equals("POST"));

    }


}
