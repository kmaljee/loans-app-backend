package kam.dnb.loanapp.api.customer.dto;

import kam.dnb.loanapp.api.customer.Customer;

public record CustomerResponse(String username, String email, String fullName, String address, String socialSecurity) {

    public CustomerResponse(final Customer customer) {
        this(
                customer.getUser().getUsername(),
                customer.getUser().getEmail(),
                customer.getFullName(),
                customer.getAddress(),
                customer.getSocialSecurityNumber()
        );
    }
}
