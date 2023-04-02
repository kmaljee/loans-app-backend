package kam.dnb.loanapp.api.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * @param userId User ID of the customer
     * @return The customer linked to the specified user ID
     */
    Optional<Customer> findByUser_Id(final Long userId);

    /**
     * @param username Username of the customer
     * @return The customer with the specified username
     */
    Optional<Customer> findByUser_Username(final String username);

}