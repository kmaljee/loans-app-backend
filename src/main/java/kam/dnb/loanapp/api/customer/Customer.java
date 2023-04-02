package kam.dnb.loanapp.api.customer;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import kam.dnb.loanapp.api.user.User;

import java.time.LocalDateTime;

@Entity
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    @Nonnull
    @OneToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private User user;

    @Nonnull
    private String socialSecurityNumber;

    @Nonnull
    private String fullName;

    private String address;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    public Long getId() {
        return id;
    }

    public Customer setId(final Long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Customer setUser(final User user) {
        this.user = user;
        return this;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public Customer setSocialSecurityNumber(final String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public Customer setFullName(final String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Customer setAddress(final String address) {
        this.address = address;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public Customer setCreated(final LocalDateTime created) {
        this.created = created;
        return this;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public Customer setUpdated(final LocalDateTime updated) {
        this.updated = updated;
        return this;
    }
}
