package kam.dnb.loanapp.api.customer.dto;

public record CreateCustomerRequest(String username, String email, String password, String verifyPassword,
                                    String fullName, String address, String socialSecurity) {
}
