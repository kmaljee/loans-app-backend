package kam.dnb.loanapp.api.auth;

import kam.dnb.loanapp.api.user.Role;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Response object to send for a successful authenticate request containing a token that can be used for subsequent
 * requests and a plaintext role specifying the authority of the token.
 */
public class AuthResponse {

    private final String token;
    private final String role;

    public AuthResponse(final String token, final Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.role = extractRole(authorities);
    }

    // Exercise note: To keep the frontend simple, users can only have a single role. It makes sense for users to only
    // have a single role, but I avoided enforcing that in the rest of the Java code except here to keep the UI simple.
    private String extractRole(final Collection<? extends GrantedAuthority> authorities) {
        // Extract only the roles from the list of authorities and remove the ROLE_ prefix
        return authorities.stream()
                .filter(authority -> authority.getAuthority().startsWith(Role.AUTHORITY_ROLE_PREFIX))
                .map(authority -> authority.getAuthority().substring(Role.AUTHORITY_ROLE_PREFIX.length()))
                .findFirst()
                .orElse("");
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }
}
