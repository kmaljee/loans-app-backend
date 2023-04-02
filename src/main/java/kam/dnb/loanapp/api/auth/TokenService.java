package kam.dnb.loanapp.api.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private static final String ISSUER = "self";
    private static final Duration VALID_LIFETIME = Duration.ofMinutes(15);

    private final JwtEncoder jwtEncoder;

    public TokenService(final JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Generates a JWT based on the user and authorities within the authentication provided
     * @param authentication The Authentication to base the token on
     * @return A valid JWT token specifying the user and scope within the authentication
     */
    public String generateToken(final Authentication authentication) {
        final Instant now = Instant.now();
        final String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        final JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(now)
                .expiresAt(now.plus(VALID_LIFETIME))
                .subject(authentication.getName())
                .claim(OAuth2ParameterNames.SCOPE, scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
