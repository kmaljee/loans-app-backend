package kam.dnb.loanapp;

import kam.dnb.loanapp.api.user.Role;
import kam.dnb.loanapp.api.user.User;
import kam.dnb.loanapp.api.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * Populates the users table with some test users. Remove prior to production - only for dev testing.
 */
@Configuration
public class PopulateTestUsers {

    @Bean
    CommandLineRunner populateTestAccounts(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        // Only populate it with sample data if there are no users (i.e. only on first startup)
        if (userRepository.count() > 0) {
            return args -> {};
        }

        return args -> userRepository.saveAll(List.of(
                new User("adviser", passwordEncoder.encode("password"), "", List.of(Role.ADVISER))
        ));
    }
}