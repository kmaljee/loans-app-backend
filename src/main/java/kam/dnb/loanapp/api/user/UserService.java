package kam.dnb.loanapp.api.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getHashedPassword(),
                getAuthorities(user.getRoles()));
    }

    public User createUser(final String username, final String password, final String email, final List<Role> roles) {
        final String hashedPassword = passwordEncoder.encode(password);
        return userRepository.save(new User(username, hashedPassword, email, roles));
    }

    public boolean usernameExists(final String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private List<GrantedAuthority> getAuthorities(final List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(Role.AUTHORITY_ROLE_PREFIX + role))
                .collect(Collectors.toList());
    }
}
