package kam.dnb.loanapp.api.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    private final TokenService tokenService;

    public AuthenticationController(final TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public AuthResponse authenticate(final Authentication authentication) {
        LOGGER.debug("Token requested for user: '{}'", authentication.getName());
        final String token = tokenService.generateToken(authentication);
        LOGGER.debug("Token '{}' granted for user '{}'", token, authentication.getName());
        return new AuthResponse(token, authentication.getAuthorities());
    }
}
