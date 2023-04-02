package kam.dnb.loanapp.api.loanapplication;

import kam.dnb.loanapp.api.auth.AuthenticationController;
import kam.dnb.loanapp.api.loanapplication.dto.LoanApplicationRequest;
import kam.dnb.loanapp.api.loanapplication.dto.LoanApplicationResponse;
import kam.dnb.loanapp.api.customer.Customer;
import kam.dnb.loanapp.api.customer.CustomerRepository;
import kam.dnb.loanapp.api.error.RequestException;
import kam.dnb.loanapp.api.user.Role;
import kam.dnb.loanapp.api.user.User;
import kam.dnb.loanapp.api.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static kam.dnb.loanapp.api.loanapplication.ApplicationFailureReason.*;

@Service
public class LoanApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    private final LoanApplicationRepository applicationRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public LoanApplicationService(final LoanApplicationRepository applicationRepository, final CustomerRepository customerRepository, final UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new loan application for the customer linked to the provided username.
     * Performs basic validation on supplied values and that the username belongs to a customer user.
     * @param username The username of the customer that this loan should be created for
     * @param loanApplicationRequest The loan application request containing details of the new loan
     * @return The successfully created loan
     */
    public LoanApplicationResponse createLoanApplicationAsUser(final String username, final LoanApplicationRequest loanApplicationRequest) {
        final BigDecimal loanAmount = loanApplicationRequest.loanAmount();
        final BigDecimal equityAmount = loanApplicationRequest.equityAmount();
        final BigDecimal salary = loanApplicationRequest.salary();

        if (loanAmount.signum() <= 0) {
            throw new RequestException(INVALID_LOAN_AMOUNT);
        }

        if (equityAmount.signum() < 0) {
            throw new RequestException(INVALID_EQUITY_AMOUNT);
        }

        if (salary.signum() < 0) {
            throw new RequestException(INVALID_SALARY);
        }

        final Customer customer = customerRepository.findByUser_Username(username)
                .orElseThrow(() -> new RequestException(INVALID_CUSTOMER));

        return createLoanApplication(customer, loanAmount, equityAmount, salary);
    }

    /**
     * @param username Username of the user making this resource request
     * @return All loan applications belonging to the specified user
     */
    // Exercise note: Would have liked to make these methods take a user / request context object instead
    // but skipped in the interest of time
    public List<LoanApplicationResponse> getApplicationsForUser(final String username) {
        LOGGER.debug("Get applications for user called by user {}", username);
        return applicationRepository.findByCustomer_User_Username(username).stream()
                .map(LoanApplicationResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * @param username Username of the user making this resource request
     * @return All loan applications
     */
    // Exercise note: Would have liked to make these methods take a user / request context object instead
    // but skipped in the interest of time
    public List<LoanApplicationResponse> getAll(final String username) {
        LOGGER.debug("Get all loan applications called by user {}", username);
        final List<Role> roles = userRepository.findByUsername(username).map(User::getRoles).orElse(Collections.emptyList());
        if (!roles.contains(Role.ADVISER)) {
            LOGGER.warn("User attempting to access all loan applications with insufficient role {}", username);
            throw new AccessDeniedException("Insufficient privileges for this action");
        }

        return applicationRepository.findAll().stream()
                .map(LoanApplicationResponse::new)
                .collect(Collectors.toList());
    }

    private LoanApplicationResponse createLoanApplication(final Customer customer, final BigDecimal loanAmount, final BigDecimal equityAmount, final BigDecimal salary) {
        final LoanApplication application = new LoanApplication()
                .setCustomer(customer)
                .setLoanAmount(loanAmount)
                .setEquityAmount(equityAmount)
                .setSalary(salary)
                .setStatus(LoanApplicationStatus.PENDING);

        return new LoanApplicationResponse(applicationRepository.save(application));
    }
}
