package kam.dnb.loanapp.api.customer;

import kam.dnb.loanapp.api.customer.dto.CreateCustomerRequest;
import kam.dnb.loanapp.api.customer.dto.CustomerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PreAuthorize("permitAll()")
    @PostMapping
    public ResponseEntity<String> register(@RequestBody final CreateCustomerRequest createCustomerRequest) {
        customerService.createCustomer(createCustomerRequest);
        return ResponseEntity.ok("Success");
    }

    @PreAuthorize("hasRole('ROLE_ADVISER')")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getSingle(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }
}
