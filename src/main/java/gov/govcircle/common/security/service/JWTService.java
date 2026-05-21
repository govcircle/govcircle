package gov.govcircle.common.security.service;

import gov.govcircle.common.security.model.dto.UserDetailsInfoDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JWTService {

    public static final String SECRET = "5367566859703373367639792F423F452848284D6251655468576D5A71347437";
    public static final String JWT_AUTHORITIES_CLAIM = "authorities";

    public String generateToken(UserDetailsInfoDTO userDetailsInfo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JWT_AUTHORITIES_CLAIM, userDetailsInfo.getAuthorities());
        return createToken(
                claims,
                userDetailsInfo.getIdentifier()
        );
    }

    private String createToken(
            Map<String, Object> claims,
            String userAddress
    ) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userAddress)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserAddress(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractUserAuthorities(String token) {
        List<String> rawList = extractClaim(
                token,
                (claims) -> claims.get(
                        JWT_AUTHORITIES_CLAIM,
                        List.class
                )
        );
        /* NOTE: code below code be used of you are not sure about type of the list, in this example if you are not sure about the String.class type
         *
         *    return Objects.nonNull(rawList) && !rawList.isEmpty()
         *                 ? rawList.stream()
         *                 .filter(item -> item instanceof String)
         *                 .map(item -> (String) item)
         *                 .toList()
         *                 : Collections.emptyList();
         */
        return rawList;

    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(
            String token,
            UserDetailsInfoDTO userDetails
    ) {
        final String userAddress = extractUserAddress(token);
        return (userAddress.equals(userDetails.getIdentifier()) && !isTokenExpired(token));

    }

}
