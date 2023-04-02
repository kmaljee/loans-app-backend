package kam.dnb.loanapp.api.loanapplication;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import kam.dnb.loanapp.api.customer.Customer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class LoanApplication {

    @Id
    @GeneratedValue
    private Long id;

    @Nonnull
    @ManyToOne
    private Customer customer;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    @Nonnull
    private BigDecimal loanAmount;

    @Nonnull
    private BigDecimal equityAmount;

    @Nonnull
    private BigDecimal salary;

    @Nonnull
    private LoanApplicationStatus status;

    public Long getId() {
        return id;
    }

    public LoanApplication setId(final Long id) {
        this.id = id;
        return this;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LoanApplication setCustomer(final Customer customer) {
        this.customer = customer;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LoanApplication setCreated(final LocalDateTime created) {
        this.created = created;
        return this;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public LoanApplication setUpdated(final LocalDateTime updated) {
        this.updated = updated;
        return this;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public LoanApplication setLoanAmount(final BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
        return this;
    }

    public BigDecimal getEquityAmount() {
        return equityAmount;
    }

    public LoanApplication setEquityAmount(final BigDecimal equityAmount) {
        this.equityAmount = equityAmount;
        return this;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public LoanApplication setSalary(final BigDecimal salary) {
        this.salary = salary;
        return this;
    }

    public LoanApplicationStatus getStatus() {
        return status;
    }

    public LoanApplication setStatus(final LoanApplicationStatus status) {
        this.status = status;
        return this;
    }
}
