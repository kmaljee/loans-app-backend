package kam.dnb.loanapp.api.user;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    @Nonnull
    private String username;

    @Nonnull
    private String hashedPassword;

    private String email;

    @Nonnull
    private List<Role> roles;

    public User() {
    }

    public User(final String username, final String hashedPassword, final String email, final List<Role> roles) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public User setId(final Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(final String username) {
        this.username = username;
        return this;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public User setHashedPassword(final String hashedPassword) {
        this.hashedPassword = hashedPassword;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(final String email) {
        this.email = email;
        return this;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public User setRoles(final List<Role> roles) {
        this.roles = roles;
        return this;
    }
}
