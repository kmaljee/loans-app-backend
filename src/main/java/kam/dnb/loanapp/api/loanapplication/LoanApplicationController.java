package kam.dnb.loanapp.api.loanapplication;

import kam.dnb.loanapp.api.loanapplication.dto.LoanApplicationRequest;
import kam.dnb.loanapp.api.loanapplication.dto.LoanApplicationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/api/loans")
public class LoanApplicationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanApplicationController.class);

    private final LoanApplicationService loanApplicationService;

    public LoanApplicationController(final LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping
    public ResponseEntity<LoanApplicationResponse> createApplication(final Principal principal, @RequestBody final LoanApplicationRequest createLoanRequest) {
        LOGGER.debug("Request made to create new loan application by user {}", principal.getName());
        return ResponseEntity.ok(loanApplicationService.createLoanApplicationAsUser(principal.getName(), createLoanRequest));
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<LoanApplicationResponse>> getForCurrentUser(final Principal principal) {
        LOGGER.debug("Request made to get loan applications for current user by user {}", principal.getName());
        return ResponseEntity.ok(loanApplicationService.getApplicationsForUser(principal.getName()));
    }

    // Exercise note: Could have put this functionality within the above method and had it behave differently depending
    // on the role of the requesting user but separating it out to reduce security risks
    @PreAuthorize("hasRole('ROLE_ADVISER')")
    @GetMapping(path = "/getAll")
    public ResponseEntity<List<LoanApplicationResponse>> getAll(final Principal principal) {
        LOGGER.debug("Request made to get all loan applications by user {}", principal.getName());
        return ResponseEntity.ok(loanApplicationService.getAll(principal.getName()));
    }
}
