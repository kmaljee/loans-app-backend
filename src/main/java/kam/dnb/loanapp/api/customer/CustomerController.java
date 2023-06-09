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

    @PostMapping
    public ResponseEntity<CustomerResponse> register(@RequestBody final CreateCustomerRequest createCustomerRequest) {
        return ResponseEntity.ok(customerService.createCustomer(createCustomerRequest));
    }

    @PreAuthorize("hasRole('ROLE_ADVISER')")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getSingle(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }
}
