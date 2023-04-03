package kam.dnb.loanapp.api.customer;

import jakarta.transaction.Transactional;
import kam.dnb.loanapp.api.customer.dto.CreateCustomerRequest;
import kam.dnb.loanapp.api.customer.dto.CustomerResponse;
import kam.dnb.loanapp.api.error.RequestException;
import kam.dnb.loanapp.api.user.Role;
import kam.dnb.loanapp.api.user.User;
import kam.dnb.loanapp.api.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserService userService;

    public CustomerService(final CustomerRepository customerRepository, final UserService userService) {
        this.customerRepository = customerRepository;
        this.userService = userService;
    }

    /**
     * Creates a new customer which includes a customer type user based on the CreateCustomerRequest provided.
     * @param createCustomerRequest Details of the new customer to create
     */
    // Exercise note: Would have liked to add in significantly more validation (email format, password requirements, etc.)
    // and add in the classes necessary to pass back multiple validation failures but skipping in the interest of time
    @Transactional
    public CustomerResponse createCustomer(final CreateCustomerRequest createCustomerRequest) {
        if (userService.usernameExists(createCustomerRequest.username())) {
            throw new RequestException(CustomerFailureReason.USERNAME_ALREADY_EXISTS);
        }

        final User newUser = userService.createUser(createCustomerRequest.username(),
                createCustomerRequest.password(),
                createCustomerRequest.email(),
                List.of(Role.CUSTOMER));

        final Customer newCustomer = new Customer()
                .setUser(newUser)
                .setFullName(createCustomerRequest.fullName())
                .setAddress(createCustomerRequest.address())
                .setSocialSecurityNumber(createCustomerRequest.socialSecurity());
        return new CustomerResponse(customerRepository.save(newCustomer));
    }

    public CustomerResponse getById(final Long customerId) {
        return new CustomerResponse(customerRepository.getReferenceById(customerId));
    }
}
