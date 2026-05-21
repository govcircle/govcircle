package gov.govcircle.common.security.config;

import gov.govcircle.common.config.Configs;
import gov.govcircle.common.security.filter.SignatureAuthenticationEntryPoint;
import gov.govcircle.common.security.manager.CustomAuthenticationManager;
import gov.govcircle.common.security.provider.UserAddressSignatureAuthenticationProvider;
import gov.govcircle.common.security.filter.AuthenticationFilter;
import gov.govcircle.common.security.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthorizationFilter jwtAuthFilter;
    private final UserAddressSignatureAuthenticationProvider userAddressSignatureAuthenticationProvider;
    private final SignatureAuthenticationEntryPoint signatureAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(crs -> crs.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
//                                .anyRequest().permitAll()
                                .requestMatchers(
                                        Configs.URLS.REST_VERIFY_SIGNATURE_ENDPOINT,
                                        Configs.URLS.REST_GENERATE_NONCE_ENDPOINT,
                                        Configs.URLS.REST_FREE_ENDPOINT
                                ).permitAll()
                                .requestMatchers(
                                        Configs.URLS.REST_PROFILE_PATH + "/**"
                                ).hasAnyRole(
                                        Configs.ROLES.DREP_ROLE_TITLE,
                                        Configs.ROLES.WALLET_ROLE_TITLE,
                                        Configs.ROLES.SPO_ROLE_TITLE,
                                        Configs.ROLES.CC_ROLE_TITLE
                                )
                                .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager())
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exCustomizer -> exCustomizer.authenticationEntryPoint(signatureAuthenticationEntryPoint));
        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new CustomAuthenticationManager(
                List.of(
                        userAddressSignatureAuthenticationProvider
                )
        );

    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter(
                authenticationManager()
        );

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:3000"

                )
        );
        configuration.setAllowedMethods(
                List.of(
                        "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"

                )
        );
        configuration.setAllowedHeaders(
                List.of(
                        "Authorization", "Content-Type", "X-Auth-Token",
                        "Origin", "Accept", "X-Requested-With"

                )
        );
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
