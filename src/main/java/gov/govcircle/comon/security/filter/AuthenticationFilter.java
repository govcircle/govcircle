package gov.govcircle.comon.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.govcircle.comon.security.model.vo.DataSignatureVO;
import gov.govcircle.comon.security.model.dto.UserAddressSignatureAuthenticationToken;
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
        String dataSignature = request
                .getReader()
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
        DataSignatureVO dataSignatureVO = mapper.readValue(
                dataSignature,
                DataSignatureVO.class
        );
        UserAddressSignatureAuthenticationToken authenticationToken = new UserAddressSignatureAuthenticationToken(
                dataSignatureVO.signature(),
                dataSignatureVO.key(),
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
        return !(path.startsWith("/verify") && request.getMethod().equals("POST"));

    }


}
